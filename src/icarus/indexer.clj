(ns icarus.indexer
  (:require [clojure.string])
  (:use [clojure.data.xml]
        [korma.core]
        [korma.db]
        [clojure-solr]))


(defn solr-doc [job]
  ;; This function should return a map the keys for which are fields from
  ;; Solr's schema.xml, and the values are a (:key coll) lookup along with
  ;; any preparatory fns for that data. I have left in some examples from
  ;; a working fn for illustration.
  ;;
  ;; The name 'job' reflects the actual use case I wrote the original
  ;; code for, as I work at an employment-oriented not-for-profit.
  ;; As I continue generalizing this it'll change.
  {:city (:city job)
   :city_slab (clojure.string/join "/"
                                   (concat [(:citySlug job)
                                            (:stateSlug job)
                                            (clojure.string/lower-case
                                             (:country_short job))]
                                           '("jobs")))
   :company (:co_title job)
   :company_slab_exact (str (clojure.string/join "/" [(:title_slug job) "careers"])
                            "::" (:co_title job))
   :date_new (:date_new job)})

(defn update-index [rows chunksize]
  "Converts db rows to solr docs then transmits them to solr for update."
  (loop [[head tail] (split-at chunksize rows)
         cnt 0]
    (with-connection (connect solr-server)
      (add-documents! (->> head (map #(solr-doc %))))
      (commit!))
    (if (not= 0 (count tail))
      (recur (split-at chunksize tail) (inc cnt))
      (str cnt " blocks processed."))))
