(ns routing.views.registration
 (:use [hiccup core page]
        [hiccup.element :only (link-to)]
        [hiccup.element :only (image)]
        [hiccup.form :only (form-to)]
        [hiccup.core :refer [html h]]
        [ring.util.response :only (response)]
        [datomic.api :as d]
        [metis.core]
        )
 )
(def db-uri "datomic:dev://localhost:4334/todo")
(d/create-database db-uri)
(def conn (d/connect db-uri))
(def db-todo (d/db conn))
;(d/db conn)


(def add-entity-schema
  [{:db/id #db/id[:db.part/db]
    :db/ident :client/firstName
    :db/index true
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}
   {:db/id #db/id[:db.part/db]
    :db/ident :client/lastName
    :db/index true
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}
   {:db/id #db/id[:db.part/db]
    :db/ident :client/email
    :db/index true
    :db/unique :db.unique/identity
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}]
  )


(d/transact conn add-entity-schema)

(defn user-input [req]
  (let [{{:keys [firstname secondname email]}:params} req
        tx-name [{:client/firstName firstname :client/lastName secondname :client/email email}
                 ]
    {:keys [db-after]} @(d/transact conn tx-name)
    data (d/touch (d/entity db-after [:client/email email]))]
    (html
     [:div
      [:h1 "Success!! You... " (:client/firstName data) " " (:client/lastName data) "!"]
      [:h2 "and your email is: " (:client/email data)]
      [:p "are now written to the database"]]
     )
    )
  )



