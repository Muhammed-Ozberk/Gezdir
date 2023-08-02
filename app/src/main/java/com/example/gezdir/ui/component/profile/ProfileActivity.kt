package com.example.gezdir.ui.component.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.gezdir.R
import com.example.gezdir.databinding.ActivityProfileBinding
import com.example.gezdir.ui.base.BaseActivity
import com.example.gezdir.ui.component.home.HomeActivity
import com.example.gezdir.util.UserManager
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.UUID
import java.util.regex.Pattern

@AndroidEntryPoint
class ProfileActivity : BaseActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val viewModel: ProfileViewModel by lazy { ViewModelProvider(this)[ProfileViewModel::class.java] }
    private val PICK_IMAGE_REQUEST_CODE = 1
    private var imagePart: Uri = Uri.EMPTY
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        binding.profileActivity = this
        binding.toolbarProfile.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.editTextName.setText(UserManager.currentUserName)
        binding.editTextSurname.setText(UserManager.currentUserSurname)
        binding.editTextUserName.setText(UserManager.currentUserUsername)
        binding.editTextEmail.setText(UserManager.currentUserEmail)
        Log.e("profile", UserManager.currentUserProfileImage)
        getProfileImage(UserManager.currentUserProfileImage)
        binding.nameError = null
        binding.surnameError = null
        binding.userNameError = null
        binding.emailError = null
        binding.passwordError = null

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("onActivityResult", "requestCode: $requestCode, resultCode: $resultCode, data: $data")

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            binding.circleImageView.setImageURI(selectedImageUri)
            imagePart = selectedImageUri!!
        }
    }

    fun chooseImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
    }

    fun onUpdateProfile(name: String, surname: String, username: String, email: String) {
        if(name.isBlank()){
            binding.nameError = "Lütfen bir isim giriniz"
        } else if(surname.isBlank()){
            binding.nameError = null
            binding.surnameError = "Lütfen bir soyisim giriniz"
        } else if(username.isBlank()){
            binding.surnameError = null
            binding.userNameError = "Lütfen bir kullanıcı adı giriniz"
        } else if(email.isBlank()){
            binding.userNameError = null
            binding.emailError = "Lütfen bir email giriniz"
        } else if(!isValidEmail(email)){
            binding.emailError = "Lütfen geçerli bir email giriniz"
        } else {
            binding.emailError = null
            val id = UserManager.currentUserId
            viewModel.onUpdateProfile(id, name, surname, username, email, imagePart) {
                if (it.success) {
                    UserManager.currentUserName = name
                    UserManager.currentUserSurname = surname
                    UserManager.currentUserUsername = username
                    UserManager.currentUserEmail = email
                    Toast.makeText(this, "Profiliniz güncellendi", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomeActivity::class.java))
                } else {
                    Toast.makeText(this, "Bir hata oluştu", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }


    fun getProfileImage(imagePart: String) {
        viewModel.getProfileImage(imagePart) {
            Picasso.get()
                .load(it)
                .into(binding.circleImageView)
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        val pattern = Pattern.compile(emailRegex)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }
}