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
   [:form {:method "POST" :action "get-submit"}
    [:div {:class "form-group"}
            [:label {:for "formGroupExampleInput"} "First Name"]
            [:input {:type "text" :name "firstname" :class "form-control" :id "formGroupExampleInput" :placeholder "eg Justus"}
             ]]
           [:div {:class "form-group"}
            [:label {:for "formGroupExampleInput2"} "Second Name"]
            [:input {:type "text" :name "secondname" :class "form-control" :id "formGroupExampleInput2" :placeholder "eg Voigt"}
             ]]
    [:div {:class "form-group"}
     [:label {:for "formGroupExampleInput2"} "Email"]
     [:input {:type "email" :name "email" :class "form-control" :id "formGroupExampleInput2" :placeholder "eg justus@cognician.com"}
      ]]
    [:div {:class "form-group"}
     [:label {:for "formGroupExampleInput2"} "Password"]
     [:input {:type "password" :name "password" :class "form-control" :id "formGroupExampleInput2" :placeholder "eg MyPasswordIsCrazyStrong.007"}
      ]]
    [:div {:class "form-group"}
     [:label {:for "formGroupExampleInput2"} "Confirm Password"]
     [:input {:type "password" :name "password-confirm" :class "form-control" :id "formGroupExampleInput2" :placeholder "eg MyPasswordIsCrazyStrong.007"}
      ]]
           [:button {:type "submit" :class "btn btn-success"} "Submit"]]))



