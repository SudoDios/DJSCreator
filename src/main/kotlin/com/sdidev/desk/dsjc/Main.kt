package com.sdidev.desk.dsjc

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import java.util.*

class Main : Application() {

    
    fun main(args: Array<String>) {
        launch(*args)
    }

    override fun start(primaryStage: Stage?) {
        val root = FXMLLoader.load<Parent>(Objects.requireNonNull(javaClass.getResource("/layouts/layout_main.fxml")))
        primaryStage!!.title = "Jar Launcher Desktop"
        primaryStage.scene = Scene(root, 520.0, 380.0)
        root.stylesheets.add(javaClass.getResource("/styles/main_style.css")!!.toExternalForm());
        primaryStage.isResizable = false
        primaryStage.show()
    }

}