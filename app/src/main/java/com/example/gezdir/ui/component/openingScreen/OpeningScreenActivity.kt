package com.example.gezdir.ui.component.openingScreen

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.example.gezdir.R
import com.example.gezdir.data.entity.Token
import com.example.gezdir.data.entity.Users
import com.example.gezdir.databinding.ActivityOpeningScreenBinding
import com.example.gezdir.ui.component.home.HomeActivity
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

class OpeningScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOpeningScreenBinding
    private lateinit var animation: android.view.animation.Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_opening_screen)
        // Cihaz karanlık modda mı kontrol et
        val isDarkMode = isDarkModeEnabled()

        if (isDarkMode) {
            binding.animationImageView.setImageResource(R.drawable.logo_size_invert)
        }


        animation = AnimationUtils.loadAnimation(this, R.anim.splash_anim)
        binding.animationImageView.startAnimation(animation)

        Handler(Looper.getMainLooper()).postDelayed({
            permissionLauncher()
        }, 1000L)


    }

    private fun isDarkModeEnabled(): Boolean {
        return when (resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK) {
            android.content.res.Configuration.UI_MODE_NIGHT_YES -> true
            android.content.res.Configuration.UI_MODE_NIGHT_NO -> false
            else -> {
                // Android 10 ve öncesi sürümler için karanlık mod desteklenmiyordu
                // Bu nedenle, cihazın API seviyesini kontrol ederek durumu belirleyebiliriz
                // Eğer API seviyesi 29'dan büyükse, o zaman karanlık modda olmalıdır.
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
            }
        }
    }

    private fun authCheck() {
        val database = FirebaseDatabase.getInstance()
        val refUser = database.getReference("users")
        val auth = FirebaseAuth.getInstance()

        // Oturum açma verilerini cihaza kaydet
        auth.addAuthStateListener { firebaseAuth ->
            val user: FirebaseUser? = firebaseAuth.currentUser
            if (user != null) {

                val queryEmail = refUser.orderByChild("email").equalTo(user.email)

                queryEmail.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val userInfo = snapshot.children.first().getValue(Users::class.java)
                        Log.e("authCheck", "onAuthStateChanged:signed_in:$userInfo")

                        userInfo?.let {
                            UserManager.currentUserId = snapshot.children.first().key.toString()
                            UserManager.currentUserName = userInfo.name.toString()
                            UserManager.currentUserSurname = userInfo.surname.toString()
                            UserManager.currentUserUsername = userInfo.username.toString()
                            UserManager.currentUserEmail = userInfo.email.toString()
                            UserManager.currentUserGezdiren = userInfo.gezdiren == true
                            UserManager.currentUserProfileImage = userInfo.profileImage.toString()
                            UserManager.currentUserPassword = userInfo.password.toString()
                            saveToken()
                            animation.cancel()
                            startActivity(Intent(this@OpeningScreenActivity, HomeActivity::class.java))
                            finish()
                        }

                    }
                    override fun onCancelled(error: DatabaseError) {
                        Log.e("firebase", "Error getting data", error.toException())
                    }
                })

            } else {
                animation.cancel()
                startActivity(Intent(this, SplashActivity::class.java))
                finish()
            }
        }
    }

    private fun saveToken() {

        if (UserManager.currentUserId != "") {
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    // FCM token değerini kullanabilirsiniz
                    val collectionRef = FirebaseFirestore.getInstance().collection("fcmTokens")
                    val query = collectionRef.whereEqualTo("userID", UserManager.currentUserId)
                    query.get().addOnSuccessListener { querySnapshot ->
                        if (querySnapshot.isEmpty) {

                            val tokenObj = Token(UserManager.currentUserId, token)
                            collectionRef.add(tokenObj)
                                .addOnSuccessListener {
                                    Log.e("saveToken", "Token kaydedildi")
                                }
                                .addOnFailureListener { exception ->
                                    Log.e("saveToken", "Token kaydedilemedi: ${exception.message}")
                                }
                        } else {
                            val docId = querySnapshot.documents[0].id
                            collectionRef.document(docId)
                                .update("token", token)
                                .addOnSuccessListener {
                                    Log.e("saveToken", "Token güncellendi")
                                }
                                .addOnFailureListener { exception ->
                                    Log.e("saveToken", "Token güncellenemedi: ${exception.message}")
                                }
                        }
                    }
                        .addOnFailureListener { exception ->
                            Log.e("FCM", "Sorgu hatası: ${exception.message}")
                        }
                } else {
                    // Token değeri alınamadı
                    Log.e("saveToken", "Token değeri alınamadı: ${task.exception?.message}")
                }
            }
        }
    }

    fun permissionLauncher() {
        // İzinler için kontrol yapılır
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            // API seviyesi 26 veya üzerindeyse, POST_NOTIFICATIONS iznini ekleyin
            val permission = arrayOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.RECORD_AUDIO,
                android.Manifest.permission.READ_CALENDAR,
                android.Manifest.permission.READ_MEDIA_IMAGES,
                android.Manifest.permission.POST_NOTIFICATIONS,
            )
            // İzin başlatıcısını başlatın
            val ret = permissionLauncherMultiple.launch(permission)
            Log.e("TAG", "onCreate: $ret")

        } else {
            val permission = arrayOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.RECORD_AUDIO,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_CALENDAR,
            )
            // İzin başlatıcısını başlatın
            permissionLauncherMultiple.launch(permission)
        }
    }


    private val permissionLauncherMultiple = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        var areAllGranted = true

        for (isGranted in result.values) {
            areAllGranted = areAllGranted && isGranted
        }

        if (areAllGranted) {
            Log.e("TAG", "onCreate: İzinler verildi")
            authCheck()
        } else {
            Log.e("per", "onCreate: ${result}")
            Toast.makeText(this, "Tüm izinleri vermelisiniz", Toast.LENGTH_SHORT).show()
        }

    }
}