package com.example.cupidswap.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cupidswap.R
import com.example.cupidswap.model.ChatModel
import com.example.cupidswap.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatAdapter(val context : Context, val list: List<ChatModel>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>(){

    val MSG_TYPE_RIGHT = 0
    val MSG_TYPE_LEFT = 1

    inner class ChatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val text_ = itemView.findViewById<TextView>(R.id.messageTxt)
        val image_ = itemView.findViewById<ImageView>(R.id.senderImage)
        // same name in user and send message lay out
    }

    override fun getItemViewType(position: Int): Int { // this is for which message to show
        return if (list[position].senderId == FirebaseAuth.getInstance().currentUser!!.phoneNumber){
            MSG_TYPE_RIGHT
        } else MSG_TYPE_LEFT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return if(viewType == MSG_TYPE_RIGHT){ ChatViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_reciver_message, parent, false)) }
        else { ChatViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_sender_message, parent, false)) }
    }

    override fun getItemCount(): Int { return list.size }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.text_.text = list[position].message
        FirebaseDatabase.getInstance().getReference("users")
            .child(list[position].senderId!!).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val data = snapshot.getValue(UserModel::class.java)
                        Glide.with(context)
                            .load(data!!.image)
                            .placeholder(R.mipmap.ic_launcher)
                            .into(holder.image_)
                    }
                    else{
                        Log.d("CupidSwap", "Chat Adapter snapshot dont exist")
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
                }

            })

    }
}