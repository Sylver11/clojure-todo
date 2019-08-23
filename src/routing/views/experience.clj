(ns routing.views.experience
 (:use [hiccup core page]
        [hiccup.element :only (link-to)]
        [hiccup.element :only (image)]
        [hiccup.form :only (form-to)]
        [hiccup.core :refer [html h]]
        [ring.util.response :only (response)]
        )
 )






(defn experience-page []
  (html
   [:form {:method "get" :action "get-submit"}
       [:input {:type "text" :name "name"}]
       [:input {:type "submit" :value "submit"}]])
  )




(defn display-result [req]
  (let [{:keys [params]} req
        param-name (get params "name")
        ]
   (html
    [:div
     [:h1 "Hello " (h param-name) "!"]
     ])))



;; [:div {:class "form-group"}
;;             [:label {:for "formGroupExampleInput"} "First Name"]
;;             [:input {:type "text" :class "form-control" :id "formGroupExampleInput" :placeholder "eg Justus"}
;;              ]]
;;            [:div {:class "form-group"}
;;             [:label {:for "formGroupExampleInput2"} "Second Name"]
;;             [:input {:type "text" :class "form-control" :id "formGroupExampleInput2" :placeholder "eg Voigt"}
;;              ]]
;;            [:button {:type "button" :class "btn btn-success"} "Submit"]
