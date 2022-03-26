(ns machine-test
  (:require [clojure.test :refer :all]
            [clojure.spec.test.alpha :as st]
            [machine :refer :all]
            [money :as money]
            [snack :as snack]))

(st/instrument `insert-money)
(st/instrument `->slot)

(deftest return-money-empties-transaction-test
  (is (= 0
         (-> default-machine
             (insert-money money/dollar)
             (return-money)
             (:transaction)))))

(deftest inserted-money-goes-to-transaction-test
  (is (= 1.01
         (-> default-machine
             (insert-money money/dollar)
             (insert-money money/cent)
             (:transaction)))))

(deftest only-insert-coins-test
  (is (thrown? Exception
               (insert-money default-machine (money/->money 1 1 1 1 1 1)))))

(deftest buy-snack-test
  (let [position 1
        stock 2
        snack (snack/->snack "mars bar")
        {:keys [inside transaction slots]} (-> default-machine
                                               (load-snack position (->slot snack stock 1))
                                               (insert-money money/dollar)
                                               (buy-snack position))]
    (testing "transaction"
      (is (= 0 transaction)))

    (testing "inside"
      (is (= (money/->money 0 0 0 1 0 0)
             inside)))

    (testing "reduced snack count"
      (let [{:keys [quantity]} (get slots position)]
        (is (= (dec stock)
               quantity))))))

(deftest slot-test
  (testing "non negative quantity"
    (is (thrown? Exception
                 (->slot nil -1 money/none)))))

(deftest cant-make-purchase-when-no-snacks-test
  (is (thrown? Exception
               (-> default-machine
                   (buy-snack 1)))))

(deftest insufficient-funds-test
  (is (thrown? Exception
               (-> default-machine
                   (load-snack 1 (->slot (snack/->snack "mars bar") 1 money/five-dollar))
                   (insert-money money/dollar)
                   (buy-snack 1)))))

(deftest returns-smallest-denomination-first-test
  (is (= (money/->money 0 0 4 0 0 0)
         (-> default-machine
             (load-money money/dollar)
             (insert-money money/quarter)
             (insert-money money/quarter)
             (insert-money money/quarter)
             (insert-money money/quarter)
             (return-money)
             (:inside)))))

(deftest change-is-returned-test
  (is (= 1.5
         (let [position 1]
           (-> default-machine
               (load-snack position (->slot (snack/->snack "mars bar") 1 0.5))
               (load-money (money/multiply money/ten-cent 10))
               (insert-money money/dollar)
               (buy-snack position)
               (return-money)
               (:inside)
               (money/amount))))))

(deftest not-enough-change-is-returned-test
  (is (thrown? Exception
              (let [position 1]
                (-> default-machine
                    (load-snack position (->slot (snack/->snack "mars bar") 1 0.5))
                    (insert-money money/dollar)
                    (buy-snack position))))))
