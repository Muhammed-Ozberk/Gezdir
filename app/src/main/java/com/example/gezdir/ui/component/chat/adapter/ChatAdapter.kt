package com.example.gezdir.ui.component.chat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gezdir.R
import com.example.gezdir.data.entity.Message
import com.example.gezdir.util.UserManager
import com.google.firebase.Timestamp
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class ChatAdapter(
    private var mContext: Context,
    private val MessageList: List<Message>,
    private val receiverProfileUrl: String
) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    private val userID = UserManager.currentUserId


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var sentMessage: TextView? = null
        var receiveMessage: TextView? = null
        var sentProfileUrl: CircleImageView? = null
        var receiveProfileUrl: CircleImageView? = null
        var sentDate: TextView? = null
        var receiveDate: TextView? = null

        init {
            sentMessage = view.findViewById(R.id.textViewSentMessage)
            receiveMessage = view.findViewById(R.id.textViewReceiveMessage)
            sentProfileUrl = view.findViewById(R.id.imageViewSentProfileImage)
            receiveProfileUrl = view.findViewById(R.id.imageViewReceiveProfileImage)
            sentDate = view.findViewById(R.id.textViewSentTime)
            receiveDate = view.findViewById(R.id.textViewReceiveTime)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(viewType, parent, false)
        return ViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        val message = MessageList[position]
        return if (message.senderID == userID) {
            R.layout.send_card_view
        } else {
            R.layout.receive_card_view
        }
    }

    override fun getItemCount(): Int {
        return MessageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = MessageList[position]


        if (message.senderID == userID) {
            holder.sentMessage?.text = message.message
            holder.sentProfileUrl?.let {
                Picasso.get().load(UserManager.currentUserProfileUrl).into(it)
            }
            val firestoreTimestamp = message.timestamp as? Timestamp

            if (firestoreTimestamp != null) {
                // Firestore Timestamp'ı Java Date nesnesine dönüştürme
                val date: Date = firestoreTimestamp.toDate()

                // Saat ve dakika biçimlerini belirleme ve Türkiye saat dilimini ayarlama
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                timeFormat.timeZone = TimeZone.getTimeZone("Europe/Istanbul")

                // Java Date nesnesini belirlediğimiz saat dakika biçimine dönüştürme
                val formattedTime: String = timeFormat.format(date)
                holder.sentDate?.text = formattedTime

            }

        } else {
            holder.receiveMessage?.text = message.message
            holder.receiveProfileUrl?.let { Picasso.get().load(receiverProfileUrl).into(it) }
            val firestoreTimestamp = message.timestamp as? Timestamp

            if (firestoreTimestamp != null) {
                // Firestore Timestamp'ı Java Date nesnesine dönüştürme
                val date: Date = firestoreTimestamp.toDate()

                // Saat ve dakika biçimlerini belirleme ve Türkiye saat dilimini ayarlama
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                timeFormat.timeZone = TimeZone.getTimeZone("Europe/Istanbul")

                // Java Date nesnesini belirlediğimiz saat dakika biçimine dönüştürme
                val formattedTime: String = timeFormat.format(date)
                holder.receiveDate?.text = formattedTime
            }
        }

    }
}