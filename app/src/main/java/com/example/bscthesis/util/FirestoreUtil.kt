package com.example.bscthesis.util

import android.content.Context
import android.util.Log
import com.example.bscthesis.databinding.DogsItemBinding
import com.example.bscthesis.model.User
import com.example.bscthesis.recycleview.item.DogItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.viewbinding.BindableItem
import java.lang.NullPointerException

object FirestoreUtil {
    private val firestoreInstance: FirebaseFirestore by lazy {FirebaseFirestore.getInstance()}

    private val currentUserDocRef: DocumentReference
        get() = firestoreInstance.document("users/${FirebaseAuth.getInstance().currentUser?.uid
            ?: throw NullPointerException("UID is null")}")

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

    fun addUsersListener(context: Context, onListen: (List<BindableItem<DogsItemBinding>>) -> Unit): ListenerRegistration{
        Log.d("DUPA", "jestem w addUsersListener")
        return firestoreInstance.collection("users")
            .addSnapshotListener{querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null){
                    Log.d("FIRESTORE", "Users listener erros", firebaseFirestoreException)
                    return@addSnapshotListener
                }
                val items = mutableListOf<BindableItem<DogsItemBinding>>()
                querySnapshot?.documents?.forEach{
                    if (it.id != FirebaseAuth.getInstance().currentUser?.uid){
                        Log.d("WICIU", it.toString())
                        it.toObject(User::class.java)?.let { it1 -> DogItem(it1, it.id, context) }?.let { it2 ->
                            items.add(
                                it2
                            )
                            Log.d("ITEM2", it2.toString())
                        }
                    }

                }
                Log.d("ITEMS", items.toString())
                onListen(items)
            }
    }

    fun removeListener(registration: ListenerRegistration) = registration.remove()

}