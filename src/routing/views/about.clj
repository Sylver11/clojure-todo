(ns routing.views.about
   (:use [hiccup core page]
        [hiccup.element :only (link-to)]
        [hiccup.element :only (image)]
        )
  )

(defn about-page []
  [:div {:id "content"}
   [:h1 {:class "text-success"} "About"]
   (link-to {:class "btn btn-primary"} "/" "Home")])
