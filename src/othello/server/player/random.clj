(ns othello.server.player.random
  (:require [othello.server.player :as p]
            [othello.server.board :as b]
            [othello.server.state :refer [state] :as st]))

(defrecord Player [player]
  p/Player
  (connect [this]
    (st/set-state state [player :status] :connected))
  (disconnect [this]
    (st/set-state state [player :status] :disconnected))
  (get-move [this]
    (let [moves (b/valid-moves-for-player (st/get-board state) player)]
      (if (seq moves)
        (rand-nth moves)
        {:errors "No valid moves!"}))))