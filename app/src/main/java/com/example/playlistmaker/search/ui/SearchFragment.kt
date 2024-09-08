package com.example.playlistmaker.search.ui

import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlinx.coroutines.launch


class SearchFragment: Fragment() {
    companion object {
        private const val EDIT_TEXT_KEY = "EDIT_TEXT_KEY"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private lateinit var binding: FragmentSearchBinding
    private val viewModel by viewModel<SearchViewModel>()
    private var isClickAllowed = true

    private var savedValue: String? = null
    private val trackList = ArrayList<Track>()
    private lateinit var trackAdapter: TrackListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                SearchState.LOADING -> showLoading()
                SearchState.CONTENT -> showContent()
                SearchState.HISTORY -> showHistory()
                SearchState.HISTORY_EMPTY -> showEmptyHistory()
                SearchState.NOTHING_FOUND -> showNothingFound()
                SearchState.COMMUNICATION_PROBLEMS -> showCommunicationProblems()
            }
        })

        viewModel.tracks.observe(viewLifecycleOwner, Observer { tracks ->
            trackAdapter.submitList(tracks)
        })

            trackAdapter = TrackListAdapter(requireContext()){ track ->
                if (clickDebounce()) {
                   // clickToTrack(track)
                    findNavController().navigate(R.id.action_searchFragment_to_playerActivity,
                      PlayerActivity.createArgs(track))
                }
            }

        setupUI()

        savedValue = savedInstanceState?.getString(EDIT_TEXT_KEY)
        binding.inputEditText.setText(savedValue)

        if (binding.inputEditText.text.isNullOrEmpty()){
            viewModel.getSearchHistory()
        } else {
            searchTracks()
        }
    }
    private fun setupUI() {
        binding.rvTrackList.adapter = trackAdapter

        binding.inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                savedValue = s.toString()
                binding.clearButton.isVisible = clearButtonVisibility(s)
                binding.clearButtonHistory.isVisible = false
                viewModel.searchDebounce(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.clearButton.setOnClickListener {
            binding.inputEditText.text.clear()
            viewModel.getSearchHistory()
            hideKeyboard(binding.inputEditText)
        }

        binding.clearButtonHistory.setOnClickListener {
            viewModel.clearHistory()
        }

        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
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
        val query = binding.inputEditText.text.toString().trim()
        if (query.isNotEmpty()) {
            viewModel.searchTracks(query)
            hideKeyboard(binding.inputEditText)
        }
    }

    private fun clickDebounce() : Boolean{
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    private fun clickToTrack(track: Track) {
        viewModel.addTrackHistory(track)
        val playerIntent = Intent(requireContext(), PlayerActivity::class.java)
        playerIntent.putExtra("track", track)
        startActivity(playerIntent)
    }
    private fun showLoading() {
        binding.progressBar.isVisible = true
        binding.rvTrackList.isVisible = false
        binding.clearButtonHistory.isVisible = false
    }
    private fun showContent() {
        binding.progressBar.isVisible = false
        binding.rvTrackList.isVisible = true
    }
    private fun showHistory() {
        if(binding.inputEditText.text.isNullOrEmpty()) {
            binding.progressBar.isVisible = false
            binding.rvTrackList.isVisible = true
            binding.clearButtonHistory.isVisible = true
            binding.SearchResult.isVisible = false
            binding.searchHistory.isVisible = true
        }else {
            viewModel.searchTracks(binding.inputEditText.text.toString().trim())
        }
    }

    private fun showEmptyHistory() {
        binding.progressBar.isVisible = false
        binding.rvTrackList.isVisible = true
        binding.clearButtonHistory.isVisible = false
        binding.SearchResult.isVisible = false
        binding.searchHistory.isVisible = true
    }
    private fun showNothingFound() {
        binding.progressBar.isVisible = false
        binding.rvTrackList.isVisible = false
        binding.SearchResult.isVisible = true
        binding.ImageProblem.setImageResource(R.drawable.ic_nothing_was_found)
        binding.SeachResultStatus.setText(R.string.nothingWasFoundText)
        binding.RefreshButton.isVisible = false
        binding.searchHistory.isVisible = false
    }
    private fun showCommunicationProblems() {
        binding.progressBar.isVisible = false
        binding.rvTrackList.isVisible = false
        binding.SearchResult.isVisible = true
        binding.ImageProblem.setImageResource(R.drawable.ic_communication_problems)
        binding.SeachResultStatus.setText(R.string.ProblemConnections)
        binding.RefreshButton.isVisible = false
        binding.searchHistory.isVisible = false
    }

    private fun hideKeyboard(editText: EditText) {
        val imm = requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EDIT_TEXT_KEY, savedValue)
    }
}