(ns machine
  (:require [money :as money]
            [clojure.spec.alpha :as spec]))

(defrecord machine [inside transaction slots])

(def default-machine
  (->machine money/none money/none []))

(defn insert-money [machine money]
  (update machine :transaction money/total money))

(spec/fdef insert-money
           :args (spec/cat :machine any?
                           :money ::money/denomination))

(defn return-money [machine]
  (assoc machine :transaction money/none))

(defn buy-snack [{:keys [transaction] :as machine}]
  (-> machine
      (update :inside money/total transaction)
      (assoc :transaction money/none)))
