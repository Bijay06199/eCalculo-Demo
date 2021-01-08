package com.raisetech.ecalculo.zorbistore.data.repo

import com.raisetech.ecalculo.zorbistore.data.api.zorbiApiServices
import com.raisetech.ecalculo.zorbistore.data.prefs.PreferenceManager
import com.raisetech.ecalculo.zorbistore.ui.auth.login.body.ResetPasswordBody
import com.raisetech.ecalculo.zorbistore.ui.auth.login.body.UpdateCustomerBody
import com.raisetech.ecalculo.zorbistore.ui.auth.login.response.UpdateCustomerResponse
import okhttp3.ResponseBody
import retrofit2.Response

class ResetPasswordRepository(var apiServices: zorbiApiServices, var preferenceManager: PreferenceManager){
    suspend fun resetPassword(id:Int,cPassword:String,nPassword:String): Response<UpdateCustomerResponse> {



        val requestData= ResetPasswordBody()

        return apiServices.resetPassword(id,cPassword,nPassword,requestData)
    }
}