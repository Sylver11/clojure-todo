(ns todo.views.todo-js
  (:require [hiccup.page :as hiccup html5]))

(defn js-page []
  (hiccup/html5
   [:div#app]
   (hiccup/include-js "/js/main.js"))
  )


