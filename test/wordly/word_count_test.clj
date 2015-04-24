(ns wordly.word-count-test
  (:require [wordly.word-count :refer :all]
            [net.cgrand.enlive-html :as enlive]
            [clojure.test :refer :all]))

;; Helper function for testing
(defn top-words-from-html [html]
  (-> html
      java.io.StringReader.
      enlive/html-resource
      top-words))

(defn top-words-from-file [filename]
  (-> filename
      slurp
      top-words-from-html))

(deftest test-text->words
  (is (= ["foo" "foo" "bar"] (text->words "FOO foo    baR"))))

(deftest test-top-words
  (let [top-words (top-words-from-file "test/wordly/data/example.html")
        [top-word occurrences] (first top-words)]
    (is (= 10 (count top-words)))
    (is (= "domain" top-word))
    (is (= 3 occurrences))))
