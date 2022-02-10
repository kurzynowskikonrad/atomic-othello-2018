(ns othello.server.http-server
  (:import [java.io ByteArrayInputStream ByteArrayOutputStream])
  (:require [mount.core :refer [defstate]]
            [org.httpkit.server :as httpkit :refer :all]
            [ring.logger :as logger]
            [ring.middleware.json :as json]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [othello.server.state :refer [state] :as st]
            [cheshire.core :as cheshire]
            [camel-snake-kebab.core :as case]
            [clojure.java.io :as io]
            [clojure.tools.logging :as log]))

(defn serialize [message]
  (cheshire/generate-string
   message
   {:key-fn case/->camelCaseString}))

(def api-config
  (assoc-in api-defaults [:static :resources] "public"))

(defn channel-notifier [channel]
  (fn [_ _ _ new-state]
    (send! channel (serialize new-state))))

(defn game-state-handler [req]
  (with-channel req channel
    (st/ui-client-connected state (.hashCode channel))
    (send! channel (serialize @state))
    (add-watch state channel (channel-notifier channel))
    (on-close channel (fn [status]
                        (remove-watch state channel)
                        (st/ui-client-disconnected state (.hashCode channel))))
    (on-receive channel (fn [data] ;; echo it back
                          (send! channel data)))))

(defroutes api-routes
  (context "/api" []
    ; important, do not remove this route:
    (GET "/hello" [] "Yes, hello! This is dog.")

    (GET "/game-state" [] game-state-handler)
    (route/not-found "Invalid API URI."))
  (route/resources "/")
  (GET "/" [] (io/resource "public/index.html"))
  (route/not-found (io/resource "public/index.html")))

(def api
  (-> api-routes
    (logger/wrap-with-logger)
    (json/wrap-json-body {:keywords? true})
    (json/wrap-json-response)
    (wrap-defaults api-config)))

(defn start-server []
  (let [port (st/get-ui-port state)]
    (log/info "Started othello http server on port:" port)
    (run-server api {:port port})))

(defn stop-server [server]
  ; server is a function that, when called, shuts down the server
  (server))

(defstate server :start (start-server)
                 :stop (stop-server server))