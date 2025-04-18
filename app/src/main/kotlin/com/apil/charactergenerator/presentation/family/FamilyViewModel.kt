package com.apil.charactergenerator.presentation.family

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apil.charactergenerator.domain.model.Family
import com.apil.charactergenerator.domain.repository.FamilyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class FamilyViewModel @Inject constructor(
    private val familyRepository: FamilyRepository
) : ViewModel() {
    
    private val _familyListState = MutableStateFlow(FamilyListState())
    val familyListState: StateFlow<FamilyListState> = _familyListState.asStateFlow()
    
    private val _familyDetailState = MutableStateFlow<FamilyDetailState>(FamilyDetailState.Loading)
    val familyDetailState: StateFlow<FamilyDetailState> = _familyDetailState.asStateFlow()
    
    private val _generationState = MutableStateFlow(FamilyGenerationState())
    val generationState: StateFlow<FamilyGenerationState> = _generationState.asStateFlow()
    
    fun loadFamilies(page: Int = 0, size: Int = 10) {
        _familyListState.update { it.copy(isLoading = true, error = null) }
        
        viewModelScope.launch {
            familyRepository.getAllFamilies(page, size)
                .onSuccess { families ->
                    _familyListState.update {
                        it.copy(
                            families = families,
                            isLoading = false
                        )
                    }
                }
                .onFailure { exception ->
                    _familyListState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message
                        )
                    }
                }
        }
    }
    
    fun getFamilyById(id: String) {
        try {
            val uuid = UUID.fromString(id)
            _familyDetailState.value = FamilyDetailState.Loading
            
            viewModelScope.launch {
                familyRepository.getFamilyById(uuid)
                    .onSuccess { family ->
                        _familyDetailState.value = FamilyDetailState.Success(family)
                    }
                    .onFailure { exception ->
                        _familyDetailState.value = FamilyDetailState.Error(
                            exception.message ?: "Failed to load family"
                        )
                    }
            }
        } catch (e: IllegalArgumentException) {
            _familyDetailState.value = FamilyDetailState.Error("Invalid family ID format")
        }
    }
    
    // Generation form state handling
    fun updateFamilyName(familyName: String) {
        _generationState.update { it.copy(familyName = familyName) }
    }
    
    fun updateMemberCount(memberCount: Int) {
        _generationState.update { it.copy(memberCount = memberCount) }
    }
    
    fun updateBackgroundHint(backgroundHint: String) {
        _generationState.update { it.copy(backgroundHint = backgroundHint) }
    }
    
    fun generateFamily(onSuccess: (Family) -> Unit) {
        val state = _generationState.value
        
        if (!state.isFormValid) {
            _generationState.update { 
                it.copy(error = "Please enter a valid member count (1-10)") 
            }
            return
        }
        
        _generationState.update { it.copy(isLoading = true, error = null) }
        
        viewModelScope.launch {
            familyRepository.generateFamily(
                familyName = state.familyName.takeIf { it.isNotBlank() },
                memberCount = state.memberCount,
                backgroundHint = state.backgroundHint.takeIf { it.isNotBlank() }
            )
                .onSuccess { family ->
                    _generationState.update { it.copy(isLoading = false) }
                    onSuccess(family)
                }
                .onFailure { exception ->
                    _generationState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message
                        )
                    }
                }
        }
    }
}

data class FamilyListState(
    val families: List<Family> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class FamilyDetailState {
    object Loading : FamilyDetailState()
    data class Success(val family: Family) : FamilyDetailState()
    data class Error(val message: String) : FamilyDetailState()
}

data class FamilyGenerationState(
    val familyName: String = "",
    val memberCount: Int = 3,
    val backgroundHint: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val isFormValid: Boolean get() = memberCount in 1..10
}
