package com.example.playlistmaker.createNewPlaylist.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.playlistmaker.R
import com.example.playlistmaker.createNewPlaylist.domain.models.Playlist


class PlaylistEditorFragment: CreateNewPlaylistFragment() {
    companion object{
        private const val ARGS_PLAYLIST_ID = "playlist_id"
        fun createArgs(playlistId: Long): Bundle =
            bundleOf(ARGS_PLAYLIST_ID to playlistId)
    }

    private val viewModel by viewModel<PlaylistEditorViewModel>()
    private var playlist: Playlist? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlist = getPlaylist()

        binding.titleCreatePlaylist.text = getString(R.string.edit)
        binding.createPlaylistButtom.text = getString(R.string.save)

        showThePlaylistCover(playlist?.artworkUri?.toUri())
        binding.namePlaylistEditText.setText(playlist?.playlistName)
        binding.descriptionPlaylistEditText.setText(playlist?.playlistDescription)
    }

    override fun saveNewPlaylist(artworkUri: String) {
        viewModel.savePlaylist(playlist?.playlistId, playlist?.artworkUri, playlist?.tracksList)
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    private fun getPlaylist(): Playlist?  {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getSerializable(ARGS_PLAYLIST_ID, Playlist::class.java)
        } else {
            requireArguments().getSerializable(ARGS_PLAYLIST_ID) as Playlist
        }
    }
}