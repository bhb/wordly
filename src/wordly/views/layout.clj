(ns wordly.views.layout
  (:require [hiccup.page :refer [html5 include-css]]))

(defn common [& body]
  (html5
    [:head
     [:title "Welcome to wordly"]
     (include-css "/css/bootstrap.css")]
    [:body
     [:div.container
      [:h1
       [:a {:href "/"}
        "Wordly"]]
      body]]
    ))
