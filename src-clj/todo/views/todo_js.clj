(ns todo.views.todo-js
  (:require [hiccup.page :as hiccup]))


(defn js-page []
  (hiccup/html5
   [:body
    [:div#app]
    (hiccup/include-js "/js/main.js")
    (hiccup/include-css "/react-css/todos.css")]
   )
  )

g
