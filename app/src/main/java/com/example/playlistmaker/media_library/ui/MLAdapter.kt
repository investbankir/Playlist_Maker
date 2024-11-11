package com.example.playlistmaker.media_library.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.media_library.ui.favorites.FavoritesFragment
import com.example.playlistmaker.media_library.ui.playlist.PlayListFragment

class MLAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle,) : FragmentStateAdapter(fragmentManager, lifecycle)  {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> FavoritesFragment.newInstance()
            else -> PlayListFragment.newInstance()
        }
    }
}