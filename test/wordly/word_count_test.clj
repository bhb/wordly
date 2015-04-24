(ns wordly.word-count-test
  (:require [wordly.word-count :refer :all]
            [net.cgrand.enlive-html :as enlive]
            [clojure.test :refer :all]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.clojure-test :refer [defspec]]
            ))

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
  (is (= ["foo" "foo" "bar"] (text->words "FOO foo    baR")))
  (is (= ["hello" "world"] (text->words "Hello. World!")))
  )

(deftest test-top-words
  (let [top-words (top-words-from-file "test/wordly/data/example.html")]
    (is (= ["domain" 3] (first top-words)))
  (testing "ignores words in script tags"
    (let [top-words (top-words-from-html "<body>foo<script>bar baz</script></body>")]
      (is (= 1 (count top-words)))
      (is (= ["foo" 1] (first top-words)))))))

(deftest test-top-words-from-str
  (is (= [] (top-words-from-str "")))
  (is (= [["foo" 1]] (top-words-from-str "Foo")))
  (is (= [["foo" 2]] (top-words-from-str "Foo foo")))
  (is (= [["hello" 1] ["world" 1]] (top-words-from-str "Hello world!"))))

(defspec check-top-words-from-str
  1000 ;; the number of iterations for test.check to test
  (prop/for-all [str gen/string-ascii]
                (let [top-words (top-words-from-str str)
                      occurrences (or (vals top-words) [])]
                  (and (<= 0 (count top-words) 10)
                       (=
                        (reverse (sort occurrences))
                        occurrences)))))
