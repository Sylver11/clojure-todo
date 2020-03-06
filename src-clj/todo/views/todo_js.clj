(ns todo.views.todo-js
  (:require [hiccup.page :as hiccup]))

(defn js-page []
  (hiccup/html5
   (hiccup/include-js "/js/main.js")
   [:div#app]
   )
  )


