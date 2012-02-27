Icarus
======

A robust Solr query API for Clojure.


Example queries
===============

.. source::

  (do-search "http://127.0.0.1:8983" (build-queryset (filter :city ["Indianapolis"
                                                                    "Muncie"]))
             {:rows 10 :fl "city,population,state"})


Composable queries
==================

.. source::

  (def query1 (filter :city "Springfield"))
  (def query2 (filter :state "Florida"))

  ;; Join the two query nodes with logical AND via ``-&`` or logical OR
  ;; via ``-|``
  (do-search "http://127.0.0.1:8983" (-& [query1 query2])
             {:rows 10 :fl "city,population,state"})

Credits
=======

Icarus is built on top of `Clojure-Solr <https://github.com/gilesc/clojure-solr>`_. The Icarus API is a more flexible interface to perform search via Solr, with a focus on expressing queries of arbitrary complexity in a clean, simple way.