package com.example.cupidswap.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cupidswap.Screens.ChatActivity
import com.example.cupidswap.databinding.SampleMessageListLayoutBinding

import com.example.cupidswap.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MessageUserAdapter (val context: Context, val list : ArrayList<String>, val chatKey : List<String>)
    : RecyclerView.Adapter<MessageUserAdapter.MessageUserViewHolder>() {

    inner  class MessageUserViewHolder(val binding: SampleMessageListLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageUserViewHolder {
        return MessageUserViewHolder(SampleMessageListLayoutBinding.inflate(LayoutInflater.from(context), parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MessageUserViewHolder, position: Int) {

        FirebaseDatabase.getInstance().getReference("users")
            .child(list[position]).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("MessageUserAdapter", "before ->"+snapshot.toString())
                    if(snapshot.exists()){
                        Log.d("MessageUserAdapter", "exists ->"+snapshot.toString())
                        val data = snapshot.getValue(UserModel::class.java)
                        Glide.with(context).load(data!!.image).into(holder.binding.imgMessageListUserImage)
                        holder.binding.txtMessageListUserName.text = data!!.name
                    }
                    else{
                        Log.d("MessageUserAdapter", "Snapshot not exists")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("MessageUserAdapter", "MessageUserAdapter onCancelled")
                }
            })

        holder.itemView.setOnClickListener {
            Log.d("MessageUserAdapter", "chat_id -> "+chatKey[position].toString() + " user_id -> " +list[position])
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("chat_id", chatKey[position])
            intent.putExtra("user_id_from_msgAdapter", list[position]) // current user
            context.startActivity(intent)
        }



    }
}