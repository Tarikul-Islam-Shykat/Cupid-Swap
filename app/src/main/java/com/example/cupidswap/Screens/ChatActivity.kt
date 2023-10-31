package com.example.cupidswap.Screens


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isEmpty
import com.bumptech.glide.disklrucache.DiskLruCache.Value
import com.example.cupidswap.R
import com.example.cupidswap.adapter.ChatAdapter
import com.example.cupidswap.databinding.ActivityChatBinding
import com.example.cupidswap.model.ChatModel
import com.example.cupidswap.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatActivity : AppCompatActivity() {


    private lateinit var  binding: ActivityChatBinding
    private  var senderId : String? = null
    private  var chatId : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //getData(intent.getStringExtra("chat_id")) // from messageUserAdapter

        verifyChatId()

        binding.btnSendMessage.setOnClickListener {
            if (binding.edtTypeMessage.text!!.isEmpty()){
                Toast.makeText(applicationContext, "Please Type Your message...", Toast.LENGTH_SHORT).show()
            }
            else{
                storeData(binding.edtTypeMessage.text.toString())
            }
        }

    }

    private fun verifyChatId() {

        val receiverId = intent.getStringExtra("user_id_from_msgAdapter") // from messageUserAdapter
        senderId = FirebaseAuth.getInstance().currentUser!!.phoneNumber
        chatId = senderId  +  receiverId // for unique id
        val reverserChatId_ = receiverId + senderId

        Log.d("ChatActivity", "receiverId -> "+receiverId.toString())
        Log.d("ChatActivity", "senderId -> "+senderId.toString())
        Log.d("ChatActivity", "chatId -> "+chatId.toString())
        Log.d("ChatActivity", "reverserChatId_ -> "+reverserChatId_.toString())

       val reference = FirebaseDatabase.getInstance().getReference("chats")
        reference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.hasChild(chatId!!)){
                    Log.d("ChatActivity", "snapshot -> has child")
                    getData(chatId)
                }
                else if (snapshot.hasChild(reverserChatId_!!))
                {
                    chatId = reverserChatId_
                    Log.d("ChatActivity", "snapshot -> has reverse child")
                    getData(chatId)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("ChatActivity","verifyChatId error ")
            }
        })
    }

    private fun getData(chatId: String?) {
        Log.d("ChatActivity", "getData chatId -> "+chatId.toString())

         FirebaseDatabase
             .getInstance()
             .getReference("chats")
             .child(chatId!!)
             .addListenerForSingleValueEvent(object : ValueEventListener{
                 override fun onDataChange(snapshot: DataSnapshot) {
                     val list = arrayListOf<ChatModel>()
                     for(show in snapshot.children){
                         showInLog(show.toString())
                         list.add(show.getValue(ChatModel::class.java)!!)
                     }
                     binding.recViewChatMessage.adapter = ChatAdapter(this@ChatActivity, list)
                 }

                 override fun onCancelled(error: DatabaseError) {
                     Toast.makeText(applicationContext, "Error in chat", Toast.LENGTH_SHORT).show()
                 }

             })

    }

    private fun storeData(msg: String?) {

        val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        val currentTime: String = SimpleDateFormat("HH:mm a", Locale.getDefault()).format(Date())

        val map = hashMapOf<String, String>()
        map["message"] = msg!!
        map["senderId"] = senderId!!
        map["currentTime"] = currentTime
        map["currentDate"] = currentDate

        showInLog(" message - > "+msg.toString())
        showInLog(senderId.toString())
        showInLog(currentTime.toString())
        showInLog(currentDate.toString())
        showInLog("chatId -> " + chatId.toString())

        val reference = FirebaseDatabase.getInstance().getReference("chats").child(chatId!!)

        reference
            .child(reference.push().key!!)
            .setValue(map)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    binding.edtTypeMessage.text = null // after sending the message make it clear
                    getData(chatId)
                }
                else
                {
                    Toast.makeText(applicationContext, "Please check the internet connection", Toast.LENGTH_SHORT).show() }
            }
    }

    fun showInLog(msg : String) = Log.d("ChatActivity", msg)


}