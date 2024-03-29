(ns machine
  (:require [money :as money]
            [clojure.spec.alpha :as spec]))

(defrecord machine [inside transaction slots])

(spec/def ::quantity nat-int?)
(spec/def ::price ::money/money)
(defrecord slot [snack quantity price])

(spec/fdef ->slot
           :args (spec/cat :snack any?
                           :quantity ::quantity
                           :price any?))

(def default-machine
  (->machine money/none 0 {}))

(defn insert-money [machine money]
  (-> machine
      (update :transaction + (money/amount money))
      (update :inside money/add money)))

(spec/fdef insert-money
           :args (spec/cat :machine any?
                           :money ::money/denomination))

(defn allocate [{:keys [ones tens quarters dollars fives twenties]} transaction]
  (let [twentyCount (min (int (/ transaction 20)) twenties)
        transaction (- transaction (* twentyCount 20))

        fiveCount (min (int (/ transaction 5)) fives)
        transaction (- transaction (* fiveCount 5))

        dollarCount (min (int transaction) dollars)
        transaction (- transaction dollarCount)

        quarterCount (min (int (/ transaction 0.25)) quarters)
        transaction (- transaction (* quarterCount 0.25))

        tenCount (min (int (/ transaction 0.1)) tens)
        transaction (- transaction (* tenCount 0.1))

        centCount (min (int (/ transaction 0.01)) ones)]
    (money/->money centCount tenCount quarterCount dollarCount fiveCount twentyCount)))


(defn return-money [{:keys [inside transaction] :as machine}]
  (let [moneyToReturn (allocate inside transaction)]
    (-> machine
        (update :inside money/subtract moneyToReturn)
        (assoc :transaction 0))))

(defn load-money [machine money]
  (assoc machine :inside money))

(defn buy-snack [{:keys [inside transaction] :as machine} position]
  (let [{:keys [price]} (get-in machine [:slots position])]
    (if (< transaction price)
      (throw Exception)
      (let [requiredChange (- transaction price)
            possibleChange (allocate inside requiredChange)]
        (if (< (money/amount possibleChange) requiredChange)
          (throw Exception)
          (-> machine
              (assoc :transaction 0)
              (update :inside money/subtract possibleChange)
              (update-in [:slots position :quantity] dec)))))))

(defn load-snack [machine position slot]
  (assoc-in machine [:slots position] slot))
