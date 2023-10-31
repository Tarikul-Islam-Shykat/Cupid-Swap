package com.example.cupidswap.Screens.Fragments

//import android.R


import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.cupidswap.R
import com.example.cupidswap.Screens.auth.Login
import com.example.cupidswap.databinding.FragmentProfileBinding
import com.example.cupidswap.model.UserModel
import com.example.cupidswap.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class ProfileFragment : Fragment() {

    private lateinit var  binding : FragmentProfileBinding

    private lateinit var beforeUserName : String
    private lateinit var beforeUserEmail : String
    private lateinit var beforeUserCity : String
    private lateinit var beforeUserImage : String
    private lateinit var beforeUserAge : String
    private lateinit var beforeUserStatus : String

    private lateinit var dialog: AlertDialog

    var courses = arrayOf(
        "Male", "Female", "Agender",
        "Cisgender", "Androgynous", "Non-binary gender",
        "Gender expression", "Gender nonconforming",
        "Transgender", "Bigender", "Intersex",
        "Omnigender", "Androgyne", "F t m",
        "Ambigender", "Trans woman", "Two-spirit", "Cishet", "Demiboy", "Demiflux", "Demigirl",
    )


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)

        getsetData()





        binding.btnProfileLogOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireContext(), Login::class.java))
            requireActivity().finish()
        }

        binding.btnEditProfile.setOnClickListener {
            setEnable()
            binding.btnEditProfile.visibility = View.GONE
            binding.btnSaveProfile.visibility = View.VISIBLE
        }

        binding.btnSaveProfile.setOnClickListener {
            getEditedData_update()
            binding.btnEditProfile.visibility = View.VISIBLE
            binding.btnSaveProfile.visibility = View.GONE
            setDisable()
        }


        return binding.root
    }


    private fun getEditedData_update() {
        Utils.showDialog(requireContext())
        Thread.sleep(1500)
        var updatedUserName : String?
        var updatedUserEmail : String?
        var updatedUserCity : String?
        var updatedUserAge : String?
        var updatedUserStatus : String?
        var updatedUserImage : String?

        val reference = FirebaseDatabase
            .getInstance()
            .getReference("users")
            .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!)

        updatedUserName = binding.edtProfileUSerName.text.toString()
        updatedUserEmail = binding.edtProfileUserEmail.text.toString()
        updatedUserCity = binding.edtProfileUserCity.text.toString()

        updatedUserStatus = binding.edtProfileUserAge.text.toString()
        updatedUserAge  = binding.edtProfileUserstatus.text.toString()

        if(beforeUserName != updatedUserName ){
            reference.child("name").setValue(updatedUserName)
        }
        if(beforeUserEmail != updatedUserEmail ){
            reference.child("email").setValue(updatedUserEmail)
        }

        if(beforeUserCity != updatedUserCity ){
            reference.child("city").setValue(updatedUserCity)
        }
        if(beforeUserAge != updatedUserAge ){
            reference.child("age").setValue(updatedUserAge)
        }

        if(beforeUserStatus != updatedUserStatus ){
            reference.child("status").setValue(updatedUserStatus)
        }
        Utils.dismissDialog(requireContext())
    }

    private fun setEnable() {
        binding.profileUserImage.isEnabled = true
        binding.edtProfileUserEmail.isEnabled = true
        binding.edtProfileUserCity.isEnabled = true
        binding.edtProfileUSerName.isEnabled = true
        binding.edtProfileUserAge.isEnabled = true
        binding.edtProfileUserstatus.isEnabled = true
    }

    private fun setDisable() {
        binding.profileUserImage.isEnabled = false
        binding.edtProfileUserEmail.isEnabled = false
        binding.edtProfileUserCity.isEnabled = false
        binding.edtProfileUSerName.isEnabled = false
        binding.edtProfileUserAge.isEnabled = false
        binding.edtProfileUserstatus.isEnabled = false
    }

    private fun getsetData() {
        Utils.showDialog(requireContext())

        FirebaseDatabase.getInstance().getReference("users")
            .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!).get()
            .addOnSuccessListener { data ->
                if(data.exists()){
                    val loaded_data = data.getValue(UserModel::class.java)

                    binding.edtProfileUSerName.setText(loaded_data!!.name.toString())
                    binding.edtProfileUserCity.setText(loaded_data!!.city.toString())
                    binding.edtProfileUserEmail.setText(loaded_data!!.email.toString())
                    binding.edtProfileUserNumber.setText(FirebaseAuth.getInstance().currentUser!!.phoneNumber.toString())
                    binding.edtProfileUserAge.setText(loaded_data!!.age.toString())
                    binding.edtProfileUserAge.setText(loaded_data!!.age.toString())
                    binding.edtProfileUserstatus.setText(loaded_data!!.status.toString())

                    beforeUserName = loaded_data!!.name.toString()
                    beforeUserEmail = loaded_data!!.email.toString()
                    beforeUserCity = loaded_data!!.city.toString()
                    beforeUserImage = loaded_data!!.image.toString()
                    beforeUserAge = loaded_data!!.age.toString()
                    beforeUserStatus = loaded_data!!.status.toString()


                    Glide.with(requireContext())
                        .load(loaded_data.image)
                        .placeholder(R.drawable.profile)
                        .into(binding.profileUserImage)

                    Utils.dismissDialog(requireContext())

                }
                else{

                }
            }
            .addOnFailureListener {
                Utils.dismissDialog(requireContext())
            }


    }


}

