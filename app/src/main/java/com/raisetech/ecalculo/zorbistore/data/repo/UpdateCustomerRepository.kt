package com.raisetech.ecalculo.zorbistore.data.repo

import com.raisetech.ecalculo.zorbistore.data.api.zorbiApiServices
import com.raisetech.ecalculo.zorbistore.data.prefs.PreferenceManager
import com.raisetech.ecalculo.zorbistore.ui.auth.login.body.UpdateCustomerBody
import com.raisetech.ecalculo.zorbistore.ui.auth.login.response.UpdateCustomerResponse
import retrofit2.Response

class UpdateCustomerRepository(var apiServices: zorbiApiServices,var preferenceManager: PreferenceManager){
    suspend fun updateCustomer(id:Int):Response<UpdateCustomerResponse>{



     val requestData=UpdateCustomerBody(preferenceManager.getAddress(),"Nepal",preferenceManager.getFirstName(),preferenceManager.getLastName(),preferenceManager.getNumber(),preferenceManager.getNumber(),preferenceManager.getAddress())

        return apiServices.updateCustomer(id,requestData)
    }
}