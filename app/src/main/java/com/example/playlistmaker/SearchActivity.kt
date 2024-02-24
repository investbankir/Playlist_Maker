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
            private const val EDIT_TEXT_KEY = "EDIT_TEXT_KEY"
        }

        private lateinit var inputEditText: EditText
        private var savedValue: String? = null
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_search)

            savedValue = savedInstanceState?.getString(EDIT_TEXT_KEY)

            val seachLinerLayout = findViewById<LinearLayout>(R.id.container)
            inputEditText = findViewById(R.id.inputEditText)
            val clearButton = findViewById<ImageView>(R.id.clearButton)


            clearButton.setOnClickListener {
                savedValue = ""
                inputEditText.setText(savedValue)
                inputEditText.clearFocus()
                hideKeyboard(inputEditText)
            }

            val textWatcherEditText = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    savedValue = s.toString()
                    Log.i(LOG_TAG, "Введенное значение: $savedValue")
                    clearButton.isVisible = clearButtonVisibility(s)
                }

                override fun afterTextChanged(s: Editable?) {}

            }

            inputEditText.addTextChangedListener(textWatcherEditText)
        }

        override fun onSaveInstanceState(outState: Bundle) {
            super.onSaveInstanceState(outState)
            outState.putString(EDIT_TEXT_KEY, savedValue)
        }

        override fun onRestoreInstanceState(savedInstanceState: Bundle) {
            super.onRestoreInstanceState(savedInstanceState)
            savedValue = savedInstanceState.getString(EDIT_TEXT_KEY)
            inputEditText.setText(savedValue)
        }

        private fun clearButtonVisibility (s: CharSequence?): Boolean = !s.isNullOrEmpty()
        private fun hideKeyboard(editText: EditText) {
            val variableHideKayboard = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            variableHideKayboard.hideSoftInputFromWindow(editText.windowToken, 0)
        }
    }