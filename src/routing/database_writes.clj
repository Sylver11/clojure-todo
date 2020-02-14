(ns routing.database-writes
  (:require [clojure.string :as string]
            [datomic.api :as d]
            [routing.views.success :as success]
            [clj-time.coerce :as c]
            [ring.util.response :as response]))

(def db-uri "datomic:dev://localhost:4334/todo-app")

(def uuid (d/squuid))

(def add-entity-schema
  [{:db/ident       :client/uuid
    :db/index       true
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one}
   {:db/ident       :client/first-name
    :db/index       true
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :client/last-name
    :db/index       true
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :client/email
    :db/unique      :db.unique/identity
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :client/password
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}])

(def add-todo-schema
  [{:db/ident       :todo/uuid
    :db/valueType   :db.type/uuid
    :db/unique      :db.unique/identity
    :db/cardinality :db.cardinality/one}
   {:db/ident       :todo/email
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :todo/item
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :todo/time
    :db/valueType   :db.type/instant
    :db/cardinality :db.cardinality/one}
   {:db/ident       :todo/due
    :db/valueType   :db.type/instant
    :db/cardinality :db.cardinality/one}
   {:db/ident       :todo/complete
    :db/valueType   :db.type/boolean
    :db/cardinality :db.cardinality/one}
   ])

(def conn #(d/connect db-uri))
(def db (comp d/db conn))

(defn setup-database! []
 ;; (d/delete-database db-uri)
  (d/create-database db-uri)
  (d/transact (conn) add-entity-schema)
  (d/transact (conn) add-todo-schema)
  )

(def valid-email-pattern
  #"[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?")

(defn user-input-valid? [{:keys [email password confirm]}]
  ;; first check we have input
  (boolean
   (when (and email password confirm)
     ;; clean whitespace from input
     (let [password (string/trim password)
           confirm  (string/trim confirm)
           email    (string/trim email)]
       ;; validate email structure and check password equality
       (and (string? email)
            (re-matches valid-email-pattern email)
            (= password confirm))))))

(defn write-user-data [{:keys [first-name last-name email password]}]
  (let [tx-name [{:db/id             "tempid.user"
                  :client/uuid       (d/squuid)
                  :client/first-name first-name
                  :client/last-name  last-name
                  :client/email      email
                  :client/password   password}]
        {:keys [db-after]} @(d/transact (conn) tx-name)
        data (d/touch (d/entity db-after [:client/email email]))]
    (success/display-success-registration data)))

(defn capture-user-registration [registration-data]
  (if (user-input-valid? registration-data)
    (write-user-data registration-data)
    "Please input a valid email address"))



;; (def custom-formatter (f/formatter "yyyy-MM-dd-H:mm"))
;; (c/to-date "1985-04-14T23:20")

(defn add-item [req]
  (let [{params :params} req
        {item :item} params
        {due :due} params
        {{:keys [first-name]} :session} req
      ;  due-updated [(f/parse custom-formatter (clojure.string/replace due #"T" "-"))]
        tx-name [{:todo/uuid (d/squuid)
                  :todo/email first-name
                  :todo/item  item
                  :todo/time  (java.util.Date.);; #inst "1985-04-14T23:20:50.52Z"
                  :todo/due   (c/to-date due)
                  :todo/complete false
                  }] ]
        ;; {:keys [db-after]}
       ;; @
    (d/transact (conn) tx-name)
    (response/redirect "/todo")
       ;; data ;; (d/touch (d/entity db-after [:todo/email first-name]))
     ;; (success/display-success-registration data)
     ))

(defn delete-item [{{:keys [delete]} :params
                  {:keys [first-name]} :session
                  :as req}]
  (let [[todo-uuid](d/q '[:find ?uuid
                           :keys uuid
                           :in $ ?first-name ?delete
                           :where
                           [?e  :todo/email ?first-name]
                           [?e  :todo/item ?delete]
                           [?e  :todo/uuid ?uuid]]
                        (db) first-name delete)
        tx-delete [[:db/retract [:todo/uuid (:uuid todo-uuid)] :todo/item delete]
                   [:db/add "datomic.tx" :db/doc "remove assertion"]]]
    (d/transact (conn) tx-delete)
    (response/redirect "/todo")
     ))

(defn done-item [{{:keys [done]} :params
                  {:keys [first-name]} :session
                  :as req}]
  (let [[todo-uuid] (d/q '[:find ?uuid
                           :keys uuid
                           :in $ ?first-name ?done
                           :where
                           [?e  :todo/email ?first-name]
                           [?e  :todo/item ?done]
                           [?e  :todo/uuid ?uuid]]
                         (db) first-name done)
        tx-done [{:todo/uuid  (:uuid todo-uuid)
                  :todo/complete true}]]
    (d/transact (conn) tx-done)
    (response/redirect "/todo")
    ))



(defn edit-item [{{:keys [old-item new-item]} :params
                  {:keys [first-name]} :session
                  :as req}]
  (let [[todo-uuid] (d/q '[ :find ?uuid
                         :keys uuid
                          :in $ ?first-name ?old-item
                          :where
                          [?e  :todo/email ?first-name]
                          [?e  :todo/item ?old-item]
                          [?e  :todo/uuid ?uuid]]
                       (db) first-name old-item)
        tx-edit [{:todo/uuid  (:uuid todo-uuid)
                  :todo/item new-item}]]
    (d/transact (conn) tx-edit)
    (response/redirect "/todo")))


(comment
  (setup-database!)

  (add-item
   {:params {:item "testing4", :due "1985-04-14T23:20"
             },
    :session {:first-name "Justus"}
     }
   )
  (edit-item
   {:params {:old-item "testing4", :new-item "new fucntion working"},
    :session {:first-name "Justus"}})

  (capture-user-registration
   {:first-name "Justus"
    :last-name  "Voigt"
    :email      "justus.sylvester@hotmail.de"
    :password   "1"
    :confirm    "1"})
  )

(defn edit-test []
  (let [tx-name [{:todo/uuid #uuid "5e4512a2-b6e3-4594-933b-129dc0edd13c" ;;(:db-id todo-entry)
                  :todo/item "now again "}]]
    (d/transact (conn) tx-name)))




