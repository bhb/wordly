(ns wordly.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [wordly.handler :refer :all]))

(defn contains-str? [^String haystack ^String needle]
  (.contains haystack needle))

;; NOTE - these are integration tests and do actually access
;; external URLs. In a real system, I'd try probably store the
;; responses locally similar to how Ruby's VCR library works.
(deftest test-app
  (testing "main route"
    (let [response (app (mock/request :get "/"))]
      (is (= (:status response) 200))
      (is (contains-str? (:body response) "Count words at new URL"))))

  (testing "valid URL"
    (let [response (app (mock/request :post "/word-counts?url=http://example.com"))]
      (is (= (:status response) 200))
      (is (contains-str? (:body response) "http://example.com"))
      (is (contains-str? (:body response) "established"))))

  (testing "invalid URL"
    (let [response (app (mock/request :post "/word-counts?url=foobar"))]
      (is (= (:status response) 200))
      (is (contains-str? (:body response) "URL is invalid"))))

  (testing "URL which does not exist"
    (let [response (app (mock/request :post "/word-counts?url=http://example.com/foobar"))]
      (is (= (:status response) 200))
      (is (contains-str? (:body response) "not available"))))

  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))
