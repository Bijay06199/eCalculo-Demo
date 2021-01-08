package com.raisetech.ecalculo.zorbistore.data.repo


import com.raisetech.ecalculo.zorbistore.data.api.zorbiApiServices
import com.raisetech.ecalculo.zorbistore.data.network.SafeApiRequest
import com.raisetech.ecalculo.zorbistore.data.prefs.PreferenceManager
import com.raisetech.ecalculo.zorbistore.ui.auth.register.body.RegisterBody
import com.raisetech.ecalculo.zorbistore.ui.auth.register.response.RegisterResponse
import retrofit2.Response

class RegisterRepository (private val zorbiApiServices: zorbiApiServices, val preferenceManager: PreferenceManager): SafeApiRequest(){

    suspend fun register(
        email:String,
        userName:String,
         address:String
    ):Response<RegisterResponse>{

        val firstName=preferenceManager.getFirstName()
        val lastName=preferenceManager.getLastName()
        val userPassword=preferenceManager.getPassword()


        val requestData= RegisterBody(address,"Nepal",email,firstName,lastName,userPassword,userName,userName,address)
        return zorbiApiServices.register(requestData)
    }
}