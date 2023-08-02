package com.example.gezdir.data.repo

import android.net.Uri
import android.util.Log
import com.example.gezdir.data.entity.Advert
import com.example.gezdir.data.entity.Conversation
import com.example.gezdir.data.entity.FcmNotification
import com.example.gezdir.data.entity.FcmNotificationData
import com.example.gezdir.data.entity.FcmResponse
import com.example.gezdir.data.entity.Message
import com.example.gezdir.data.entity.MessageList
import com.example.gezdir.data.entity.Notification
import com.example.gezdir.data.entity.RegistrationResult
import com.example.gezdir.data.entity.Token
import com.example.gezdir.data.entity.Users
import com.example.gezdir.services.FcmApiService
import com.example.gezdir.util.UserManager
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

class DataRepository(
    @Named("users")
    private val refUser: DatabaseReference,
    @Named("advert")
    private val refAdvert: DatabaseReference,
    private val storageRef: StorageReference,
    @Named("conversation")
    private val conversationRef: CollectionReference,
    @Named("messages")
    private val messagesRef: CollectionReference,
    @Named("fcmTokens")
    private val fcmTokensRef: CollectionReference
) {

    fun getSplashAdvertList(onComplete: (List<Advert>) -> Unit) {

        refAdvert.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val advertList = ArrayList<Advert>()
                val downloadTasks = mutableListOf<Task<Uri>>()

                for (ds in snapshot.children) {
                    val advert = ds.getValue(Advert::class.java)
                    advert?.let {
                        val imageRef = storageRef.child(it.image!!)
                        val downloadTask = imageRef.downloadUrl
                            .addOnSuccessListener { uri ->
                                val imageUrl = uri.toString()
                                advertList.add(
                                    Advert(
                                        ds.key,
                                        it.username,
                                        it.title,
                                        it.content,
                                        it.time,
                                        it.price,
                                        imageUrl
                                    )
                                )
                            }
                            .addOnFailureListener { exception ->
                                exception.printStackTrace()
                            }
                        downloadTasks.add(downloadTask)
                    }
                }

                Tasks.whenAllComplete(downloadTasks)
                    .addOnSuccessListener {
                        onComplete(advertList)
                    }
                    .addOnFailureListener { exception ->
                        exception.printStackTrace()
                    }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("firebase", "Error getting data", error.toException())
            }
        })
    }

    fun getMostPopularCityList(onComplete: (List<Advert>) -> Unit) {


        refAdvert.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val advertList = ArrayList<Advert>()
                val downloadTasks = mutableListOf<Task<Uri>>()

                if (UserManager.currentUserGezdiren) {

                    for (ds in snapshot.children) {
                        val advert = ds.getValue(Advert::class.java)
                        advert?.let {
                            val imageRef = storageRef.child(it.image!!)
                            val downloadTask = imageRef.downloadUrl
                                .addOnSuccessListener { uri ->
                                    val imageUrl = uri.toString()
                                    if (it.username == UserManager.currentUserUsername) {
                                        advertList.add(
                                            Advert(
                                                ds.key,
                                                it.username,
                                                it.title,
                                                it.content,
                                                it.time,
                                                it.price,
                                                imageUrl
                                            )
                                        )
                                    }

                                }
                                .addOnFailureListener { exception ->
                                    exception.printStackTrace()
                                }
                            downloadTasks.add(downloadTask)
                        }
                    }
                } else {
                    for (ds in snapshot.children) {
                        val advert = ds.getValue(Advert::class.java)
                        advert?.let {
                            val imageRef = storageRef.child(it.image!!)
                            val downloadTask = imageRef.downloadUrl
                                .addOnSuccessListener { uri ->
                                    val imageUrl = uri.toString()
                                    advertList.add(
                                        Advert(
                                            ds.key,
                                            it.username,
                                            it.title,
                                            it.content,
                                            it.time,
                                            it.price,
                                            imageUrl
                                        )
                                    )
                                }
                                .addOnFailureListener { exception ->
                                    exception.printStackTrace()
                                }
                            downloadTasks.add(downloadTask)
                        }
                    }
                }

                Tasks.whenAllComplete(downloadTasks)
                    .addOnSuccessListener {
                        onComplete(advertList)
                    }
                    .addOnFailureListener { exception ->
                        exception.printStackTrace()
                    }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("firebase", "Error getting data", error.toException())
            }
        })
    }

    fun getMessageSenderList(senderID: String, onComplete: (List<MessageList>) -> Unit) {
        val query = conversationRef.where(
            Filter.or(
                Filter.equalTo("userOne", senderID),
                Filter.greaterThanOrEqualTo("userTwo", senderID)
            )
        )

        query.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.e("getMessageSenderList", "Hata: $exception")
                exception.printStackTrace()
                return@addSnapshotListener
            }
            if (snapshot != null && !snapshot.isEmpty && snapshot.documents.size > 0) {
                val messageSenderList = mutableListOf<MessageList>()
                var pendingDownloads = snapshot.documents.size
                for (ds in snapshot.documents) {
                    val conversation = ds.toObject(Conversation::class.java)
                    if (conversation != null) {
                        if (conversation.userOne == senderID) {
                            val queryUsername = refUser.orderByKey().equalTo(conversation.userTwo)
                            queryUsername.get().addOnSuccessListener { snapshotUser ->
                                if (snapshotUser.exists()) {
                                    for (dsUser in snapshotUser.children) {
                                        val user = dsUser.getValue(Users::class.java)
                                        if (user != null) {
                                            user.profileImage?.let { imageName ->
                                                getImageUrl(imageName) { url ->
                                                    Log.e("getMessageSenderList", "url: $url")
                                                    messageSenderList.add(
                                                        MessageList(
                                                            user.username,
                                                            conversation.lastMessage,
                                                            ds.id,
                                                            conversation.userTwo,
                                                            url
                                                        )
                                                    )
                                                    pendingDownloads--
                                                    if (pendingDownloads == 0) {
                                                        Log.e(
                                                            "getMessageSenderList",
                                                            "messageSenderList: $messageSenderList"
                                                        )
                                                        onComplete(messageSenderList)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }.addOnFailureListener { exceptionUser ->
                                Log.e("firebase", "Veri alınamadı", exceptionUser)
                            }
                        } else {
                            val queryUsername = refUser.orderByKey().equalTo(conversation.userOne)
                            queryUsername.get().addOnSuccessListener { snapshotUser ->
                                if (snapshotUser.exists()) {
                                    for (dsUser in snapshotUser.children) {
                                        val user = dsUser.getValue(Users::class.java)
                                        if (user != null) {
                                            user.profileImage?.let { imageName ->
                                                getImageUrl(imageName) { url ->
                                                    Log.e("getMessageSenderList", "url: $url")
                                                    messageSenderList.add(
                                                        MessageList(
                                                            user.username,
                                                            conversation.lastMessage,
                                                            ds.id,
                                                            conversation.userOne,
                                                            url
                                                        )
                                                    )
                                                    pendingDownloads--
                                                    if (pendingDownloads == 0) {
                                                        Log.e(
                                                            "getMessageSenderList",
                                                            "messageSenderList: $messageSenderList"
                                                        )
                                                        onComplete(messageSenderList)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }.addOnFailureListener { exceptionUser ->
                                Log.e("firebase", "Veri alınamadı", exceptionUser)
                            }
                        }
                    }
                }
            } else {
                onComplete(listOf())
            }
        }
    }

    fun getImageUrl(imageName: String?, onComplete: (String) -> Unit) {
        var imageRef: StorageReference? = null
        if (imageName != null && imageName.isNotEmpty()) {
            imageRef = imageName.let { storageRef.child(it) }
        } else {
            imageRef = storageRef.child("images/default.png")
        }
        imageRef.downloadUrl.addOnSuccessListener { uri ->
            val imageUrl = uri.toString()
            onComplete(imageUrl)
        }.addOnFailureListener { exception ->
            exception.printStackTrace()
        }
    }

    fun login(email: String, password: String, onComplete: (RegistrationResult) -> Unit) {

        val auth = FirebaseAuth.getInstance()

// Oturum açma verilerini cihaza kaydet
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    onComplete(RegistrationResult(true, "", ""))

                } else {
                    Log.e("firebase", "signInWithEmail:failure", task.exception)
                    onComplete(
                        RegistrationResult(
                            false,
                            "password",
                            "Kullanıcı adı veya şifre hatalı"
                        )
                    )
                    // Giriş işlemi başarısız
                    // Hata mesajını elde edebilirsiniz: task.exception?.message
                }

            }
    }

    fun register(
        name: String,
        surname: String,
        userName: String,
        email: String,
        password: String,
        isGezdiren: Boolean,
        onComplete: (RegistrationResult) -> Unit
    ) {
        val user = Users(
            "",
            name,
            surname,
            userName,
            email,
            password,
            isGezdiren,
        )

        val queryEmail = refUser.orderByChild("email").equalTo(email)
        val queryUsername = refUser.orderByChild("username").equalTo(userName)

        queryUsername.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    onComplete(RegistrationResult(false, "username", "Kullanıcı adı zaten var"))
                } else {
                    queryEmail.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                onComplete(
                                    RegistrationResult(
                                        false,
                                        "email",
                                        "E-posta zaten var"
                                    )
                                )
                            } else {
                                refUser.push().setValue(user)
                                    .addOnSuccessListener {
                                        val auth = FirebaseAuth.getInstance()

                                        auth.createUserWithEmailAndPassword(email, password)
                                            .addOnCompleteListener { task ->
                                                if (task.isSuccessful) {
                                                    // Kullanıcı kaydı başarılı
                                                    val user: FirebaseUser? = auth.currentUser
                                                    // Kullanıcı verilerini kullanarak işlemler yapabilirsiniz
                                                } else {
                                                    // Kayıt işlemi başarısız
                                                    // Hata mesajını elde edebilirsiniz: task.exception?.message
                                                }
                                            }

                                        onComplete(
                                            RegistrationResult(
                                                true,
                                                "",
                                                "Kayıt başarılı"
                                            )
                                        )
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.e("register", "register: ${exception.message}")
                                    }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.e("firebase", "Veri alınamadı", error.toException())
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("firebase", "Veri alınamadı", error.toException())
            }
        })
    }

    fun updateProfile(
        id: String,
        name: String,
        surname: String,
        userName: String,
        email: String,
        imagePart: Uri?,
        onComplete: (RegistrationResult) -> Unit
    ) {

        val queryEmail = refUser.orderByChild("email").equalTo(email)
        val queryUsername = refUser.orderByChild("username").equalTo(userName)

        queryUsername.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists() && UserManager.currentUserUsername != userName) {
                    onComplete(RegistrationResult(false, "username", "Kullanıcı adı zaten var"))
                } else {
                    queryEmail.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists() && UserManager.currentUserEmail != email) {
                                onComplete(
                                    RegistrationResult(
                                        false,
                                        "email",
                                        "E-posta zaten var"
                                    )
                                )
                            } else {

                                if (imagePart != null && imagePart != Uri.EMPTY && imagePart != Uri.parse(
                                        ""
                                    )
                                ) {
                                    Log.e("adSave", "imagePart: $imagePart")
                                    val imageRef =
                                        storageRef.child("images/${System.currentTimeMillis()}.jpg")
                                    val uploadTask = imageRef.putFile(imagePart)

                                    uploadTask.addOnSuccessListener { taskSnapshot ->
                                        val imagePath = taskSnapshot.metadata?.path
                                        val user = Users(
                                            id = "",
                                            name = name,
                                            surname = surname,
                                            username = userName,
                                            email = email,
                                            password = UserManager.currentUserPassword,
                                            gezdiren = UserManager.currentUserGezdiren,
                                            profileImage = imagePath
                                        )

                                        refUser.child(id).setValue(user)
                                            .addOnSuccessListener {
                                                if (imagePath != null) {
                                                    UserManager.currentUserProfileImage = imagePath
                                                }
                                                onComplete(
                                                    RegistrationResult(
                                                        true,
                                                        "",
                                                        "Kayıt başarılı"
                                                    )
                                                )
                                            }
                                            .addOnFailureListener { exception ->
                                                Log.e("register", "register: ${exception.message}")
                                            }

                                    }.addOnFailureListener { exception ->
                                        Log.e("adSave", "exception: $exception")
                                        exception.printStackTrace()
                                    }
                                } else {
                                    val user = Users(
                                        id = "",
                                        name = name,
                                        surname = surname,
                                        username = userName,
                                        email = email,
                                        password = UserManager.currentUserPassword,
                                        gezdiren = UserManager.currentUserGezdiren,
                                        profileImage = UserManager.currentUserProfileImage
                                    )

                                    refUser.child(id).setValue(user)
                                        .addOnSuccessListener {
                                            onComplete(
                                                RegistrationResult(
                                                    true,
                                                    "",
                                                    "Kayıt başarılı"
                                                )
                                            )
                                        }
                                        .addOnFailureListener { exception ->
                                            Log.e("register", "register: ${exception.message}")
                                        }
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.e("firebase", "Veri alınamadı", error.toException())
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("firebase", "Veri alınamadı", error.toException())
            }
        })
    }

    fun adSave(
        username: String,
        title: String,
        content: String,
        time: String,
        price: String,
        imagePart: Uri
    ): Boolean {
        val imageRef = storageRef.child("images/${System.currentTimeMillis()}.jpg")
        val uploadTask = imageRef.putFile(imagePart)

        uploadTask.addOnSuccessListener { taskSnapshot ->
            val imagePath = taskSnapshot.metadata?.path
            val advert = Advert(
                "",
                username,
                title,
                content,
                time,
                price,
                imagePath
            )
            refAdvert.push().setValue(advert)
                .addOnSuccessListener {
                    Log.e("register", "register: Success")
                }
                .addOnFailureListener { exception ->
                    Log.e("register", "register: ${exception.message}")
                }
        }.addOnFailureListener { exception ->
            Log.e("adSave", "exception: $exception")
            exception.printStackTrace()
        }
        return true
    }

    fun adUpdate(
        advertID: String,
        username: String,
        title: String,
        content: String,
        time: String,
        price: String,
        imagePart: Uri
    ): Boolean {
        val imageRef = storageRef.child("images/${System.currentTimeMillis()}.jpg")
        val uploadTask = imageRef.putFile(imagePart)

        uploadTask.addOnSuccessListener { taskSnapshot ->
            val imagePath = taskSnapshot.metadata?.path
            val advert = Advert(
                "",
                username,
                title,
                content,
                time,
                price,
                imagePath
            )
            refAdvert.child(advertID).setValue(advert)
                .addOnSuccessListener {
                    Log.e("register", "register: Success")
                }
                .addOnFailureListener { exception ->
                    Log.e("register", "register: ${exception.message}")
                }
        }.addOnFailureListener { exception ->
            Log.e("adSave", "exception: $exception")
            exception.printStackTrace()
        }
        return true
    }

    fun adRemove(advertID: String): Boolean {
        refAdvert.child(advertID).removeValue()
        return true
    }

    fun createConversation(
        senderID: String,
        receiverUsername: String,
        onComplete: (Pair<Conversation, String>) -> Unit
    ) {
        val queryUsername = refUser.orderByChild("username").equalTo(receiverUsername)

        queryUsername.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                for (ds in snapshot.children) {
                    val receiverID = ds.key.toString()
                    val receiverUser = ds.getValue(Users::class.java)
                    val conversation = Conversation("", senderID, receiverID, "")

                    val query = conversationRef.whereEqualTo("userOne", senderID)
                        .whereEqualTo("userTwo", receiverID)
                        .limit(1)

                    query.get().addOnSuccessListener { documents ->
                        if (documents.isEmpty) {
                            val queryReverse = conversationRef.whereEqualTo("userOne", receiverID)
                                .whereEqualTo("userTwo", senderID)
                                .limit(1)

                            queryReverse.get().addOnSuccessListener { reverseDocuments ->
                                if (reverseDocuments.isEmpty) {
                                    conversationRef.add(conversation)
                                        .addOnSuccessListener { documentRef ->
                                            if (receiverUser != null) {
                                                getImageUrl(receiverUser.profileImage){
                                                    onComplete(
                                                        Pair<Conversation, String>(
                                                            Conversation(
                                                                documentRef.id,
                                                                senderID,
                                                                receiverID,
                                                                ""
                                                            ), it
                                                        )
                                                    )
                                                }
                                            }
                                        }.addOnFailureListener { exception ->
                                            Log.e("createConversation", "Hata: $exception")
                                            exception.printStackTrace()
                                        }
                                } else {
                                    if (receiverUser != null) {
                                        getImageUrl(receiverUser.profileImage){
                                            onComplete(
                                                Pair<Conversation, String>(
                                                    Conversation(
                                                        reverseDocuments.documents[0].id,
                                                        senderID,
                                                        receiverID,
                                                        ""
                                                    ), it
                                                )
                                            )
                                        }
                                    }
                                }
                            }.addOnFailureListener { exception ->
                                Log.e("createConversation", "Hata: $exception")
                                exception.printStackTrace()
                            }
                        } else {
                            if (receiverUser != null) {
                                getImageUrl(receiverUser.profileImage){
                                    onComplete(
                                        Pair<Conversation, String>(
                                            Conversation(
                                                documents.documents[0].id,
                                                senderID,
                                                receiverID,
                                                ""
                                            ), it
                                        )
                                    )
                                }
                            }
                        }
                    }.addOnFailureListener { exception ->
                        Log.e("createConversation", "Hata: $exception")
                        exception.printStackTrace()
                    }
                }
            }
        }.addOnFailureListener { exception ->
            Log.e("firebase", "Veri alınamadı", exception)
        }
    }

    fun getMessageList(conversationID: String, onComplete: (List<Message>) -> Unit) {
        val query = messagesRef.whereEqualTo("conversationID", conversationID)
            .orderBy("timestamp")
        val notificationCollection = FirebaseFirestore.getInstance().collection("notification")
        val queryNoti = notificationCollection.whereEqualTo("conversationID", conversationID)
            .whereEqualTo("receiverID", UserManager.currentUserId)


        query.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.e("getMessageList", "Hata: $exception")
                exception.printStackTrace()
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val messageList = mutableListOf<Message>()
                for (ds in snapshot.documents) {
                    val message = ds.toObject(Message::class.java)
                    if (message != null) {
                        messageList.add(message)
                    }
                }
                queryNoti.get().addOnSuccessListener { documents ->
                    if (documents != null) {
                        for (ds in documents.documents) {
                            val notification = ds.toObject(Notification::class.java)
                            notificationCollection.document(ds.id).delete()
                        }
                    }
                }
                onComplete(messageList)
            }
        }
    }

    fun sendMessage(message: Message) {
        Log.e("sendMessage", "message: $message")
        val firestore = FirebaseFirestore.getInstance()
        val messagesCollection = firestore.collection("messages")
        val notificationCollection = firestore.collection("notification")
        val conversationCollection = firestore.collection("conversation")
        val query = conversationCollection.document(message.conversationID.toString())

        messagesCollection.add(message).addOnSuccessListener {
            message.receiverID?.let { it1 ->
                message.message?.let { it2 ->
                    sendFirebaseNotification(
                        it1, UserManager.currentUserUsername,
                        it2
                    )
                    notificationCollection.add(
                        Notification(
                            UserManager.currentUserUsername,
                            message.message,
                            message.conversationID,
                            UserManager.currentUserId,
                            message.receiverID
                        )
                    )
                    query.update("lastMessage", message.message)
                }?.addOnFailureListener { exception ->
                    Log.e("sendMessage", "Hata: $exception")
                    exception.printStackTrace()
                }

            }
        }.addOnFailureListener { exception ->
            Log.e("sendMessage", "Hata: $exception")
            exception.printStackTrace()
        }
    }

    fun getNotificationList(onComplete: (List<Notification>) -> Unit) {
        val notificationCollection = FirebaseFirestore.getInstance().collection("notification")
        val query = notificationCollection.whereEqualTo("receiverID", UserManager.currentUserId)

        query.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.e("getNotificationList", "Hata: $exception")
                exception.printStackTrace()
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val notificationList = mutableListOf<Notification>()
                for (ds in snapshot.documents) {
                    val notification = ds.toObject(Notification::class.java)
                    if (notification != null) {
                        notificationList.add(notification)
                    }
                }
                onComplete(notificationList)
            }
        }

    }

    private fun sendFirebaseNotification(userID: String, username: String, messageText: String) {

        val query = fcmTokensRef.whereEqualTo("userID", userID)

        query.get().addOnSuccessListener { snapshot ->
            if (snapshot != null ) {
                for (ds in snapshot.documents) {
                    val token = ds.toObject(Token::class.java)
                    if (token != null) {
                        val retrofit = Retrofit.Builder()
                            .baseUrl("https://fcm.googleapis.com")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                        val fcmApiService = retrofit.create(FcmApiService::class.java)

                        val notification = FcmNotification(
                            to = token.token.toString(),
                            notification = FcmNotificationData(
                                title = username,
                                body = messageText
                            )
                        )

                        val call = fcmApiService.sendNotification(notification = notification)
                        call.enqueue(object : Callback<FcmResponse> {
                            override fun onResponse(
                                call: Call<FcmResponse>,
                                response: Response<FcmResponse>
                            ) {
                                if (response.isSuccessful) {
                                    val fcmResponse = response.body()
                                    Log.e(
                                        "sendFirebaseNotification",
                                        "fcmResponse: $fcmResponse"
                                    )
                                    // Bildirim başarıyla gönderildi
                                } else {
                                    Log.e("sendFirebaseNotification", "response: $response")
                                    // Bildirim gönderme hatası
                                }
                            }

                            override fun onFailure(call: Call<FcmResponse>, t: Throwable) {
                                Log.e("sendFirebaseNotification", "Hata: $t")
                                // İstek başarısız oldu
                            }
                        })

                    }
                }
            }
        }.addOnFailureListener { exception ->
            Log.e("sendFirebaseNotification", "Hata: $exception")
            exception.printStackTrace()
        }
    }

    fun isThereANotification(onComplete: (Boolean) -> Unit) {
        val notificationCollection = FirebaseFirestore.getInstance().collection("notification")
        val query = notificationCollection.whereEqualTo("receiverID", UserManager.currentUserId)

        query.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.e("getNotificationList", "Hata: $exception")
                exception.printStackTrace()
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.size() > 0 && snapshot.documents.size > 0 && snapshot.documents[0].exists()) {
                onComplete(true)
            } else {
                onComplete(false)
            }
        }

    }
}