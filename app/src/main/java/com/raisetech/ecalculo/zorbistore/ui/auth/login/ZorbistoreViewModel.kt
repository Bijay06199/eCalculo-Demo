package com.raisetech.ecalculo.zorbistore.ui.auth.login

import android.content.Intent
import android.view.View
import androidx.lifecycle.viewModelScope
import com.raisetech.ecalculo.zorbistore.base.BaseViewModel
import com.raisetech.ecalculo.zorbistore.data.repo.ResetPasswordRepository
import com.raisetech.ecalculo.zorbistore.ui.auth.login.response.UpdateCustomerResponse
import com.raisetech.ecalculo.zorbistore.ui.main.MainActivity
import com.raisetech.ecalculo.zorbistore.utils.AuthListenerInfo
import kotlinx.coroutines.launch

class ZorbistoreViewModel(private val resetPasswordRepository: ResetPasswordRepository):BaseViewModel() {

    var currentPassword:String?=null
    var newPassword:String?=null
    var confirmPassword:String?=null
    var authListenerInfo:AuthListenerInfo?=null
    var resetPassword:UpdateCustomerResponse?=null



    fun reset(view: View){

        if (currentPassword.isNullOrEmpty()){
            authListenerInfo?.onInfo("Enter current password")
        }
        else if (newPassword.isNullOrEmpty()){
            authListenerInfo?.onInfo("Enter new password")

        }
        else if(newPassword!=confirmPassword){
            authListenerInfo?.onInfo("Password doesnot match")
        }
        else {

            viewModelScope.launch {
                authListenerInfo?.onStarted()
                var response=resetPasswordRepository.resetPassword(resetPasswordRepository.preferenceManager.getCustomerId()!!,currentPassword!!,newPassword!!)

                resetPassword=response.body()
                if (resetPassword?.status=="SUCCESS"){
                    Intent(view.context,MainActivity::class.java).also {
                        view.context.startActivity(it)
                    }
                }
            }
        }
    }

}