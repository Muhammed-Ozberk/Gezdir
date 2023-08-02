package com.example.gezdir.ui.component.home.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.gezdir.R
import com.example.gezdir.data.entity.MessageList
import com.example.gezdir.ui.component.chat.ChatActivity
import com.example.gezdir.util.UserManager
import com.squareup.picasso.Picasso

class MessagesAdapter(private var mContext: Context, private val MessageList: List<MessageList>) :
    RecyclerView.Adapter<MessagesAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var sendersOfMessagesCardView: CardView? = null
        var username: TextView? = null
        var lastMessage: TextView? = null
        var image: ImageView? = null

        init {
            sendersOfMessagesCardView = view.findViewById(R.id.sendersOfMessagesCardView)
            username = view.findViewById(R.id.textViewUsername)
            lastMessage = view.findViewById(R.id.textViewLastMessage)
            image = view.findViewById(R.id.circleImageViewSender)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(mContext)
                .inflate(R.layout.senders_of_messages_card_view, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return MessageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val messageList = MessageList[position]

        holder.username?.text = messageList.username
        holder.lastMessage?.text = messageList.lastMessage
        Picasso.get()
            .load(messageList.image)
            .into(holder.image)

        holder.sendersOfMessagesCardView?.setOnClickListener {
            val intent = Intent(mContext, ChatActivity::class.java)
            intent.putExtra("conversationID", messageList.conversationID)
            intent.putExtra("senderID", UserManager.currentUserId)
            intent.putExtra("receiverID", messageList.receiverID)
            intent.putExtra("username", messageList.username)
            intent.putExtra("image", messageList.image)
            mContext.startActivity(intent)
        }
    }

}