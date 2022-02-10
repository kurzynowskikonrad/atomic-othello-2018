(ns othello.server.game
  (:require [mount.core :as mount :refer [defstate]]
            [othello.server.state :refer [state] :as st]
            [othello.server.board :as board]
            [othello.server.player.state :as players]))

(defn handle-player-error []
  (st/update-status state :game-over-due-to-error)
  (if (= :player-one (st/get-current-player state))
    (st/set-game-result state :player-two-won-due-to-error)
    (st/set-game-result state :player-one-won-due-to-error)))

(defn game-result[]
  (case (board/winner (st/get-board state))
    :player-one :player-one-won
    :player-two :player-two-won
    :tied))

(defn handle-game-over []
  (st/apply-changes state {:status :game-over
                           :current-player :none
                           :game-result (game-result)}))

(defn check-for-game-over []
  (when (board/game-over? (st/get-board state))
    (handle-game-over)))

(defn apply-valid-move [move]
  (let [board (st/get-board state)
        player (st/get-current-player state)
        new-board (board/update-board-for-move board move player)
        new-player (board/determine-next-player new-board player)]
    (st/apply-changes state {:board new-board
                             :current-player new-player})))

(defn handle-valid-move [move]
  (apply-valid-move move)
  (check-for-game-over))

(defn valid-move? [move]
  (and
   move
   (not (:errors move))
   (vector? move)
   (= 2 (count move))
   (board/valid-move? (st/get-board state) move (st/get-current-player state))))

(defn log-move [move]
  (st/log-move state move))

(defn handle-move [move]
  (log-move move)
  (if (valid-move? move)
    (handle-valid-move move)
    (handle-player-error)))

(defn reset-game-if-appropriate []
  (Thread/sleep 5000)
  (if (st/restart-after-game-over? state)
    (st/initialize-game state)))

(defn disconnect-players []
  (st/update-status state :disconnecting-players)
  (while (st/disconnecting-players? state) (Thread/sleep 10)))

(defn run-game []
  (st/update-status state :running)
  (while (st/running? state)
    (handle-move (players/get-next-move)))
  (disconnect-players))

(defn wait-for-clients []
  (st/update-status state :waiting-for-clients)
  (while (st/waiting-for-clients? state) (Thread/sleep 100)))

(defn run-games []
  (st/initialize-game state)
  (while (st/launch-game? state)
    (wait-for-clients)
    (run-game)
    (reset-game-if-appropriate)))

(defstate game :start (run-games))