(ns routing.views.logout
  (:require [hiccup.core :as hiccup]
        ))


(defn success [req]
(-> (ring.util.response/redirect "/todo")
            (assoc :session (-> nil)))
  )


