package com.example.gezdir.data.repo

import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.storage.StorageReference
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.Mockito.`when`

class DataRepositoryTest {

    private val usersReference = mock(DatabaseReference::class.java)
    private val advertsReference = mock(DatabaseReference::class.java)
    private val advertReference = mock(DatabaseReference::class.java)
    private val storageReference = mock(StorageReference::class.java)
    private val conversationsReference = mock(CollectionReference::class.java)
    private val messagesReference = mock(CollectionReference::class.java)

    private lateinit var repository: DataRepository

    @Before
    fun setUp() {
        repository = DataRepository(
            usersReference,
            advertsReference,
            storageReference,
            conversationsReference,
            messagesReference
        )
    }

    @Test
    fun `adRemove delegates deletion to the requested Firebase child`() {
        `when`(advertsReference.child("advert-42")).thenReturn(advertReference)

        assertTrue(repository.adRemove("advert-42"))

        verify(advertsReference).child("advert-42")
        verify(advertReference).removeValue()
        verifyNoInteractions(usersReference, storageReference, conversationsReference, messagesReference)
    }
}
