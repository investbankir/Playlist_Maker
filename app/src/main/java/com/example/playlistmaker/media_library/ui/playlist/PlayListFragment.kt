package com.example.playlistmaker.media_library.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.playlistmaker.R
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.search.ui.SearchFragment
import com.example.playlistmaker.selected_playlist.ui.SelectedPlaylistFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayListFragment : Fragment() {
    companion object{
        fun newInstance(): PlayListFragment {
            return PlayListFragment()
        }
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val viewModel : PlaylistViewModel by viewModel()
    private lateinit var adapter: PlaylistAdapter
    private var isClickAllowed = true

    private var _binding : FragmentPlaylistBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistBinding.inflate(inflater, container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newPlayListButton.setOnClickListener {
            findNavController().navigate(R.id.action_mediaLibraryFragment_to_createNewPlaylistFragment2)
        }

        adapter = PlaylistAdapter {
            if (clickDebounce()) {
            findNavController().navigate(R.id.action_mediaLibraryFragment_to_selectedPlaylistFragment,
                SelectedPlaylistFragment.createArgs(it))
            }
        }
       // binding.rvPlaylists.adapter {        }

        binding.rvPlaylists.adapter = adapter
        binding.rvPlaylists.layoutManager = GridLayoutManager(requireContext(), 2)
        viewModel.getPlaylists()

        viewModel.playlists.observe(viewLifecycleOwner) { list ->
            if (list.isNotEmpty()) {
                showPlaylists()
                adapter.updatePlaylists(list)
            } else {
                showPlaceholder()
            }
        }
    }
    private fun showPlaceholder() {
        binding.rvPlaylists.isVisible = false
        binding.imageML.isVisible = true
        binding.textPlaceholder.isVisible = true
    }

    private fun showPlaylists() {
        binding.rvPlaylists.isVisible = true
        binding.imageML.isVisible = false
        binding.textPlaceholder.isVisible = false
    }
    private fun clickDebounce() : Boolean {
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

    override fun onStop() {
        super.onStop()
        isClickAllowed = true
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
