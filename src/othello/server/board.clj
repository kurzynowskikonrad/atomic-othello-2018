(ns othello.server.board)

(def player-one-symbol 1)
(def player-two-symbol 2)
(def empty-symbol 0)

(def directions {:up         (fn [pos] (update pos 0 dec))
                 :down       (fn [pos] (update pos 0 inc))
                 :left       (fn [pos] (update pos 1 dec))
                 :right      (fn [pos] (update pos 1 inc))
                 :up-left    (fn [pos] (map dec pos))
                 :up-right   (fn [pos] [(dec (pos 0)) (inc (pos 1))])
                 :down-left  (fn [pos] [(inc (pos 0)) (dec (pos 1))])
                 :down-right (fn [pos] (map inc pos))})

(def all-directions (keys directions))
(def all-direction-fns (vals directions))

(def symbols {:player-one player-one-symbol
              :player-two player-two-symbol
              :empty empty-symbol})

(defn opponent [player]
  (case player
    :player-one :player-two
    :player-two :player-one
    :none))

(defn player-symbol [player]
  (get symbols player empty-symbol))

(defn opponent-symbol [player]
  (player-symbol (opponent player)))

(defn score
  "Count the number of squares occupied by the given player"
  [board player]
  (count (filter #{(player-symbol player)} (flatten board))))

(defn winner
  "This function returns the winner given the current state of the board.
   It does not validate that the game is over."
  [board]
  (let [p1-score (score board :player-one)
        p2-score (score board :player-two)]
    (cond
      (> p1-score p2-score) :player-one
      (> p2-score p1-score) :player-two
      :else :tie)))

(defn get-piece [board position]
  (get-in board position))

(defn place-piece [board position player]
  (assoc-in board position (player-symbol player)))

(defn valid-index? [n]
  (and (>= n 0)
       (< n 8)))

(defn valid-position? [pos]
  (and (valid-index? (nth pos 0))
       (valid-index? (nth pos 1))))

(defn can-move-further [position direction]
  (let [dir-fn (get directions direction)
        further (dir-fn position)]
    (valid-position? further)))

(defn find-bracketing-piece
  "Return the position of the bracketing piece, nil if there isn't one."
  [board position player direction]
  (cond
    (= (get-piece board position) (player-symbol player))
    position
    (and (= (get-piece board position) (opponent-symbol player))
         (can-move-further position direction))
    (recur board ((get directions direction) position) player direction)
    :else nil))

(defn- opponent-piece-present? [board position player]
  (= (get-piece board position) (opponent-symbol player)))

(defn- bracketing-piece-present? [board position player direction]
  (boolean (find-bracketing-piece board position player direction)))

(defn- check-for-flip [board position player direction]
  (let [position ((get directions direction) position)]
    (and
     (opponent-piece-present? board position player)
     (bracketing-piece-present? board position player direction))))

(defn would-flip?
  "Return whether this move would result in any flips."
  [board position player direction]
  (if (can-move-further position direction)
    (check-for-flip board position player direction)))

(defn- position-is-empty? [board pos]
  (and
   (valid-position? pos)
   (= (get-piece board pos) empty-symbol)))

(defn- would-flip-in-some-direction? [board position player]
  (some (partial would-flip? board position player) all-directions))

(defn valid-move? [board position player]
  (and
   (vector? position)
   (position-is-empty? board position)
   (would-flip-in-some-direction? board position player)))

(def all-positions (for [r (range 8) c (range 8)] [r c]))

(defn valid-moves-for-player [board player]
  (filter #(valid-move? board % player) all-positions))

(defn any-valid-moves-for-player? [board player]
  (not (empty? (valid-moves-for-player board player))))

(defn any-valid-moves? [board]
  (or (any-valid-moves-for-player? board :player-one)
      (any-valid-moves-for-player? board :player-two)))

(defn determine-next-player [board player]
  (let [opponent (opponent player)]
    (if (any-valid-moves-for-player? board opponent)
      opponent
      player)))

(defn game-over? [board]
  (not (any-valid-moves? board)))

(defn- make-flips-impl [board position player direction]
  (let [initial-pos ((directions direction) position)
        bracketer (find-bracketing-piece board initial-pos player direction)]
    (loop [board board position initial-pos]
      (if (= position bracketer)
        board
        (recur (place-piece board position player) ((directions direction) position))))))

(defn make-flips [board position player direction]
  (if (would-flip? board position player direction)
    (make-flips-impl board position player direction)
    board))

(defn update-board-for-move
  "Update board to reflect move by a player"
  [board position player]
  (if (valid-move? board position player)
    (let [board (place-piece board position player)]
      (reduce #(make-flips %1 position player %2) board all-directions))))