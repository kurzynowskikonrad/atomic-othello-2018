(ns othello.server.player.robot
  (:require [othello.server.player :as p]
            [othello.server.state :refer [state] :as st]))

(defrecord Player [player remaining-moves all-moves]
  p/Player
  (connect [this]
    (st/set-state state [player :status] :connected))
  (disconnect [this]
    (st/set-state state [player :status] :disconnected)
    (reset! remaining-moves all-moves))
  (get-move [this]
    (let [move (first @remaining-moves)]
      (swap! remaining-moves rest)
      (if (= :timeout move)
        (Thread/sleep 1000000)
        move))))

(defn make-robot-player [player moves]
  (->Player player (atom moves) moves))