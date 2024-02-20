package com.example.playlistmaker

import android.inputmethodservice.InputMethodService
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.isVisible

class SearchActivity : AppCompatActivity() {

    companion object {
        private const val LOG_TAG = "SeachActivity"
    }
    var enteredValue: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val seachLinerLayout = findViewById<LinearLayout>(R.id.container)
        var inputEditText = findViewById<EditText>(R.id.inputEditText)
        val clearButton = findViewById<ImageView>(R.id.clearButton)


        clearButton.setOnClickListener {
            enteredValue = ""
            inputEditText.setText(enteredValue)
            inputEditText.clearFocus()
            hideKayboard(inputEditText)
        }


        val textWatcherEditText = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                enteredValue = s.toString()
                Log.i(LOG_TAG, "Введенное значение: $inputEditText")
                clearButton.isVisible = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {}

        }

        inputEditText.addTextChangedListener(textWatcherEditText)
    }

    private fun clearButtonVisibility (s: CharSequence?): Boolean = !s.isNullOrEmpty()
    private fun hideKayboard(editText: EditText) {
        val variableHideKayboard = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        variableHideKayboard.hideSoftInputFromWindow(editText.windowToken, 0)
    }
}