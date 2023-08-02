package com.example.gezdir.ui.component.createAd

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.gezdir.data.repo.DataRepository
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateAdViewModel @Inject constructor (private val dataRepo : DataRepository): ViewModel() {

    fun onSavedClick(username:String,adTitle: String, adContent: String,adTime:String, adPrice: String, imagePart: Uri):Boolean {
        return dataRepo.adSave(username,adTitle, adContent,adTime, adPrice, imagePart)
    }

}