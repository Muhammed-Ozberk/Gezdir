package com.example.gezdir.ui.component.splash.letsStart


import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.gezdir.R
import com.example.gezdir.databinding.LetsStartCardViewBinding
import com.example.gezdir.ui.component.splash.register.RegisterCustomPopup

class LetStartCustomPopup : DialogFragment() {

    private val viewModel: LetStartViewModel by lazy { ViewModelProvider(this)[LetStartViewModel::class.java] }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val binding = DataBindingUtil.inflate<LetsStartCardViewBinding>(
            layoutInflater,
            R.layout.lets_start_card_view,
            null,
            false
        )
        val builder = AlertDialog.Builder(requireActivity()).apply {
            setView(binding.root)
        }

        binding.letsStartCustom = this
        binding.registerCustomPopup = RegisterCustomPopup()
        binding.supportFragmentManager = requireActivity().supportFragmentManager

        return builder.create()
    }

    override fun onDismiss(dialog: DialogInterface) {
        // Pop-up kapatıldığında yapılacak işlemler
        super.onDismiss(dialog)
    }

    fun onCardViewBuyServiceClick(
        popupRegister: RegisterCustomPopup,
        supportFragmentManager: FragmentManager
    ) {
        val isGezdiren = false
        viewModel.onCardViewBuyServiceClick(popupRegister, supportFragmentManager, isGezdiren)
    }

    fun onCardViewToBeGezdirenClick(
        popupRegister: RegisterCustomPopup,
        supportFragmentManager: FragmentManager
    ) {
        val isGezdiren = true
        viewModel.onCardViewToBeGezdirenClick(popupRegister, supportFragmentManager, isGezdiren)
    }

    fun onCloseClick() {
        dismiss()
    }
}