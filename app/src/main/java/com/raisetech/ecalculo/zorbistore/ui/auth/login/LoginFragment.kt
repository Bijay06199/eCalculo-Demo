package com.raisetech.ecalculo.zorbistore.ui.auth.login


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.andrognito.flashbar.Flashbar
import com.raisetech.ecalculo.R
import com.raisetech.ecalculo.activities.startup.LoginSplashActivity
import com.raisetech.ecalculo.zorbistore.base.BaseFragment
import com.raisetech.ecalculo.zorbistore.ui.auth.login.orders.CustomerOrderActivity
import com.raisetech.ecalculo.zorbistore.ui.auth.login.response.CustomerResponse
import com.raisetech.ecalculo.zorbistore.ui.auth.register.RegisterNameActivity
import com.raisetech.ecalculo.zorbistore.utils.AuthListenerInfo
import com.raisetech.ecalculo.zorbistore.utils.bindings.GlideApp
import com.raisetech.ecalculo.zorbistore.utils.extentions.dangerFlashBar
import com.raisetech.ecalculo.zorbistore.utils.extentions.infoFlashBar
import com.raisetech.ecalculo.zorbistore.utils.extentions.successFlashBar
import com.raisetech.ecalculo.zorbistore.utils.extentions.warningFlashBar
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment() : BaseFragment<com.raisetech.ecalculo.databinding.FragmentLoginBinding, LoginViewModel>(), AuthListenerInfo {


    var flashbar: Flashbar? = null
    override fun getLayoutId(): Int = R.layout.fragment_login
    override fun getViewModel(): LoginViewModel = loginViewModel
    private val loginViewModel: LoginViewModel by viewModel()

    var itemList = ArrayList<CustomerResponse>()

    var id:Int?=null
    var firstName:String?=""
    var lastName:String?=""
    var number:String?=""
    var address:String?=""
    var email:String?=""
    var shippingAddress:String?=""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setUpObservers()

    }

    private fun setUpObservers() {
        with(viewDataBinding) {
            with(loginViewModel) {

                //  getCustomers()
                loginSuccessEvent.observe(viewLifecycleOwner, Observer {

                    lLLoginLayout.visibility = View.GONE
                    afterLogin.visibility = View.VISIBLE

                    progressBar4.visibility = View.GONE

                     address = customer?.data?.address
                     email = customer?.data?.email
                     firstName = customer?.data?.firstName
                     id = customer?.data?.id
                     lastName = customer?.data?.lastName
                     number = customer?.data?.primaryContactNumber


                    preferenceManager.setEmail(email)
                    preferenceManager.setFirstName(firstName)
                    preferenceManager.setLastName(lastName)
                    preferenceManager.setNumber(number)
                    preferenceManager.setAddress(address)
                    preferenceManager.setCustomerId(id)

                    var intent = Intent(this@LoginFragment.activity, com.raisetech.ecalculo.zorbistore.ui.main.MainActivity::class.java)
                    startActivity(intent)






                })


                cl2.setOnClickListener {

                    val intent =
                            Intent(this@LoginFragment.activity, EditprofileActivity::class.java)
                    intent.putExtra(com.raisetech.ecalculo.zorbistore.constants.Constants.FirstName, firstName)
                    intent.putExtra(com.raisetech.ecalculo.zorbistore.constants.Constants.LastName, lastName)
                    intent.putExtra(com.raisetech.ecalculo.zorbistore.constants.Constants.Email, email)
                    intent.putExtra(com.raisetech.ecalculo.zorbistore.constants.Constants.Number, number)
                    intent.putExtra(com.raisetech.ecalculo.zorbistore.constants.Constants.Address, address)
                    intent.putExtra(com.raisetech.ecalculo.zorbistore.constants.Constants.Addrss1, address)
                    startActivity(intent)
                }


                customerDetailEvent.observe(viewLifecycleOwner, Observer {
//
//                    var firstName = customer?.get(0)?.firstName
//                    var lastName = customer?.get(0)?.lastName
//                    var email = customer?.get(0)?.email
//                    var number = customer?.get(0)?.username
//                    var address = customer?.get(0)?.shipping?.address1
//                    var address1 = customer?.get(0)?.shipping?.address2





                })
            }
        }
    }

    private fun initView() {


        loginViewModel.authListenerInfo = this
        viewDataBinding.progressBar4.visibility = View.GONE
        GlideApp.with(this)
                .load(preferenceManager.getImage())
                .into(viewDataBinding.ivPerson)


        btnAdmin.setOnClickListener {
            startActivity(Intent(this@LoginFragment.activity, LoginSplashActivity::class.java))
        }

        with(viewDataBinding) {
            linkClick.setOnClickListener {
                val url = "https://raisetech.com.np/"

                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }

            cl1.setOnClickListener {
                startActivity(
                        Intent(
                                this@LoginFragment.activity,
                                CustomerOrderActivity::class.java
                        )
                )
            }

            tvUserName.setText(preferenceManager.getFirstName())
            if (preferenceManager.getIsLoggedIn()) {
                lLLoginLayout.visibility = View.GONE
                afterLogin.visibility = View.VISIBLE
                btnAdmin.visibility=View.GONE
            } else if (!preferenceManager.getIsLoggedIn()) {
                lLLoginLayout.visibility = View.VISIBLE
                afterLogin.visibility = View.GONE
                btnAdmin.visibility=View.VISIBLE

            }

            with(loginViewModel) {
                setupUI(hideKeyboard)


                cl3.setOnClickListener {
                    preferenceManager.setIsLoggedIn(false)
                    lLLoginLayout.visibility = View.VISIBLE
                    afterLogin.visibility = View.GONE
                    btnAdmin.visibility=View.VISIBLE
                }

            }
        }


        with(viewDataBinding) {
            btnRegister.setOnClickListener {
                startActivity(Intent(this@LoginFragment.activity, RegisterNameActivity::class.java))

            }
        }
    }


    companion object {
        val TAG = LoginFragment::class.java.simpleName
        fun start(activity: FragmentActivity, containerId: Int) {
            val fragment = LoginFragment()
            activity.supportFragmentManager.beginTransaction()
                    .replace(containerId, fragment)
                    .addToBackStack(TAG)
                    .commit()
        }
    }

    override fun onSuccess(message: String) {
        flashbar = successFlashBar(message)
        flashbar?.show()
    }

    override fun onStarted() {
        viewDataBinding.progressBar4.visibility = View.VISIBLE
        var animation = AnimationUtils.loadAnimation(requireContext(), R.anim.rotation_anim)
        animation.setInterpolator(LinearInterpolator())
        viewDataBinding.progressBar4.startAnimation(animation)
        viewDataBinding.progressBar4.visibility = View.GONE

    }

    override fun onInfo(message: String) {
        flashbar = infoFlashBar(message)
        flashbar?.show()
    }

    override fun onWarning(message: String) {
        flashbar = warningFlashBar(message)
        flashbar?.show()
    }

    override fun onDanger(message: String) {
        flashbar = dangerFlashBar(message)
        flashbar?.show()
    }
}