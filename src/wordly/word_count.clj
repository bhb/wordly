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
       (re-seq #"[A-Za-z]+")
       (map string/lower-case)))

(defn all-text [resource]
  (-> resource
      (enlive/select [:body])
      (enlive/transform [:img] nil)
      (enlive/transform [:script] nil)
      (enlive/transform [:style] nil)
      texts
      first))

(defn occurences-then-word [[word occurrences]]
  [
   (* -1
      occurrences ) ;; First, sort by highest number of occurrences
   word             ;; then, sort alphabetically by word
   ]
  )

(defn top-words-from-str [str]
  (->> str
       text->words
       frequencies ;; generate an array like [["dog" 2] ["cat" 1]]
       (sort-by occurences-then-word)
       ))

(defn top-words [resource]
  (->> resource
       all-text
       top-words-from-str))

(defn top-words-from-url [url]
  (-> url
      java.net.URL.
      enlive/html-resource
      top-words))

;; My assumption is that "meaningful" means something outside
;; this (incomplete) list of these common words
(def non-meaningful-words
  #{
    "a"
    "an"
    "and"
    "as"
    "at"
    "be"
    "but"
    "by"
    "do"
    "for"
    "from"
    "have"
    "he"
    "her"
    "i"
    "i'm"
    "in"
    "is"
    "it"
    "me"
    "my"
    "not"
    "of"
    "on"
    "or"
    "say"
    "she"
    "that"
    "the"
    "they"
    "this"
    "to"
    "we"
    "with"
    "you"
    })

(defn meaningful-words [frequencies]
  (remove
   (fn [[word count]]
     (contains? non-meaningful-words word))
   frequencies))
