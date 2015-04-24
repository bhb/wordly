;; In order to avoid a dependency on an external datastore,
;; we just keep the count data in memory.

(ns wordly.storage
  (:require [clojure.set :as set]))

(def store (atom {}))

(defn has-key? [url]
  (contains? @store url))

(defn set [url data]
  (swap! store assoc url data))

(defn get [url]
  (clojure.core/get @store url))

(defn all []
  (vec @store))
