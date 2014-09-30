(ns clj-interpretor.core
  (:require [clojure.tools.reader.edn :as edn]
            [clojure.tools.analyzer :as ana]
            [clojure.tools.analyzer.env :as env]
            [clojure.tools.analyzer.jvm :as ana.jvm]))

;;(def ast-test *1)

(defmacro string-it [& x] (apply str x))

(def code2parse0 (string-it
                  (+ 1 2)
                  ))
(def code2parse2 (string-it
                  (fn [a b]
                    (+ a b))
                  ))
;;(def code2parse "(fn [a b] (+ a b))")

(defn analyze [form]
  (binding [ana/macroexpand-1 ana.jvm/macroexpand-1
            ana/create-var    ana.jvm/create-var
            ana/parse         ana.jvm/parse
            ana/var?          var?]
       (env/ensure (ana.jvm/global-env)
         (ana.jvm/run-passes (ana/-analyze form)))))

;; (defn eval-ast [ast, state]
;;   (if (:children ast)
;;     (parse-ast )
;;     2))

;; (eval-ast ast-test)

(defn parse
  "I don't do a whole lot."
  [x]
  (let [forms (edn/read-string x)
        ast (ana.jvm/analyze forms)
        f (:form ast)
        ;;f (parse-ast ast)
        ]
       (eval f)))

(defn -main []
  (println "parsing:" ((parse code2parse2) 1 2)))
