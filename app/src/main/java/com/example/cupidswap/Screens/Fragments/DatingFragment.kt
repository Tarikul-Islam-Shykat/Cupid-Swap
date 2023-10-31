package com.example.cupidswap.Screens.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.cupidswap.R
import com.example.cupidswap.adapter.DatingAdapter
import com.example.cupidswap.databinding.FragmentDatingBinding
import com.example.cupidswap.model.UserModel
import com.example.cupidswap.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction

class DatingFragment : Fragment() {

    private lateinit var binding : FragmentDatingBinding
    private lateinit var manager : CardStackLayoutManager

    companion object{
        var  list : ArrayList<UserModel>? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDatingBinding.inflate(layoutInflater)

        getData()
        return binding.root
    }

    private fun init_() {

        manager = CardStackLayoutManager(requireContext(), object : CardStackListener{
            override fun onCardDragging(direction: Direction?, ratio: Float) {
            }

            override fun onCardSwiped(direction: Direction?) {
                if(manager!!.topPosition == list!!.size){
                    Toast.makeText(context, "You reached at the end", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCardRewound() {
            }

            override fun onCardCanceled() {
            }

            override fun onCardAppeared(view: View?, position: Int) {
            }

            override fun onCardDisappeared(view: View?, position: Int) {
            }

        })

        manager.setVisibleCount(3)
        manager.setTranslationInterval(0.6f)
        manager.setScaleInterval(0.8f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)

    }

    private fun getData() {
        FirebaseDatabase.getInstance().getReference("users").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    list = arrayListOf()
                    for(data in snapshot.children){
                        val model = data.getValue(UserModel::class.java)
                        if(model!!.number == FirebaseAuth.getInstance().currentUser!!.phoneNumber){
                            continue
                        }
                        list!!.add(model!!)
                    }
                    list!!.shuffle()
                    init_() // calling the init method
                    binding.cardStackView.layoutManager = manager
                    binding.cardStackView.itemAnimator = DefaultItemAnimator()
                    binding.cardStackView.adapter = DatingAdapter(requireContext(), list!!)

                }
                else
                {
                    Utils.dismissDialog(requireContext())
                    Log.e("DatingFragment", "Error getting data")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Utils.dismissDialog(requireContext())
                Log.d("DatingFragment", error.toString())
            }

        })
    }


}