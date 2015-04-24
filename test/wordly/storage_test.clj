(ns wordly.storage-test
  (:require [wordly.storage :as s]
            [clojure.test :refer :all]))

(deftest test-all
  (let [store (s/init (atom nil))]
    (s/set store :x 1)
    (s/set store :y 2)
    (is (= [[:y 2] [:x 1]] (s/all store)))))

(deftest test-fetch-memoized
  (let [store (s/init (atom nil))]
    (s/set store :x 1)
    (is (= 1 (s/fetch-memoized store :x (constantly 1))))
    (is (= 1 (s/fetch-memoized store :x (constantly 2))))
    (is (= nil (s/get store :y)))
    (is (= 3 (s/fetch-memoized store :y (constantly 3))))))
