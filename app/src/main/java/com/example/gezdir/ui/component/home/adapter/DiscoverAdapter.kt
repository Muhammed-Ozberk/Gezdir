package com.example.gezdir.ui.component.home.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.gezdir.R
import com.example.gezdir.data.entity.Advert
import com.example.gezdir.ui.component.adDetail.AdDetailActivity
import com.example.gezdir.ui.component.adEditing.AdEditingActivity
import com.example.gezdir.util.UserManager
import com.squareup.picasso.Picasso

class DiscoverAdapter(private var mContext: Context, private val AdvertList: List<Advert>) :
    RecyclerView.Adapter<DiscoverAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var advertCardView: CardView? = null
        var username: TextView? = null
        var title: TextView? = null
        var price: TextView? = null
        var image: ImageView? = null

        init {
            advertCardView = view.findViewById(R.id.advertCardView)
            username = view.findViewById(R.id.textViewUsername)
            title = view.findViewById(R.id.textViewTitle)
            price = view.findViewById(R.id.price)
            image = view.findViewById(R.id.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(mContext).inflate(R.layout.advert_card_view, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return AdvertList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val advert = AdvertList[position]

        holder.username?.text = advert.username
        holder.title?.text = advert.title
        holder.price?.text = advert.price
        Picasso.get()
            .load(advert.image)
            .into(holder.image)

        holder.advertCardView?.setOnClickListener {
            if (UserManager.currentUserGezdiren) {
                val intent = Intent(mContext, AdEditingActivity::class.java)
                intent.putExtra("id", advert.id)
                intent.putExtra("username", advert.username)
                intent.putExtra("title", advert.title)
                intent.putExtra("content", advert.content)
                intent.putExtra("time", advert.time)
                intent.putExtra("price", advert.price)
                intent.putExtra("image", advert.image)
                mContext.startActivity(intent)
            } else {
                val intent = Intent(mContext, AdDetailActivity::class.java)
                intent.putExtra("id", advert.id)
                intent.putExtra("username", advert.username)
                intent.putExtra("title", advert.title)
                intent.putExtra("content", advert.content)
                intent.putExtra("time", advert.time)
                intent.putExtra("price", advert.price)
                intent.putExtra("image", advert.image)
                mContext.startActivity(intent)
            }

        }

    }
}