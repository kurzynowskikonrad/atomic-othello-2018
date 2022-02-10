(ns othello.server.player)

(defprotocol Player
  "A simple protocol representing a player."
  (connect [this] "Run any necessary initialization.")
  (get-move [this] "Get the next move for the player.")
  (disconnect [this] "Shutdown the client connection."))