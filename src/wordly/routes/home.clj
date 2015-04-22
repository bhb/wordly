(ns wordly.routes.home
  (:require [compojure.core :refer :all]
            [wordly.views.layout :as layout]))

(defn home []
  (layout/common [:h1 "Wordly"]
                 [:form { :action "/word-counts" :method "POST"}
                  [:label "URL"]
                  [:input {:type "text" :name "url"}]
                  [:input {:type "submit" :value "Submit"}]
                  ]
                 ))

(defn new-word-count [request]
  (let [url (get-in request [:params :url])]
    (println request)
    (layout/common [:h1 "Words for" url]
                   [:p url]
                   )))

(defroutes home-routes
  (GET "/" [] (home))
  (POST "/word-counts" request (new-word-count request))
  )
