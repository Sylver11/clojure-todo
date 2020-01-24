(ns routing.views.login
  (:require [hiccup.core :as hiccup]
            [datomic.api :as d]
            [routing.database-writes :as database-writes]
            [ring.util.response :as response]))


(defn login-form []
  (hiccup/html
   [:div
    [:form {:method "POST" :action "get-user-data"}
    [:div {:class "form-group"}
     [:label {:for "formGroupExampleInput2"} "Email"]
     [:input {:type "email" :name "email" :class "form-control" :id "formGroupExampleInput2" :placeholder "eg justus@cognician.com"}
      ]]
    [:div {:class "form-group"}
     [:label {:for "formGroupExampleInput2"} "Password"]
     [:input {:type "password" :name "password" :class "form-control" :id "formGroupExampleInput2" :placeholder "eg MyPasswordIsCrazyStrong.007"}
      ]]
    [:button {:type "submit" :class "btn btn-success"} "Login"]]
    ]))




(defn get-user-by-email-and-password [{:keys [email password]}]
  (hiccup/html
   [:div
    (let [client (d/q '[:find ?uuid ?first-name
                      :keys  uuid first-name
                      :where
                      [?c :client/email ?email]
                      [?c :client/password ?password]
                      [?c :client/uuid ?uuid]
                      [?c :client/first-name ?first-name]
                      ]
                      (database-writes/db))]
      (if (clojure.string/blank? (str (:uuid client)))
        [:p "It is blank"]
        [:p "It is not blank"]
        ;; (do (response/redirect "/my-todo")
        ;;      (println "redirecting"))
        ;; [:p "Please try egain. Either your email or password was incorrect"]
        ;;     )
        ))]))


(comment
  (get-user-by-email-and-password
    {:email      "testing@testing.com"
    :password   "11"}))
