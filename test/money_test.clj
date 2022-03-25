(ns money-test
  (:require [clojure.test :refer :all]
            [clojure.spec.test.alpha :as st]
            [money :refer :all]))

(st/instrument `->money)

(deftest total-test
  (is (= (->money 2 4 6 8 10 12)
         (total (->money 1 2 3 4 5 6)
                (->money 1 2 3 4 5 6)))))


(deftest money-is-same-if-contains-same-money-test
  (is (= (->money 1 2 3 4 5 6)
         (->money 1 2 3 4 5 6))))

(deftest money-not-equal-if-contains-different-test
  (is (not= (->money 0 0 0 1 0 0)
            (->money 100 0 0 0 0 0))))

(deftest no-negative-values-test
  (are
    [ones tens quarters dollars fives twenties]
    (thrown? Exception (->money ones tens quarters dollars fives twenties))
    -1 0 0 0 0 0
    0 -2 0 0 0 0
    0 0 -3 0 0 0
    0 0 0 -4 0 0
    0 0 0 0 -5 0
    0 0 0 0 0 -6))

(deftest amount-test
  (are
    [ones tens quarters dollars fives twenties sum]
    (= sum (parse-double (format "%.2f" (amount (->money ones tens quarters dollars fives twenties)))))
    0 0 0 0 0 0 0.00
    1 0 0 0 0 0 0.01
    1 2 0 0 0 0 0.21
    1 2 3 0 0 0 0.96
    1 2 3 4 0 0 4.96
    1 2 3 4 5 0 29.96
    1 2 3 4 5 6 149.96
    11 0 0 0 0 0 0.11
    110 0 0 0 100 0 501.10))

(deftest subtract-test
  (testing "degenerate"
    (is (thrown? Exception
                 (subtract none cent))))

  (testing "happy"
    (is (= (subtract (->money 10 10 10 10 10 10)
                     (->money 1 2 3 4 5 6))
           (->money 9 8 7 6 5 4)))))
