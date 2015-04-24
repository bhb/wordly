;; In order to avoid a dependency on an external datastore,
;; we just keep the count data in memory.

(ns wordly.storage)

(defonce store (atom {}))

(defn put [url data]
  (swap! store assoc url data))

(defn get [url]
  (clojure.core/get @store url))
