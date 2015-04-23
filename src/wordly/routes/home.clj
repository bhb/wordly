(ns wordly.routes.home
  (:require [compojure.core :refer :all]
            [wordly.word-count :as wc]
            [wordly.views.layout :as layout]))

(defn home []
  (layout/common [:h1 "Wordly"]
                 [:form { :action "/word-counts" :method "POST"}
                  [:label "URL"]
                  [:input {:type "text" :name "url"}]
                  [:input {:type "submit" :value "Submit"}]
                  ]
                 ))

(defn row [pair]
  (let [[word count] pair]
    [:tr
     [:td word]
     [:td count]
     ]))

(defn table [frequencies]
  [:table
   [:tr
    [:th "Word"]
    [:th "Occurences"]]
   (map row frequencies)
   ])

(defn new-word-count [request]
  (let [url (get-in request [:params :url])]
    (layout/common [:h1 "Words for " url]
                   (table (wc/top-words url)))))

(defroutes home-routes
  (GET "/" [] (home))
  (GET "/word-counts" request (new-word-count request))
  (POST "/word-counts" request (new-word-count request)))
