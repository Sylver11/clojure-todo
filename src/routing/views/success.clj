(ns routing.views.success
 (:use [hiccup.page]
        [hiccup.element :only (link-to)]
        [hiccup.element :only (image)]
        [hiccup.form :only (form-to)]
        [hiccup.core :refer [html h]]
        [ring.util.response :only (response)]
        )
 )


(defn display-success-registration [data]
  (html
     [:div
      [:h1 "Success!! You... " (:client/firstName data) " " (:client/lastName data) "!"]
      [:h2 "and your email is: " (:client/email data)]
      [:p "are now written to the database"]]
     ))
