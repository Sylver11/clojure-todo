(ns routing.views.success
 (:use [hiccup core page]
        [hiccup.element :only (link-to)]
        [hiccup.element :only (image)]
        [hiccup.form :only (form-to)]
        [hiccup.core :refer [html h]]
        [ring.util.response :only (response)]
        [datomic.api :as d]
        )
 )

(def db-uri "datomic:dev://localhost:4334/todo")
(d/create-database db-uri)
(def conn (d/connect db-uri))
(d/db conn)


(def add-entity-schema
  [{
    :db/ident :secondname/secondname2
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/value
    :db/doc "Todo"}
   {
    :db/ident :firstname/firstname2
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/value
    :db/doc "Todo"}])

(d/transact conn add-entity-schema)

(defn user-input [req]
  (let [{{:keys [firstname secondname feeling]}:params} req
        feels (cond
                (= feeling "1"):down
                (= feeling "2"):sad
                (= feeling "3"):okayisch
                (= feeling "4"):great
                (= feeling "5"):excellent)
        tx [{:firstname/firstname2 firstname}
            {:secondname/secondname2 secondname}
            ]
        ]
    (d/transact conn tx)
(html
 [:div
     [:h1 "Success!! You... " (h firstname) " " (h secondname) "!"]
     [:h2 "that feels... " (h feels)]
     [:p "are now written to the database"]]
    )
    )
  )



