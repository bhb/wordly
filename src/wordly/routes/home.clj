(ns wordly.routes.home
  (:require [compojure.core :refer :all]
            [wordly.views.layout :as layout]))

(defn home []
  (layout/common [:h1 "Wordly"]
                 [:form
                  [:label "URL"]
                  [:input {:type "text"}]
                  [:input {:type "submit" :value "Submit"}]
                  ]
                 ))

(defroutes home-routes
  (GET "/" [] (home)))
