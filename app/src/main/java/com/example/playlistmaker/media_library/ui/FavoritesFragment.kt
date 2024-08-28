package com.example.playlistmaker.media_library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.search.ui.TrackListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.playlistmaker.databinding.FragmentFavoriteBinding
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.search.domain.models.Track

class FavoritesFragment : Fragment() {
    private val viewModel : FavoritesViewModel by viewModel()
    private var _binding : FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var searchBinding: FragmentSearchBinding

    private lateinit var trackAdapter: TrackListAdapter
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

        setupRecyclerView()

        viewModel.getFavoriteLiveData().observe(viewLifecycleOwner) { tracks ->
            updateUI(tracks)
        }

        viewModel.getFavorites()
    }

    private fun setupRecyclerView() {
        trackAdapter = TrackListAdapter(requireContext(), arrayListOf()) { track ->
        }

        with(searchBinding.rvTrackList) {
            layoutManager = LinearLayoutManager(context)
            adapter = trackAdapter
        }
    }

    private fun updateUI(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            searchBinding.rvTrackList.isVisible = false
            binding.imageML.isVisible = true
        } else {
            searchBinding.rvTrackList.isVisible = true
            binding.imageML.isVisible = false
            trackAdapter.trackList.clear()
            trackAdapter.trackList.addAll(tracks)
            trackAdapter.notifyDataSetChanged()
        }
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
