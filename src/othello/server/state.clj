(ns othello.server.state
  (:require [mount.core :as mount :refer [defstate]]
            [clojure.tools.logging :as log]
            [clojure.string :refer [join] :as string]))

(def starting-board [[0 0 0 0 0 0 0 0]
                     [0 0 0 0 0 0 0 0]
                     [0 0 0 0 0 0 0 0]
                     [0 0 0 1 2 0 0 0]
                     [0 0 0 2 1 0 0 0]
                     [0 0 0 0 0 0 0 0]
                     [0 0 0 0 0 0 0 0]
                     [0 0 0 0 0 0 0 0]])

(defn build-state [options]
  (atom {:status :idle
         :config (merge {:min-turn-time 0
                         :max-turn-time 15000} options)
         :player-one {:status :idle}
         :player-two {:status :idle}
         :ui-clients []}))

(defn changed? [key-path map-1 map-2]
  (not= (get-in map-1 key-path) (get-in map-2 key-path)))

(defn update-status [state status]
  (swap! state assoc :status status))

(defn set-game-result [state result]
  (swap! state assoc :game-result result))

(defn set-state [state path value]
  (swap! state assoc-in path value))

(defn apply-changes [state updates]
  (swap! state merge updates))

(defn log-move [state move]
  (swap! state update-in [(:current-player @state) :moves] conj move)
  (swap! state assoc :last-move move))

(defn initialize-game [state]
  (apply-changes state {:status :idle
                        :game-result :none
                        :current-player :player-one
                        :board starting-board
                        :player-one {:status :idle
                                     :moves []}
                        :player-two {:status :idle
                                     :moves []}}))

(defn exiting? [state]
  (= :exiting (:status @state)))

(defn launch-game? [state]
  (= :idle (:status @state)))

(defn running? [state]
  (= :running (:status @state)))

(defn get-board [state]
  (:board @state))

(defn get-status [state]
  (:status @state))

(defn get-game-result [state]
  (:game-result @state))

(defn get-min-turn-time [state]
  (-> @state :config :min-turn-time))

(defn get-max-turn-time [state]
  (-> @state :config :max-turn-time))

(defn get-current-player [state]
  (:current-player @state))

(defn player-ready? [state player]
  (= :connected (get-in @state [player :status])))

(defn player-one-ready? [state]
  (player-ready? state :player-one))

(defn player-two-ready? [state]
  (player-ready? state :player-two))

(defn get-player-moves [state player]
  (get-in @state [player :moves]))

(defn get-player-one-moves [state]
  (get-player-moves state :player-one))

(defn get-player-two-moves [state]
  (get-player-moves state :player-two))

(defn get-player-one-status [state]
  (get-in @state [:player-one :status]))

(defn get-player-two-status [state]
  (get-in @state [:player-two :status]))

(defn get-player-status [state player]
  (get-in @state [player :status]))

(defn get-default-player-name [player]
  (-> player
      name
      string/capitalize
      (string/replace "-" " ")))

(defn get-player-name [state player]
  (get-in @state [:config player :name] (get-default-player-name player)))

(defn get-player-config [state player]
  (get-in @state [:config player]))

(defn- player-port-key [player]
  (case player
    :player-one :p1-port
    :player-two :p2-port))

(defn get-server-port [state player]
  (let [port-key (player-port-key player)]
    (-> @state :config port-key)))

(defn get-ui-port [state]
  (-> @state :config :ui-port))

(defn ui-clients-connected [state]
  (if-let [clients (:ui-clients @state)]
    (count clients)
    0))

(defn ui-clients-connected? [state]
  (> (ui-clients-connected state) 0))

(defn ui-client-connected [state client]
  (swap! state update-in [:ui-clients] conj client))

(defn ui-client-disconnected [state client]
  (swap! state update-in [:ui-clients] (fn [clients] (remove #{client} clients))))

(defn wait-for-a-ui-client [state]
  (get-in @state [:config :wait-for-ui]))

(defn ui-clients-ready? [state]
  (if (wait-for-a-ui-client state)
    (ui-clients-connected? state)
    true))

(defn clients-ready? [state]
  (and
   (player-one-ready? state)
   (player-two-ready? state)
   (ui-clients-ready? state)))

(defn player-connected? [state player]
  (= :connected (-> @state player :status)))

(defn disconnecting-players? [state]
  (and (= :disconnecting-players (get-status state))
       (or
        (player-connected? state :player-one)
        (player-connected? state :player-two))))

(defn waiting-for-clients? [state]
  (not (clients-ready? state)))

(defn add-watcher [state id key-path watcher]
  (add-watch state id
             (fn [id _ old-state new-state]
               (when (changed? key-path old-state new-state)
                 (watcher id old-state new-state)))))

(defn on-status-change-to [state id status-flag watcher]
  (add-watcher state id [:status]
               (fn [id old-state new-state]
                 (if (= status-flag (:status new-state))
                   (watcher id old-state new-state)))))

(defn remove-watcher [state id]
  (remove-watch state id))

(defn restart-after-game-over? [state]
  (get-in @state [:config :play-multiple-games] false))

(defstate state :start (build-state (mount/args)))