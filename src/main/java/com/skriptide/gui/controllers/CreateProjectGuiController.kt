package com.skriptide.gui.controllers

import com.skriptide.CoreManager
import com.skriptide.include.ActiveWindow
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.control.ComboBox
import javafx.scene.image.Image
import javafx.stage.DirectoryChooser
import javafx.stage.Stage
import java.io.File
import javafx.scene.control.CheckBox




class CreateProjectGuiController {


    @FXML
    private var projectNameField: TextField? = null

    @FXML
    private var projectPathField: TextField? = null

    @FXML
    private var choosePathButton: Button? = null

    @FXML
    private var createButton: Button? = null

    @FXML
    private var cancelButton: Button? = null

    @FXML
    private var skriptVersionComboBox: ComboBox<String>? = null

    @FXML
    private var openAfterCreation: CheckBox? = null

    var rootProjectFolder = ""

    fun initGui(manager: CoreManager, thisWindow: ActiveWindow, returnWindow: ActiveWindow) {

        openAfterCreation!!.isSelected = true
        openAfterCreation!!.isDisable = true
        createButton?.isDisable = true
        rootProjectFolder = manager.configManager.defaultProjectPath.absolutePath
        skriptVersionComboBox?.items!!.add("2.2 bensku-dev33")
        skriptVersionComboBox?.selectionModel!!.select(0)

        projectNameField?.setOnKeyReleased { ev ->


            projectPathField?.text = File(rootProjectFolder, projectNameField?.text).absolutePath

            if(projectNameField?.text == "") {
                if(!createButton?.isDisabled!!) createButton?.isDisable = true
            } else {
                if(createButton?.isDisabled!!) createButton?.isDisable = false

                //Check existing projects for possible duplications
                var found = false
                manager.configManager.projects.values.forEach {
                    if(it.path == projectPathField?.text) {
                        found = true
                    }
                }
                createButton?.isDisable = found
            }


        }

        choosePathButton?.setOnAction {
            val fileChooserWindow = Stage()
            val dirChooser = DirectoryChooser()
            dirChooser.title = "Choose save path for the Project"
            val dir = dirChooser.showDialog(fileChooserWindow)
            if(dir != null) {
                rootProjectFolder = dir.absolutePath
                projectPathField?.text = File(rootProjectFolder, projectNameField?.text).absolutePath

            }

        }
        projectPathField!!.text = manager.configManager.defaultProjectPath.absolutePath


        createButton?.setOnAction {
            if(!projectPathField?.text?.contains(rootProjectFolder)!!) return@setOnAction
            manager.configManager.defaultProjectPath = File(rootProjectFolder)
            manager.projectManager.createNewProject(projectNameField?.text!!,projectPathField?.text!!, skriptVersionComboBox?.selectionModel?.selectedItem!!, openAfterCreation?.isSelected!!)

                thisWindow.close()
            if(!openAfterCreation?.isSelected!!) {
                returnWindow.stage.show()
            }
        }

        cancelButton!!.setOnAction {
            thisWindow.close()
            returnWindow.stage.show()
        }
    }
}