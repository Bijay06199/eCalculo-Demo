package com.raisetech.ecalculo.zorbistore.ui.auth.register


import android.content.Intent
import android.view.View
import com.raisetech.ecalculo.zorbistore.base.BaseViewModel
import com.raisetech.ecalculo.zorbistore.data.repo.RegisterRepository
import com.raisetech.ecalculo.zorbistore.utils.AuthListenerInfo
import com.raisetech.ecalculo.zorbistore.utils.SingleLiveEvent

class RegisterPasswordViewModel(private val registerRepository: RegisterRepository) : BaseViewModel(){

    var userPassword:String?=null
    val backClickedEvent = SingleLiveEvent<Unit>()
    var authListenerInfo: AuthListenerInfo?=null

    fun next(view:View){

        if(userPassword.isNullOrEmpty()){
            authListenerInfo?.onInfo("Please enter password")
        }
        else if(userPassword!!.length<8){
            authListenerInfo?.onWarning("Please enter password more than 6 characters")

        }
        else{

            registerRepository.preferenceManager.setPassword(userPassword!! )
            Intent(view.context,RegisterFinalActivity::class.java).also {
                view.context.startActivity(it)
            }
        }
    }

    fun back(){
        backClickedEvent.call()
    }

}