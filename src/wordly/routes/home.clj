(ns wordly.routes.home
  (:require [compojure.core :refer :all]
            [wordly.word-count :as wc]
            [wordly.storage :as storage]
            [wordly.validation :as validation]
            [wordly.views.layout :as layout]))

(defmacro dbg [body]
  `(let [x# ~body]
     (println "dbg:" '~body "=" x#)
          x#))

(defn home []
  (layout/common [:a {:href "/word-counts/new"}
                  "Count words at a URL"
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

(defn render-errors [errors]
  (when (not (empty? errors))
    [:div "Form could not be submitted"
      [:ul
       (list (map #(vector :li %) errors))
       ]
     ]))

(defn new-word-count-template [errors]
  (list
   (render-errors errors)
   [:form { :action "/word-counts" :method "POST"}
    [:label "URL"]
    [:input {:type "text" :name "url"}]
    [:input {:type "submit" :value "Submit"}]
    ]))

(defn show-word-count-template [url]
  (let [top-words (or (get storage/get url)
                      (wc/top-words url))]
    (list [:h1 "Words for " url]
          (table top-words))))

(defn new-word-count []
  (layout/common
   (new-word-count-template {})))

(defn create-word-count [request]
  (layout/common
   (let [url (get-in request [:params :url])
         errors (validation/validate-url url)]
     (if (not (empty? errors))
       (new-word-count-template errors)
       (show-word-count-template url)))))

(defroutes home-routes
  (GET "/" [] (home))
  (GET "/word-counts/new" [] (new-word-count))
  (GET "/word-counts" request (create-word-count request)) ;; TODO - replace with /word-counts/id
  (POST "/word-counts" request (create-word-count request)))
