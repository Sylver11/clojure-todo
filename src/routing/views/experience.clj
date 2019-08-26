(ns routing.views.experience
 (:use [hiccup core page]
        [hiccup.element :only (link-to)]
        [hiccup.element :only (image)]
        [hiccup.form :only (form-to)]
        [hiccup.core :refer [html h]]
        [ring.util.response :only (response)]
        )
 )




(defn display-result [req]
  (println "Request map:" req)
   (let [{{firstname :firstname secondname :secondname} :params} req]
    (html
    [:div
     [:h1 "Hello " (h firstname secondname) "!"]
     ])))




(defn experience-page []
  (html
   [:form {:method "get" :action "get-submit"}
    [:div {:class "form-group"}
            [:label {:for "formGroupExampleInput"} "First Name"]
            [:input {:type "text" :name "firstname" :class "form-control" :id "formGroupExampleInput" :placeholder "eg Justus"}
             ]]
           [:div {:class "form-group"}
            [:label {:for "formGroupExampleInput2"} "Second Name"]
            [:input {:type "text" :name "secondname" :class "form-control" :id "formGroupExampleInput2" :placeholder "eg Voigt"}
             ]]
           [:button {:type "submit" :class "btn btn-success"} "Submit"]
 ]

   ))



