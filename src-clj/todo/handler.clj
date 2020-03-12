(ns todo.handler
  (:gen-class)
  (:require [compojure.core :refer [defroutes GET POST]]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [ring.util.response :as response]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.middleware.session :refer [wrap-session]]
            [todo.database-writes :as database-writes]
            [todo.layout :as layout]
            [todo.views.todo :as todo]
            [todo.views.registration :as registration]
            [todo.views.index :as index]
            [todo.views.todo-js :as todo-js]
            [todo.views.login :as login]
            [todo.views.todo-script :as todo-script])
  )


(defroutes app-routes
  (GET "/" req
    (layout/application "My TODO" (index/index-page) req))

  (GET "/register" req
    (layout/application "Registration" (registration/register-form) req))
  (GET "/todo" req
    (let [{{:keys [first-name]}:session} req]
      (if (= nil first-name)
        (ring.util.response/redirect "/login")
        (layout/application "My Todo" (todo/todo-list first-name) req)
        )))
  (GET "/login" req
    (let [{{:keys [first-name]}:session} req]
      (if (= nil first-name)
        (layout/application "Login" (login/login-form) req)
        (ring.util.response/redirect "/todo")
        )))
  (POST "/logout" [req]
   (-> (ring.util.response/redirect "/")
            (assoc :session (-> nil))))
  (POST "/get-user-data" req
    (todo.views.login/get-user-by-email-and-password req))
  (POST "/get-submit" req
    (database-writes/capture-user-registration
                         (:params req)))
  (POST "/add-item-by-user" req
     (database-writes/add-item req))
  (POST "/delete-by-user" req
     (database-writes/delete-item req))
  (POST "/done-by-user" req
     (database-writes/done-item req))
  (POST "/edit-by-user" req
     (database-writes/edit-item req))
  (GET "/map" req
    (str req))
  (GET "/todo-js" req
    (layout/application "My Clojure Script TOOD" (todo-js/js-page) req))

  (GET "/testing" req
    (layout/application "My Clojure Script TOOD" (todo-script/testing) req))


  ;; (GET "/testing" []
  ;;   (todo.views.testing/hello))

 (route/not-found "Not Found"))


(def app
  (-> app-routes
      (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))
      (wrap-session {:cookie-attrs {:max-age 3600}})
      ))

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

(defn -main []
  ;; 1. stop everything
  (stop-server!)
  ;; 2. "stuart sierra reloaded method" reload changed code
  ;; 3. start everything
  (start-server!))

#_(reset-server!)
