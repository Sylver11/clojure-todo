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
  [{:db/ident       :todo/email
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :todo/item
    :db/unique      :db.unique/identity
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
  (d/delete-database db-uri)
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
        tx-name [{:todo/email first-name
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

(defn delete-item [req]
  (let [{params :params} req
        {delete :delete} params
        {{:keys [first-name]} :session} req
        tx-delete [[:db/retract [:todo/item delete] :todo/email first-name]
            [:db/add "datomic.tx" :db/doc "remove incorrect assertion"]]]
    (d/transact (conn) tx-delete)
    (response/redirect "/todo")
       ;; data ;; (d/touch (d/entity db-after [:todo/email first-name]))
     ;; (success/display-success-registration data)
     ))

(defn done-item [req]
  (let [{params :params} req
        {done :done} params
        {{:keys [first-name]} :session} req
        tx-delete [{:todo/email first-name
                                 :todo/item done
                                 :todo/complete true}]]
    (d/transact (conn) tx-delete)
    (response/redirect "/todo")
       ;; data ;; (d/touch (d/entity db-after [:todo/email first-name]))
     ;; (success/display-success-registration data)
    ))




(comment
  (setup-database!)

  (add-item
   {:params {:item "testing1", :due "1985-04-14T23:20"
             },
    :session {:first-name "Justus"}
     }
    )

  (capture-user-registration
   {:first-name "Justus"
    :last-name  "Voigt"
    :email      "justus.sylvester@hotmail.de"
    :password   "1"
    :confirm    "1"})
  )

(defn delete []
  (let [tx-name [[:db/retract [:todo/item "testing3"] :todo/email "Justus"]
            [:db/add "datomic.tx" :db/doc "remove incorrect assertion"]]]
    (d/transact (conn) tx-name)))




