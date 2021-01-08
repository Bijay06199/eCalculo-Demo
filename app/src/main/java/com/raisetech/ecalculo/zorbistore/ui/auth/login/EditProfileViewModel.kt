package com.raisetech.ecalculo.zorbistore.ui.auth.login

import androidx.lifecycle.viewModelScope
import com.raisetech.ecalculo.zorbistore.base.BaseViewModel
import com.raisetech.ecalculo.zorbistore.data.repo.UpdateCustomerRepository
import com.raisetech.ecalculo.zorbistore.ui.auth.login.response.UpdateCustomerResponse
import com.raisetech.ecalculo.zorbistore.utils.ApiException
import com.raisetech.ecalculo.zorbistore.utils.AuthListenerInfo
import com.raisetech.ecalculo.zorbistore.utils.NoInternetException
import com.raisetech.ecalculo.zorbistore.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.NullPointerException

class EditProfileViewModel(private val updateCustomerRepository: UpdateCustomerRepository) : BaseViewModel(){

    var firstName:String?=null
    var lastName:String?=null
    var email:String?=null
    var mobileNumber:String?=null
    var address:String?=null
    var additionalInfo:String?=null
    var authListenerInfo:AuthListenerInfo?=null
    var customer:UpdateCustomerResponse?=null
    var customerEvent=SingleLiveEvent<Unit>()


    fun updateCustomer(){

        if (firstName.isNullOrEmpty()){
            authListenerInfo?.onInfo("Enter First Name")
        }
        else if (lastName.isNullOrEmpty()){
            authListenerInfo?.onInfo("Enter last Name")
        }
        else if (email.isNullOrEmpty()){
            authListenerInfo?.onInfo("Enter email")
        }
        else if (mobileNumber.isNullOrEmpty()){
            authListenerInfo?.onInfo("Enter mobile number")
        }
        else if (address.isNullOrEmpty()){
            authListenerInfo?.onInfo("Enter address")
        }
        else{
            viewModelScope.launch {
                updateCustomerRepository.preferenceManager.setFirstName(firstName)
                updateCustomerRepository.preferenceManager.setLastName(lastName)
                updateCustomerRepository.preferenceManager.setEmail(email)
                updateCustomerRepository.preferenceManager.setNumber(mobileNumber)
                updateCustomerRepository.preferenceManager.setAddress(address)
                try {
                    var response=updateCustomerRepository.updateCustomer(updateCustomerRepository.preferenceManager.getCustomerId()!!)
                    customer=response.body()
                    if (customer?.status=="SUCCESS")
                     customerEvent.call()
                }catch (e:NoInternetException){
                    authListenerInfo?.onInfo(e.message!!)
                }catch (e:ApiException){
                    authListenerInfo?.onInfo(e.message!!)

                }catch (e:NullPointerException){
                    authListenerInfo?.onInfo(e.message!!)

                }
            }
        }
    }



}