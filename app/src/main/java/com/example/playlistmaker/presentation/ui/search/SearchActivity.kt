package com.example.playlistmaker.presentation.ui.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.TrackListAdapter
import com.example.playlistmaker.TrackResponse
import com.example.playlistmaker.data.network.TrackApiService
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.presentation.ui.player.PlayerActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {

    companion object {
        private const val LOG_TAG = "SeachActivity"
        private const val EDIT_TEXT_KEY = "EDIT_TEXT_KEY"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    enum class SearchForResults {
        empty, showSearchHistory, success, nothingWasFound, communicationProblems
    }

    private val iTunesUrl = "https://itunes.apple.com"
    private var searchHistoryTrackList = ArrayList<Track>()
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val searchHistoryClass : SearchHistory? = SearchHistory()

    val retrofit = Retrofit.Builder()
        .baseUrl(iTunesUrl)
        .addConverterFactory(
            GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(TrackApiService::class.java)
    private var savedValue: String? = null
    private val trackList = ArrayList<Track>()
    private lateinit var trackAdapter : TrackListAdapter

    private lateinit var inputEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var backMainActivity: Button
    private lateinit var rvTrackList: RecyclerView
    private lateinit var seachLinerLayout: LinearLayout
    private lateinit var searchResult: LinearLayout
    private lateinit var imageProblem: ImageView
    private lateinit var seachResultStatus: TextView
    private lateinit var refreshButton: Button
    private lateinit var searchHistory : LinearLayout
    private lateinit var titleSearchHistory: TextView
    private lateinit var clearButtonHistory: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        trackAdapter = TrackListAdapter(this, trackList) {
            track -> clickToTrack(track)
        }

        seachLinerLayout = findViewById(R.id.container)
        inputEditText = findViewById(R.id.inputEditText)
        clearButton = findViewById(R.id.clearButton)
        searchResult = findViewById(R.id.SearchResult)
        imageProblem = findViewById(R.id.ImageProblem)
        seachResultStatus = findViewById(R.id.SeachResultStatus)
        refreshButton = findViewById(R.id.RefreshButton)
        searchHistory = findViewById(R.id.searchHistory)
        clearButtonHistory = findViewById(R.id.clearButtonHistory)
        titleSearchHistory = findViewById(R.id.titleSearchHistory)
        progressBar = findViewById(R.id.progressBar)

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
            searchHistoryTrackList = SearchHistory.read()
            showResult(
                if (searchHistoryTrackList.size > 0)
                    SearchForResults.showSearchHistory
                else SearchForResults.empty
            )
        }

        clearButtonHistory.setOnClickListener{
            SearchHistory.clearSearchHistory()
            searchHistoryTrackList.clear()
            showResult(SearchForResults.empty)
        }

        rvTrackList = findViewById(R.id.rvTrackList)
        rvTrackList.adapter = trackAdapter

        val textWatcherEditText = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                savedValue = s.toString()
                Log.i(LOG_TAG, "Введенное значение: $savedValue")
                clearButton.isVisible = clearButtonVisibility(s)
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {}

        }

        inputEditText.addTextChangedListener(textWatcherEditText)
        searchHistoryTrackList = SearchHistory.read()

        showResult(
            if (searchHistoryTrackList.size > 0)
                SearchForResults.showSearchHistory
            else SearchForResults.empty
        )

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

    private fun showResult (result : SearchForResults) {
        when (result) {
            SearchForResults.empty -> {
                progressBar.visibility = View.GONE
                rvTrackList.visibility = View.GONE
                searchResult.visibility = View.GONE
                titleSearchHistory.visibility = View.GONE
                clearButtonHistory.visibility = View.GONE
            }
            SearchForResults.showSearchHistory -> {
                progressBar.visibility = View.GONE
                rvTrackList.visibility = View.VISIBLE
                searchResult.visibility = View.GONE
                titleSearchHistory.visibility = View.VISIBLE
                clearButtonHistory.visibility = View.VISIBLE
                trackAdapter.trackList = searchHistoryTrackList
                trackAdapter.notifyDataSetChanged()
            }
            SearchForResults.success -> {
                progressBar.visibility = View.GONE
                rvTrackList.visibility = View.VISIBLE
                searchResult.visibility = View.GONE
                titleSearchHistory.visibility = View.GONE
                clearButtonHistory.visibility = View.GONE
                trackAdapter.trackList = trackList
                trackAdapter.notifyDataSetChanged()
            }
            SearchForResults.nothingWasFound -> {
                progressBar.visibility = View.GONE
                rvTrackList.visibility = View.GONE
                titleSearchHistory.visibility = View.GONE
                clearButtonHistory.visibility = View.GONE
                searchResult.visibility = View.VISIBLE
                imageProblem.setImageResource(R.drawable.ic_nothing_was_found)
                seachResultStatus.setText(R.string.nothingWasFoundText)
                refreshButton.visibility = View.GONE
            }
            SearchForResults.communicationProblems -> {
                progressBar.visibility = View.GONE
                rvTrackList.visibility = View.GONE
                titleSearchHistory.visibility = View.GONE
                clearButtonHistory.visibility = View.GONE
                imageProblem.setImageResource(R.drawable.ic_communication_problems)
                searchResult.visibility = View.VISIBLE
            }
        }
    }
    private fun trackSearch(){
        if (inputEditText.text.isNotEmpty()) {
            progressBar.visibility = View.VISIBLE
            rvTrackList.visibility = View.GONE
            searchResult.visibility = View.GONE
            titleSearchHistory.visibility = View.GONE
            clearButtonHistory.visibility = View.GONE
            hideKeyboard(inputEditText)

            iTunesService.search(inputEditText.text.toString()).enqueue(object : Callback<TrackResponse> {
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>) {
                    if (response.isSuccessful) {
                        val trackResponse = response.body()
                        trackList.clear()
                        if (trackResponse?.results?.isNotEmpty()==true){
                            trackList.addAll(trackResponse.results)
                            trackAdapter.notifyDataSetChanged()//
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

    val searchRunnable = Runnable { trackSearch() }
    fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }
    private fun clickToTrack(track: Track) {
        if (clickDebounce()) {
            searchHistoryClass?.addTrack(track)

            val playerIntent = Intent(this, PlayerActivity::class.java)
            playerIntent.putExtra("track", track)
            startActivity(playerIntent)
        }
    }
    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

}