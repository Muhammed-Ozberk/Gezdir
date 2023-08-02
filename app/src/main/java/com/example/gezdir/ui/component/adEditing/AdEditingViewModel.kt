package com.example.gezdir.ui.component.adEditing

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.gezdir.data.repo.DataRepository
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdEditingViewModel @Inject constructor (private val dataRepo : DataRepository) : ViewModel() {

    fun onUpdateClick(
        advertID: String,
        username: String,
        adTitle: String,
        adContent: String,
        adTime: String,
        adPrice: String,
        imagePart: Uri
    ): Boolean {
        return dataRepo.adUpdate(advertID, username, adTitle, adContent, adTime, adPrice, imagePart)
    }

    fun onRemoveClick(advertID: String): Boolean {

        return dataRepo.adRemove(advertID)
    }
}