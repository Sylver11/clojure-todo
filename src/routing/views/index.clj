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
     [:span ]

    ;; [:h1 {:class "display-3"} "TODO App"]


      [:ul {:style "list-style-type: none;"}
       [:li [:div {:class "media"}
   (image {:class "d-flex align-self-start mr-3"} "img/me.jpg")
   [:div {:class "media-body"}
    [:h5 {:class "mt-0"} "Hi there, my name is Justus Voigt"]
    [:p {:class ""} "Welcome to my Clojure todo app. Check it out on Github: "   [:span [:a {:style "display:inline;" :href " https://github.com/sylver11/clojure-todo " } "clojure-todo"]]   ]

    [:p {:class "lead"} "The project runs on the following stack: "]
    ]]]

       [:li [:a {:class "list-group-item list-group-item-action list-group-item-light" :href "https://github.com/ring-clojure/ring" :target "_blank"  } "ring-clojure"]]
                                                                                                     [:li [:a {:class "list-group-item list-group-item-action list-group-item-light" :href "https://www.datomic.com/"  :target "_blank"  } "datomic"]]
                                                                                                     [:li [:a {:class "list-group-item list-group-item-action list-group-item-light" :href "https://github.com/weavejester/compojure"  :target "_blank"  } "compojure"]]
                                                                                                     [:li [:a {:class "list-group-item list-group-item-action list-group-item-light" :href "https://github.com/weavejester/hiccup"  :target "_blank"  } "hiccup"]]
                                                                                                     [:li [:a {:class "list-group-item list-group-item-action list-group-item-light" :href "https://github.com/clj-time/clj-time"  :target "_blank"  } "clj-time"]]
                                                                                                     [:li [:a {:class "list-group-item list-group-item-action list-group-item-light" :href "https://getbootstrap.com/"  :target "_blank"  } "bootstrap"]]
                                                                                                     ]]]]

  )
