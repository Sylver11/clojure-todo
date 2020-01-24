(ns routing.views.todo
   (:require [hiccup core page]
        [hiccup.element :refer (link-to)]
       ; [hiccup.element :only (image)]
        )
   )


(defn todo-list []
  [:div {:id "content"}
   [:h1 {:class "text-success"} "About"]
   (link-to {:class "btn btn-primary"} "/" "Home")])
