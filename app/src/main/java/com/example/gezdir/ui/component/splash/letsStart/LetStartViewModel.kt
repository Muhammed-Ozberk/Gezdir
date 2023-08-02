package com.example.gezdir.ui.component.splash.letsStart

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import com.example.gezdir.ui.component.splash.register.RegisterCustomPopup
import dagger.hilt.android.lifecycle.HiltViewModel

class LetStartViewModel:ViewModel() {

    fun onCardViewBuyServiceClick(
        popupRegister: RegisterCustomPopup, supportFragmentManager: FragmentManager,isGezdiren:Boolean
    ) {
        val bundle = Bundle()
        bundle.putBoolean("isGezdiren", isGezdiren) // userId verisini Bundle'a ekle
        popupRegister.arguments = bundle // Bundle'ı Fragment'a ata

        popupRegister.show(supportFragmentManager, "register")
    }

    fun onCardViewToBeGezdirenClick(
        popupRegister: RegisterCustomPopup, supportFragmentManager: FragmentManager,isGezdiren:Boolean
    ) {
        val bundle = Bundle()
        bundle.putBoolean("isGezdiren", isGezdiren) // userId verisini Bundle'a ekle
        popupRegister.arguments = bundle // Bundle'ı Fragment'a ata

        popupRegister.show(supportFragmentManager, "register")
    }
}