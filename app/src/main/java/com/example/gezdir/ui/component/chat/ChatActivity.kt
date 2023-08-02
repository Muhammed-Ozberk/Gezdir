package com.example.gezdir.ui.component.chat


import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.gezdir.R
import com.example.gezdir.data.entity.Message
import com.example.gezdir.databinding.ActivityChatBinding
import com.example.gezdir.ui.base.BaseActivity
import com.example.gezdir.ui.component.chat.adapter.ChatAdapter
import com.google.firebase.firestore.FieldValue
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date

@AndroidEntryPoint
class ChatActivity : BaseActivity() {

    private lateinit var binding: ActivityChatBinding
    private val viewModel: ChatViewModel by lazy { ViewModelProvider(this)[ChatViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat)
        binding.chatActivity = this
        binding.username = intent.getStringExtra("username")
        binding.toolbar2.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        Log.e("imageDene", "onCreate: " + intent.getStringExtra("image"))

        getMessageList {
            binding.chatAdapter =
                intent.getStringExtra("image")?.let { it1 -> ChatAdapter(this, it, it1) }
        }


    }


    private fun getMessageList(onComplete: (List<Message>) -> Unit) {
        val conversationID = intent.getStringExtra("conversationID")
        viewModel.getMessageList(conversationID!!) {
            onComplete(it)
        }
    }


    fun onSendClick(message: String) {
        binding.editTextText.setText("")
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val timeFormat = SimpleDateFormat("HH:mm:ss")

        viewModel.sendMessage(
            Message(
                messageID = null,
                senderID = intent.getStringExtra("senderID"),
                receiverID = intent.getStringExtra("receiverID"),
                conversationID = intent.getStringExtra("conversationID"),
                message = message,
                date = dateFormat.format(currentDate),
                time = timeFormat.format(currentDate),
                isSeen = null,
                timestamp = FieldValue.serverTimestamp()
            )
        )

    }

}

