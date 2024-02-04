package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val search_Button = findViewById<Button>(R.id.search_button)
        val searchClickListener:View.OnClickListener = object : View.OnClickListener {
            override fun onClick(p0: View?) {
               val searchIntent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(searchIntent)
            }
        }
        search_Button.setOnClickListener(searchClickListener)

        val media_library_Button = findViewById<Button>(R.id.media_library_button)
        media_library_Button.setOnClickListener{
            val media_libraryIntent = Intent(this, Media_libraryActivity::class.java)
            startActivity(media_libraryIntent)
        }
        val settings_Button = findViewById<Button>(R.id.settings_button)
        settings_Button.setOnClickListener {
           val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }
}