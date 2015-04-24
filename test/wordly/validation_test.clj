(ns wordly.validation-test
  (:require [wordly.validation :refer :all]
            [clojure.test :refer :all]))

(deftest test-validate-url
  (is (= ["URL cannot be blank"] (validate-url nil)))
  (is (= ["URL cannot be blank"] (validate-url "")))
  (is (= ["URL is invalid"] (validate-url "asdfasdfdsf")))
  (with-redefs [page-available? (constantly false)]
    (is (= ["Webpage is not available"] (validate-url "http://foo.com")))
    )
  (with-redefs [page-available? (constantly true)]
    (is (= [] (validate-url "http://foo.com")))
    ))
