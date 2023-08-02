package com.example.gezdir.ui.component.home.fragment.other

import androidx.lifecycle.ViewModel
import com.example.gezdir.data.repo.DataRepository
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OtherViewModel @Inject constructor (private val dataRepo : DataRepository): ViewModel() {

    fun getProfileImage(imagePart: String, onComplete: (String) -> Unit) {
        dataRepo.getImageUrl(imagePart) {
            onComplete(it)
        }
    }
}