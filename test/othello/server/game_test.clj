(ns othello.server.game-test
  (:require [clojure.test :refer :all]
            [mount.core :as mount]
            [othello.server.game :refer :all]
            [othello.server.state :as st :refer [state]]
            [othello.server.player :as player]
            [othello.server.player.robot :as robot]
            [othello.server.text-ui]
            [othello.server.player.state :refer [player-one player-two]]))

(defn build-robot [player moves]
  (robot/->Player player (atom moves)))

(defn play-game [p1-moves p2-moves expected-result expected-board]
  (mount/start-with {#'state (st/build-state {:play-multiple-games false
                                              :min-turn-time 0
                                              :p1-type :robot
                                              :p2-type :robot
                                              :p1-moves p1-moves
                                              :p2-moves p2-moves})})
  (is (= expected-result (st/get-game-result state)))
  (is (= expected-board (st/get-board state)))

  (mount/stop))

(deftest run-sample-games
  (testing "The game should finish if a player one returns a nil move"
    (play-game [[2 4]]
               [[2 5]]
               :player-two-won-due-to-error
               [[0 0 0 0 0 0 0 0]
                [0 0 0 0 0 0 0 0]
                [0 0 0 0 1 2 0 0]
                [0 0 0 1 2 0 0 0]
                [0 0 0 2 1 0 0 0]
                [0 0 0 0 0 0 0 0]
                [0 0 0 0 0 0 0 0]
                [0 0 0 0 0 0 0 0]]))
  (testing "The game should finish if a player two returns an invalid move"
    (play-game [[2 4] [2 6]]
               [[2 5] [2 7]]
               :player-one-won-due-to-error
               [[0 0 0 0 0 0 0 0]
                [0 0 0 0 0 0 0 0]
                [0 0 0 0 1 1 1 0]
                [0 0 0 1 2 0 0 0]
                [0 0 0 2 1 0 0 0]
                [0 0 0 0 0 0 0 0]
                [0 0 0 0 0 0 0 0]
                [0 0 0 0 0 0 0 0]]))
  (testing "Normal game"
    (play-game
     [[2 4] [5 3] [6 5] [2 6] [6 3] [7 3] [5 1] [2 3] [7 4] [0 7] [5 2] [0 4] [1 2] [5 6] [0 6] [0 3] [6 7] [1 1] [7 1] [6 1] [3 2] [1 0] [0 0] [5 5] [2 0] [5 0] [6 6] [3 7] [0 5] [4 0]]
     [[2 5] [5 4] [6 2] [1 4] [6 4] [4 2] [7 6] [1 6] [2 2] [7 5] [7 2] [1 5] [4 5] [1 3] [0 1] [4 7] [3 6] [2 1] [4 1] [7 0] [6 0] [3 1] [3 5] [4 6] [0 2] [1 7] [7 7] [5 7] [2 7] [3 0]]
     :player-two-won
     [[1 2 2 1 1 1 1 1]
      [1 1 2 1 1 1 2 2]
      [1 2 1 1 2 2 2 2]
      [1 1 2 2 2 2 2 2]
      [1 1 1 2 2 2 2 2]
      [1 1 2 1 1 2 2 2]
      [2 2 2 2 2 2 2 2]
      [2 2 2 2 2 2 2 2]])))
