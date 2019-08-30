(ns routing.handler
 ;; (:use routing.views)
  (:require [compojure.core :refer :all]
            [compojure.core :refer [defroutes GET POST]]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [routing.layout :as layout]
            [routing.views.about :as about]
            [routing.views.index :as index]
            [routing.views.portfolio :as portfolio]
            [routing.views.experience :as experience]
            [routing.views.success :as success]
            [routing.views.error :as error]
            )
  )



(defroutes app-routes
  (GET "/" []   (layout/application "Justus Voigt" (index/index-page)))
 (GET "/experience" [] (layout/application "My experience" (experience/experience-page)))
  (GET "/about" [] (layout/application "Just about me" (about/about-page)))
  (GET "/portfolio" [] (layout/application "My projects" (portfolio/portfolio-page)))
  ;; (POST "/get-submit" req (layout/application "Your input" (experience/display-result req)))
   (POST "/get-submit" req (layout/application "Your input" (success/user-input req)))
 ;; (POST "/submit" req (layout/application "Your input" (success/entity req)))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes (assoc-in site-defaults [:security :anti-forgery] false)))



