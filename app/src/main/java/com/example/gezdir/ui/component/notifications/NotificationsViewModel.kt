package com.example.gezdir.ui.component.notifications

import androidx.lifecycle.ViewModel
import com.example.gezdir.data.entity.Notification
import com.example.gezdir.data.repo.DataRepository
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor (private val dataRepo : DataRepository): ViewModel() {

    fun getNotificationList( onComplete: (List<Notification>) -> Unit) {
        dataRepo.getNotificationList() {
            onComplete(it)
        }
    }
}