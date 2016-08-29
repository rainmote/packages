(set-env!
  :resource-paths #{"resources"}
  :dependencies '[[cljsjs/boot-cljsjs "0.5.2" :scope "test"]])

(require '[cljsjs.boot-cljsjs.packaging :refer :all])

(def +lib-version+ "3.5.1")
(def +version+ (str +lib-version+ "-0"))

(task-options!
 pom { :project     'cljsjs/raven
       :version     +version+
       :description "raven-js: JavaScript client for Sentry https://getsentry.com"
       :url         "https://docs.getsentry.com/hosted/clients/javascript/"
       :scm         { :url "https://github.com/getsentry/raven-js" }
       :license     { "MIT" "https://github.com/getsentry/raven-js/blob/master/LICENSE" }})

(deftask package []
  (comp
    (download :url (format "https://github.com/getsentry/raven-js/archive/%s.zip" +lib-version+)
              :checksum "0aa05801b31f2787167ecc1770564161"
              :unzip true)
    (sift :move { #"^raven-js.*/dist/raven\.js$"      "cljsjs/raven/development/raven.inc.js"
                  #"^raven-js.*/dist/raven\.min\.js$" "cljsjs/raven/production/raven.min.inc.js" })
    (sift :include #{#"^cljsjs"})
    (deps-cljs :name "cljsjs.raven")
    (pom)
    (jar)))
