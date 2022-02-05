package com.example.sociallyactive

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sociallyactive.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PostAdapter(options: FirestoreRecyclerOptions<Post>,val listener:Ipostadapter) : FirestoreRecyclerAdapter<Post, PostAdapter.postViewholder>(
    options
) {

    class postViewholder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val postText: TextView = itemView.findViewById(R.id.postTitle)
        val userText: TextView = itemView.findViewById(R.id.userName)
        val createdAt: TextView = itemView.findViewById(R.id.createdAt)
        val likeCount: TextView = itemView.findViewById(R.id.likeCount)
        val userImage: ImageView = itemView.findViewById(R.id.userImage)
        val likeButton: ImageView = itemView.findViewById(R.id.likeButton)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): postViewholder {
        val viewholder= postViewholder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        )
        viewholder.likeButton.setOnClickListener{
            listener.onLIkeClicekd(snapshots.getSnapshot(viewholder.adapterPosition).id)
        }
        return viewholder
    }

    override fun onBindViewHolder(holder: postViewholder, position: Int, model: Post) {
        holder.postText.text = model.text
        holder.userText.text = model.createdby.displayname
        Glide.with(holder.userImage.context).load(model.createdby.imageurl).circleCrop()
            .into(holder.userImage)
        holder.likeCount.text = model.likedBy.size.toString()

        holder.createdAt.text = Utils.getTimeAgo(model.createdAt)

        val auth = Firebase.auth
        val currentUserId = auth.currentUser!!.uid
        val isLiked = model.likedBy.contains(currentUserId)
        if(isLiked) {
            holder.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.likeButton.context, R.drawable.ic_liked))
        } else {
            holder.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.likeButton.context, R.drawable.ic_unliked))
        }


    }
}


interface Ipostadapter{
    fun onLIkeClicekd(postId:String)
}