package com.apil.charactergenerator.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apil.charactergenerator.domain.repository.AuthRepository
import com.apil.charactergenerator.presentation.theme.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ApiMode {
    REST, GRAPHQL
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _settingsState = MutableStateFlow(SettingsState())
    val settingsState: StateFlow<SettingsState> = _settingsState.asStateFlow()
    
    fun updateThemeMode(themeMode: ThemeMode) {
        _settingsState.update { it.copy(themeMode = themeMode) }
        saveSettings()
    }
    
    fun updateApiMode(apiMode: ApiMode) {
        _settingsState.update { it.copy(apiMode = apiMode) }
        saveSettings()
    }
    
    fun logout(onLogoutComplete: () -> Unit) {
        viewModelScope.launch {
            authRepository.logout()
            onLogoutComplete()
        }
    }
    
    private fun saveSettings() {
        // In a real app, you'd save these settings to DataStore or SharedPreferences
    }
}

data class SettingsState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val apiMode: ApiMode = ApiMode.REST
)
