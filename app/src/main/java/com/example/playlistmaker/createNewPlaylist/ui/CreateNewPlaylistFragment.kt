package com.example.playlistmaker.createNewPlaylist.ui

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.example.playlistmaker.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

open class CreateNewPlaylistFragment: Fragment() {
    
    lateinit var confirmDialog: MaterialAlertDialogBuilder
    companion object{
        val URIKEY = "uriKey"
    }

    private val viewModel: CreateNewPlaylistViewModel  by viewModel()

    private var _binding : FragmentNewPlaylistBinding? = null
    val binding get() = _binding!!
    var artworkUri: String = ""

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {uri ->
        if (uri != null) {
            artworkUri = uri.toString()
            viewModel.setUri(uri.toString())
            showThePlaylistCover(uri)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.isCreateNewPlaylistFragmentFiled = false

        if (savedInstanceState != null) {
            artworkUri = savedInstanceState.getString(URIKEY, null) ?: ""
        } else {
            artworkUri = viewModel.artworkUri ?: ""
        }

        if (artworkUri.isNotEmpty()) {
            showThePlaylistCover(artworkUri.toUri())
        }
        binding.nameNewPlaylist.editText?.setText(viewModel.playlistName)
        binding.descriptionNewPlaylist.editText?.setText(viewModel.playlistDescription)


        viewModel.isAddedPlaylist.observe(viewLifecycleOwner) { state ->
            when (state) {
                PlaylistAddedState.SUCCESS -> {
                    findNavController().popBackStack()
                    viewModel.resetPlaylistAddedState()
                    val playlistName = viewModel.playlistName
                    Toast.makeText(requireContext(), "Плейлист \"$playlistName\" создан", Toast.LENGTH_SHORT).show()                }
                PlaylistAddedState.EMPTY -> {
                    Toast.makeText(requireContext(), "Ошибка при создании плейлиста", Toast.LENGTH_SHORT).show()
                }
                null -> {

                }
            }
        }

        binding.createPlaylistButtom.isEnabled = false

        binding.nameNewPlaylist.editText?.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.createPlaylistButtom.isEnabled = !s.isNullOrEmpty()
                viewModel.playlistName = s.toString()
                viewModel.filledIn()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.descriptionNewPlaylist.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.playlistDescription = s.toString()
                viewModel.filledIn()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.pickImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.createPlaylistButtom.setOnClickListener{
            saveNewPlaylist(artworkUri)
        }

        binding.backBattonNewPlaylist.setOnClickListener {
            if (viewModel.isCreateNewPlaylistFragmentFiled) {
                showExitConfirmationDialog()
            } else {
                findNavController().popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewModel.isCreateNewPlaylistFragmentFiled) {
                    showExitConfirmationDialog()
                } else {
                    findNavController().popBackStack()
                }
            }
        })
    }
    open fun saveNewPlaylist(artworkUri: String) {
        viewModel.addNewPlaylist(artworkUri)
    }
    fun showThePlaylistCover (uri: Uri?) {
        if (uri != null) {
            Glide.with(this)
                .load(uri)
                .placeholder(R.drawable.ic_player_placeholder)
                .centerCrop()
                .into(binding.pickImage)
        }
    }
    private fun showExitConfirmationDialog() {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setPositiveButton("Завершить") { _, _ ->
                findNavController().popBackStack()
            }
            .setNegativeButton("Отмена") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(URIKEY, artworkUri)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}