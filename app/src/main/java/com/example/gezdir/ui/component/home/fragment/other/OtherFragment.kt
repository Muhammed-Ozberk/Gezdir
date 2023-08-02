package com.example.gezdir.ui.component.home.fragment.other

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.gezdir.R
import com.example.gezdir.databinding.FragmentOtherBinding
import com.example.gezdir.ui.component.home.fragment.discover.DiscoverViewModel
import com.example.gezdir.ui.component.profile.ProfileActivity
import com.example.gezdir.ui.component.splash.SplashActivity
import com.example.gezdir.ui.component.virtualTrip.VirtualTripActivity
import com.example.gezdir.util.UserManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtherFragment : Fragment() {

    private lateinit var binding: FragmentOtherBinding
    private val viewModel: OtherViewModel by lazy { ViewModelProvider(this)[OtherViewModel::class.java] }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_other, container, false)
        binding.otherFragment = this
        binding.name = UserManager.currentUserName
        binding.email = UserManager.currentUserEmail
        Log.e("other", UserManager.currentUserProfileImage)
        getProfileImage(UserManager.currentUserProfileImage)

        binding.virtualTripCardView.setOnClickListener {
            val intent = Intent(context, VirtualTripActivity::class.java)
            startActivity(intent)
        }

        binding.profileCardView.setOnClickListener {
            val intent = Intent(context, ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.logoutCardView.setOnClickListener {

            val auth = FirebaseAuth.getInstance()

// Kullanıcı oturumunu kapat
            auth.signOut()
            FirebaseMessaging.getInstance().deleteToken().addOnSuccessListener {
                Log.e("token", "deleted")
            }.addOnFailureListener {
                Log.e("token", "not deleted")
            }


// Oturum açma verilerini cihazdan sil
            auth.addAuthStateListener { firebaseAuth ->
                val user = firebaseAuth.currentUser
                if (user == null) {
                    // Kullanıcı oturumu kapattı, oturum açma verilerini sil
                    UserManager.currentUserId = ""
                    UserManager.currentUserName = ""
                    UserManager.currentUserSurname = ""
                    UserManager.currentUserUsername = ""
                    UserManager.currentUserEmail = ""
                    UserManager.currentUserGezdiren = false
                    UserManager.currentUserProfileImage = ""
                    UserManager.currentUserUID = ""
                    // Uygulamayı kapat
                    startActivity(Intent(context, SplashActivity::class.java))
                }
            }
        }

        return binding.root
    }

    fun getProfileImage(imagePart: String) {
        viewModel.getProfileImage(imagePart) {
            Picasso.get()
                .load(it)
                .into(binding.circleImageViewOther)
            binding.otherAnimationViewContainer.visibility = View.GONE
            binding.otherConstraintLayout.visibility = View.VISIBLE
        }
    }


}