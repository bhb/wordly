(ns wordly.routes.home
  (:require [compojure.core :refer :all]
            [wordly.word-count :as wc]
            [wordly.storage :as storage]
            [wordly.validation :as validation]
            [wordly.views.layout :as layout])
  (:import [java.net URLEncoder])
  )

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

;; TODO - rename this 'top-words'
(defn frequencies-table [frequencies]
  [:table
   [:tr
    [:th "Word"]
    [:th "Occurences"]
    ]
   (map row frequencies)
   ])

(defn urls-table [urls-and-frequencies]
  [:table
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
                  "Count words at a URL"
                  ]
                 (urls-table (storage/all))
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

;; TODO - move to storage
(defn memoize-top-words [url]
  (when (not (storage/has-key? url))
    (storage/set url (wc/top-words-from-url url)))
  (storage/get url))

(defn show-word-count-template [url]
  (let [top-words (memoize-top-words url)]
    (list [:h3 "Words for " url]
          (frequencies-table top-words))))

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
