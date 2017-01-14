(ns cats-dogs-nighttime.core
  (:require [play-clj.core :refer :all]
            [play-clj.ui :refer :all]
            [play-clj.g2d :refer :all]))

(defscreen text-screen
  :on-show
  (fn [screen entities]
    (update! screen :camera (orthographic) :renderer (stage))
    (assoc (label "0" (color :white))
           :id :fps
           :x 5))

  :on-render
  (fn [screen entities]
    (->> (for [entity entities]
           (case (:id entity)
             :fps (doto entity (label! :set-text (str (game :fps))))
             entity))
         (render! screen)))

  :on-resize
  (fn [screen entities]
    (height! screen 300)))

(def speed 10)

(defn create-enemy []
  (println "enemy spawned"))

(defn move [entities direction]
  (for [entity entities]
    (if (:movable entity)
      (case direction
        :down (update entity :y #(- % speed))
        :up (update entity :y #(+ % speed))
        :left (update entity :x #(- % speed))
        :right (update entity :x #(+ % speed))
        nil)
      entity)))

(defscreen main-screen
  :on-show
  (fn [screen entities]
    (add-timer! screen :spawn-enemy 10 2)
    (update! screen :renderer (stage) :camera (orthographic))
    (let [background (assoc (texture "bg.png") :night? true)
          cat (assoc (texture "cat-2.png") :x 300 :y 300 :width 80 :height 80 :night? true :movable true)
          dog (assoc (texture "dog-2.png") :x 200 :y 200 :width 80 :height 80 :night? false)]
      [background cat dog]))
  :on-render
  (fn [screen entities]
    (clear!)
    (render! screen entities))
  :on-key-down
  (fn [screen entities]
    (cond
      (= (:key screen) (key-code :dpad-up))
      (move entities :up)
      (= (:key screen) (key-code :dpad-down))
      (move entities :down)
      (= (:key screen) (key-code :dpad-right))
      (move entities :right)
      (= (:key screen) (key-code :dpad-left))
      (move entities :left)))
  :on-resize
  (fn [screen entities]
    (height! screen 600))
  :on-timer
  (fn [screen entities]
    (case (:id screen)
      :spawn-enemy (conj entities (create-enemy))
      nil)))

(defgame cats-dogs-nighttime-game
  :on-create
  (fn [this]
    (set-screen! this main-screen text-screen)))
