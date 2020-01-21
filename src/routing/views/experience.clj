(ns routing.views.experience
  (:require [hiccup.core :as hiccup]
            [datomic.api :as d]
            [routing.database-writes :as database-writes]))

(defn experience-page []
  (hiccup/html
   [:div
    [:ul
     (for [client (d/q '[:find ?fn ?ln ?em
                         :keys first-name last-name email
                         :where
                         [?c :client/first-name ?fn]
                         [?c :client/last-name ?ln]
                         [?c :client/email ?em]]
                       (database-writes/db))]
       [:li [:strong (:first-name client) " "
             (:last-name client)] [:br]
        [:a {:href (str "mailto:" (:email client))}
         (:email client)]])]]
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
    [:button {:type "submit" :class "btn btn-success"} "Submit"]]))



