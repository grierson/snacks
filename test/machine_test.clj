(ns machine-test
  (:require [clojure.test :refer :all]
            [clojure.spec.test.alpha :as st]
            [machine :refer :all]
            [money :as money]))

(st/instrument `insert-money)

(deftest return-money-empties-transaction-test
  (is (= money/none
         (-> default-machine
             (insert-money money/dollar)
             (return-money)
             (:transaction)))))

(deftest inserted-money-goes-to-transaction-test
  (is (= 1.01
         (-> default-machine
             (insert-money money/dollar)
             (insert-money money/cent)
             (:transaction)
             (money/amount)))))

(deftest only-insert-coins-test
  (is (thrown? Exception
               (insert-money default-machine (money/->money 1 1 1 1 1 1)))))

