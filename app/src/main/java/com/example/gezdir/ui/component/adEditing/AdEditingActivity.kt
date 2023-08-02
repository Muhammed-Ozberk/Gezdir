package com.example.gezdir.ui.component.adEditing

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.gezdir.R
import com.example.gezdir.databinding.ActivityAdEditingBinding
import com.example.gezdir.ui.base.BaseActivity
import com.example.gezdir.ui.component.home.HomeActivity
import com.example.gezdir.util.UserManager
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdEditingActivity : BaseActivity() {
    private lateinit var binding: ActivityAdEditingBinding
    private val viewModel: AdEditingViewModel by lazy { ViewModelProvider(this)[AdEditingViewModel::class.java] }
    private val PICK_IMAGE_REQUEST_CODE = 1
    private var imagePart: Uri = Uri.EMPTY
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ad_editing)
        binding.editTextContent.setText(intent.getStringExtra("content"))
        binding.editTextTitle.setText(intent.getStringExtra("title"))
        binding.editTextPrice.setText(intent.getStringExtra("price"))
        binding.editTextTime.setText(intent.getStringExtra("time"))
        Picasso.get()
            .load(intent.getStringExtra("image"))
            .into(binding.imageViewSelectedPictureEd)
        binding.adEditingActivity = this
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
            binding.imageViewSelectedPictureEd.setImageURI(selectedImageUri)
            imagePart = selectedImageUri!!
        }
    }

    fun chooseImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
    }

    fun onUpdateClick(adTitle: String, adContent: String, adTime: String, adPrice: String) {

        if (imagePart == Uri.EMPTY) {
            Toast.makeText(this, "Lütfen bir resim seçin", Toast.LENGTH_SHORT).show()
        } else if (adTitle.isBlank()) {
            binding.editTextTitleError = "Lütfen bir tanıtım içeriği giriniz"
        } else if (adContent.isBlank()) {
            binding.editTextTitleError = null
            binding.editTextContentError = "Lütfen detaylı bir açıklama giriniz"
        } else if (adTime.isBlank()) {
            binding.editTextContentError = null
            binding.editTextTimeError = "Lütfen gezi süresini giriniz"
        } else if (adPrice.isBlank()) {
            binding.editTextTimeError = null
            binding.editTextPriceError = "Lütfen fiyat giriniz"
        } else {
            binding.editTextPriceError = null
            val username = UserManager.currentUserUsername
            val advertID = intent.getStringExtra("id").toString()
            if (viewModel.onUpdateClick(
                    advertID,
                    username,
                    adTitle,
                    adContent,
                    adTime,
                    adPrice,
                    imagePart
                )
            ) {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
        }
    }

    fun onRemoveClick() {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("İlanı silme")
            .setMessage("Bu ilanı silmek istediğinize emin misiniz?")
            .setPositiveButton("Evet") { _, _ ->
                // Silme işlemi için gerekli kodları buraya yazın
                val advertID = intent.getStringExtra("id").toString()
                if (viewModel.onRemoveClick(advertID)) {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                }
            }
            .setNegativeButton("Hayır", null)
            .create()

        alertDialog.show()
    }
}