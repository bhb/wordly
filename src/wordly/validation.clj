(ns wordly.validation
  (:require
   [clojure.string :as string]
   [clj-http.client :as client])
  (:import [org.apache.commons.validator UrlValidator]))

(defn valid-url? [url]
  (let [validator (UrlValidator.)]
    (.isValid validator url)))

(defn page-available? [url]
  (-> url
      (client/head {:throw-exceptions false})
      :status
      (= 200)))

(defn validate-url [url]
  (cond
    (string/blank? url) ["URL cannot be blank"]
    (not (valid-url? url)) ["URL is invalid"]
    (not (page-available? url)) ["Webpage is not available"]
    :else []
  ))
