(ns routing.database-writes
  (:require [datomic.api :as d]
            [routing.views.success :as success]))

(def db-uri "datomic:dev://localhost:4334/todo")

(def add-entity-schema
  [{:db/ident       :client/firstName
    :db/index       true
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :client/lastName
    :db/index       true
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :client/email
    :db/unique      :db.unique/identity
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}])

(def conn #(d/connect db-uri))
(def db (comp d/db conn))

(defn setup-database! []
  (d/create-database db-uri)
  (d/transact (conn) add-entity-schema))

(defn -main [& args]
  (setup-database!))

(defn write-user-data [firstname secondname email password confirm]
  (let [tx-name [{:client/firstName firstname
                  :client/lastName secondname
                  :client/email email}]
        {:keys [db-after]} @(d/transact conn tx-name)
        data (d/touch (d/entity db-after [:client/email email]))]
    (success/display-success-registration data)))

(def valid-email-pattern
  #"[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?")

(defn user-input-valid? [email password confirm-pass]
  ;; first check we have input
  (when (and email password confirm-pass)
    ;; clean whitespace from input
    (let [password     (clojure.string/trim password)
          confirm-pass (clojure.string/trim confirm-pass)
          email        (clojure.string/trim email)]
      ;; validate email structure and check password equality
      (and (string? email)
           (re-matches valid-email-pattern email)
           ()
           (= password confirm-pass)))))

(defn testing [firstname secondname email password confirm]
  (if (user-input-valid? email password confirm)
    (write-user-data firstname secondname email password confirm)
    "Please input a valid email address"))


(defn capture-user-registration [req]
  (println "Request map:" req)
  (let [{{:keys [firstname secondname email password password-confirm]}:params} req]
    (testing firstname secondname email password password-confirm)
    )
  )


