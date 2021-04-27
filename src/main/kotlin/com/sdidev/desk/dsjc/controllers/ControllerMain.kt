package com.sdidev.desk.dsjc.controllers

import com.jfoenix.controls.JFXButton
import com.jfoenix.controls.JFXCheckBox
import com.jfoenix.controls.JFXComboBox
import com.jfoenix.controls.JFXTextField
import com.sdidev.desk.dsjc.model.ModelInput
import com.sdidev.desk.dsjc.utils.Core
import com.sdidev.desk.dsjc.utils.Tools
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox
import javafx.stage.FileChooser
import java.io.File
import java.net.URL
import java.util.*
import kotlin.system.exitProcess


class ControllerMain : Initializable {

    @FXML
    private lateinit var anchor_root: AnchorPane

    @FXML
    private lateinit var btn_icon: VBox

    @FXML
    private lateinit var img_icon: ImageView

    @FXML
    private lateinit var edit_name: JFXTextField

    @FXML
    private lateinit var edit_exec_path: JFXTextField

    @FXML
    private lateinit var btn_choice_exe: JFXButton

    @FXML
    private lateinit var combo_box_category: JFXComboBox<String>

    @FXML
    private lateinit var check_terminal: JFXCheckBox

    @FXML
    private lateinit var edit_description: JFXTextField

    @FXML
    private lateinit var btn_close: JFXButton

    @FXML
    private lateinit var btn_create: JFXButton


    val fileChooser = FileChooser()
    var icon_path: String = ""


    override fun initialize(location: URL?, resources: ResourceBundle?) {
        def()

    }


    private fun def() {
        combo_box_category.items.addAll(
            "AudioVideo",
            "Audio",
            "Video",
            "Development",
            "Education",
            "Game",
            "Graphics",
            "Network",
            "Office",
            "Settings",
            "Utility"
        )
        combo_box_category.selectionModel.select(combo_box_category.items.size - 1)

        btn_icon.setOnMouseClicked {
            fileChooser.extensionFilters.clear()
            fileChooser.extensionFilters.add(getImageExt())
            val file = fileChooser.showOpenDialog(btn_icon.scene.window)
            if (file != null) {
                val image = Image(file.toURI().toString())
                img_icon.image = image
                icon_path = file.absolutePath
            }
        }

        btn_choice_exe.setOnMouseClicked {
            fileChooser.extensionFilters.clear()
            fileChooser.extensionFilters.add(getJarExt())
            val file = fileChooser.showOpenDialog(btn_icon.scene.window)
            if (file != null) {
                edit_exec_path.text = file.absolutePath
                file.setExecutable(true,false)
            }
        }





        btn_close.setOnAction {
            exitProcess(0)
        }

        btn_create.setOnAction {
            val name = edit_name.text.toString()
            val exec_path = edit_exec_path.text.toString()
            val category = combo_box_category.selectionModel.selectedItem.toString()
            val isTerminal = check_terminal.isSelected
            val description = edit_description.text.toString()
            if (checkIEOrNotF(name, false) || checkIEOrNotF(exec_path, true)) {
                if (checkIEOrNotF(name, false)) {
                    Tools.showSnackbar(anchor_root, "Please write name", "#e76f51", "#212121")
                }
                if (checkIEOrNotF(exec_path, true)) {
                    Tools.showSnackbar(anchor_root, "Exec file not found", "#e76f51", "#212121")
                }
                if (checkIEOrNotF(name, false) && checkIEOrNotF(exec_path, true)) {
                    Tools.showSnackbar(anchor_root, "Please complete a fields", "#e76f51", "#212121")
                }
            } else if (icon_path.isNotEmpty()) {
                if (checkIEOrNotF(icon_path, true)) {
                    Tools.showSnackbar(anchor_root, "Icon file not found", "#e76f51", "#212121")
                } else {
                    startCreate(ModelInput(name,exec_path,icon_path,category,isTerminal,description))
                }
            } else {
                startCreate(ModelInput(name,exec_path,"",category,isTerminal,description))
            }
        }
    }



    private fun startCreate (modelInput: ModelInput) {
        Thread(Core(modelInput,object : Core.OnCallbackListener{
            override fun onError(error: String) {
                Platform.runLater {
                    Tools.showSnackbar(anchor_root, error, "#e76f51", "#212121")
                }
            }

            override fun onProgress(log: String) {
                println(log)
            }

            override fun onComplete() {
                Platform.runLater {
                    edit_name.text = ""
                    edit_exec_path.text = ""
                    edit_description.text = ""
                    check_terminal.isSelected = false
                    combo_box_category.selectionModel.select(combo_box_category.items.size - 1)
                    img_icon.image = Image(javaClass.getResource("/images/icon_apps.png").toString())
                    icon_path = ""
                    Tools.showSnackbar(anchor_root, "Launcher Created", "#74c69d", "#212121")
                }
            }
        })).start()
    }




    private fun getImageExt(): FileChooser.ExtensionFilter {
        return FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg", "*.png")
    }

    private fun getJarExt(): FileChooser.ExtensionFilter {
        return FileChooser.ExtensionFilter("JAR files (*.jar)", "*.jar")
    }

    private fun checkIEOrNotF(input: String, isPath: Boolean): Boolean {
        return if (isPath) {
            val file = File(input)
            !file.exists()
        } else {
            input.trim().isEmpty()
        }
    }
}