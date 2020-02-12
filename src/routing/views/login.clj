(ns routing.views.login
  (:require [hiccup.core :as hiccup]
            [datomic.api :as d]
            [routing.database-writes :as database-writes]
            ))


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


(defn get-user-by-email-and-password [req]
  (let [{params :params} req
        {email :email} params
        {password :password} params
        ;; {session :session}
       ;; req
        [client] (d/q '[:find ?uuid ?first-name
                          :in $ ?email ?password
                          :keys  uuid first-name
                          :where
                          [?c :client/email ?email]
                          [?c :client/password ?password]
                          [?c :client/uuid ?uuid]
                          [?c :client/first-name ?first-name]]
                    (database-writes/db) email password)]
      (if (clojure.string/blank? (str client))
        (hiccup/html
         [:div
           [:p "Sorry either your email or password do not match up."]
          [:a {:href "/login"} "Return to login"]])
        (-> (ring.util.response/redirect "/todo")
            (assoc :session (-> :username (:first-name client))))
        )))

(comment
  (get-user-by-email-and-password
    {:params {:email      "joseph@cognician.com"
    :password   "1"}
     }
    )
  )


