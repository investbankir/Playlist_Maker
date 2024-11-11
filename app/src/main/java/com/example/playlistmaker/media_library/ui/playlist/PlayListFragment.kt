package com.example.playlistmaker.media_library.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayListFragment : Fragment() {
    companion object{
        fun newInstance(): PlayListFragment {
            return PlayListFragment()
        }
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

        adapter = PlaylistAdapter{}

        binding.newPlayListButton.setOnClickListener {
            findNavController().navigate(R.id.action_mediaLibraryFragment_to_createNewPlaylistFragment2)
        }

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
