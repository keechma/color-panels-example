(ns keechma-color-panels.core
  (:require-macros
   [reagent.ratom :refer [reaction]])
  (:require
   [reagent.core :as reagent]
   [keechma.ui-component :as ui]
   [keechma.controller :as controller]
   [keechma.app-state :as app-state]
   ))

(defonce debug?
  ^boolean js/goog.DEBUG)



(defn panel-render [ctx]
  (let [bg-color (or (get-in @(ui/current-route ctx) [:data :color]) "white")]
    [:div {:style {:height "500px" :margin "20px" :padding "20px" :background-color bg-color}}
     [:a {:href (ui/url ctx {:color "cyan"})} "Cyan"]
     [:br]
     [:a {:href (ui/url ctx {:color "yellow"})} "Yellow"]
     [:br]
     [:a {:href (ui/url ctx {:color "gray"})} "Gray"]]))

(def panel-component
  (ui/constructor
   {:renderer panel-render}))

(defn dev-setup []
  (when debug?
    (enable-console-print!)
    (println "dev mode")))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Initialize App

(def history-app-definition
  {:components    {:main panel-component}
   :controllers   {}
   :subscriptions {}
   :router        :history
   :html-element  (.getElementById js/document "app1")})

(def hashchange-app-definition
  {:components    {:main panel-component}
   :controllers   {}
   :subscriptions {}
   :html-element  (.getElementById js/document "app2")})

(defonce history-running-app (clojure.core/atom))

(defn history-start-app! []
  (reset! history-running-app (app-state/start! history-app-definition)))

(defonce hashchange-running-app (clojure.core/atom))

(defn hashchange-start-app! []
  (reset! hashchange-running-app (app-state/start! hashchange-app-definition)))

(defn reload-history-app! []
  (let [current @history-running-app]
    (if current
      (app-state/stop! current history-start-app!)
      (history-start-app!))))

(defn reload-hashchange-app! []
  (let [current @hashchange-running-app]
    (if current
      (app-state/stop! current hashchange-start-app!)
      (hashchange-start-app!))))

(defn reload []
  (reload-history-app!)
  (reload-hashchange-app!))

(defn ^:export main []
  (dev-setup)
  (history-start-app!)
  (hashchange-start-app!))
