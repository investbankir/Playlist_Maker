package com.example.playlistmaker.media_library.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator

class MediaLibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButtonML.setOnClickListener {
            finish()
        }

        binding.viewPagerML.adapter = MLAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.MLTabLayout, binding.viewPagerML) {
            tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.favourites_tracks)
                1 -> tab.text = getString((R.string.playLists))
            }
        }
        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}