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
                Toast.makeText(this@MainActivity,"Чпоньк! А поиска то пока нет :P",Toast.LENGTH_SHORT).show()
            }
        }
        search_Button.setOnClickListener(searchClickListener)

        val media_library_Button = findViewById<Button>(R.id.media_library_button)
        media_library_Button.setOnClickListener{
            Toast.makeText(this@MainActivity, "А тут будет Медиатека, точно тебе говорю!", Toast.LENGTH_SHORT).show()
        }
        val settings_Button = findViewById<Button>(R.id.settings_button)
        settings_Button.setOnClickListener {
           val settingsIntent = Intent(this, SearchActivity::class.java)
            startActivity(settingsIntent)
        }
    }
}