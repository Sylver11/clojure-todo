(ns routing.views.experience
 (:use [hiccup core page]
        [hiccup.element :only (link-to)]
        [hiccup.element :only (image)]
        [hiccup.form :only (form-to)]
        [hiccup.core :refer [html h]]
        [ring.util.response :only (response)]
        )
 )

;; Dummy variable
;;(def req {"name" "justus"})



(defn experience-page []
  (html
   [:form {:method "get" :action "get-submit"}
       [:input {:type "text" :name "name"}]
       [:input {:type "submit" :value "submit"}]])
  )




;;This part should be capturing the parameter which have been inputed above.
;;[req] should be the input
;; the let function which in this case is a associative destructuring takes the key :params from req and from that takes the key :name to associate params with the name.
;; But it throws me an error saying that it cannot resolve params in this context.
;; However, according to documentation req should be a simple map {"name" "justus"} and it even says that in the url
;;but I somehow struggle to the the destructuring right. I also dont really know how to debug this.
;; I was thinking about using a dummy value for req which you can see on the top

(defn display-result [req]
  (let [{{params :name} :params} req
       ;; param-name (get params "name")
        ]
   (html
    [:div
     [:h1 "Hello " (h params) "!"]
     ]))
(println params)

  )






;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;
;;this to be ingored
;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; [:div {:class "form-group"}
;;             [:label {:for "formGroupExampleInput"} "First Name"]
;;             [:input {:type "text" :class "form-control" :id "formGroupExampleInput" :placeholder "eg Justus"}
;;              ]]
;;            [:div {:class "form-group"}
;;             [:label {:for "formGroupExampleInput2"} "Second Name"]
;;             [:input {:type "text" :class "form-control" :id "formGroupExampleInput2" :placeholder "eg Voigt"}
;;              ]]
;;            [:button {:type "button" :class "btn btn-success"} "Submit"]
