(ns todo.views.registration
  (:require [hiccup.core :as hiccup]
            [datomic.api :as d]
            [todo.database-writes :as database-writes]))

(defn register-form []
  (hiccup/html
   [:form {:method "POST" :action "get-submit"}
    [:div {:class "form-group"}
     [:label {:for "first-name"} "First Name"]
     [:input {:type        "text"
              :name        "first-name"
              :class       "form-control"
              :id          "first-name"
              :placeholder "eg Justus"}]]

    [:div {:class "form-group"}
     [:label {:for "formGroupExampleInput2"} "Second Name"]
     [:input {:type "text" :name "last-name" :class "form-control"
              :id "formGroupExampleInput2" :placeholder "eg Voigt"}
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
     [:input {:type "password" :name "confirm" :class "form-control" :id "formGroupExampleInput2" :placeholder "eg MyPasswordIsCrazyStrong.007"}
      ]]
    [:button {:type "submit" :class "btn btn-success"} "Register"]]))


