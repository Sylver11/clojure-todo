(ns routing.views.registration
  (:use [ring.util.response :only (response)]
        [routing.database-writes :as database-writes]
        )
 )


;; (defn capture-user-registration [req]
;;   (let [{{:keys [firstname secondname email password confirm]}:params} req]
;;     (if-not (?nil (user-input-validation email))
;;       (database-writes/write-user-data firstname secondname email password confirm)
;;       )
;;     )
;;   )


;; (defn user-input-validation
;;   [email]
;;   (let [pattern #"[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?"]
;;     (and (string? email) (re-matches pattern email))))
