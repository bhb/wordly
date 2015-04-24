(ns wordly.routes.home
  (:require [compojure.core :refer :all]
            [wordly.word-count :as wc]
            [wordly.storage :as storage]
            [wordly.validation :as validation]
            [wordly.views.layout :as layout])
  (:import [java.net URLEncoder])
  )

(defonce STORE (atom nil))

(defn url-encode [str]
  (URLEncoder/encode str "UTF-8"))

(defn details-link [url]
  [:a
   {:href (str "/word-counts?url=" (url-encode url)) }
   "Details"])

(defn row-data [[url frequencies]]
  (let [[top-word occurences] (first frequencies)]
    [
     url
     top-word
     occurences
     (details-link url)
     ]))

(defn row [xs]
  [:tr
   (map (fn [x] [:td x]) xs)])

(defn top-words-table [frequencies]
  [:table.table
   [:tr
    [:th "Word"]
    [:th "Occurences"]
    ]
   (map row frequencies)
   ])

(defn urls-table [urls-and-frequencies]
  [:table.table
   [:tr
    [:th "URL"]
    [:th "Most common word"]
    [:th "Occurences"]
    [:th "Details"]
    (->> urls-and-frequencies
        (map row-data)
        (map row))
    ]])

(defn home []
  (layout/common [:a {:href "/word-counts/new"}
                  "Count words at new URL"
                  ]
                 (urls-table (storage/all STORE))
                 ))

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

(defn memoize-top-words [url]
  (storage/fetch-memoized STORE
                          url
                          #(wc/top-words-from-url url)))

(defn show-word-count-template [url]
  (let [top-words (memoize-top-words url)]
    (list [:h3 "Words at " url]
          (top-words-table top-words))))

(defn new-word-count []
  (layout/common
   (new-word-count-template {})))

(defn show-word-count [request]
  (layout/common
   (let [url (get-in request [:params :url])]
         (if (storage/get STORE url)
           (show-word-count-template url)
           [:h1 "URL NOT FOUND"]))))

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
  (GET "/word-counts" request (show-word-count request))
  (POST "/word-counts" request (create-word-count request)))

(defn init []
  (storage/init STORE))
