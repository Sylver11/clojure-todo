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
     [:label {:class "mt-3" :for "customRange3"} "How well do you fell today?"]
     [:input {:type "range" :name "feeling" :class "custom-range" :min "0" :max "5" :step "1" :id "customRange3"}]
     ]
           [:button {:type "submit" :class "btn btn-success"} "Submit"]]))




(defn display-result [req]
 (println "Request map:" req)
  (let [{{:keys [firstname secondname feeling]}:params} req
        feels (cond
                (= feeling "1"):down
                (= feeling "2"):sad
                (= feeling "3"):okayisch
                (= feeling "4"):great
                (= feeling "5"):excellent)
        ]
    (html
    [:div
     [:h1 "Hello " (h firstname) " " (h secondname) "!"]
     [:h2 "You are feeling " (h feels)]
     [:p "Would you like to write this to the database?"]
     [:form {:method "POST" :action "submit"}
       [:button {:type "submit" :class "btn btn-success"} "Submit"]]
     ])))

