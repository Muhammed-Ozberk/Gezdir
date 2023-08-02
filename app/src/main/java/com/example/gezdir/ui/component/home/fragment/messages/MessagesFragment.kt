package com.example.gezdir.ui.component.home.fragment.messages

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.gezdir.R
import com.example.gezdir.databinding.FragmentDiscoverBinding
import com.example.gezdir.databinding.FragmentMessagesBinding
//import com.example.gezdir.databinding.FragmentMessagesBinding
import com.example.gezdir.ui.component.home.adapter.MessagesAdapter
import com.example.gezdir.ui.component.home.fragment.discover.DiscoverViewModel
import com.example.gezdir.util.UserManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessagesFragment : Fragment() {
    private lateinit var binding: FragmentMessagesBinding
    private val viewModel: MessagesViewModel by lazy { ViewModelProvider(this)[MessagesViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_messages, container, false)
        binding.messagesFragment = this
        getMessageSenderList()


        return binding.root
    }

    private fun getMessageSenderList(){
        val senderID=UserManager.currentUserId
        viewModel.getMessageSenderList(senderID){
            binding.messagesAdapter= context?.let { it1 -> MessagesAdapter(it1,it) }
            binding.messagesAnimationViewContainer.visibility=View.GONE
            binding.messagesConstraintLayout.visibility=View.VISIBLE
        }
    }

}