package com.example.sociallyactive.daos

import com.example.sociallyactive.models.Post
import com.example.sociallyactive.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PostDao {

    val db= FirebaseFirestore.getInstance()
    val postcollections=db.collection("posts")
    val auth=Firebase.auth

    fun addPost(text:String)
    {
         val currentUserId =  auth.currentUser!!.uid
        GlobalScope.launch {
            val userdao = Userdao()
            val user = userdao.getuserid(currentUserId).await().toObject(User::class.java)!!

            val currenttime = System.currentTimeMillis()
            val post= Post(text,user,currenttime)

            postcollections.document().set(post)
        }

    }

    fun getPostById(postId: String): Task<DocumentSnapshot> {
        return postcollections.document(postId).get()
    }

    fun updatelikes(postId:String)
    {
        GlobalScope.launch {
            val currentUserId = auth.currentUser!!.uid
            val post = getPostById(postId).await().toObject(Post::class.java)!!
            val isLiked = post.likedBy.contains(currentUserId)

            if(isLiked) {
                post.likedBy.remove(currentUserId)
            } else {
                post.likedBy.add(currentUserId)
            }
            postcollections.document(postId).set(post)
        }

    }


    }

