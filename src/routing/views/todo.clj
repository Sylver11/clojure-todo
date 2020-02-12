(ns routing.views.todo
  (:require
   [hiccup.core :as hiccup]
   [datomic.api :as d]
   [routing.database-writes :as database-writes])
  )




(defn todo-list [first-name]
  (hiccup/html
   [:div
    [:p "Hi " (str first-name)]
    [:form {:method "POST" :action "add-item-by-user"}
    [:div {:class "form-group"}
     [:label {:for "formGroupExampleInput2"} "item"]
     [:input {:type "text" :name "item" :class "form-control" :id "formGroupExampleInput2" :placeholder "Do dishes"}
      ]
     [:label {:for "formGroupExampleInput2"} "due"]
     [:input {:type "datetime-local" :name "due" :class "form-control" :id "formGroupExampleInput2" }
      ]
     ]
     [:button {:type "submit" :class "btn btn-success"} "Add item"]]

[:div
    [:ul
     (for [todo (d/q '[:find ?item ?time ?complete
                       :in $ ?first-name
                       :keys item time complete
                       :where
                       [?e  :todo/email ?first-name]
                       [?e  :todo/item ?item]
                       [?e  :todo/time ?time]
                       [?e  :todo/complete ?complete]
                       ]
                       (database-writes/db) first-name)]
       [:li (:item todo) [:br] (:time todo) [:br] (:complete todo) [:form {:method "POST" :action "delete-by-user"}[:button {:name "delete" :value (:item todo)} "Delete"]]
        [:form {:method "POST" :action "done-by-user"}[:button {:name "done" :value (:item todo)} "Done"]]] )]]]
   ))


;; (for [movies (d/q '[:find ?movie-title
;;                   :keys movie-title
;;                   :where
;;                        [_  :movie/title ?movie-title]
;;                        ]
;;                        (db))]
;;   [:li (:movie-title movies)])


;; (d/q '[:find ?item
;;                        :where
;;                        [_  :todo/item ?item]
;;                        ]
;;                        (db))xb

