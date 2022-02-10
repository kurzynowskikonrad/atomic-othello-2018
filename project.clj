(defproject othello "0.1.0-SNAPSHOT"
  :description "Othello Game for Atomic Games 2018"
  :url "http://www.atomicobject.com/careers/accelerator"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins []
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/tools.cli "0.3.7"]
                 [org.clojure/tools.logging "0.4.1"]
                 [clj-logging-config "1.9.12"]
                 [org.clojure/core.async "0.4.474"]

                 [mount "0.1.13"]
                 [cheshire "5.8.0"]
                 [camel-snake-kebab "0.4.0"]
                 [clj-time "0.14.4"]

                 [http-kit "2.3.0"]
                 [ring/ring-core "1.6.3"]
                 [ring/ring-servlet "1.6.3" :exclusions [javax.servlet/servlet-api]]
                 [ring/ring-json "0.4.0"]
                 [ring/ring-defaults "0.3.2"]
                 [ring-logger "1.0.1"]
                 [compojure "1.6.1"]
                 ]
   :injections []
  :main ^:skip-aot othello.server.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :test {:env {:in-test-mode true}
                    :dependencies []}
             :dev {}})
