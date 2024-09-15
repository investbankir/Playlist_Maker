package com.example.playlistmaker.media_library.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.playlistmaker.R
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.search.ui.TrackListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.playlistmaker.databinding.FragmentFavoriteBinding
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val CLICK_DEBOUNCE_DELAY = 1000L

class FavoritesFragment : Fragment() {
    private val viewModel : FavoritesViewModel by viewModel()

    private var isClickAllowed = true

    private lateinit var trackAdapter: TrackListAdapter

    private var _binding : FragmentFavoriteBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trackAdapter = TrackListAdapter {
            if (clickDebounce()) {
                findNavController().navigate(R.id.action_favoritesFragment_to_playerActivity,
                    PlayerActivity.createArgs(it))
            }
        }
        setupRecyclerView()

        viewModel.getFavoriteLiveData().observe(viewLifecycleOwner) { tracks ->
            updateUI(tracks)
        }

        viewModel.getFavorites()
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

    private fun setupRecyclerView() {
            with(binding.rvFragment) {
                layoutManager = LinearLayoutManager(context)
                adapter = trackAdapter
        }
    }

    private fun updateUI(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            binding.rvFragment.isVisible = false
            binding.imageML.isVisible = true
            binding.textML.isVisible = true
        } else {
            binding.rvFragment.isVisible = true
            binding.imageML.isVisible = false
            binding.textML.isVisible = false
            trackAdapter.submitList(tracks)
        }
    }
    override fun onResume() {
        super.onResume()
        viewModel.getFavorites()
    }
    override fun onStop() {
        super.onStop()
        isClickAllowed = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): FavoritesFragment {
            return FavoritesFragment()
        }
    }
}
