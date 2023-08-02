package com.example.gezdir.ui.component.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.gezdir.data.entity.Message
import com.example.gezdir.data.repo.DataRepository
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor (private val dataRepo : DataRepository) : ViewModel() {

    fun getMessageList(conversationID: String, onComplete: (List<Message>) -> Unit) {
        dataRepo.getMessageList(conversationID) {
            onComplete(it)
        }
    }

    fun sendMessage(message: Message){
        dataRepo.sendMessage(message)
    }


}