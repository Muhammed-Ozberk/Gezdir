package com.example.gezdir.di

import com.example.gezdir.data.repo.DataRepository
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideDataRepository(
        @Named("users")
        refUser: DatabaseReference,
        @Named("advert")
        refAdvert: DatabaseReference,
        storageRef: StorageReference,
        @Named("conversation")
        conversationRef: CollectionReference,
        @Named("messages")
        messagesRef: CollectionReference,
        @Named("fcmTokens")
        fcmTokensRef: CollectionReference
    ): DataRepository {
        return DataRepository(
            refUser,
            refAdvert,
            storageRef,
            conversationRef,
            messagesRef,
            fcmTokensRef
        )
    }

    @Provides
    @Singleton
    @Named("users")
    fun provideFirebaseDatabaseReferenceUser(): DatabaseReference {
         return FirebaseDatabase.getInstance().getReference("users")
    }

    @Provides
    @Singleton
    @Named("advert")
    fun provideFirebaseDatabaseReferenceAdvert(): DatabaseReference {
         return FirebaseDatabase.getInstance().getReference("advert")
    }

    @Provides
    @Singleton
    fun provideFirebaseStorageReference(): StorageReference {
         return FirebaseStorage.getInstance().reference
    }

    @Provides
    @Singleton
    @Named("conversation")
    fun provideFirebaseFirestoreReferenceConversation(): CollectionReference {
         return FirebaseFirestore.getInstance().collection("conversation")
    }

    @Provides
    @Singleton
    @Named("messages")
    fun provideFirebaseFirestoreReferenceMessages(): CollectionReference {
         return FirebaseFirestore.getInstance().collection("messages")
    }

    @Provides
    @Singleton
    @Named("fcmTokens")
    fun provideFirebaseFirestoreReferenceFcmTokens(): CollectionReference {
         return FirebaseFirestore.getInstance().collection("fcmTokens")
    }

}