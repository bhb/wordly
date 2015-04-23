(ns wordly.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [wordly.handler :refer :all]))

(defn contains? [^String haystack ^String needle]
  (.contains haystack needle))

(deftest test-app
  (testing "main route"
    (let [response (app (mock/request :get "/"))]
      (is (= (:status response) 200))))

  (testing "valid URL"
    (let [response (app (mock/request :get "/word-counts?url=http://example.com"))]
      (is (= (:status response) 200))
      (is (contains? (:body response) "http://example.com"))
      (is (contains? (:body response) "established"))))

  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))


