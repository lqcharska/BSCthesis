package com.example.bscthesis.util

import com.example.bscthesis.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentReference
import java.lang.NullPointerException

object FirestoreUtil {
    private val firestoreInstance: FirebaseFirestore by lazy {FirebaseFirestore.getInstance()}

    private val currentUserDocRef: DocumentReference
        get() = firestoreInstance.document("users/${FirebaseAuth.getInstance().uid
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

    fun nameYourDoggie(name : String = ""){
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

}