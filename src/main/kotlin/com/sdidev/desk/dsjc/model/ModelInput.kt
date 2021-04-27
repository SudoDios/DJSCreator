package com.sdidev.desk.dsjc.model

data class ModelInput(
    var name: String = "", var execPath: String = "",
    var iconPath: String = "", var category : String = "Utility", var isTerminal : Boolean = false,
    var description : String = ""
)

