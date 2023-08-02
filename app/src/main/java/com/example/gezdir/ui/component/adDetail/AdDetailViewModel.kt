package com.example.gezdir.ui.component.adDetail

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.example.gezdir.data.repo.DataRepository
import com.example.gezdir.ui.component.chat.ChatActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdDetailViewModel @Inject constructor (private val dataRepo : DataRepository) : ViewModel() {

    fun onChatClick(mContext: Context, senderID:String, receiverUsername: String){
        dataRepo.createConversation(senderID, receiverUsername) {
            val intent = Intent(mContext, ChatActivity::class.java)
            intent.putExtra("conversationID", it.first.conversationID)
            intent.putExtra("senderID", senderID)
            intent.putExtra("receiverID", it.first.userTwo)
            intent.putExtra("username", receiverUsername)
            intent.putExtra("image", it.second)
            mContext.startActivity(intent)
            (mContext as Activity).finish()
        }
    }

}