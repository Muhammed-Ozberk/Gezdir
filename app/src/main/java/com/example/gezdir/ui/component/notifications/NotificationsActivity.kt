package com.example.gezdir.ui.component.notifications

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.gezdir.R
import com.example.gezdir.databinding.ActivityNotificationsBinding
import com.example.gezdir.ui.base.BaseActivity
import com.example.gezdir.ui.component.notifications.adapter.NotificationsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationsActivity : BaseActivity() {

    private lateinit var binding: ActivityNotificationsBinding
    private val viewModel: NotificationsViewModel by lazy { ViewModelProvider(this)[NotificationsViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notifications)
        binding.toolbar4.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.notificationsActivity = this
        getNotificationList()

    }

    private fun getNotificationList(){
        viewModel.getNotificationList(){
            binding.notificationsAdapter= NotificationsAdapter(this,it)
        }
    }

}