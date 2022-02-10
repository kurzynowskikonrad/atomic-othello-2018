(ns othello.server.util-test
  (:require [clojure.test :refer :all]
            [othello.server.util :refer :all]
            [clj-time.core :as t]))

(deftest test-time-limited
  (let [value (time-limited 1000 :timeout :something)]
    (is (= :something value))))