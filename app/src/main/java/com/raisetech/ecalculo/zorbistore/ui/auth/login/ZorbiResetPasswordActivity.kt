package com.raisetech.ecalculo.zorbistore.ui.auth.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import com.andrognito.flashbar.Flashbar
import com.raisetech.ecalculo.R
import com.raisetech.ecalculo.databinding.ActivityZorbiResetPasswordBinding
import com.raisetech.ecalculo.zorbistore.base.BaseActivity
import com.raisetech.ecalculo.zorbistore.utils.AuthListenerInfo
import com.raisetech.ecalculo.zorbistore.utils.extentions.dangerFlashBar
import com.raisetech.ecalculo.zorbistore.utils.extentions.infoFlashBar
import com.raisetech.ecalculo.zorbistore.utils.extentions.successFlashBar
import com.raisetech.ecalculo.zorbistore.utils.extentions.warningFlashBar
import org.koin.androidx.viewmodel.ext.android.viewModel

class ZorbiResetPasswordActivity :BaseActivity<ActivityZorbiResetPasswordBinding,ZorbistoreViewModel>(),AuthListenerInfo {

    override fun getLayoutId(): Int =R.layout.activity_zorbi_reset_password
    override fun getViewModel(): ZorbistoreViewModel =zorbiStoreViewModel
    private val zorbiStoreViewModel:ZorbistoreViewModel by viewModel()
    var flashbar:Flashbar?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()

    }

    private fun initView() {
        with(viewDataBinding){
            with(zorbiStoreViewModel){



                progressBar4.visibility=View.GONE
                authListenerInfo=this@ZorbiResetPasswordActivity
            }
        }
    }


    override fun onSuccess(message: String) {
        flashbar = successFlashBar(message)
        flashbar?.show()
    }

    override fun onStarted() {
        viewDataBinding.progressBar4.visibility = View.VISIBLE
        var animation = AnimationUtils.loadAnimation(this, R.anim.rotation_anim)
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