(ns routing.handler
  (:require [compojure.core :refer [defroutes GET POST]]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [ring.util.response :as response]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.middleware.session :refer [wrap-session]]
            ;; [ring.middleware.session :as session]
            ;; [noir.session :as session]
            [routing.database-writes :as database-writes]
            [routing.layout :as layout]
            [routing.views.todo :as todo]
            [routing.views.registration :as registration]
            [routing.views.index :as index]
            [routing.views.login :as login]
            [routing.views.success :as success]))

(defroutes app-routes

  (GET "/" []
    (layout/application "My TODO" (index/index-page)))

  (GET "/register" []
    (layout/application "Registration" (registration/register-form)))

  (GET "/todo" req
    (let [{{:keys [first-name]}:session} req]
      (if (= nil first-name)
        (ring.util.response/redirect "/login")
        (layout/application "My Todo" (todo/todo-list first-name))
        ))
    )

  (GET "/login" []
    (layout/application "Login" (login/login-form)))

  (POST "/logout" [req]
   (-> (ring.util.response/redirect "/")
            (assoc :session (-> nil))))

  (POST "/get-user-data" req
    (routing.views.login/get-user-by-email-and-password req))

  (POST "/get-submit" req
    (database-writes/capture-user-registration
                         (:params req)))


  (POST "/add-item-by-user" req

     (database-writes/add-item req)
    )

  (POST "/delete-by-user" req

     (database-writes/delete-item req)
    )

   (POST "/done-by-user" req

     (database-writes/done-item req)
     )
  (POST "/edit-by-user" req

     (database-writes/edit-item req)
    )

 (GET "/success" []
   (layout/application "Your input" (success/display-success-registration)))


 (GET "/map" req
   (str req))

 (route/not-found "Not Found"))


(def app
  (-> app-routes
      (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))
      (wrap-session {:cookie-attrs {:max-age 3600}})
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
