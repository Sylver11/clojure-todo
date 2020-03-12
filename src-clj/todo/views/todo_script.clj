(ns todo.views.todo-script
  (:require [hiccup.page :as hiccup]))

(defn testing []
  (hiccup/html5
   [:body
    [:div#testing]
    (hiccup/include-js "/js/main.js")
    ;(hiccup/include-css "/react-css/todos.css")
    ]
   ))
