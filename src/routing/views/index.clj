(ns routing.views.index
 (:use [hiccup core page]
        [hiccup.element :only (link-to)]
        [hiccup.element :only (image)]
        )
  )

(defn index-page []
  [:div
   [:div {:class "jumbotron jumbotron-fluid"}
   [:div {:class "container"}
    [:h1 {:class "display-3"} "Justus Voigt - Clojure developer"]
    [:p {:class "lead"} "Welcome to my personal website. This will still be populated."]]]
  [:div {:class "media"}
   (image {:class "d-flex align-self-start mr-3"} "img/me.jpg")
   [:div {:class "media-body"}
    [:h5 {:class "mt-0"} "To be functional"]
    [:p "Lorem ipsum here"]
    [:p "lorem ipsum again"]]]]
  )
