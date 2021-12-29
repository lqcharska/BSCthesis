package com.example.bscthesis.util

import android.content.Context
import android.util.Log
import com.example.bscthesis.CurrentUserConstants
import com.example.bscthesis.R
import com.example.bscthesis.databinding.DogsItemBinding
import com.example.bscthesis.databinding.ImageMessageItemBinding
import com.example.bscthesis.databinding.TextMessageItemBinding
import com.example.bscthesis.model.*
import com.example.bscthesis.recycleview.item.DogItem
import com.example.bscthesis.recycleview.item.ImageMessageItem
import com.example.bscthesis.recycleview.item.TextMessageItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.viewbinding.BindableItem
import java.lang.NullPointerException

object  FirestoreUtil {
    private val firestoreInstance: FirebaseFirestore by lazy {FirebaseFirestore.getInstance()}

    private val currentUserDocRef: DocumentReference
        get() = firestoreInstance.document("users/${FirebaseAuth.getInstance().currentUser?.uid
            ?: throw NullPointerException("UID is null")}")

    private val chatChannelCollectionRef = firestoreInstance.collection("chatChannels")

    fun initCurrentUserIfItsFirstTime(onComplete: () -> Unit){
        currentUserDocRef.get().addOnSuccessListener { documentSnapshot ->
            if(!documentSnapshot.exists()){
                val newUser = User(FirebaseAuth.getInstance().currentUser?.displayName ?: "",
                    "","","","","","","",null)
                currentUserDocRef.set(newUser).addOnSuccessListener { onComplete() }
            }
            else onComplete()
        }
    }

    fun nameYourDoggie(name: String = ""){
        val userFieldMap = mutableMapOf<String, Any>()
        if(name.isNotBlank()) userFieldMap["name"] = name
        currentUserDocRef.update(userFieldMap)
    }

    fun describeDoggie(bread: String = "", sex: String = "", age: String = "", size: String = "",
                       neutered: String = "", notLike: String = "", beHereFor: String = "",
                       profilePicturePath: String? = null){

        val userFieldMap = mutableMapOf<String, Any>()
        if (bread.isNotBlank()) userFieldMap["bread"] = bread
        if (sex.isNotBlank()) userFieldMap["sex"] = sex
        if (age.isNotBlank()) userFieldMap["age"] = age
        if (size.isNotBlank()) userFieldMap["size"] = size
        if (neutered.isNotBlank()) userFieldMap["neutered"] = neutered
        if (notLike.isNotBlank()) userFieldMap["notLike"] = notLike
        if (beHereFor.isNotBlank()) userFieldMap["beHereFor"] = beHereFor
        if (profilePicturePath != null) userFieldMap["profilePicturePath"] = profilePicturePath
        currentUserDocRef.update(userFieldMap)
    }

    fun getCurrentUser(onComplete: (User) -> Unit){
        currentUserDocRef.get().addOnSuccessListener { it.toObject(User::class.java)?.let { it1 ->
            onComplete(
                it1
            )
        } }
    }

    fun getCurrentUserContacts(context: Context, onListen: (List<BindableItem<DogsItemBinding>>) -> Unit): ListenerRegistration{
        val listOfContacts = mutableListOf<String>()
        firestoreInstance.collection("users").document(FirebaseAuth.getInstance().currentUser?.uid.toString()).collection("engagedChatChannels")
            .addSnapshotListener { value, error ->
                value?.documents?.forEach {
                    listOfContacts.add(it.id)
                }
            }
            return firestoreInstance.collection("users").addSnapshotListener { value, error ->
                val items = mutableListOf<BindableItem<DogsItemBinding>>()
                value?.documents?.forEach {
                    if (it.id in listOfContacts) {
                        it.toObject(User::class.java)?.let { it1 -> DogItem(it1, it.id, context) }?.let { it2 ->
                                items.add(it2)
                            }
                    }
                }
                onListen(items)
            }

    }

    private fun doTheDoggiesLikeEachOther(otherDoggie: User): Boolean{
        var areDoggieInLove = false
        if (CurrentUserConstants.USER_SEX == "Female") {
            if (otherDoggie.sex == "Female" && (otherDoggie.notLike == "None" || otherDoggie.notLike == "Male")) {
                areDoggieInLove =
                    !(CurrentUserConstants.USER_NOT_LIKE == "Female" || CurrentUserConstants.USER_NOT_LIKE == "Both")
            }
            if (otherDoggie.sex == "Male" && (otherDoggie.notLike == "None" || otherDoggie.notLike == "Male")) {
                areDoggieInLove =
                    !(CurrentUserConstants.USER_NOT_LIKE == "Male" || CurrentUserConstants.USER_NOT_LIKE == "Both")
            }
            if (CurrentUserConstants.USER_NOT_LIKE == "Both" && otherDoggie.beHereFor == "Bring help" && (otherDoggie.notLike == "None" || otherDoggie.notLike == "Male")) {
                areDoggieInLove = true
            }
        }

        if (CurrentUserConstants.USER_SEX == "Male") {
            if (otherDoggie.sex == "Female" && (otherDoggie.notLike == "None" || otherDoggie.notLike == "Female")) {
                areDoggieInLove =
                    !(CurrentUserConstants.USER_NOT_LIKE == "Female" || CurrentUserConstants.USER_NOT_LIKE == "Both")
            }
            if (otherDoggie.sex == "Male" && (otherDoggie.notLike == "None" || otherDoggie.notLike == "Female")) {
                areDoggieInLove =
                    !(CurrentUserConstants.USER_NOT_LIKE == "Male" || CurrentUserConstants.USER_NOT_LIKE == "Both")
            }
            if (CurrentUserConstants.USER_NOT_LIKE == "Both" && otherDoggie.beHereFor == "Bring help" && (otherDoggie.notLike == "None" || otherDoggie.notLike == "Female")) {
                areDoggieInLove = true
            }
        }

        return areDoggieInLove
    }

    fun addUsersListener(context: Context, onListen: (List<BindableItem<DogsItemBinding>>) -> Unit): ListenerRegistration{
        return firestoreInstance.collection("users")
            .addSnapshotListener{querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null){
                    Log.d("FIRESTORE", "Users listener erros", firebaseFirestoreException)
                    return@addSnapshotListener
                }
                val items = mutableListOf<BindableItem<DogsItemBinding>>()
                querySnapshot?.documents?.forEach{
                    if (it.id != FirebaseAuth.getInstance().currentUser?.uid){
                        it.toObject(User::class.java)?.let { it1 -> DogItem(it1, it.id, context) }?.let { it2 ->
                            if (doTheDoggiesLikeEachOther(it2.dog)) {
                                items.add(it2)
                            }
                        }
                    }

                }
                onListen(items)
            }
    }

    fun removeListener(registration: ListenerRegistration) = registration.remove()

    fun getOrCreateChatChannel(otherUserId: String, onComplete: (channelId: String) -> Unit){
        currentUserDocRef.collection("engagedChatChannels")
            .document(otherUserId).get().addOnSuccessListener {
                //if chat channel exists, so we have already talked with other user
                if(it.exists()){
                    onComplete(it["channelId"] as String)
                    return@addOnSuccessListener
                }

                val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

                val newChannel = chatChannelCollectionRef.document()
                newChannel.set(ChatChannel(mutableListOf(currentUserId, otherUserId)))

                currentUserDocRef.collection("engagedChatChannels")
                    .document(otherUserId)
                    .set(mapOf("channelId" to newChannel.id))

                firestoreInstance.collection("users").document(otherUserId)
                    .collection("engagedChatChannels")
                    .document(currentUserId)
                    .set(mapOf("channelId" to newChannel.id))

                onComplete(newChannel.id)
            }

    }

    fun addChatMessagesListener(channelId: String, context: Context,
                                onListen: (List<BindableItem<TextMessageItemBinding>>) -> Unit): ListenerRegistration{
        return chatChannelCollectionRef.document(channelId).collection("messages")
            .orderBy("time")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.e("FIRESTORE", "ChatMessagesListener error", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                val items = mutableListOf<BindableItem<TextMessageItemBinding>>()
                querySnapshot?.documents?.forEach {
                    if (it["type"] == MessageType.TEXT) {
                        items.add(TextMessageItem(it.toObject(TextMessage::class.java)!!, context))
                    }
                    onListen(items)
                }
            }
    }

//    fun addChatImageMessagesListener(channelId: String, context: Context,
//                                    onListen: (List<BindableItem<ImageMessageItemBinding>>) -> Unit): ListenerRegistration{
//        return chatChannelCollectionRef.document(channelId).collection("messages")
//            .orderBy("time")
//            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
//                if (firebaseFirestoreException != null) {
//                    Log.e("FIRESTORE", "ChatMessagesListener error", firebaseFirestoreException)
//                    return@addSnapshotListener
//                }
//
//                val items = mutableListOf<BindableItem<ImageMessageItemBinding>>()
//                querySnapshot?.documents?.forEach {
//                    if (it["type"] == MessageType.IMAGE) {
//                        items.add(ImageMessageItem(it.toObject(ImageMessage::class.java)!!, context))
//                    }
//                    onListen(items)
//                }
//            }
//    }

    fun sendMessage(message: Message, channelId: String){
        chatChannelCollectionRef.document(channelId)
            .collection("messages")
            .add(message)

    }

}