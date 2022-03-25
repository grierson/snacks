(ns logic_test
  (:require [clojure.test :refer :all]
            [clojure.spec.test.alpha :as st]
            [logic :as logic]))

(st/instrument `logic/->money)

(deftest total-test
  (is (= (logic/->money 2 4 6 8 10 12)
         (logic/total (logic/->money 1 2 3 4 5 6)
                      (logic/->money 1 2 3 4 5 6)))))

(deftest money-is-same-if-contains-same-money-test
  (is (= (logic/->money 1 2 3 4 5 6)
         (logic/->money 1 2 3 4 5 6))))

(deftest money-not-equal-if-contains-different-test
  (is (not= (logic/->money 0 0 0 1 0 0)
            (logic/->money 100 0 0 0 0 0))))

(deftest no-negative-values-test
  (are
    [ones tens quarters dollars fives twenties]
    (thrown? Exception (logic/->money ones tens quarters dollars fives twenties))
    -1 0 0 0 0 0
    0 -1 0 0 0 0
    0 0 -1 0 0 0
    0 0 0 -1 0 0
    0 0 0 0 -1 0
    0 0 0 0 0 -1))

(deftest amount-test
  (are
    [ones tens quarters dollars fives twenties amount]
    (= amount (parse-double (format "%.2f" (logic/amount (logic/->money ones tens quarters dollars fives twenties)))))
    0 0 0 0 0 0 0.00
    1 0 0 0 0 0 0.01
    1 2 0 0 0 0 0.21
    1 2 3 0 0 0 0.96
    1 2 3 4 0 0 4.96
    1 2 3 4 5 0 29.96
    1 2 3 4 5 6 149.96
    11 0 0 0 0 0 0.11
    110 0 0 0 100 0 501.10))
