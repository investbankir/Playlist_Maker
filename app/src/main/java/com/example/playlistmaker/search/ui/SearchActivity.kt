package com.example.playlistmaker.search.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.creator.Creator

class SearchActivity : AppCompatActivity() {
    private lateinit var viewModel: SearchViewModel
    companion object {
        private const val LOG_TAG = "SeachActivity"
        private const val EDIT_TEXT_KEY = "EDIT_TEXT_KEY"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
    private var savedValue: String? = null
    private val handler = Handler(Looper.getMainLooper())
    private val trackList = ArrayList<Track>()
    private lateinit var trackAdapter: TrackListAdapter

    private lateinit var inputEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var backMainActivity: Button
    private lateinit var rvTrackList: RecyclerView
    private lateinit var seachLinerLayout: LinearLayout
    private lateinit var searchResult: LinearLayout
    private lateinit var imageProblem: ImageView
    private lateinit var seachResultStatus: TextView
    private lateinit var refreshButton: Button
    private lateinit var searchHistory: LinearLayout
    private lateinit var titleSearchHistory: TextView
    private lateinit var clearButtonHistory: Button
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val factory = SearchViewModelFactory(
            Creator.provideSearchInteractor(),
            Creator.provideHistoryInteractor()
        )

            viewModel = ViewModelProvider(this, factory).get(SearchViewModel::class.java)

        viewModel.state.observe(this, Observer { state ->
            when (state) {
                SearchState.LOADING -> showLoading()
                SearchState.CONTENT -> showContent()
                SearchState.HISTORY -> showHistory()
                SearchState.HISTORY_EMPTY -> showEmptyHistory()
                SearchState.NOTHING_FOUND -> showNothingFound()
                SearchState.COMMUNICATION_PROBLEMS -> showCommunicationProblems()
            }
        })

        viewModel.tracks.observe(this, Observer { tracks ->
            trackAdapter.trackList = ArrayList(tracks)
            trackAdapter.notifyDataSetChanged()
        })

        trackAdapter = TrackListAdapter(this, trackList) { track ->
            clickToTrack(track)
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
        rvTrackList = findViewById(R.id.rvTrackList)

        backMainActivity.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setupUI()

        savedValue = savedInstanceState?.getString(EDIT_TEXT_KEY)
        inputEditText.setText(savedValue)

        viewModel.getSearchHistory()
    }

    private fun setupUI() {
        rvTrackList.adapter = trackAdapter

        inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                savedValue = s.toString()
                clearButton.isVisible = clearButtonVisibility(s)
                searchDebounce()
                }

            override fun afterTextChanged(s: Editable?) {}
        })

        clearButton.setOnClickListener {
            inputEditText.text.clear()
            viewModel.getSearchHistory()
            hideKeyboard(inputEditText)
        }

        clearButtonHistory.setOnClickListener {
            viewModel.clearHistory()
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchTracks()
                true
            } else {
                false
            }
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Boolean {
        return !s.isNullOrEmpty()
    }

    private fun searchTracks() {
        showLoading()
        val query = inputEditText.text.toString().trim()
        if (query.isNotEmpty()) {
            viewModel.searchTracks(query)
            hideKeyboard(inputEditText)
        }
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private val searchRunnable = Runnable {
        val query = inputEditText.text.toString().trim()
        if (query.isNotEmpty()) {
            viewModel.searchDebounced(query)
        }
    }
    private fun clickToTrack(track: Track) {
        viewModel.addTrackHistory(track)
        val playerIntent = Intent(this, PlayerActivity::class.java)
        playerIntent.putExtra("track", track)
        startActivity(playerIntent)
    }
    private fun showLoading() {
        progressBar.isVisible = true
        rvTrackList.isVisible = false
        clearButtonHistory.isVisible = false
    }
    private fun showContent() {
        progressBar.isVisible = false
        rvTrackList.isVisible = true
    }
    private fun showHistory() {
        progressBar.isVisible = false
        rvTrackList.isVisible = true
        clearButtonHistory.isVisible = true
        searchResult.isVisible = false
        searchHistory.isVisible = true
    }

    private fun showEmptyHistory() {
        progressBar.isVisible = false
        rvTrackList.isVisible = true
        clearButtonHistory.isVisible = false
        searchResult.isVisible = false
        searchHistory.isVisible = true
    }
    private fun showNothingFound() {
        progressBar.isVisible = false
        rvTrackList.isVisible = false
        searchResult.isVisible = true
        imageProblem.setImageResource(R.drawable.ic_nothing_was_found)
        seachResultStatus.setText(R.string.nothingWasFoundText)
        refreshButton.isVisible = false
        searchHistory.isVisible = false
    }
    private fun showCommunicationProblems() {
        progressBar.isVisible = false
        rvTrackList.isVisible = false
        searchResult.isVisible = true
        imageProblem.setImageResource(R.drawable.ic_communication_problems)
        seachResultStatus.setText(R.string.ProblemConnections)
        refreshButton.isVisible = false
        searchHistory.isVisible = false
    }

    private fun hideKeyboard(editText: EditText) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
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
}