(ns othello.server.player.state
  (:require [othello.server.player :as player]
            [othello.server.state :refer [state] :as st]
            [othello.server.util :as util]
            [othello.server.player.remote :as remote]
            [othello.server.player.robot :as robot]
            [othello.server.player.random :as random]
            [mount.core :refer [defstate] :as mount]))

(defn build-player [player type moves]
  (case type
    :remote (remote/make-remote-player player)
    :robot (robot/make-robot-player player moves)
    :random (random/->Player player)
    (remote/make-remote-player player)))

(defn start-player [player type moves]
  (st/set-state state [player :status] :starting)
  (let [player (build-player player type moves)
        connect-id (str player :connecting)
        disconnect-id (str player :disconnecting)]
    (st/on-status-change-to state connect-id :waiting-for-clients
                            (fn [id old-state new-state]
                              (player/connect player)))
    (st/on-status-change-to state disconnect-id :disconnecting-players
                            (fn [id old-state new-state]
                              (player/disconnect player)))
    player))

(defn stop-player [player]
  (st/remove-watcher state player))

(defstate player-one :start (start-player :player-one (-> @state :config :p1-type) (-> @state :config :p1-moves))
  :stop (stop-player :player-one))
(defstate player-two :start (start-player :player-two (-> @state :config :p2-type) (-> @state :config :p2-moves))
  :stop (stop-player :player-two))

(defn current-player []
  (if (= :player-one (st/get-current-player state))
    player-one
    player-two))

(defn get-next-move []
  (util/time-limited (st/get-max-turn-time state) :timeout
                     (util/exec-and-wait (st/get-min-turn-time state) player/get-move (current-player))))