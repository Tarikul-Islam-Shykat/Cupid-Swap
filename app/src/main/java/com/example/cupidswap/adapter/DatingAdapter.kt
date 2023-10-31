package com.example.cupidswap.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.NotificationCompat.MessagingStyle.Message
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cupidswap.Screens.ChatActivity
import com.example.cupidswap.databinding.SampleItemUserLayoutBinding
import com.example.cupidswap.model.UserModel
import kotlin.math.log

class DatingAdapter (val context: Context, val list: ArrayList<UserModel>)
    : RecyclerView.Adapter<DatingAdapter.DatingViewHolder>() {

    inner class DatingViewHolder(val binding: SampleItemUserLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatingViewHolder {
        return  DatingViewHolder(SampleItemUserLayoutBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: DatingViewHolder, position: Int) {


        holder.binding.txtDatingCardUserName.text = list[position].name
        holder.binding.txtDatingCardUserEmail.text = list[position].email

        Glide.with(context).load(list[position].image).into(holder.binding.imgUserImageDating)

        holder.binding.imgDatingCardChat.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("user_id_from_msgAdapter", list[position].number)
            Log.d("DatingAdapter", "userId : "+list[position].number.toString())
            context.startActivity(intent)
        }

    }
}