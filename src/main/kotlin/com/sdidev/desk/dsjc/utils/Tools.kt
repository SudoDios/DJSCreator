package com.sdidev.desk.dsjc.utils

import com.jfoenix.controls.JFXSnackbar
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent
import javafx.geometry.Insets
import javafx.scene.control.Label
import javafx.scene.effect.DropShadow
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.util.Duration

class Tools {

    companion object {
        fun showSnackbar(root: Pane?, message: String?, bodyColor: String, textColor: String?) {
            val label = Label(message)
            label.textFill = Color.valueOf(textColor)
            label.style = "-fx-font-family: ubvazir; -fx-font-size: 14;"
            val hBox = HBox()
            hBox.children.add(label)
            hBox.style = "-fx-background-color: $bodyColor; -fx-background-radius: 12"
            HBox.setMargin(label, Insets(10.0, 15.0, 10.0, 15.0))
            hBox.effect = DropShadow()
            val pane = VBox()
            pane.children.add(hBox)
            VBox.setMargin(hBox, Insets(15.0, 15.0, 15.0, 15.0))
            val snackbarEvent = SnackbarEvent(pane, Duration.seconds(2.0), null)
            val snackbar = JFXSnackbar(root)
            snackbar.enqueue(snackbarEvent)
        }

        fun getHomeDir () : String {
            return System.getProperty("user.home")
        }
    }

}