package com.example.gezdir.ui.component.splash.register

import android.content.Context
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import com.example.gezdir.data.entity.RegistrationResult
import com.example.gezdir.data.repo.DataRepository
import com.example.gezdir.ui.component.splash.login.LoginCustomPopup
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor (private val dataRepo : DataRepository): ViewModel() {

    fun onRegisterClick(
        dialogFragment: DialogFragment,
        popupLogin: LoginCustomPopup,
        supportFragmentManager: FragmentManager,
        name: String,
        surname: String,
        userName: String,
        email: String,
        password: String,
        isGezdiren: Boolean,
        onComplete: (RegistrationResult) -> Unit
    ) {
        dataRepo.register(name, surname, userName, email, password, isGezdiren){
            if (it.success){
                openLoginPopup(dialogFragment, popupLogin, supportFragmentManager)
            }else{
                onComplete(it)
            }
        }
    }

    fun openLoginPopup(
        dialogFragment: DialogFragment,
        popupLogin: LoginCustomPopup,
        supportFragmentManager: FragmentManager
    ) {
        dialogFragment.dismiss()
        popupLogin.show(supportFragmentManager, "login")
    }
}