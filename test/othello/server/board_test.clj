(ns othello.server.board-test
  (:require [clojure.test :refer :all]
            [othello.server.board :refer :all]))

(deftest test-opponent
  (is (= :player-two (opponent :player-one)))
  (is (= :player-one (opponent :player-two)))
  (is (= :none (opponent :bogus))))

(deftest test-player-symbol
  (is (= 1 (player-symbol :player-one)))
  (is (= 2 (player-symbol :player-two)))
  (is (= 0 (player-symbol :bogus))))

(deftest test-opponent-symbol
  (is (= 2 (opponent-symbol :player-one)))
  (is (= 1 (opponent-symbol :player-two)))
  (is (= 0 (opponent-symbol :bogus))))

(deftest test-score
  (let [board [[0 0 0 0 0 2 0 0]
               [0 0 0 0 2 0 0 0]
               [0 0 0 2 0 0 0 0]
               [0 0 2 0 0 0 0 0]
               [0 2 0 1 1 1 0 0]
               [2 2 0 0 0 0 0 0]
               [0 0 0 0 0 0 0 0]
               [0 0 0 0 0 0 0 0]]]
    (is (= 3 (score board :player-one)))
    (is (= 7 (score board :player-two)))))

(deftest test-winner-one
  (let [board [[0 0 0 0 0 2 0 0]
               [0 0 0 0 2 0 0 0]
               [0 0 0 2 0 0 0 0]
               [0 0 2 0 0 0 0 0]
               [0 2 0 1 1 1 0 0]
               [2 2 0 0 0 0 0 0]
               [0 0 0 0 0 0 0 0]
               [1 1 1 1 1 1 1 1]]]
    (is (= :player-one (winner board)))))

(deftest test-winner-two
  (let [board [[0 0 0 0 0 2 0 0]
               [0 0 0 0 2 0 0 0]
               [0 0 0 2 0 0 0 0]
               [0 0 2 0 0 0 0 0]
               [0 2 0 1 1 1 0 0]
               [2 2 0 0 0 0 0 0]
               [0 0 0 0 0 0 0 0]
               [0 0 0 0 0 0 0 0]]]
    (is (= :player-two (winner board)))))

(deftest test-get-piece
  (let [board [[0 0 0 0 0 2 0 0]
               [0 0 0 0 2 0 0 0]
               [0 0 0 2 0 0 0 0]
               [0 0 7 0 0 0 0 0]
               [0 2 0 1 1 1 0 0]
               [2 2 0 0 0 0 0 0]
               [0 0 0 0 0 0 0 0]
               [1 1 1 1 1 1 1 1]]]
    (is (= 7 (get-piece board [3 2])))))

(deftest test-place-piece
  (let [board    [[0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0]]
        expected [[0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 1]]]
    (is (= expected (place-piece board [7 7] :player-one)))))

(deftest test-can-move-further-up
  (is (can-move-further [1 1] :up))
  (is (can-move-further [7 0] :up))
  (is (not (can-move-further [0 5] :up))))

(deftest test-can-move-further-down
  (is (can-move-further [0 1] :down))
  (is (can-move-further [6 7] :down))
  (is (not (can-move-further [7 0] :down))))

(deftest test-can-move-further-left
  (is (can-move-further [1 1] :left))
  (is (can-move-further [0 7] :left))
  (is (not (can-move-further [1 0] :left))))

(deftest test-can-move-further-right
  (is (can-move-further [1 1] :right))
  (is (can-move-further [0 6] :right))
  (is (not (can-move-further [1 7] :right))))

(deftest test-can-move-further-up-left
  (is (can-move-further [1 1] :up-left))
  (is (can-move-further [7 7] :up-left))
  (is (not (can-move-further [1 0] :up-left)))
  (is (not (can-move-further [0 1] :up-left)))
  (is (not (can-move-further [0 0] :up-left))))

(deftest test-can-move-further-up-right
  (is (can-move-further [1 1] :up-right))
  (is (can-move-further [6 6] :up-right))
  (is (not (can-move-further [1 7] :up-right)))
  (is (not (can-move-further [0 6] :up-right)))
  (is (not (can-move-further [0 7] :up-right))))

(deftest test-can-move-further-down-left
  (is (can-move-further [0 1] :down-left))
  (is (can-move-further [6 1] :down-left))
  (is (not (can-move-further [0 0] :down-left)))
  (is (not (can-move-further [7 7] :down-left)))
  (is (not (can-move-further [7 0] :down-left))))

(deftest test-can-move-further-down-right
  (is (can-move-further [0 0] :down-right))
  (is (can-move-further [6 6] :down-right))
  (is (not (can-move-further [0 7] :down-right)))
  (is (not (can-move-further [7 0] :down-right)))
  (is (not (can-move-further [7 7] :down-right))))

(deftest test-find-bracketing-piece
  (let [board [[0 0 0 0 0 0 0 0]
               [0 0 0 0 0 0 0 0]
               [0 0 0 0 0 0 0 0]
               [0 0 0 1 2 0 0 0]
               [0 0 0 2 1 0 0 0]
               [0 0 0 0 0 0 0 0]
               [0 0 0 0 0 0 0 0]
               [0 0 0 0 0 0 0 0]]]
    (is (= nil (find-bracketing-piece board [3 4] :player-one :up-left)))
    (is (= nil (find-bracketing-piece board [3 4] :player-one :up)))
    (is (= nil (find-bracketing-piece board [3 4] :player-one :up-right)))
    (is (= [3 3] (find-bracketing-piece board [3 4] :player-one :left)))
    (is (= nil (find-bracketing-piece board [3 4] :player-one :right)))
    (is (= nil (find-bracketing-piece board [3 4] :player-one :down-left)))
    (is (= [4 4] (find-bracketing-piece board [3 4] :player-one :down)))
    (is (= nil (find-bracketing-piece board [3 4] :player-one :right)))))

(deftest test-find-bracketing-piece-edges
  (let [board [[2 0 0 1 0 0 0 0]
               [2 2 2 0 0 0 0 0]
               [0 2 0 0 0 1 1 2]
               [2 1 0 0 0 0 0 0]
               [2 1 0 0 0 0 0 0]
               [2 1 0 0 0 0 0 0]
               [2 1 0 0 0 0 0 0]
               [2 1 0 0 0 0 0 0]]]
    (is (= nil (find-bracketing-piece board [1 1] :player-one :left)))
    (is (= [2 7] (find-bracketing-piece board [2 5] :player-two :right)))
    (is (= [3 1] (find-bracketing-piece board [1 1] :player-one :down)))
    (is (= nil (find-bracketing-piece board [3 1] :player-two :down)))
    (is (= nil (find-bracketing-piece board [1 0] :player-one :up)))
    (is (= [7 0] (find-bracketing-piece board [7 1] :player-two :left)))))

(deftest would-flip-respects-boundaries
  (let [board [[0 1 0 0 0 0 0 0]
               [0 0 2 2 2 1 0 0]
               [0 1 2 2 2 0 0 0]
               [0 1 0 0 0 0 0 0]
               [0 1 0 0 0 0 0 0]
               [0 1 0 0 0 0 0 0]
               [0 1 0 0 0 0 0 0]
               [0 2 0 0 0 0 0 0]]]
    (is (would-flip? board [1 1] :player-one :right))
    (is (would-flip? board [1 1] :player-two :down))
    (is (not (would-flip? board [2 1] :player-one :right)))
    (is (not (would-flip? board [0 1] :player-one :right)))))

(deftest valid-move-answers-correctly
  (let [board [[0 0 0 0 0 0 0 0]
               [0 0 0 0 0 0 0 0]
               [0 0 0 0 0 0 0 0]
               [0 0 0 1 2 1 0 0]
               [0 0 0 2 1 0 0 0]
               [0 0 0 0 0 0 0 0]
               [0 0 0 0 0 0 0 0]
               [0 0 0 0 0 0 0 0]]]
    (is (not (valid-move? board [2 3] :player-one))) ; no-flip
    (is (valid-move? board [2 4] :player-one)) ; flip
    (is (not (valid-move? board [3 2] :player-one))) ; no-flip
    (is (not (valid-move? board [3 3] :player-one))) ; taken)
    (is (valid-move? board [5 3] :player-one)) ; flip
    (is (valid-move? board [5 4] :player-two)))) ; flip

(deftest valid-moves-for-player-answers-correctly
  (let [board [[0 0 0 0 0 0 0 0]
               [0 0 0 0 0 0 0 0]
               [0 0 0 0 0 0 0 0]
               [0 0 0 1 2 0 0 0]
               [0 0 0 2 1 0 0 0]
               [0 0 0 0 0 0 0 0]
               [0 0 0 0 0 0 0 0]
               [0 0 0 0 0 0 0 0]]]
    (is (= (set [[2 4] [3 5] [4 2] [5 3]]) (set (valid-moves-for-player board :player-one))))
    (is (= (set [[2 3] [3 2] [4 5] [5 4]]) (set (valid-moves-for-player board :player-two))))))

(deftest any-valid-moves-for-player?-answers-correctly
  (let [board [[0 0 0 0 0 0 0 0]
               [0 0 0 0 0 0 0 0]
               [0 0 0 0 0 0 0 0]
               [1 2 2 2 2 0 0 0]
               [0 0 0 2 2 0 0 0]
               [0 0 0 0 0 0 0 0]
               [0 0 0 0 0 0 0 0]
               [0 0 0 0 0 0 0 0]]]
    (is (any-valid-moves-for-player? board :player-one))
    (is (not (any-valid-moves-for-player? board :player-two)))))

(deftest make-flips-for-player-one
  (let [board [[0 0 0 0 0 0 0 0]
               [0 0 0 0 0 0 0 0]
               [0 0 0 0 0 0 0 0]
               [1 2 2 2 2 0 0 0]
               [0 0 0 2 2 0 0 0]
               [0 0 0 0 0 0 0 0]
               [0 0 0 0 0 0 0 0]
               [0 0 0 0 0 0 0 0]]
        flipped [[0 0 0 0 0 0 0 0]
                 [0 0 0 0 0 0 0 0]
                 [0 0 0 0 0 0 0 0]
                 [1 1 1 1 1 0 0 0]
                 [0 0 0 2 2 0 0 0]
                 [0 0 0 0 0 0 0 0]
                 [0 0 0 0 0 0 0 0]
                 [0 0 0 0 0 0 0 0]]]
    (is (= flipped (make-flips board [3 5] :player-one :left)))))

(deftest update-board-for-move-1
  (let [board [[0 0 0 0 0 0 0 0]
               [0 0 0 0 0 0 0 0]
               [0 0 0 0 0 0 0 0]
               [1 2 2 2 2 0 0 0]
               [0 0 0 2 2 2 0 0]
               [0 0 0 0 0 2 0 0]
               [0 0 0 0 0 1 0 0]
               [0 0 0 0 0 0 0 0]]
        updated [[0 0 0 0 0 0 0 0]
                 [0 0 0 0 0 0 0 0]
                 [0 0 0 0 0 0 0 0]
                 [1 1 1 1 1 1 0 0]
                 [0 0 0 2 2 1 0 0]
                 [0 0 0 0 0 1 0 0]
                 [0 0 0 0 0 1 0 0]
                 [0 0 0 0 0 0 0 0]]]
    (is (= updated (update-board-for-move board [3 5] :player-one)))))