(ns othello.server.core
  (:require [mount.core :as mount]
            [othello.server.options :as options]
            [othello.server.state]
            [othello.server.text-ui]
            [othello.server.http-server]
            [othello.server.game])
  (:gen-class))

(defn -main
  [& args]
  (let [options (options/parse-or-exit args)]
    (-> (mount/with-args options)
        mount/start)))