package com.example.cupidswap.Screens.auth

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

import com.example.cupidswap.Screens.Dashboard.Dashboard
import com.example.cupidswap.databinding.ActivityRegistrationBinding
import com.example.cupidswap.model.UserModel
import com.example.cupidswap.utils.Utils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage

class Registration : AppCompatActivity() {

    private lateinit var  binding: ActivityRegistrationBinding
    private  var imageUri: Uri? = null

    private var selectImage = registerForActivityResult(ActivityResultContracts.GetContent()){ fecthcUri ->
        imageUri = fecthcUri
        binding.edtRegUserImage.setImageURI(imageUri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.edtRegUserImage.setOnClickListener {
            selectImage.launch("image/*")
        }

        binding.btnRegUser.setOnClickListener {
            Utils.dismissKeyboard(this@Registration)
            validateData()
        }


    }

    private fun validateData() {
        if(binding.edtRegUserName.text.toString().isEmpty() || binding.edtRegUserEmail.text.toString().isEmpty() ||
            binding.edtRegUserCity.text.toString().isEmpty() || imageUri == null)
        {
            Toast.makeText(applicationContext, "Please fill up the credential", Toast.LENGTH_SHORT).show()
        }else if(binding.edtRegCheckbox.isChecked == false)
        {
            Toast.makeText(applicationContext, "Please accept the terms and condition", Toast.LENGTH_SHORT).show()
        }
        else{
            uploadImage()
        }

    }

    private fun uploadImage() {
        Utils.showDialog(this)

        val storageRef = FirebaseStorage
            .getInstance().getReference("profile")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("profile.jpg")
        
        storageRef.putFile(imageUri!!)
            .addOnSuccessListener {
                
                storageRef.downloadUrl
                    .addOnSuccessListener {downloadUrl ->
                        storeAllData(downloadUrl)
                    }
                    .addOnFailureListener {
                        Utils.dismissDialog(this@Registration)
                        Toast.makeText(applicationContext, "Failed to get Url", Toast.LENGTH_SHORT).show()
                    }
                
            }
            .addOnFailureListener{
                Utils.dismissDialog(this@Registration)
                Toast.makeText(applicationContext, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeAllData(imageUri: Uri?) {


        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            // Get new FCM registration token
             val token = task.result

            val data = UserModel(
                name = binding.edtRegUserName.text.toString(),
                image = imageUri.toString(),
                email =   binding.edtRegUserEmail.text.toString(),
                city =   binding.edtRegUserCity.text.toString(),
                number = FirebaseAuth.getInstance().currentUser!!.phoneNumber,
                fcmToken = token
            )

            FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!) // based on the phone number
                .setValue(data)
                .addOnCompleteListener {task ->
                    Utils.dismissDialog(this@Registration)
                    if(task.isSuccessful){
                        startActivity(Intent(this@Registration, Dashboard::class.java))
                        Toast.makeText(applicationContext, "Register Successful", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(applicationContext, "Failed to save the data", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Utils.dismissDialog(this@Registration)
                    Toast.makeText(applicationContext, "Failed to save the data", Toast.LENGTH_SHORT).show()
                }
        })



    }
}