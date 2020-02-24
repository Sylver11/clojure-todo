(defproject todo "0.1.0-SNAPSHOT"
  :description "A clojure todo app"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :repositories {"my.datomic.com" {:url "https://my.datomic.com/repo"
                                   :creds :gpg}}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.520"]
                 [compojure "1.6.1"]
                 [hiccup "1.0.0"]
                 [ring/ring-defaults "0.3.2"]
                 [com.datomic/datomic-pro "0.9.5951"]
                 [ring/ring-jetty-adapter "1.6.3"]
                 [org.eclipse.jetty/jetty-server "9.3.7.v20160115"]
                 [clj-time "0.15.2"]]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler todo.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}})
