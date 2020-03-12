(ns todo.todo-script)

;;(set! (.-innerHTML "") "Hi!")




(-> js/document
    (.getElementById "testing")
    (.-innerHTML)
    (set! "Hello Clojure!"))
