package com.example.gezdir.ui.component.notifications.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.gezdir.R
import com.example.gezdir.data.entity.Notification
import com.example.gezdir.ui.component.chat.ChatActivity
import com.example.gezdir.util.UserManager

class NotificationsAdapter(private var mContext: Context, private val NotificationList: List<Notification>) :
    RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var notiCardView: CardView? = null
        var username: TextView? = null
        var lastMessage: TextView? = null

        init {
            notiCardView = view.findViewById(R.id.notiCardView)
            username = view.findViewById(R.id.textViewNotiUsername)
            lastMessage = view.findViewById(R.id.textViewNotiLastMessage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(mContext)
                .inflate(R.layout.notifications_card_view, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return NotificationList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notificationList = NotificationList[position]

        holder.username?.text = notificationList.username
        holder.lastMessage?.text = notificationList.lastMessage


        holder.notiCardView?.setOnClickListener {
            val intent = Intent(mContext, ChatActivity::class.java)
            intent.putExtra("conversationID", notificationList.conversationID)
            intent.putExtra("senderID", UserManager.currentUserId)
            intent.putExtra("receiverID", notificationList.senderID)
            intent.putExtra("username", notificationList.username)
            intent.putExtra("image", notificationList.senderImage)
            mContext.startActivity(intent)
            (mContext as Activity).finish()
        }
    }

}