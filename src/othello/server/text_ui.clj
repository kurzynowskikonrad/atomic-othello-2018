(ns othello.server.text-ui
  (:require [mount.core :as mount :refer [defstate]]
            [othello.server.state :refer [state] :as st]
            [clojure.string :refer [join] :as string]
            [clojure.tools.logging :as log]
            [clojure.pprint :as pp]))

(def final-states #{:player-one-won
                    :player-two-won
                    :tied
                    :player-one-won-due-to-error
                    :player-two-won-due-to-error})

(defn format-sym [sym]
  (-> sym
      name
      string/capitalize
      (string/replace "-" " ")))

(defn pretty [value]
  (with-out-str (pp/pprint value)))

(defn get-player-status-message [state player]
  (let [status (st/get-player-status state player)]
    (join [(st/get-player-name state player) " | " (format-sym status) "..."])))

(defn log-player-move [state player]
  (let [moves (st/get-player-moves state player)]
    (when (not (empty? moves))
      (log/info (join [(format-sym player) " played: " (last moves)])))))

(defn log-final-state [state]
  (log/info "Player One moves:" (st/get-player-one-moves state))
  (log/info "Player Two moves:" (st/get-player-two-moves state))
  (log/info "Final board:\n" (pretty (st/get-board state))))

(defn log-status [state]
  (let [status (st/get-status state)
        message (join ["Othello | " (format-sym status) "..."])]
    (log/info message)
    (when (contains? final-states status)
      (log-final-state state))))

(defn log-player-one-status [state]
  (log/info (get-player-status-message state :player-one)))

(defn log-player-one-moves [state]
  (log-player-move state :player-one))

(defn log-player-two-status [state]
  (log/info (get-player-status-message state :player-two)))

(defn log-player-two-moves [state]
  (log-player-move state :player-two))

(def state-change-table [[[:status] log-status]
                         [[:player-one :status] log-player-one-status]
                         [[:player-one :moves] log-player-one-moves]
                         [[:player-two :status] log-player-two-status]
                         [[:player-two :moves] log-player-two-moves]])

(defn log-state-changes [_ _ old-state new-state]
  (doseq [[key-path logger] state-change-table]
    (when (st/changed? key-path old-state new-state)
      (logger (atom new-state)))))

(defn start-ui []
  (add-watch state :id-text-ui log-state-changes))

(defn stop-ui []
  (remove-watch state :id-text-ui))

(defstate text-ui :start (start-ui)
  :stop (stop-ui))