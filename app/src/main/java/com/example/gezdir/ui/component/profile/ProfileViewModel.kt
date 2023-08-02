package com.example.gezdir.ui.component.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.gezdir.data.entity.RegistrationResult
import com.example.gezdir.data.repo.DataRepository
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor (private val dataRepo : DataRepository): ViewModel() {

    fun onUpdateProfile(
        id: String,
        name: String,
        surname: String,
        username: String,
        email: String,
        imagePart: Uri,
        onComplete: (RegistrationResult) -> Unit
    ) {
        dataRepo.updateProfile(id, name, surname, username, email, imagePart) {
            onComplete(it)
        }
    }

    fun getProfileImage(imagePart: String, onComplete: (String) -> Unit) {
        dataRepo.getImageUrl(imagePart) {
            onComplete(it)
        }
    }
}