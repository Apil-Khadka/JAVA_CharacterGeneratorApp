package com.apil.charactergenerator.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apil.charactergenerator.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()
    
    private val _registerState = MutableStateFlow(RegisterState())
    val registerState: StateFlow<RegisterState> = _registerState.asStateFlow()
    
    fun updateLoginUsername(username: String) {
        _loginState.update { it.copy(username = username) }
    }
    
    fun updateLoginPassword(password: String) {
        _loginState.update { it.copy(password = password) }
    }
    
    fun updateRegisterUsername(username: String) {
        _registerState.update { it.copy(username = username) }
    }
    
    fun updateRegisterPassword(password: String) {
        _registerState.update { it.copy(password = password) }
    }
    
    fun updateRegisterConfirmPassword(confirmPassword: String) {
        _registerState.update { it.copy(confirmPassword = confirmPassword) }
    }
    
    fun login(onSuccess: () -> Unit) {
        val state = _loginState.value
        
        if (!state.isFormValid) {
            _loginState.update { it.copy(error = "Please fill all fields") }
            return
        }
        
        _loginState.update { it.copy(isLoading = true, error = null) }
        
        viewModelScope.launch {
            authRepository.login(state.username, state.password)
                .onSuccess {
                    _loginState.update { it.copy(isLoading = false) }
                    onSuccess()
                }
                .onFailure { exception ->
                    _loginState.update { 
                        it.copy(isLoading = false, error = exception.message) 
                    }
                }
        }
    }
    
    fun register(onSuccess: () -> Unit) {
        val state = _registerState.value
        
        if (!state.isFormValid) {
            _registerState.update { 
                it.copy(error = "Please fill all fields") 
            }
            return
        }
        
        if (state.password != state.confirmPassword) {
            _registerState.update { 
                it.copy(error = "Passwords do not match") 
            }
            return
        }
        
        _registerState.update { it.copy(isLoading = true, error = null) }
        
        viewModelScope.launch {
            authRepository.register(state.username, state.password)
                .onSuccess {
                    _registerState.update { it.copy(isLoading = false) }
                    onSuccess()
                }
                .onFailure { exception ->
                    _registerState.update { 
                        it.copy(isLoading = false, error = exception.message) 
                    }
                }
        }
    }
    
    fun isLoggedIn(): Boolean {
        return authRepository.isLoggedIn()
    }
    
    fun logout() {
        authRepository.logout()
    }
}

data class LoginState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val isFormValid: Boolean get() = username.isNotBlank() && password.isNotBlank()
}

data class RegisterState(
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val isFormValid: Boolean get() = 
        username.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank()
}
