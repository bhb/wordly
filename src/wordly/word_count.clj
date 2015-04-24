(ns wordly.word-count
  (:require [clj-http.client :as client]
            [clojure.string  :as string]
            [net.cgrand.enlive-html :as enlive]))

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

;; Note: only works for ASCII words
(defn text->words [text]
  (->> text
       (re-seq #"\w+")
       (map string/lower-case)))

(defn all-text [resource]
  (-> resource
      (enlive/select [:body])
      (enlive/transform [:img] nil)
      (enlive/transform [:script] nil)
      (enlive/transform [:style] nil)
      texts
      first))

(defn sort-fn [[word occurrences]]
  [
   (* -1
      occurrences ) ;; First, sort by highest number of occurances
   word             ;; then, sort alphabetically by word
   ]
  )

;; TODO - generatively test this
(defn top-words-from-str [str]
  (->> str
       text->words
       frequencies ;; generate pairs like ["dog" 2]
       (sort-by sort-fn)
       (take 10)))

(defn top-words [resource]
  (->> resource
      all-text
      top-words-from-str))

(defn top-words-from-url [url]
  (-> url
      java.net.URL.
      enlive/html-resource
      top-words))
