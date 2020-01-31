(ns routing.views.logout
  (:require [hiccup.core :as hiccup]
            [noir.session :as session]
        ))




(defn success []
  (session/clear!)
  (hiccup/html
   [:p "Success you have been logged out!"]
   [:a {:href "/login"}] "Back to Login"))

