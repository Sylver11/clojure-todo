(ns routing.views.success
 (:use [hiccup.page]
        [hiccup.core :refer [html h]]
        )
 )


(defn display-success-registration [data]
  (html
     [:div
      [:h1 "Success!! You... " (:todo/email data) " " (:todo/item data) "!"]
      ;; [:h2 "and your email is: " (:client/email data)]
      [:p "are now written to the database"]]
     ))





