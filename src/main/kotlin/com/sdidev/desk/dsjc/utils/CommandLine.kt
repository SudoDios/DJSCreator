package com.sdidev.desk.dsjc.utils

import com.sdidev.desk.dsjc.model.ModelInput
import javafx.application.Platform
import java.io.File
import java.io.FileNotFoundException
import java.io.PrintWriter
import java.util.concurrent.TimeUnit

class CommandLine () {

    companion object {
        fun runCommand (command : String,onCommandCallback: OnCommandCallback) {
            Thread {
                val proc: Process = Runtime.getRuntime().exec(command)
                val success = proc.waitFor(3, TimeUnit.SECONDS);
                Platform.runLater {
                    if (success) {
                        onCommandCallback.onFinished()
                    } else {
                        onCommandCallback.onError()
                    }
                }
            }.start()
        }

        fun createFile (dir : String,fileName : String,onCommandCallback: OnCommandCallback) {
            val file = File(dir,fileName)
            if (!file.exists()) {
                if (file.createNewFile()) {
                    onCommandCallback.onFinished()
                } else {
                    onCommandCallback.onFinished()
                }
            } else {
                onCommandCallback.onFinished()
            }
        }
        fun writeShellToFile (execFile : File) {
            var printer : PrintWriter? = null
            try {
                printer = PrintWriter(File(execFile.parent,"runner.sh"))
                printer.println("#!/bin/sh \n")
                printer.println("cd ${execFile.parent}")
                printer.println("java -jar ${execFile.name}")
            } catch (e: FileNotFoundException) {
                println(e.toString())
                e.printStackTrace()
            } finally {
                printer?.close()
            }
        }

        fun writeDesktopToFile (modelInput: ModelInput,dekstopFile : File) {
            var printer : PrintWriter? = null
            try {
                printer = PrintWriter(dekstopFile)
                printer.println("[Desktop Entry]")
                printer.println("Name=${modelInput.name}")
                printer.println("Exec=${modelInput.execPath}")
                if (modelInput.iconPath.isNotEmpty()) printer.println("Icon=${modelInput.iconPath}")
                printer.println("Terminal=${modelInput.isTerminal}")
                printer.println("Categories=${modelInput.category};")
                printer.println("Type=Application")
                printer.println("Comment=${modelInput.description}")
            } catch (e: FileNotFoundException) {
                println(e.toString())
                e.printStackTrace()
            } finally {
                printer?.close()
            }
        }
    }


    interface OnCommandCallback {
        fun onFinished ()
        fun onError ()
    }
}