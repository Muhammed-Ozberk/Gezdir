package com.example.gezdir.ui.component.home.fragment.discover

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.gezdir.R
import com.example.gezdir.data.entity.Advert
import com.example.gezdir.data.entity.Token
import com.example.gezdir.data.entity.Users
import com.example.gezdir.databinding.FragmentDiscoverBinding
import com.example.gezdir.ui.component.createAd.CreateAdActivity
import com.example.gezdir.ui.component.home.adapter.DiscoverAdapter
import com.example.gezdir.ui.component.notifications.NotificationsActivity
import com.example.gezdir.ui.component.splash.SplashActivity
import com.example.gezdir.util.UserManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiscoverFragment : Fragment() {
    private lateinit var binding: FragmentDiscoverBinding
    private val viewModel: DiscoverViewModel by lazy { ViewModelProvider(this)[DiscoverViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_discover, container, false)

        binding.discoveriFragment = this

        isThereANotification()

        getMostPopularCitiyList() {
            binding.animationView.visibility = View.GONE
            binding.discoverConstraintLayout.visibility = View.VISIBLE
            binding.mostPopularCitiesAdapter = context?.let { it1 -> DiscoverAdapter(it1, it) }
        }
        viewModel.getProfileUrl(UserManager.currentUserProfileImage).toString()

        binding.helloName = "Merhaba, ${UserManager.currentUserName}"

        if (UserManager.currentUserGezdiren) {
            binding.buttonCreateAd1.visibility = View.VISIBLE
        }
        if (UserManager.currentUserGezdiren) {
            binding.textViewMostPopularCities.text = "İlanlarım"
        }

        return binding.root
    }

    private fun getMostPopularCitiyList(onComplete: (List<Advert>) -> Unit) {
        return viewModel.getMostPopularCitiyList() {
            onComplete(it)
        }
    }

    fun goToCreateAdFragment() {
        val intent = Intent(activity, CreateAdActivity::class.java)
        startActivity(intent)
    }

    fun onNotificationClick() {
        val intent = Intent(activity, NotificationsActivity::class.java)
        startActivity(intent)
    }

    fun isThereANotification(){
        viewModel.isThereANotification(){
            if (it){
                binding.imageViewBell.setImageResource(R.drawable.bell_dolu)
            }else{
                binding.imageViewBell.setImageResource(R.drawable.bell)
            }
        }
    }



}