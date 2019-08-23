(ns routing.layout
  (:use [hiccup.page :only (html5 include-css include-js)]
        [hiccup.element :only (link-to)]
        [hiccup.element :only (image)]
        )
  )


(defn application [title & content]
  (html5 {:ng-app "myApp" :lang "en"}
         [:head
          [:title title]
          (include-css "https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css")
          (include-css "css/styles.css")
          (include-css "css/all.css")
          (include-css "css/bootstrap.css")
          (include-css "css/freelancer.css")
          (include-js "https://code.jquery.com/jquery-3.3.1.slim.min.js")
          (include-js "https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js")
          (include-js "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js")
         ;; (include-js "js/ui-bootstrap-tpls-0.7.0.min.js")
          (include-js "js/script.js")
          ]

          [:body

           ;;navbar
           [:nav {:class "navbar navbar-expand-lg navbar-light bg-light"}
            (link-to {:class "navbar-brand"} "/" "Home")
            [:button {:class "navbar-toggler" :type "button" :data-toggle "collapse" :data-target "#navbarSupportedContent" :aria-controls "navbarSupportedContent" :aria-expanded "false" :aria-label "Toggle navigation"}
             [:span {:class "navbar-toggler-icon"}]]
            [:div {:class "collapse navbar-collapse" :id "navbarSupportedcontent"}
             [:ul {:class "navbar-nav mr-auto"}
              [:li {:class "nav-item active"}
               (link-to {:class "nav-link"} "/about" "About")]
              [:li {:class "nav-item"}
               (link-to {:class "nav-link"} "/experience" "Experience")]
              [:li {:class "nav-item"}
               (link-to {:class "nav-link"} "/portfolio" "Portfolio")]
              ]
             [:form {:class "form-inline my-2 my-lg-0"}
              [:input {:class "form-control mr-sm-2" :tye "text" :placeholder "Search" :aria-label "Search"}]
              [:button {:class "btn btn-outline-success my-2 my-sm-0" :type "Submit"}  "Search"]
              ]
             ]
            ]

           ;;main site with pre-loader
           [:div {:class "spinner-border spinner-border-sm loader" :id "mdb-preloader" :role "status"}]
           [:div {:class "pre-content container" :id "post-content"} content]
           ]
          )
  )
