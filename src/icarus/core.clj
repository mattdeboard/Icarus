(ns icarus.core
  (:require [clojure-solr :as cs]
            [clojure.zip :as cz]
            [clojure.string]
            [clojure.walk]))

(def connectors {:or " OR "
                 :and " AND "
                 :not " NOT "})

(def default-op (:or connectors))

(defn paren-wrap [s]
  (str "(" s ")"))

(defn parse-vals [i]
  (str (name (first i)) ":"
       (if (sequential? (second i))
         (paren-wrap (clojure.string/join default-op (second i)))
         (second i))))
  
(defn map-to-query [m]
  "Converts '{:param value}' maps to Solr-ready querystring."
  (loop [k m
         acc ""]
    (if (map? k)
      (str acc (paren-wrap
                (clojure.string/join (:and connectors)
                                      (for [i m] (parse-vals i)))))
      (if (empty? (rest k))
        (str acc (map-to-query (first k)))
        (recur (rest k) (str acc (map-to-query (first k))))))))

(defn build-querystring [n]
  (let [k (-> n cz/children)
        s (clojure.walk/walk #(map-to-query %)
                             #(clojure.string/join (:and connectors) %)
                             (-> n cz/children))]
    (if (> 1 (count k))
      (paren-wrap s)
      s)))

(defn merge-queries [coll sep]
  "Merge a collection of query nodes with a logical connector, suitable
for passage to Solr."
  (paren-wrap (clojure.string/join sep (map build-querystring coll))))

(defn -| [coll] (merge-queries coll (:or connectors)))
(defn -& [coll] (merge-queries coll (:and connectors)))
    
(defmulti qfilter
  (fn [& args] (= clojure.lang.PersistentVector (type (first args)))))

(defmethod qfilter true [& args]
  (let [q (first args)
        m (rest args)]
    (cz/append-child q (list (apply hash-map m)))))

(defmethod qfilter false [& args]
  (cz/seq-zip (list (apply hash-map args))))

(defn -?> [url q params]
  (cs/with-connection (cs/connect url)
    (cs/search q params)))



