package com.example.gezdir.ui.component.splash.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import com.example.gezdir.data.entity.RegistrationResult
import com.example.gezdir.data.repo.DataRepository
import com.example.gezdir.ui.component.home.HomeActivity
import com.example.gezdir.ui.component.splash.letsStart.LetStartCustomPopup
import com.example.gezdir.ui.component.splash.register.RegisterCustomPopup
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val dataRepo: DataRepository) : ViewModel() {

    fun onLoginClick(
        mContext: Context,
        email: String,
        password: String,
        onComplete: (RegistrationResult) -> Unit
    ) {
        dataRepo.login(email, password) {
            onComplete(it)
        }
    }

    fun onNowRegisterClick(
        popupLetsStart: LetStartCustomPopup, supportFragmentManager: FragmentManager
    ) {
        popupLetsStart.show(supportFragmentManager, "register")
    }
}