package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val searchButton = findViewById<Button>(R.id.search_button)
        val searchClickListener:View.OnClickListener = object : View.OnClickListener {
            override fun onClick(p0: View?) {
               val searchIntent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(searchIntent)
            }
        }
        searchButton.setOnClickListener(searchClickListener)

        val mediaLibraryButton = findViewById<Button>(R.id.media_library_button)
        mediaLibraryButton.setOnClickListener{
            val mediaLibraryIntent = Intent(this, Media_libraryActivity::class.java)
            startActivity(mediaLibraryIntent)
        }
        val settingsButton = findViewById<Button>(R.id.settings_button)
        settingsButton.setOnClickListener {
           val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }
}