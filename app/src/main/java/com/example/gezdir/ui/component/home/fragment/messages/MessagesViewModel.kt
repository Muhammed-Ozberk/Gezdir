package com.example.gezdir.ui.component.home.fragment.messages

import androidx.lifecycle.ViewModel
import com.example.gezdir.data.entity.MessageList
import com.example.gezdir.data.repo.DataRepository
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor (private val dataRepo : DataRepository): ViewModel() {

    fun getMessageSenderList(senderID: String, onComplete: (List<MessageList>) -> Unit) {

        return dataRepo.getMessageSenderList(senderID) { messageSenderList ->
            onComplete(messageSenderList)
        }

    }

}