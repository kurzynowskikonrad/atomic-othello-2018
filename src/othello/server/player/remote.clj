(ns othello.server.player.remote
  (:require [othello.server.player :as p]
            [othello.server.state :refer [state] :as st]
            [othello.server.board :as board]
            [clojure.java.io :as io]
            [cheshire.core :as json]
            [camel-snake-kebab.core :as case]
            [clojure.string :as string]
            [clojure.tools.logging :as log])
  (:import [java.net Socket ServerSocket]))

(defn format-player-symbol [sym]
  (-> sym name (string/replace "-" " ")))

(defn encode-message [message]
  (json/generate-string
   message
   {:key-fn case/->camelCaseString}))

(defn decode-response [msg]
  (vec (json/parse-string msg case/->kebab-case-keyword)))

(defn write-message [writer message]
  (.write writer (encode-message message))
  (.write writer "\n")
  (.flush writer))

(defn write-game-state [player internal-state]
  (let [writer (:writer @internal-state)]
    (write-message writer {:board (st/get-board state)
                           :max-turn-time (st/get-max-turn-time state)
                           :player (board/player-symbol player)})))

(defn read-response [internal-state]
  (let [reader (:reader @internal-state)]
    (decode-response (. reader readLine))))

(defn wait-for-connection [internal-state player]
  (log/info (string/join ["Listening for " (format-player-symbol player) " on port " (st/get-server-port state player)]))
  (let [server (ServerSocket. (st/get-server-port state player))
        client (.accept server)]
    (swap! internal-state merge {:writer (io/writer client)
                                 :reader (io/reader client)})
    (.close server)))

(defrecord Player [player internal-state]
  p/Player
  (connect [this]
    (future
      (wait-for-connection internal-state player)
      (st/set-state state [player :status] :connected)))

  (disconnect [this]
    (.close (:reader @internal-state))
    (.close (:writer @internal-state))
    (st/set-state state [player :status] :disconnected))

  (get-move [this]
    (write-game-state player internal-state)
    (read-response internal-state)))

(defn make-remote-player [player]
  (->Player player (atom {})))

