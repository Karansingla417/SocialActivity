package com.example.sociallyactive.daos

import com.example.sociallyactive.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.w3c.dom.Document

class Userdao {
    private val db=FirebaseFirestore.getInstance()
    private val usercollection=db.collection("users")

    fun adduser(user:User?)
    {
        user?.let{
            GlobalScope.launch (Dispatchers.IO){
                usercollection.document(user.uid).set(it)
            }
        }
    }

    fun getuserid(uid:String):Task<DocumentSnapshot>
    {
        return usercollection.document(uid).get()
    }


}