(ns routing.views.error
 (:use [hiccup core page]
        [hiccup.element :only (link-to)]
        [hiccup.element :only (image)]
        )
  )
 (defn not-found []
     [:h1 {:class "info-warning"} "Soz cant find that page you are looking for"]
  [:p "There's no requested page. "]
     (link-to {:class "btn btn-primary"} "/" "Take me to Home"))
