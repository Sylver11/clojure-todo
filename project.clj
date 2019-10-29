(defproject routing "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :repositories {"my.datomic.com" {:url "https://my.datomic.com/repo"
                                 :creds :gpg}}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [compojure "1.6.1"]
                 [hiccup "1.0.0"]
                 [ring/ring-defaults "0.3.2"]
                 [com.datomic/datomic-pro "0.9.5951"]]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler routing.handler/app}
  :main routing.database-writes
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}})
