package com.example.gezdir.ui.component.splash.login

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.gezdir.R
import com.example.gezdir.databinding.LoginBinding
import com.example.gezdir.ui.component.home.HomeActivity
import com.example.gezdir.ui.component.splash.register.RegisterCustomPopup
import com.example.gezdir.ui.component.splash.SplashActivityViewModel
import com.example.gezdir.ui.component.splash.letsStart.LetStartCustomPopup
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
class LoginCustomPopup : DialogFragment() {

    private val viewModel: LoginViewModel by lazy { ViewModelProvider(this)[LoginViewModel::class.java] }
    private lateinit var binding: LoginBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DataBindingUtil.inflate<LoginBinding>(
            layoutInflater,
            R.layout.login,
            null,
            false
        )
        val builder = AlertDialog.Builder(requireActivity()).apply {
            setView(binding.root)
        }

        binding.loginCustom = this
        binding.letsStart = LetStartCustomPopup()
        binding.supportFragmentManager = requireActivity().supportFragmentManager
        binding.emailError = null
        binding.passwordError = null

        return builder.create()
    }

    override fun onDismiss(dialog: DialogInterface) {
        // Pop-up kapatıldığında yapılacak işlemler
        super.onDismiss(dialog)
    }

    fun onLoginClick(email: String, password: String) {
        binding.loginCardView.visibility = View.GONE
        binding.loginAnimationViewContainer.visibility = View.VISIBLE
        if (email.isBlank()) {
            binding.emailError = "Email gerekli"
            binding.loginAnimationViewContainer.visibility = View.GONE
            binding.loginCardView.visibility = View.VISIBLE
        } else if (password.isBlank()) {
            binding.emailError = null
            binding.passwordError = "Şifre gerekli"
            binding.loginAnimationViewContainer.visibility = View.GONE
            binding.loginCardView.visibility = View.VISIBLE
        } else {
            binding.passwordError = null
            viewModel.onLoginClick(requireContext(), email, password){
                Log.e("LoginCustomPopup", "onLoginClick: $it")
                if (!it.success) {
                    binding.editTextEmail.setText("")
                    binding.editTextPassword.setText("")
                    binding.emailError = it.message
                    binding.loginAnimationViewContainer.visibility = View.GONE
                    binding.loginCardView.visibility = View.VISIBLE
                }
                else {
                    val intent = Intent(context, HomeActivity::class.java)
                    startActivity(intent)
                    dismiss()
                }
            }
        }

    }

    fun onCloseClick() {
        dismiss()
    }

    fun onNowRegisterClick(
        popupLetsStart: LetStartCustomPopup,
        supportFragmentManager: FragmentManager
    ) {
        dismiss()
        viewModel.onNowRegisterClick(popupLetsStart, supportFragmentManager)

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
