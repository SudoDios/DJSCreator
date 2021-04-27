package com.sdidev.desk.dsjc.utils

import com.sdidev.desk.dsjc.model.ModelInput
import java.io.File

class Core (var modelInput: ModelInput,var onCallbackListener: OnCallbackListener) : Runnable{

    private lateinit var newAppDirFile : File
    private lateinit var newExecDirFile : File
    private lateinit var newIconDirFile : File
    private lateinit var newShellDirFile : File

    private lateinit var desktopFirDir : File
    private val execFileName = File(modelInput.execPath).name
    private val iconFileName : String? = null


    override fun run() {
        createAppDir()
    }

    private fun createAppDir () {
        onCallbackListener.onProgress("Creating App Directory ...")
        newAppDirFile = File("${Tools.getHomeDir()}/apps/${modelInput.name.replace(" ", "_").toLowerCase()}")
        if (!newAppDirFile.exists()) {
            if (!newAppDirFile.mkdirs()) {
                onCallbackListener.onError("Error When Create App Dir")
            } else {
                onCallbackListener.onProgress("App Directory Create Completed")
                copyFiles()
            }
        } else {
            onCallbackListener.onProgress("App Directory Create Completed")
            copyFiles()
        }
    }

    private fun copyFiles () {
        onCallbackListener.onProgress("Copy Jar File ...")
        CommandLine.runCommand("cp ${modelInput.execPath} ${newAppDirFile.absolutePath}", object : CommandLine.OnCommandCallback {
            override fun onFinished() {
                onCallbackListener.onProgress("Copied Jar File")
                newExecDirFile = File(newAppDirFile.absolutePath,File(modelInput.execPath).name)
                if (modelInput.iconPath.isNotEmpty()) {
                    onCallbackListener.onProgress("Copy Icon File ...")
                    CommandLine.runCommand("cp ${modelInput.iconPath} ${newAppDirFile.absolutePath}",
                        object : CommandLine.OnCommandCallback {
                            override fun onFinished() {
                                onCallbackListener.onProgress("Copied Icon File")
                                newIconDirFile = File(newAppDirFile.absolutePath,File(modelInput.iconPath).name)
                                createShellR()
                            }
                            override fun onError() {
                                onCallbackListener.onError("Error When Copy Icon File")
                            }
                        })
                } else {
                    createShellR()
                }
            }
            override fun onError() {
                onCallbackListener.onError("Error When Copy Jar File")
            }
        })
    }


    private fun createShellR () {
        onCallbackListener.onProgress("Create Shell Runner File ...")
        newShellDirFile = File(newAppDirFile.absolutePath,"runner.sh")
        if (!newShellDirFile.exists()) {
            if (!newShellDirFile.createNewFile()) {
                onCallbackListener.onError("Error When Create Shell Script")
            } else {
                onCallbackListener.onProgress("Created Shell Runner")
                newShellDirFile.setExecutable(true,false)
                CommandLine.writeShellToFile(File(newAppDirFile.absolutePath,execFileName))
                createDesktopFile()
            }
        } else {
            onCallbackListener.onProgress("Created Shell Runner")
            newShellDirFile.setExecutable(true,false)
            CommandLine.writeShellToFile(File(newAppDirFile.absolutePath,execFileName))
            createDesktopFile()
        }
    }


    private fun createDesktopFile () {
        onCallbackListener.onProgress("Create Desktop Entry ...")
        desktopFirDir = File("${Tools.getHomeDir()}/.local/share/applications","${modelInput.name.replace(" ","_").toLowerCase()}.desktop")
        val model = if (modelInput.iconPath.isNotEmpty()) {
            ModelInput(modelInput.name,"${newAppDirFile.absolutePath}/runner.sh", newIconDirFile.absolutePath,modelInput.category,modelInput.isTerminal,modelInput.description)
        } else {
            ModelInput(modelInput.name,"${newAppDirFile.absolutePath}/runner.sh", "",modelInput.category,modelInput.isTerminal,modelInput.description)
        }
        if (!desktopFirDir.parentFile.exists()) desktopFirDir.parentFile.mkdirs()
        if (!desktopFirDir.exists()) {
            if (!desktopFirDir.createNewFile()) {
                onCallbackListener.onError("Error When Create Desktop Entry")
            } else {
                onCallbackListener.onProgress("Created Desktop Entry")
                desktopFirDir.setExecutable(true,false)
                CommandLine.writeDesktopToFile(model, desktopFirDir)
                onCallbackListener.onComplete()
            }
        } else {
            onCallbackListener.onProgress("Created Desktop Entry")
            desktopFirDir.setExecutable(true,false)
            CommandLine.writeDesktopToFile(model, desktopFirDir)
            onCallbackListener.onComplete()
        }
    }


    interface OnCallbackListener {
        fun onError (error : String)
        fun onProgress(log : String)
        fun onComplete ()
    }
}