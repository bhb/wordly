;; Handy helper functions
(ns wordly.util)

(defmacro dbg [body]
  `(let [x# ~body]
     (println "dbg:" '~body "=" x#)
          x#))
