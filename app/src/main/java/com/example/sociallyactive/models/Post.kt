package com.example.sociallyactive.models

data class Post (
       val text:String="",
       val createdby :User =User(),
       val createdAt :Long=0L,
       val likedBy: ArrayList<String> = ArrayList()
        )