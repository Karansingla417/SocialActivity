package com.example.sociallyactive

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sociallyactive.daos.PostDao
import com.example.sociallyactive.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), Ipostadapter {

    private lateinit var adapter: PostAdapter
    private lateinit var postDao: PostDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener{
                val intent=Intent(this,CreatePostActivity::class.java)
            startActivity(intent)
        }

        setupRecyclerView()

    }

    private fun setupRecyclerView() {

        postDao = PostDao()
        val postsCollections = postDao.postcollections
        val query = postsCollections.orderBy("createdAt", Query.Direction.DESCENDING)
        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post::class.java).build()

        adapter= PostAdapter(recyclerViewOptions,this)

        recyclerview.adapter=adapter
        recyclerview.layoutManager= LinearLayoutManager(this)

    }
    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onLIkeClicekd(postId: String) {
        postDao.updatelikes(postId)
    }

}