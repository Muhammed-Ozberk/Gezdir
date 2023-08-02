package com.example.gezdir.ui.component.splash

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.gezdir.R
import com.example.gezdir.data.entity.Advert
import com.example.gezdir.databinding.ActivitySplashBinding
import com.example.gezdir.ui.base.BaseActivity
import com.example.gezdir.ui.component.splash.adapter.SplashAdapter
import com.example.gezdir.ui.component.splash.letsStart.LetStartCustomPopup
import com.example.gezdir.ui.component.splash.login.LoginCustomPopup
import com.example.gezdir.ui.component.splash.register.RegisterCustomPopup
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val viewModel: SplashActivityViewModel by lazy { ViewModelProvider(this)[SplashActivityViewModel::class.java] }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        binding.splashActivity = this
        binding.letStartCustomPopup = LetStartCustomPopup()
        binding.registerCustomPopup = RegisterCustomPopup()
        binding.loginCustomPopup = LoginCustomPopup()
        binding.supportFragmentManager = supportFragmentManager

        getSplashAdvertList()

    }

    fun onLetsStartClick(
        popupLetsStart: LetStartCustomPopup,
        supportFragmentManager: FragmentManager
    ) {
        viewModel.onLetsStartClick(popupLetsStart, supportFragmentManager)

    }

    fun onRegisterClick(
        popupRegister: RegisterCustomPopup,
        supportFragmentManager: FragmentManager
    ) {
        viewModel.onRegisterClick(popupRegister, supportFragmentManager)
    }

    fun onLoginClick(
        popupLogin: LoginCustomPopup,
        supportFragmentManager: FragmentManager
    ) {
        viewModel.onLoginClick(popupLogin, supportFragmentManager)
    }

    private fun getSplashAdvertList() {
        return viewModel.getSplashAdvertList() {
            val dialogFragment = DialogFragment()
            val popupLogin = LoginCustomPopup()
            val supportFragmentManager = supportFragmentManager
            binding.splashAdapter = SplashAdapter(
                this,
                it as ArrayList<Advert>,
                dialogFragment,
                popupLogin,
                supportFragmentManager
            )
            binding.splashAnimationViewContainer.visibility = View.GONE
            binding.splashConstraintLayout.visibility = View.VISIBLE
        }
    }

}