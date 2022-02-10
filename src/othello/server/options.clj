(ns othello.server.options
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clojure.tools.logging :as log]
            [clojure.string :as string]))

(defn parse-player-type [arg]
  (keyword arg))

(defn valid-player-type? [type]
  (some #{type} [:random :remote :robot]))

(defn valid-timeout? [arg]
  (and (integer? arg) (pos? arg)))

(defn parse-moves [arg]
  (read-string arg))

(defn validate-moves [moves]
  (vector? moves))

(def cli-options
  [[nil "--p1-type TYPE" "Player one's type - remote, random, or robot"
    :default :remote
    :parse-fn parse-player-type
    :validate [valid-player-type? "Must be \"remote\", \"random\", or \"robot\""]]

   [nil "--p2-type TYPE" "Player two's type - remote, random, or robot"
    :default :remote
    :parse-fn parse-player-type
    :validate [valid-player-type? "Must be \"remote\", \"random\" or \"robot\""]]

   [nil "--p1-name NAME" "Player one's team name"
    :default "Player One"]

   [nil "--p2-name NAME" "Player two's team name"
    :default "Player Two"]

   [nil "--p1-moves MOVES" "Moves for a P1 robot player"
    :default []
    :parse-fn parse-moves
    :validate [validate-moves]]

   [nil "--p2-moves MOVES" "Moves for a P2 robot player"
    :default []
    :parse-fn parse-moves
    :validate [validate-moves]]

   [nil "--p1-port PORT" "Port number for the P1 client"
    :default 1337
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]

   [nil "--p2-port PORT" "Port number for the P2 client"
    :default 1338
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]

   [nil "--ui-port PORT" "Port number for UI clients"
    :default 8080
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]

   ["-w" "--wait-for-ui" "Wait for a UI client to connect before starting game"]

   ["-m" "--min-turn-time MILLIS" "Minimum amount of time to wait between turns."
    :default 1000
    :parse-fn #(Integer/parseInt %)
    :validate [valid-timeout? "Must be a positive integer."]]

   ["-x" "--max-turn-time MILLIS" "Maximum amount of time to allow an AI for a turn."
    :default 15000
    :parse-fn #(Integer/parseInt %)
    :validate [valid-timeout? "Must be a positive integer."]]

   ["-h" "--help"]])

(defn parse [args]
  (parse-opts args cli-options))

(defn usage [options-summary]
  (string/join
   \newline
   ["This application is a board for dueling Othello AI implementations."
    ""
    "You can specify that the server should use a random player for one or both players."
    ""
    "The player can be one of three types:"
    "remote - the game will listen for a player to connect to the server"
    "random - the game will make a random valid move for the player"
    "robot - the game use moves specified in the --p1-moves or --p2-moves argument"
    ""
    "The game will log moves to the console and run a webserver on port 8080 for a UI."
    "Use the --ui-port to specify a different UI port."
    "Pass the --wait-for-ui option in order to have the server wait for a UI connection before starting the game."
    ""
    "The game will by default time out if a player has not responeded within 15 seconds"
    "You can change this with the --max-turn-time arg (--max-turn-time 20000 for 20 seconds)."
    ""
    "Usage:"
    "java -jar othello.jar"
    "java -jar othello.jar --p2-type random --wait-for-ui"
    ""
    "Options:"
    options-summary
    ""]))

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (string/join \newline errors)))

(defn invalid-options-msg [summary]
  (log/info "Sorry, some required options were missing.")
  (newline)
  (usage summary))

(defn exit [status msg]
  (log/info msg)
  (System/exit status))

(defn parse-or-exit [args]
  (let [{:keys [options errors summary]} (parse args)]
    (cond
      (:help options) (exit 0 (usage summary))
      errors (exit 1 (error-msg errors)))
    options))