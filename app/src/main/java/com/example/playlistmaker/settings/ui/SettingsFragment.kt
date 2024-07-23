package com.example.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment: Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.settingsState.observe(viewLifecycleOwner) { state ->
            handleSettingsState(state)
        }

        setupUi()
    }
    override fun onResume() {
        super.onResume()
        viewModel.synchronizeTheme()
    }

    private fun handleSettingsState(state: SettingsState) {
        binding.themeSwitch.isChecked = state.isDarkThemeEnabled
    }

    private fun setupUi() {
        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setDarkTheme(isChecked)
        }

        binding.toShare.setOnClickListener {
            viewModel.shareApp(getString(R.string.shareApp))
        }

        binding.massageToSupport.setOnClickListener {
            viewModel.contactSupport(
                getString(R.string.emailDeveloper),
                getString(R.string.titleMassageSupport),
                getString(R.string.textMassageSupport)
            )
        }

        binding.sendUserAgreement.setOnClickListener {
            viewModel.openUserAgreement(getString(R.string.offer))
        }
    }
}