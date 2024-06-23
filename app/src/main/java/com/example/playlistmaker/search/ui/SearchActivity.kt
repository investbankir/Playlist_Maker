package com.example.playlistmaker.search.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.example.playlistmaker.search.presentation.SearchState
import com.example.playlistmaker.search.presentation.SearchViewModel
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.player.ui.PlayerActivity
//import com.example.playlistmaker.search.data.SearchHistory
import com.example.playlistmaker.search.presentation.SearchViewModelFactory




class SearchActivity : AppCompatActivity() {
    private lateinit var viewModel: SearchViewModel


    companion object {
        private const val LOG_TAG = "SeachActivity"
        private const val EDIT_TEXT_KEY = "EDIT_TEXT_KEY"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
/////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////
    //////////////////////


//    private val iTunesUrl = "https://itunes.apple.com"
    private var searchHistoryTrackList = ArrayList<Track>()
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

  //  val retrofit = Retrofit.Builder()
    //    .baseUrl(iTunesUrl)
      //  .addConverterFactory(
        //    GsonConverterFactory.create())
        //.build()
   // private val iTunesService = retrofit.create(TrackApiService::class.java)

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
  //  private val searchHistoryClass : SearchHistory? = SearchHistory()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        viewModel = ViewModelProvider(this, SearchViewModelFactory(this))[SearchViewModel::class.java]

        viewModel.state.observe(this, Observer { state ->
            when (state) {
                SearchState.LOADING -> showLoading()
                SearchState.CONTENT -> showContent()
                SearchState.HISTORY -> showHistory()
                SearchState.NOTHING_FOUND -> showNothingFound()
                SearchState.COMMUNICATION_PROBLEMS -> showCommunicationProblems()
            }
        })

        viewModel.tracks.observe(this, Observer { tracks ->
            trackAdapter.trackList = ArrayList(tracks)
            trackAdapter.notifyDataSetChanged()
        })

        viewModel.getSearchHistory()
        ///////////////////////////
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
            inputEditText.text.clear()
            viewModel.getSearchHistory()
            hideKeyboard(inputEditText)
        }

        clearButtonHistory.setOnClickListener {
            viewModel.clearHistory()
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

       // inputEditText.addTextChangedListener(textWatcherEditText)
       // searchHistoryTrackList = SearchHistory.read()

        viewModel.getSearchHistory()
        hideKeyboard(inputEditText)

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchTracks()
                true
            }
            false
        }
    }
    private fun clearButtonVisibility(s: CharSequence?): Boolean {
        return !s.isNullOrEmpty()
    }


    private fun searchTracks() {
        val query = inputEditText.text.toString()
        if (query.isNotEmpty()) {
            viewModel.searchTracks(query)
            hideKeyboard(inputEditText)
        }
    }

    private fun showLoading() {
        progressBar.isVisible = true
        rvTrackList.isVisible = false
        searchResult.isVisible = false
        searchHistory.isVisible = false
    }

    private fun showContent() {
        progressBar.isVisible = false
        rvTrackList.isVisible = true
        searchResult.isVisible = false
        searchHistory.isVisible = false
    }

    private fun showHistory() {
        progressBar.isVisible = false
        rvTrackList.isVisible = true
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

    val searchRunnable = Runnable { searchTracks() }
    fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }
    private fun clickToTrack(track: Track) {
        if (clickDebounce()) {
            //searchHistoryClass?.addTrack(track)
            viewModel.addTrackHistory(track)

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