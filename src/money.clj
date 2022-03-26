(ns money
  (:require [clojure.spec.alpha :as spec]))

(spec/def ::ones nat-int?)
(spec/def ::tens nat-int?)
(spec/def ::quarters nat-int?)
(spec/def ::dollars nat-int?)
(spec/def ::fives nat-int?)
(spec/def ::twenties nat-int?)

(spec/def ::money (spec/keys :req-un [::ones ::tens ::quarters ::dollars ::fives ::twenties]))
(defrecord money [ones tens quarters dollars fives twenties])

(def none (->money 0 0 0 0 0 0))
(def cent (->money 1 0 0 0 0 0))
(def ten-cent (->money 0 1 0 0 0 0))
(def quarter (->money 0 0 1 0 0 0))
(def dollar (->money 0 0 0 1 0 0))
(def five-dollar (->money 0 0 0 0 1 0))
(def twenty-dollar (->money 0 0 0 0 0 1))

(spec/def ::denomination #{cent ten-cent quarter dollar five-dollar twenty-dollar})

(spec/fdef ->money
           :args (spec/cat :ones ::ones :tens ::tens :quarters ::quarters :dollars ::dollars :fives ::fives :twenties ::twenties)
           :ret ::money)

(defn add [m1 m2]
  (->money
    (+ (:ones m1) (:ones m2))
    (+ (:tens m1) (:tens m2))
    (+ (:quarters m1) (:quarters m2))
    (+ (:dollars m1) (:dollars m2))
    (+ (:fives m1) (:fives m2))
    (+ (:twenties m1) (:twenties m2))))

(defn multiply [m1 multiplier]
  (->money
    (* (:ones m1) multiplier)
    (* (:tens m1) multiplier)
    (* (:quarters m1) multiplier)
    (* (:dollars m1) multiplier)
    (* (:fives m1) multiplier)
    (* (:twenties m1) multiplier)))

(defn amount [{:keys [ones tens quarters dollars fives twenties]}]
  (+ (* ones 0.01)
     (* tens 0.10)
     (* quarters 0.25)
     dollars
     (* fives 5)
     (* twenties 20)))

(defn subtract [m1 m2]
  (->money
    (- (:ones m1) (:ones m2))
    (- (:tens m1) (:tens m2))
    (- (:quarters m1) (:quarters m2))
    (- (:dollars m1) (:dollars m2))
    (- (:fives m1) (:fives m2))
    (- (:twenties m1) (:twenties m2))))

(spec/fdef subtract
           :args (spec/cat :m1 ::money :m2 ::money)
           :ret ::money)
