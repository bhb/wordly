;; In order to avoid a dependency on an external datastore,
;; we just keep the count data in memory.

(ns wordly.storage
  (:require [clojure.set :as set]))

(defn has-key? [store url]
  (contains? @store url))

(defn set [store url data]
  (swap! store assoc url data))

(defn get [store url]
  (clojure.core/get @store url))

(defn all [store]
  (vec @store))

(defn init [store]
  (reset! store {})
  store
  )

(defn fetch-memoized [store key value-fn]
  (when (not (has-key? store key))
    (set store key (value-fn)))
  (get store key))
