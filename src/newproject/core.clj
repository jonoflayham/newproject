(ns newproject.core
  (:require [clojure.java.shell :as sh])
  (:import (java.io File)))

(defn mkdir [& paths] (apply sh/sh (concat ["mkdir" "-p"] paths)))

(defn create-project [base-folder name]
  (mkdir (str base-folder "/src/" name) (str base-folder "/test/" name))
  (spit (str base-folder "/.gitignore")
        ".*\n!.gitignore\n*.iml\ntarget\n")
  (spit (str base-folder "/deps.edn")
        {:deps {(symbol "org.clojure/clojure") {:mvn/version (clojure-version)}}})
  (spit (str base-folder "/src/" name "/core.clj")
        (str "(ns " name ".core)"))
  (spit (str base-folder "/test/" name "/core-test.clj")
        (str "(ns " name ".core)")))

(defn -main [base-folder name]
  (if (.exists (new File base-folder))
    (throw (new Exception "A file or folder already exists at the path provided"))
    (do (create-project base-folder name)
        (shutdown-agents))))
