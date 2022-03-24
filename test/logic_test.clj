(ns logic_test
  (:require [clojure.test :refer :all]
            [logic :as logic]))

(deftest total-test
  (is (= (logic/make-money 2 4 6 8 10 12)
         (logic/total (logic/make-money 1 2 3 4 5 6)
                      (logic/make-money 1 2 3 4 5 6)))))

(deftest money-is-same-if-contains-same-money-test
  (is (= (logic/make-money 1 2 3 4 5 6)
         (logic/make-money 1 2 3 4 5 6))))

(deftest money-not-equal-if-contains-different-test
  (is (not= (logic/make-money 0 0 0 1 0 0)
            (logic/make-money 100 0 0 0 0 0))))

(deftest no-negative-values-test
  (are
    [ones tens quarters dollars fives twenties]
    (thrown? Exception (logic/make-money ones tens quarters dollars fives twenties))
    -1 0 0 0 0 0
    0 -1 0 0 0 0
    0 0 -1 0 0 0
    0 0 0 -1 0 0
    0 0 0 0 -1 0
    0 0 0 0 0 -1))

(deftest amount-test
  (are
    [ones tens quarters dollars fives twenties amount]
    (= amount (logic/amount (logic/make-money ones tens quarters dollars fives twenties)))
    0 0 0 0 0 0 0.0
    1 0 0 0 0 0 0.01
    1 2 0 0 0 0 0.21
    1 2 3 0 0 0 0.96
    1 2 3 4 0 0 4.96
    1 2 3 4 5 0 29.96
    1 2 3 4 5 6 149.96
    11 0 0 0 0 0 0.11
    110 0 0 0 100 0 501.1))

