package com.example.gezdir.ui.component.splash.register

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.gezdir.R
import com.example.gezdir.databinding.RegisterBinding
import com.example.gezdir.ui.component.splash.login.LoginCustomPopup
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
class RegisterCustomPopup : DialogFragment() {

    private lateinit var binding: RegisterBinding
    private val viewModel: RegisterViewModel by lazy { ViewModelProvider(this)[RegisterViewModel::class.java] }
    private var isGezdiren: Boolean = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = DataBindingUtil.inflate<RegisterBinding>(
            layoutInflater,
            R.layout.register,
            null,
            false
        )
        val builder = AlertDialog.Builder(requireActivity()).apply {
            setView(binding.root)
        }

        binding.registerCustom = this
        binding.loginCustomPopup = LoginCustomPopup()
        binding.supportFragmentManager = requireActivity().supportFragmentManager
        binding.nameError = null
        binding.surnameError = null
        binding.userNameError = null
        binding.emailError = null
        binding.passwordError = null

        isGezdiren = arguments?.getBoolean("isGezdiren", false) ?: false

        return builder.create()

    }

    override fun onDismiss(dialog: DialogInterface) {
        // Pop-up kapatıldığında yapılacak işlemler
        super.onDismiss(dialog)
    }

    fun onRegisterClick(
        dialogFragment: DialogFragment,
        popupLogin: LoginCustomPopup,
        supportFragmentManager: FragmentManager,
        name: String,
        surname: String,
        userName: String,
        email: String,
        password: String,
    ) {

        if (name.isBlank()) {
            binding.nameError = "İsim gerekli"
        } else if (surname.isBlank()) {
            binding.nameError = null
            binding.surnameError = "Soyisim gerekli"
        } else if (userName.isBlank()) {
            binding.surnameError = null
            binding.userNameError = "Kullanıcı adı gerekli"
        } else if (email.isBlank()) {
            binding.userNameError = null
            binding.emailError = "Email gerekli"
        } else if (!isValidEmail(email)) {
            binding.emailError = "Email geçersiz"
        } else if (password.isBlank()) {
            binding.emailError = null
            binding.passwordError = "Şifre gerekli"
        } else if (password.length < 8) {
            binding.passwordError = "Şifre en az 8 karakter olmalı"
        } else if (!isStrongPassword(password)) {
            binding.passwordError = "Şifre en az 1 büyük harf, 1 küçük harf, 1 sayı ve 1 özel karakter içermeli"
        } else {
            binding.passwordError = null
            viewModel.onRegisterClick(dialogFragment,popupLogin, supportFragmentManager, name, surname, userName, email, password, isGezdiren){
                if (it.field=="username"){
                    binding.userNameError = it.message
                }else if (it.field=="email"){
                    binding.userNameError = null
                    binding.emailError = it.message
                }
            }
        }
    }

    fun onCloseClick() {
        dismiss()
    }

    fun onLoginClick(
        dialogFragment: DialogFragment,
        popupLogin: LoginCustomPopup,
        supportFragmentManager: FragmentManager
    ) {
        viewModel.openLoginPopup(dialogFragment,popupLogin, supportFragmentManager)
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        val pattern = Pattern.compile(emailRegex)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    private fun isStrongPassword(password: String): Boolean {
        val passwordRegex =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\$%^&*()_+=|\\\\{}\\[\\]:\";'<>?,./~]).*\$"
        val pattern = Pattern.compile(passwordRegex)
        val matcher = pattern.matcher(password)
        return matcher.matches()
    }

}