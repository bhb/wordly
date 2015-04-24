(ns wordly.views.layout
  (:require [hiccup.page :refer [html5 include-css]]))

(defn common [& body]
  (html5
    [:head
     [:title "Welcome to wordly"]
     (include-css "/css/screen.css")]
    [:body
     [:h1
      [:a {:href "/"}
       "Wordly"]]
     body]))
