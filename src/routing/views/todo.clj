(ns routing.views.todo
  (:require
   [hiccup.core :as hiccup]
   [datomic.api :as d]
   [routing.database-writes :as database-writes]
   [clj-time.core :as t]
   [clj-time.local :as l]
   [clj-time.coerce :as c]
   [clojure.edn :as edn]
   [clojure.string :as string])
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
 [:ul {:style "list-style-type: none;"}
  (for [todo
        (sort-by :updated-due
                 (for [todo (d/q '[:find ?item ?time ?due ?complete
                                                  :in $ ?first-name
                                                  :keys item time due complete
                                                  :where
                                                  [?e  :todo/email ?first-name]
                                                  [?e  :todo/item ?item]
                                                  [?e  :todo/time ?time]
                                                  [?e  :todo/due ?due]
                                                  [?e  :todo/complete ?complete]
                                                  ]
                                                (database-writes/db) first-name)
                                      ]
                   (assoc todo :updated-due
                          (cond
                            (= (:complete todo) true) 99999
                            (t/after?  (edn/read-string {:readers c/data-readers}(pr-str (c/from-date (:due todo))))
                                         (edn/read-string {:readers c/data-readers}(pr-str (c/from-date (java.util.Date.)))))
                            (t/in-minutes (t/interval  (edn/read-string {:readers c/data-readers}
                                                                        (pr-str (c/from-date (java.util.Date.))))   (edn/read-string {:readers c/data-readers} (pr-str (c/from-date (:due todo))))))

                            :else 0
                            ))
                   ))]

         [:li  {:style "padding:10px; margin:20px;" :class (str "list-item-"  (string/replace (:item todo) #"  *" ""))} [:script "


$(document).ready(function() {


   var doneItem = "(:complete todo)"
   var totalMinutes = "(if (t/after?  (edn/read-string {:readers c/data-readers}
                                                       (pr-str (c/from-date (:due todo))))
                                      (edn/read-string {:readers c/data-readers}
                                                       (pr-str (c/from-date (java.util.Date.)))))
                         (t/in-minutes (t/interval  (edn/read-string {:readers c/data-readers}
                                                                     (pr-str (c/from-date (java.util.Date.))))   (edn/read-string {:readers c/data-readers} (pr-str (c/from-date (:due todo))))))
                         "'Overdue'"
                         )"
    if (doneItem == true){
     $('."(str "list-item-"  (string/replace (:item todo) #"  *" ""))"').css(\"background-color\", \"green\");
     $('."(str "time-"  (string/replace (:item todo) #"  *" ""))"').html(\"Complete\");
    }
    else if (totalMinutes != \"Overdue\"){
  d = Math.floor(totalMinutes/1440); // 60*24
  h = Math.floor((totalMinutes-(d*1440))/60);
  m = Math.round(totalMinutes%60);

    $('."(str "time-"  (string/replace (:item todo) #"  *" ""))"').html((d + \" days, \" + h + \" hours, \" +m+\" minutes \"));
    }
    else {
   $('."(str "list-item-"  (string/replace (:item todo) #"  *" ""))"').css(\"background-color\", \"yellow\");
 $('."(str "time-"  (string/replace (:item todo) #"  *" ""))"').html(\"Overdue\");
    }

});"] [:p {:style "font-size: 25px;"} (:item todo)]
                 [:p {:style "display: inline" :class (str "time-"  (string/replace (:item todo) #"  *" ""))}]
                 [:p {:style "display: inline" :class (str "min-"  (string/replace (:item todo) #"  *" ""))}]

                 [:form {:style "display: inline;" :method "POST" :action "delete-by-user"}
                  [:button {  :style "border:0; background:none;"
                            :type "submit"
                            :name "delete" :value (:item todo)} [:i {:class "material-icons"} "delete"]]]
                 [:form {:style "display:none;" :id (str "edit-input-"
                                                         (string/replace (:item todo) #"  *" ""))  :method "POST" :action "edit-by-user"}
                  [:input {:type "text" :name "old-item" :class "form-control" :id "formGroupExampleInput2" :value (:item todo) :style "display:none"  } ]
                  [:input {:type "text" :name "new-item" ;; :style "display:none;"
                           :id "form-control"
                           ;; :id "formGroupExampleInput2"
                           :placeholder "Do dishes"}
                   ]
                  [:button {:type "submit" :class "btn btn-warning"} "Edit"]
                  ]

                 [:i  {:style "cursor: pointer;" :class "material-icons" :id (str  "inputField" (string/replace (:item todo) #"  *" ""))} "&#xe3c9;"]
                 [:form {:style "display: inline;" :method "POST" :action "done-by-user"}
                  [:button { :style "border:0; background:none;"  :name "done" :value (:item todo)} [:i {:class "material-icons"} "done"]]]
                 [:script "

$('#"(str  "inputField" (string/replace (:item todo) #"  *" "")) "').click(function(){
 var x = document.getElementById('"(str "edit-input-" (string/replace (:item todo) #"  *" "")) "');
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

   ))


