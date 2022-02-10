(ns othello.server.state-test
  (:require [clojure.test :refer :all]
            [othello.server.state :refer :all]))

(deftest test-changed?
  (let [state-1 {:winner nil
                 :loser {:name "fred"
                         :level 1}}
        state-2 {:winner :player-one
                 :loser {:name "tom"
                         :level 1}}]
    (is (changed? [:winner] state-1 state-2))
    (is (changed? [:loser :name] state-1 state-2))
    (is (not (changed? [:loser :level] state-1 state-2)))))

(deftest test-update-status
  (let [state (atom {:status :idle})]
    (update-status state :starting)
    (is (= :starting (:status @state)))))

(deftest test-update
  (let [state (atom {:player-one {:status :idle}})]
    (set-state state [:player-one :status] :connected)
    (is (player-one-ready? state))))

(deftest test-player-connected?
  (let [state (atom {:player-one {:status :idle}})]
    (set-state state [:player-one :status] :connected)
    (is (player-connected? state :player-one))
    (set-state state [:player-one :status] :disconnected)
    (is (not (player-connected? state :player-one)))))