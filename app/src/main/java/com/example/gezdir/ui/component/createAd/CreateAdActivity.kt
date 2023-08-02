package com.example.gezdir.ui.component.createAd


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.gezdir.R
import com.example.gezdir.databinding.ActivityCreateAdBinding
import com.example.gezdir.ui.base.BaseActivity
import com.example.gezdir.ui.component.home.HomeActivity
import com.example.gezdir.util.UserManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateAdActivity : BaseActivity() {
    private lateinit var binding: ActivityCreateAdBinding
    private val viewModel: CreateAdViewModel by lazy { ViewModelProvider(this)[CreateAdViewModel::class.java] }
    private val PICK_IMAGE_REQUEST_CODE = 1
    private var imagePart: Uri = Uri.EMPTY


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_ad)
        binding.createAdActivity = this
        binding.editTextTitleError = null
        binding.editTextContentError = null
        binding.editTextTimeError = null
        binding.editTextPriceError = null

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("onActivityResult", "requestCode: $requestCode, resultCode: $resultCode, data: $data")

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            binding.imageViewSelectedPicture.setImageURI(selectedImageUri)
            imagePart = selectedImageUri!!
        }
    }

    fun chooseImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
    }

    fun onSavedClick(adTitle: String, adContent: String, adTime: String, adPrice: String) {

        if (imagePart == Uri.EMPTY) {
            Toast.makeText(this, "Lütfen bir resim seçin", Toast.LENGTH_SHORT).show()
        } else if(adTitle.isBlank()){
            binding.editTextTitleError = "Lütfen bir tanıtım içeriği giriniz"
        } else if(adContent.isBlank()){
            binding.editTextTitleError = null
            binding.editTextContentError = "Lütfen detaylı bir açıklama giriniz"
        } else if(adTime.isBlank()){
            binding.editTextContentError = null
            binding.editTextTimeError = "Lütfen gezi süresini giriniz"
        } else if(adPrice.isBlank()){
            binding.editTextTimeError = null
            binding.editTextPriceError = "Lütfen fiyat giriniz"
        } else {
            binding.editTextPriceError = null
            val username=UserManager.currentUserUsername
            if (viewModel.onSavedClick(username ,adTitle, adContent, adTime, adPrice, imagePart)){
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
        }
    }

}