package com.raisetech.ecalculo.zorbistore.utils


interface AuthListenerInfo {
    fun onSuccess(message:String)

    fun onStarted()

    fun onInfo(message: String)

    fun onWarning(message: String)

    fun onDanger(message: String)

}