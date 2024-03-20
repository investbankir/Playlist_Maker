    package com.example.playlistmaker

    import android.inputmethodservice.InputMethodService
    import androidx.appcompat.app.AppCompatActivity
    import android.os.Bundle
    import android.text.Editable
    import android.text.TextWatcher
    import android.util.Log
    import android.view.View
    import android.view.inputmethod.EditorInfo
    import android.view.inputmethod.InputMethodManager
    import android.widget.Button
    import android.widget.EditText
    import android.widget.ImageView
    import android.widget.LinearLayout
    import android.widget.TextView
    import androidx.core.view.isVisible
    import androidx.recyclerview.widget.RecyclerView
    import com.google.gson.GsonBuilder
    import retrofit2.Call
    import retrofit2.Callback
    import retrofit2.Response
    import retrofit2.Retrofit
    import retrofit2.converter.gson.GsonConverterFactory
    import retrofit2.create
    import java.util.Date


    class SearchActivity : AppCompatActivity() {

        companion object {
            private const val LOG_TAG = "SeachActivity"
            private const val EDIT_TEXT_KEY = "EDIT_TEXT_KEY"
        }
        private val iTunesUrl = "https://itunes.apple.com"

        val retrofit = Retrofit.Builder()
            .baseUrl(iTunesUrl)
            .addConverterFactory(
                GsonConverterFactory.create())
            .build()
        private val iTunesService = retrofit.create(TrackApiService::class.java)
        private var savedValue: String? = null
        private val trackList = ArrayList<Track>()
        private val trackAdapter = TrackListAdapter(trackList)

        private lateinit var inputEditText: EditText
        private lateinit var clearButton: ImageView
        private lateinit var backMainActivity: Button
        private lateinit var rvTrackList: RecyclerView
        private lateinit var seachLinerLayout: LinearLayout
        private lateinit var searchResult: LinearLayout
        private lateinit var imageProblem: ImageView
        private lateinit var seachResultStatus: TextView
        private lateinit var refreshButton: Button


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_search)

            seachLinerLayout = findViewById(R.id.container)
            inputEditText = findViewById(R.id.inputEditText)
            clearButton = findViewById(R.id.clearButton)
            searchResult = findViewById(R.id.SearchResult)
            imageProblem = findViewById(R.id.ImageProblem)
            seachResultStatus = findViewById(R.id.SeachResultStatus)
            refreshButton = findViewById(R.id.RefreshButton)

            backMainActivity = findViewById(R.id.backMainActivity)
            backMainActivity.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            savedValue = savedInstanceState?.getString(EDIT_TEXT_KEY)

            clearButton.setOnClickListener {
                savedValue = ""
                inputEditText.setText(savedValue)
                inputEditText.clearFocus()
                hideKeyboard(inputEditText)
                trackList.clear()
                trackAdapter.notifyDataSetChanged()
                searchResult.visibility = View.GONE
            }

            rvTrackList = findViewById(R.id.rvTrackList)
            rvTrackList.adapter = trackAdapter

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

                inputEditText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    trackSearch()
                    true
                }
                false
            }
        }
        private fun clearButtonVisibility(s: CharSequence?): Boolean {
            return !s.isNullOrEmpty()
        }
        enum class SearchForResults {
            success, nothingWasFound, communicationProblems
        }
        private fun showResult (result : SearchForResults ) {
            when (result) {
                SearchForResults.success -> {
                    rvTrackList.visibility = View.VISIBLE
                    searchResult.visibility = View.GONE
                }
                SearchForResults.nothingWasFound -> {
                    rvTrackList.visibility = View.GONE
                    searchResult.visibility = View.VISIBLE
                    imageProblem.setImageResource(R.drawable.ic_nothing_was_found)
                    seachResultStatus.setText(R.string.nothingWasFoundText)
                    refreshButton.visibility = View.GONE
                }
                SearchForResults.communicationProblems -> {
                    rvTrackList.visibility = View.GONE
                    searchResult.visibility = View.VISIBLE
                }
            }
        }
        private fun trackSearch(){
            if (inputEditText.text.isNotEmpty()) {
                iTunesService.search(inputEditText.text.toString()).enqueue(object : Callback<TrackResponse> {
                    override fun onResponse(
                        call: Call<TrackResponse>,
                        response: Response<TrackResponse>
                    ) {
                        if (response.code() == 200) {
                            trackList.clear()
                            if (response.body()?.results?.isNotEmpty()==true){
                                trackList.addAll(response.body()?.results!!)
                                trackAdapter.notifyDataSetChanged()
                            }
                            if (trackList.isEmpty()) {
                                showResult(SearchForResults.nothingWasFound)
                            } else {
                                showResult(SearchForResults.success)
                            }
                        } else {
                            showResult(SearchForResults.communicationProblems)
                        }
                    }

                    override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                        showResult(SearchForResults.communicationProblems)
                        refreshButton.visibility = View.VISIBLE
                        refreshButton.setOnClickListener{
                            trackSearch()
                        }
                    }
                }
                )
            }
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
        private fun hideKeyboard(editText: EditText) {
            val variableHideKayboard = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            variableHideKayboard.hideSoftInputFromWindow(editText.windowToken, 0)
        }

    }