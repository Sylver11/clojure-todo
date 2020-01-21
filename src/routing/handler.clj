(ns routing.handler
  (:require [compojure.core :refer [defroutes GET POST]]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [routing.database-writes :as database-writes]
            [routing.layout :as layout]
            [routing.views.about :as about]
            [routing.views.experience :as experience]
            [routing.views.index :as index]
            [routing.views.portfolio :as portfolio]
            [routing.views.success :as success]))

(defroutes app-routes

  (GET "/" []
    (layout/application "Justus Voigt" (index/index-page)))

  (GET "/experience" []
    (layout/application "My experience" (experience/experience-page)))

  (GET "/about" []
    (layout/application "Just about me" (about/about-page)))

  (GET "/portfolio" []
    (layout/application "My projects" (portfolio/portfolio-page)))

  (POST "/get-submit" req
    (layout/application "Your input"
                        (database-writes/capture-user-registration
                         (:params req))))

  (GET "/success" []
    (layout/application "Your input" (success/display-success-registration)))

  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes
                 (assoc-in site-defaults [:security :anti-forgery] false)))

(defonce *server (atom nil))

(defn start-server! []
  (reset! *server
          (jetty/run-jetty #'app
                           {:port  4000
                            :join? false})))

(defn stop-server! []
  (when-some [server @*server]
    (.stop server))
  (reset! *server nil))

(defn reset-server! []
  ;; 1. stop everything
  (stop-server!)
  ;; 2. "stuart sierra reloaded method" reload changed code
  ;; 3. start everything
  (start-server!))

#_(reset-server!)
