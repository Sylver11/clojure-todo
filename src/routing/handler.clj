(ns routing.handler
  (:require [compojure.core :refer [defroutes GET POST]]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [ring.util.response :as response]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            ;; [ring.middleware.session :refer [wrap-session]]
            [noir.session :as session]
            [routing.database-writes :as database-writes]
            [routing.layout :as layout]
            [routing.views.todo :as todo]
            [routing.views.registration :as registration]
            [routing.views.index :as index]
            [routing.views.login :as login]
            [routing.views.success :as success]
            [routing.views.logout :as logout]))

(defroutes app-routes

  (GET "/" []
    (layout/application "My TODO" (index/index-page)))

  (GET "/register" []
    (layout/application "Registration" (registration/register-form)))

  (GET "/my-todo" []
    (if (= nil (session/get :username))
      (response/redirect "/login")
      (layout/application "My Todo list" (todo/todo-list)) )
    )

  (GET "/login" []
    (layout/application "Login" (login/login-form)))

  (POST "/logout" []
   (do (session/remove! :username)
        (response/redirect "/")
    ;; (layout/application "Logout" (logout/success))
        ))

  (POST "/get-user-data" req
    (routing.views.login/get-user-by-email-and-password
                         (:params req)))

  (POST "/get-submit" req
    (database-writes/capture-user-registration
                         (:params req)))

  (GET "/success" []
    (layout/application "Your input" (success/display-success-registration)))

  (route/not-found "Not Found"))


(def app
  (-> app-routes
      (session/wrap-noir-session)
      (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))
      ;; (wrap-session {:cookie-attrs {:max-age 3600}})
      )
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
