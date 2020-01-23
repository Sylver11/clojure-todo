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



;; (defn get-user-by-email-and-password [email password]
;;   (let [client (d/q '[:find ?u ?fn
;;                       ;; :in $
;;                       :keys uuid first-name
;;                       :where
;;                       [?c :client/uuid ?u]
;;                       [?c :client/first-name ?fn]
;;                       [?c :client/email ?email]
;;                       [?c :client/password ?password]
;;                       ]
;;                     (database-writes/db))]
;;     [:h2 "Hi " (:first-name client)]
;;     [:p "Your uuid code is " (:uuid client)]))




(defn get-user-by-email-and-password [email password]
  (hiccup/html
   [:div
    (let [client (d/q '[:find ?uuid ?first-name
                      :keys  uuid first-name
                      :where
                      [?c :client/email "testing@testing.com"]
                      [?c :client/password "11"]
                      [?c :client/uuid ?uuid]
                      [?c :client/first-name ?first-name]
                      ]
                      (database-writes/db))]
      (if-not (clojure.string/blank? :uuid client)
        (do (response/redirect "/my-todo" 200)
        (println "redirecting"))
        [:p "Please try egain. Either your email or password was incorrect"]
        )
     [:p "Hi " (:first-name client)]
     [:h1 "Your uuid code is " (:uuid client)]
     )
    ]
   ))

(comment
  (get-user-by-email-and-password "testing@testing.com" "11"))
