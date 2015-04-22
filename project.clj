(defproject wordly "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [
                 [clj-http "1.1.0"]
                 [compojure "1.1.6"]
                 [enlive "1.1.5"]
                 [hiccup "1.0.5"]
                 [ring-server "0.3.1"]
                 [org.clojure/clojure "1.6.0"]
                 ]
  :plugins [[lein-ring "0.8.12"]]
  :ring {:handler wordly.handler/app
         :init wordly.handler/init
         :destroy wordly.handler/destroy}
  :profiles
  {:uberjar {:aot :all}
   :production
   {:ring
    {:open-browser? false, :stacktraces? false, :auto-reload? false}}
   :dev
   {:dependencies [[ring-mock "0.1.5"] [ring/ring-devel "1.3.1"]]}})
