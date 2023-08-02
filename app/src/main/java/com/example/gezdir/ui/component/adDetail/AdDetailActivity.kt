package com.example.gezdir.ui.component.adDetail


import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.gezdir.R
import com.example.gezdir.databinding.ActivityAdDetailBinding
import com.example.gezdir.ui.base.BaseActivity
import com.example.gezdir.util.UserManager
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityAdDetailBinding
    private val viewModel: AdDetailViewModel by lazy { ViewModelProvider(this)[AdDetailViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ad_detail)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.adDetailActivity = this
        binding.username = intent.getStringExtra("username")
        binding.title = intent.getStringExtra("title")
        binding.content = intent.getStringExtra("content")
        binding.price = intent.getStringExtra("price")
        binding.time = intent.getStringExtra("time")
        Picasso.get().load(intent.getStringExtra("image")).into(binding.imageViewAd)

    }

    fun onChatClick(){
        val senderID = UserManager.currentUserId
        val receiverUsername= intent.getStringExtra("username")
        val mContext = this
        if (receiverUsername != null) {
            viewModel.onChatClick(mContext,senderID, receiverUsername)
        }
    }

}