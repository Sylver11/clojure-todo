(ns todo.views.todo
  (:require
   [hiccup.core :as hiccup]
   [datomic.api :as d]
   [todo.database-writes :as database-writes]
   [clj-time.core :as t]
   [clj-time.local :as l]
   [clj-time.coerce :as c]
   [clojure.edn :as edn]
   [clojure.string :as string])
  )



(def render-modal
  [:body
                [:script "$(window).on('load',function(){
        $('#myModal').modal('show');
    });"]
               [:div {:class"modal fade" :id "myModal" :tabindex "-1" :role "dialog"}
           [:div {:class "modal-dialog" :role "document"}
            [:div {:class "modal-content"}
             [:div {:class "modal-header"}
              [:h5 {:class "modal-title" :id "exampleModal3Label"} "Welcome to your personal Todo app"]
              [:button {:type "button" :class "close" :data-dismiss "modal" :aria-label "Close"} [:span {:aria-hidden "true"} "&times;"] ]]
             [:div {:class "modal-body"} "You can add, edit and delete any of your entries."]
             [:div {:class "modal-footer"}
              [:button {:type "button" :class "btn btn-secondary" :data-dismiss "modal"} "Close"]
              ]]]] ])



(defn todo-list-ordered-by-time [first-name]
  (let [now (c/from-date (java.util.Date.))]
  (->> (d/q '[:find [?e ...]
              :in $ ?first-name
              :where
              [?e :todo/email ?first-name]
              [?e :todo/item]
              [?e :todo/due]
              [?e :todo/complete]]
            (database-writes/db) first-name)
       (map #(d/entity (database-writes/db) %))
       (sort-by (fn [{:todo/keys [item due complete] :as todo}]
                  (let [due (c/from-date due)]
                    (cond (= complete true)  99999
                          (t/after? due now) (t/in-minutes (t/interval now due))
                          :else              0)))))))

(defn render-page [first-name & render-modal]
  (hiccup/html
   render-modal
   [:div
    [:p "Hi " (str first-name)]
    [:form {:method "POST" :action "add-item-by-user"}
    [:div {:class "form-group"}

     [:input {:required "" :type "text" :name "item" :class "form-control" :id "formGroupExampleInput1" :placeholder "Do dishes"}
      ]
     [:label {:for "formGroupExampleInput1"} "Item"]


     [:input {:required "" :type "datetime-local" :name "due" :class "form-control" :id "formGroupExampleInput2" }
      ]
     [:label {:for "formGroupExampleInput2"} "Due"]
     ]
     [:button {:type "submit" :class "btn btn-success"} "Add item"]]
   [:br]

[:div
 [:ul {:style "list-style-type: none;" :class "list-group"}
  (for [todo (todo-list-ordered-by-time first-name)]

         [:li  {:style "padding:10px; margin:20px;" :class "list-group-item" :id (str "list-item-"  (string/replace (:todo/item todo) #"  *" ""))} [:script "


$(document).ready(function() {


   var doneItem = "(:todo/complete todo)"
   var totalMinutes = "(if (t/after?  (edn/read-string {:readers c/data-readers}
                                                       (pr-str (c/from-date (:todo/due todo))))
                                      (edn/read-string {:readers c/data-readers}
                                                       (pr-str (c/from-date (java.util.Date.)))))
                         (t/in-minutes (t/interval  (edn/read-string {:readers c/data-readers}
                                                                     (pr-str (c/from-date (java.util.Date.))))   (edn/read-string {:readers c/data-readers} (pr-str (c/from-date (:todo/due todo))))))
                         "'Overdue'"
                         )"
    if (doneItem == true){
     $('#"(str "list-item-"  (string/replace (:todo/item todo) #"  *" ""))"').css(\"background-color\", \"#9ACD32\");
     $('."(str "time-"  (string/replace (:todo/item todo) #"  *" ""))"').html(\"Complete\");
    }
    else if (totalMinutes != \"Overdue\"){
  d = Math.floor(totalMinutes/1440); // 60*24
  h = Math.floor((totalMinutes-(d*1440))/60);
  m = Math.round(totalMinutes%60);

    $('."(str "time-"  (string/replace (:todo/item todo) #"  *" ""))"').html((d + \" days, \" + h + \" hours, \" +m+\" minutes \"));
    }
    else {
   $('."(str "list-item-"  (string/replace (:todo/item todo) #"  *" ""))"').css(\"background-color\", \"#FFFF66\");
 $('."(str "time-"  (string/replace (:todo/item todo) #"  *" ""))"').html(\"Overdue\");
    }

});"] [:p {:style "font-size: 25px;"} (:todo/item todo)]
                 [:p {:style "display: inline" :class (str "time-"  (string/replace (:todo/item todo) #"  *" ""))}]
                 [:p {:style "display: inline" :class (str "min-"  (string/replace (:todo/item todo) #"  *" ""))}]

                 [:form {:style "display: inline;" :method "POST" :action "delete-by-user"}
                  [:button {  :style "border:0; background:none;"
                            :type "submit"
                            :name "delete" :value (:todo/item todo)} [:i {:class "material-icons"} "delete"]]]
                 [:form {:style "display:none;" :id (str "edit-input-"
                                                         (string/replace (:todo/item todo) #"  *" ""))  :method "POST" :action "edit-by-user"}
                  [:input {:type "text" :name "old-item" :class "form-control" :id "formGroupExampleInput2" :value (:todo/item todo) :style "display:none"  } ]
                  [:input {:type "text" :name "new-item" ;; :style "display:none;"
                           :id "form-control"
                           ;; :id "formGroupExampleInput2"
                           :placeholder "Do dishes"}
                   ]
                  [:button {:type "submit" :class "btn btn-warning"} "Edit"]
                  ]

                 [:i  {:style "cursor: pointer;" :class "material-icons" :id (str  "inputField" (string/replace (:todo/item todo) #"  *" ""))} "&#xe3c9;"]
                 [:form {:style "display: inline;" :method "POST" :action "done-by-user"}
                  [:button { :style "border:0; background:none;"  :name "done" :value (:todo/item todo)} [:i {:class "material-icons"} "done"]]]
                 [:script "

$('#"(str  "inputField" (string/replace (:todo/item todo) #"  *" "")) "').click(function(){
 var x = document.getElementById('"(str "edit-input-" (string/replace (:todo/item todo) #"  *" "")) "');
console.log(\"this is running\");
  if (x.style.display === \"none\") {
console.log(\"the if statement is also running\");
    x.style.display = \"inline\";
  } else {
    x.style.display = \"none\";
  }
});
  "]])


  ]]]
   )
  )









(defn todo-list [first-name]

  (let [[client-new] (d/q '[:find ?new
                  :keys new
                  :in $ ?first-name
                  :where
                  [?e  :client/first-name ?first-name]
                  [?e  :client/new ?new]]
                (database-writes/db) first-name)]
    (if (= (:new client-new) true)
      (render-page first-name render-modal)
     (render-page first-name)
     ; (print (:new client-new))
     )
     ))





