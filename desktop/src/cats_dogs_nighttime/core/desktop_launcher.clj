(ns cats-dogs-nighttime.core.desktop-launcher
  (:require [cats-dogs-nighttime.core :refer :all])
  (:import [com.badlogic.gdx.backends.lwjgl LwjglApplication]
           [org.lwjgl.input Keyboard])
  (:gen-class))

(defn -main
  []
  (LwjglApplication. cats-dogs-nighttime-game "cats-dogs-nighttime" 800 600)
  (Keyboard/enableRepeatEvents true))
