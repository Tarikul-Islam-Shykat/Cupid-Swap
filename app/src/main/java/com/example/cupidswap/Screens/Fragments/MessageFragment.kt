package com.example.cupidswap.Screens.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cupidswap.adapter.MessageUserAdapter
import com.example.cupidswap.databinding.FragmentMessageBinding
import com.example.cupidswap.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception

class MessageFragment : Fragment() {

    private lateinit var binding: FragmentMessageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMessageBinding.inflate(layoutInflater)
        getData()
        return binding.root
    }

    private fun getData() {
        Utils.showDialog(requireContext())


        val currentId = FirebaseAuth.getInstance().currentUser!!.phoneNumber

        FirebaseDatabase.getInstance().getReference("chats")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    var sender_id = arrayListOf<String>() // list
                    var chat_Id = arrayListOf<String>() //  new list

                    for (data in snapshot.children){
                        Log.d("MessageFragment", data.toString())
                        if(data.key!!.contains(currentId!!)) { // if the chats contains my number then its my chat.

                            Log.d("MessageFragment", "MessageFragment sender id -> "+data.key!!.replace(currentId!!, ""))
                            Log.d("MessageFragment", "MessageFragment chat id  -> "+data.key!!)

                            sender_id.add(data.key!!.replace(currentId!!, "")) // so removed the id of my user, now i will have the number of sender id
                            chat_Id.add(data.key!!) // new list contains the chatID
                        }
                    }

                    try {
                        binding.recViewMessageList.adapter = MessageUserAdapter(requireContext(), sender_id, chat_Id)
                    }catch (e:Exception){
                        Utils.dismissDialog(requireContext())
                    }

                    Utils.dismissDialog(requireContext())
                }

                override fun onCancelled(error: DatabaseError) {
                    Utils.dismissDialog(requireContext())
                    Log.d("CupidSwap","MessageFragment get data canceled")
                }

            })
    }


}