(ns routing.handler
  (:require [compojure.core :refer [defroutes GET POST]]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.middleware.session :refer [wrap-session]]
            [routing.database-writes :as database-writes]
            [routing.layout :as layout]
            [routing.views.todo :as todo]
            [routing.views.registration :as registration]
            [routing.views.index :as index]
            [routing.views.portfolio :as portfolio]
            [routing.views.success :as success]))

(defroutes app-routes

  (GET "/" []
    (layout/application "Justus Voigt" (index/index-page)))

  (GET "/register" []
    (layout/application "Register here" (registration/register-form)))

  (GET "/my-todo" []
    (layout/application "My Todo list" (todo/todo-list)))

  (GET "/portfolio" []
    (layout/application "My projects" (portfolio/portfolio-page)))

  (POST "/get-user-data" req
    (layout/application ""
                        (routing.views.login/get-user-by-email-and-password
                         (:params req))))

  (POST "/get-submit" req
    (layout/application "Your input"
                        (database-writes/capture-user-registration
                         (:params req))))

  (GET "/success" []
    (layout/application "Your input" (success/display-success-registration)))

  (route/not-found "Not Found"))

(def app1
  (wrap-defaults app-routes
                 (assoc-in site-defaults [:security :anti-forgery] false)))

(def app
  (-> app-routes
      (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))
      (wrap-session {:cookie-attrs {:max-age 3600}}))
  )

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
