package com.example.playlistmaker.main.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.R

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener{_, destination, _ ->
            when(destination.id) {
                R.id.selectedPlaylistFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                } else -> {
                binding.bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }
    }
}