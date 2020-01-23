(ns routing.database-writes
  (:require [clojure.string :as string]
            [datomic.api :as d]
            [routing.views.success :as success]))

(def db-uri "datomic:dev://localhost:4334/todo")

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

(def conn #(d/connect db-uri))
(def db (comp d/db conn))

(defn setup-database! []
  (d/delete-database db-uri)
  (d/create-database db-uri)
  (d/transact (conn) add-entity-schema)
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

(comment
  (setup-database!)

  (capture-user-registration
   {:first-name "Miweerwerwwrwerke"
    :last-name  "Jerwerwerwerwack"
    :email      "serwerwrwyay@hello.com"
    :password   "asdfsa1111"
    :confirm    "asdfsa1111"})
  )
