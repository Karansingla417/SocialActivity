package com.example.sociallyactive

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sociallyactive.daos.PostDao
import kotlinx.android.synthetic.main.activity_create_post.*

class CreatePostActivity : AppCompatActivity() {

    private lateinit var postdao:PostDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        postdao= PostDao()

        postbutton.setOnClickListener{
            val input = postinput.text.toString().trim()
            if(input.isNotEmpty())
            {
                postdao.addPost(input)
                finish()

            }
        }

    }
}