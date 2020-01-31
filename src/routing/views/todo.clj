(ns routing.views.todo
  (:require [hiccup core page]
            [hiccup.element :refer (link-to)]
            [noir.session :as session]
            [noir.response :as resp]
                                        ; [hiccup.element :only (image)]

            [hiccup.core :as hiccup])
  )





(defn todo-list []
  (hiccup/html
   (if (clojure.string/blank? (str (session/get :username)))
     [:div {:id "content"}
      [:h1 {:class "text-success"} "Unfortunelty you are not logged in."]
      (link-to {:class "btn btn-primary"} "/login" "Login here")]
     [:div
      [:h1 "Welcome " (session/get :username)]
      [:p "this is your personal Todo list"]]
     )))

