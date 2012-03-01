(ns icarus.xml)

(defn solr-xml-docs [rows chunksize]
  (let [tags (sexp-as-element
              [:add {}
               (for [jj (take chunksize rows)]
                 (let [job (merge jj (job-buid jj))]
                   [:doc {}
                    [:field {:name "buid"} (str (:buid_id job))]
                    [:field {:name "city"} (:city job)]
                    [:field {:name "city_ac"} (:city job)]
                    [:field {:name "city_exact"} (:city job)]
                    [:field {:name "city_slab"}
                     (clojure.string/join
                      "/"
                      (concat [(:citySlug job)
                               (:stateSlug job)
                               (clojure.string/lower-case
                                (:country_short job))]
                              '("jobs")))]
                    [:field {:name "city_slab_exact"}
                     (clojure.string/join
                      "/"
                      (concat [(:citySlug job)
                               (:stateSlug job)
                               (clojure.string/lower-case
                                (:country_short job))]
                              '("jobs")))]
                    [:field {:name "company"} (:co_title job)]
                    [:field {:name "company_ac"} (:co_title job)]
                    [:field {:name "company_ac_exact"}
                     (:co_title job)]
                    [:field {:name "company_slab"}
                     (str (clojure.string/join
                           "/"
                           [(:title_slug job) "careers"])
                          "::"
                          (:co_title job))]
                    [:field {:name "company_slab_exact"}
                     (str (clojure.string/join
                           "/"
                           [(:title_slug job) "careers"])
                          "::"
                          (:co_title job))]
                    [:field {:name "country"} (:country job)]
                    [:field {:name "country_ac"} (:country job)]
                    [:field {:name "country_ac_exact"} (:country job)]
                    [:field {:name "country_short"} (:country_short job)]
                    [:field {:name "country_slab"}
                     (clojure.string/join
                      "/"
                      (concat [(clojure.string/lower-case
                                (:country_short job))]
                              '("jobs")))]
                    [:field {:name "country_slab_exact"}
                     (clojure.string/join
                      "/"
                      (concat [(clojure.string/lower-case
                                (:country_short job))]
                              '("jobs")))]
                    [:field {:name "date_new"} (str (:date_new job))]
                    [:field {:name "date_new_exact"} (str (:date_new job))]
                    [:field {:name "description"} (:description job)]
                    [:field {:name "django_ct"} "seo.joblisting"]
                    [:field {:name "django_id"} (str "seo.joblisting." (:id job))]
                    [:field {:name "full_loc"} (full-loc job)]
                    [:field {:name "full_loc_exact"} (full-loc job)]
                    [:field {:name "id"} (str (:id job))]
                    [:field {:name "location"} (:location job)]
                    [:field {:name "location_exact"} (:location job)]
                    [:field {:name "state_slab"}
                     (clojure.string/join
                      "/"
                      (concat [(:stateSlug job)
                               (clojure.string/lower-case
                                (:country_short job))]
                              '("jobs")))]
                    [:field {:name "state_slab_exact"}
                     (clojure.string/join
                      "/"
                      (concat [(:stateSlug job)
                               (clojure.string/lower-case
                                (:country_short job))]
                              '("jobs")))]
                    [:field {:name "text"}]
                    [:field {:name "title"} (:title job)]
                    [:field {:name "title_ac"} (:title job)]
                    [:field {:name "title_exact"} (:title job)]
                    [:field {:name "title_slab"} (title-slab job)]
                    [:field {:name "title_slab_exact"} (title-slab job)]
                    [:field {:name "title_slug"} (:titleSlug job)]
                    [:field {:name "uid"} (str (:uid job))]]))])]
    (with-open [out-file (java.io.FileWriter. "/tmp/foo.xml")]
      (emit tags out-file))))

