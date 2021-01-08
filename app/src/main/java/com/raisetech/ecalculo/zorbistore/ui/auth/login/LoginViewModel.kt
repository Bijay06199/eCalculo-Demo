package com.raisetech.ecalculo.zorbistore.ui.auth.login


import android.view.View
import androidx.lifecycle.viewModelScope
import com.raisetech.ecalculo.zorbistore.base.BaseViewModel
import com.raisetech.ecalculo.zorbistore.data.prefs.PreferenceManager
import com.raisetech.ecalculo.zorbistore.data.repo.CustomerDetailsRepository
import com.raisetech.ecalculo.zorbistore.data.repo.CustomerRepository
import com.raisetech.ecalculo.zorbistore.ui.auth.login.response.CustomerResponse
import com.raisetech.ecalculo.zorbistore.utils.ApiException
import com.raisetech.ecalculo.zorbistore.utils.AuthListenerInfo
import com.raisetech.ecalculo.zorbistore.utils.NoInternetException
import com.raisetech.ecalculo.zorbistore.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import java.lang.NullPointerException

class LoginViewModel(private val preferenceManager: PreferenceManager, private val customerDetailsRepository: CustomerDetailsRepository, private val customerRepository: CustomerRepository) : BaseViewModel(){
    var email:String?=null
    var loginSuccessEvent=SingleLiveEvent<Unit>()
    var password:String?=null
    var authListenerInfo: AuthListenerInfo?=null

    fun login(view: View){


    }

    var customerDetailEvent= SingleLiveEvent<Unit>()
    var customerEvent=SingleLiveEvent<Unit>()
    var customer:CustomerResponse?=null





    fun getCustomers(){

        if (email.isNullOrEmpty()){
            authListenerInfo?.onDanger("Please enter email")

        }
        else if (password.isNullOrEmpty()){
            authListenerInfo?.onDanger("Please enter password")
        }
        else {

            viewModelScope.launch {
                authListenerInfo?.onStarted()

                try {
                    val response= customerRepository.getCustomer(email!!,password!!)
                    customer=response.body()
                    if (customer?.status=="SUCCESS"){
                        loginSuccessEvent.call()
                        preferenceManager.setIsLoggedIn(true)
                    }
                    else
                    {
                        authListenerInfo?.onInfo("Invalid Username")
                    }

                }
                catch (e: NoInternetException){

                }catch (e:ApiException){

                }catch (e:NullPointerException){

                }

            }




        }


    }

//    fun editProfile(view: View){
//
//        Intent(view.context,EditprofileActivity::class.java).also {
//            view.context.startActivity(it)
//        }
//    }
}