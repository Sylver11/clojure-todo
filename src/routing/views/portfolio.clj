(ns routing.views.portfolio
   (:use [hiccup core page]
        [hiccup.element :only (link-to)]
        [hiccup.element :only (image)]
        ))


(defn portfolio-page []
  [:div {:class "portfolio-page"}
   [:div {:class "spellgeek"}
    [:div {:class "portfolio-item mx-auto" :data-toggle "modal" :data-target "#portfolioModal1"}
     [:div {:class "portfolio-item-caption d-flex align-items-center justify-content-center h-100 w-100"}
      [:div {:class "portfolio-item-caption-content text-center text-white"}]
      [:i {:class "fas fa-plus fa-3x"}]
      ]
     (image {:class "img-fluid"} "img/spellgeek.gif")
     ]
    ]
   ;;;;;;;
   ;;Modal
   ;;;;;;;
   ;;;;;;;
   [:div {:class "portfolio-modal modal fade" :id "portfolioModal1" :tabindex "-1":role"dialog" :aria-labelledby "portfolioModal1Label" :aria-hidden "true"}
            [:div {:class "modal-dialog modal-xl" :role "document"}
             [:div {:class "modal-content"}
              [:button {:type "button" :class "close" :data-dismiss "modal" :aria-label "Close"}
               [:span {:aria-hidden "true"}
                [:i {:class "fas fa-times"}]
                ]
               ]
              [:div {:class "modal-body text-center"}
               [:div {:class "container"}
                [:div {:class "row justify-content-center"}
                 [:div {:class "col-lg-8"}
                  [:h2 {:class "portfolio-modal-title text-secondary text-uppercase mb-0"} "Spellgeek"]
                  [:div {:class "divider-custom"}
                   [:div {:class "divider-custom-line"}]
                   [:div {:class "divider-custom-icon"}
                    [:i {:class "fas fa-star"}]]
                   [:div {:class "divider-custom-line"}]
                   ]
                  (image {:class "img-fluid rounded mb-5"} "img/spellgeek.gif")
                  [:p {:class "mb-5"}
                   [:a {:href"https://spellgeek.com" :target "_blank" :rel "noopener noreferrer"}
                    [:i "Spellgeek"]
                    ]
                   "was my first serious
                entrepreneurial
                venture
                which I founded with a good friend from Stellenbosch University.
                The website offers academic proofreading and editing services to foreign
                language speakers. I contributed to the layout,
                design and code of the website. The business is currently on hold while we
                continue to raise capital."
                   ]
                  [:button {:type "button" :class "btn btn-primary" :href "#" :data-dismiss "modal" :value "Close Window"}
                   [:i {:class "fas fa-times fa-fw"}]
                   ]
                  ]
                 ]
                ]
               ]
              ]
             ]
            ]

   ])
