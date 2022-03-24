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

(defn make-money
  ([money]
   (if (spec/valid? ::money money)
     money
     (throw Exception)))
  ([ones tens quarters dollars fives twenties]
   (make-money (->money ones tens quarters dollars fives twenties))))

(defn total [m1 m2]
  (make-money
    (+ (:ones m1) (:ones m2))
    (+ (:tens m1) (:tens m2))
    (+ (:quarters m1) (:quarters m2))
    (+ (:dollars m1) (:dollars m2))
    (+ (:fives m1) (:fives m2))
    (+ (:twenties m1) (:twenties m2))))

(defn amount [{:keys [ones tens quarters dollars fives twenties]}]
  (parse-long (format "%.2f"
                      (+ (* ones 0.01)
                         (* tens 0.10)
                         (* quarters 0.25)
                         dollars
                         (* fives 5)
                         (* twenties 20)))))

(amount (make-money 1 2 0 0 0 0))
