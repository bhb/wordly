;; Handy helper functions
(ns wordly.util)

word-count(defmacro dbg [body]
  `(let [x# ~body]
     (println "dbg:" '~body "=" x#)
          x#))
