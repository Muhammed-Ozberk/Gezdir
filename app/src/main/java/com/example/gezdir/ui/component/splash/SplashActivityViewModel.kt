package com.example.gezdir.ui.component.splash

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import com.example.gezdir.data.entity.Advert
import com.example.gezdir.data.repo.DataRepository
import com.example.gezdir.ui.component.splash.letsStart.LetStartCustomPopup
import com.example.gezdir.ui.component.splash.login.LoginCustomPopup
import com.example.gezdir.ui.component.splash.register.RegisterCustomPopup
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashActivityViewModel @Inject constructor (private val dataRepo : DataRepository): ViewModel() {

    fun onRegisterClick(
        popupRegister: RegisterCustomPopup, supportFragmentManager: FragmentManager
    ) {
        popupRegister.show(supportFragmentManager, "register")
    }

    fun onLetsStartClick(
        popupLetsStart: LetStartCustomPopup, supportFragmentManager: FragmentManager
    ) {
        popupLetsStart.show(supportFragmentManager, "letsStart")
    }

    fun onLoginClick(popupLogin: LoginCustomPopup, supportFragmentManager: FragmentManager) {
        popupLogin.show(supportFragmentManager, "login")
    }

    fun getSplashAdvertList(onComplete: (List<Advert>) -> Unit) {
        dataRepo.getSplashAdvertList(){
            onComplete(it)
        }
    }
}