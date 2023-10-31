package com.example.cupidswap.Screens.Dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.cupidswap.R
import com.example.cupidswap.databinding.ActivityDashboardBinding
import com.google.android.material.navigation.NavigationView

class Dashboard : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener{
    private lateinit var binding: ActivityDashboardBinding
    var actionBarDrawerToggle: ActionBarDrawerToggle ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // nav drawer set up
        actionBarDrawerToggle = ActionBarDrawerToggle(this@Dashboard, binding.dashboardDrawerLayout, R.string.open_nav, R.string.close_nav)
        binding.dashboardDrawerLayout.addDrawerListener(actionBarDrawerToggle!!)
        actionBarDrawerToggle!!.syncState()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // showing the button
        binding.dashboardNavView.setNavigationItemSelectedListener(this)

        val navController = findNavController(R.id.hostFragment)
        NavigationUI.setupWithNavController(binding.bottomNavigationView,navController)


    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.rateUs ->{
                Toast.makeText(applicationContext, "Rate us", Toast.LENGTH_SHORT).show()
            }
        }
        return true
     }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return  if (actionBarDrawerToggle!!.onOptionsItemSelected(item)){
            true
        }
        else{
            super.onOptionsItemSelected(item)
        }
    }


    override fun onBackPressed() {

        if(binding.dashboardDrawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.dashboardDrawerLayout.close()
        }
        else { super.onBackPressed() }
    }
}