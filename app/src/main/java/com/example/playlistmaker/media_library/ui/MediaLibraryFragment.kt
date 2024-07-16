package com.example.playlistmaker.media_library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.google.android.material.tabs.TabLayoutMediator
import com.example.playlistmaker.databinding.FragmentMediaLibraryBinding

class MediaLibraryFragment: Fragment() {

    private lateinit var binding: FragmentMediaLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediaLibraryBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPagerML.adapter = MLAdapter(childFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.MLTabLayout, binding.viewPagerML) {
                tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.favourites_tracks)
                1 -> tab.text = getString((R.string.playLists))
            }
        }
        tabMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
    }
}