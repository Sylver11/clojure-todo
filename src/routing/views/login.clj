(ns routing.views.login
  (:require [hiccup.core :as hiccup]
            [datomic.api :as d]
            [routing.database-writes :as database-writes]
            [ring.util.response :as response]
            [noir.session :as session]))


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


;; (defn redirect-login []
;;   routing.handler/app
;;   {:status 302
;;    :header "/my-todo"
;;    :body ""
;;    }
;;   )

(defn get-user-by-email-and-password [{:keys [email password]}]
  (hiccup/html
   [:div
    (let [[client] (d/q '[:find ?uuid ?first-name
                          :in $ ?email ?password
                          :keys  uuid first-name
                          :where
                          [?c :client/email ?email]
                          [?c :client/password ?password]
                          [?c :client/uuid ?uuid]
                          [?c :client/first-name ?first-name]]
                    (database-writes/db) email password)]
      (if (clojure.string/blank? (str client))
        [:div
         [:p "Sorry either your email or password do not match up."]
         [:a {:href "/login"} "Return to login"]]
        (session/put! :username (:email client))
        ;; (response/redirect "/my-todo" 200)
        ;; (do (response/redirect "/my-todo"))
        ))]))

(comment
  (get-user-by-email-and-password
   ;"testing@testing.com" "11"
    {:email      "testing@testing.com"
    :password   "11"}
    ))


