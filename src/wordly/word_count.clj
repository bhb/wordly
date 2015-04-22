(ns wordly.word-count
  (:require [clj-http.client :as client]
            [clojure.string  :as string]
            [net.cgrand.enlive-html :as enlive]))

(def sample-html (client/get "http://example.com/"))

;; Stolen with minor modifications from Enlive
(defn text
  "Returns the text value of a node."
  [node]
  (cond
    (string? node) node
    (:tag node) (apply str (string/join " " (map text (:content node))))
    :else " "))

;; Stolen with minor modifications from Enlive
(defn texts
  "Returns the text value of a nodes collection."
  [nodes]
    (map text nodes))

;; NOTES:
;; (html-resource (java.io.StringReader. "<html><body><h1></h1></body></html>"))

#_(let [str (-> "http://google.com"
                java.net.URL.
                enlive/html-resource
                (enlive/select [:body])
                (enlive/transform [:script] nil)
                (enlive/transform [:style] nil)
                texts
                first
                )
        text (as-> str %
               (string/split % #"\s+")
               (remove string/blank? %)
               (map string/lower-case %)
               (frequencies %)
               (sort-by second %)
               (reverse %)
               (take 10 %)
                      )
        ]
    text
      ;;(string/split #"\w")
      )
