(ns logic
  (:require [clojure.spec.alpha :as spec]))

(spec/def ::ones nat-int?)
(spec/def ::tens nat-int?)
(spec/def ::quarters nat-int?)
(spec/def ::dollars nat-int?)
(spec/def ::fives nat-int?)
(spec/def ::twenties nat-int?)

(spec/def ::money (spec/keys :req-un [::ones ::tens ::quarters ::dollars ::fives ::twenties]))
(defrecord money [ones tens quarters dollars fives twenties])

(spec/fdef ->money
           :args (spec/cat :ones ::ones :tens ::tens :quarters ::quarters :dollars ::dollars :fives ::fives :twenties ::twenties)
           :ret ::money)

(defn total [m1 m2]
  (->money
    (+ (:ones m1) (:ones m2))
    (+ (:tens m1) (:tens m2))
    (+ (:quarters m1) (:quarters m2))
    (+ (:dollars m1) (:dollars m2))
    (+ (:fives m1) (:fives m2))
    (+ (:twenties m1) (:twenties m2))))

(defn amount [{:keys [ones tens quarters dollars fives twenties]}]
    (+ (* ones 0.01)
       (* tens 0.10)
       (* quarters 0.25)
       dollars
       (* fives 5)
       (* twenties 20)))
