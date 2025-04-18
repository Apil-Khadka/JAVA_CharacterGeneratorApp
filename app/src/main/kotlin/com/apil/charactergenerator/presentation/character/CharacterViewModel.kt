package com.apil.charactergenerator.presentation.character

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apil.charactergenerator.domain.model.Character
import com.apil.charactergenerator.domain.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
) : ViewModel() {
    
    private val _characterListState = MutableStateFlow(CharacterListState())
    val characterListState: StateFlow<CharacterListState> = _characterListState.asStateFlow()
    
    private val _characterDetailState = MutableStateFlow<CharacterDetailState>(CharacterDetailState.Loading)
    val characterDetailState: StateFlow<CharacterDetailState> = _characterDetailState.asStateFlow()
    
    private val _generationState = MutableStateFlow(CharacterGenerationState())
    val generationState: StateFlow<CharacterGenerationState> = _generationState.asStateFlow()
    
    fun loadCharacters(page: Int = 0, size: Int = 10) {
        _characterListState.update { it.copy(isLoading = true, error = null) }
        
        viewModelScope.launch {
            characterRepository.getAllCharacters(page, size)
                .onSuccess { characters ->
                    _characterListState.update {
                        it.copy(
                            characters = characters,
                            isLoading = false
                        )
                    }
                }
                .onFailure { exception ->
                    _characterListState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message
                        )
                    }
                }
        }
    }
    
    fun getCharacterBySeed(seed: String) {
        _characterDetailState.value = CharacterDetailState.Loading
        
        viewModelScope.launch {
            characterRepository.getCharacterBySeed(seed)
                .onSuccess { character ->
                    _characterDetailState.value = CharacterDetailState.Success(character)
                }
                .onFailure { exception ->
                    _characterDetailState.value = CharacterDetailState.Error(
                        exception.message ?: "Failed to load character"
                    )
                }
        }
    }
    
    // Generation form state handling
    fun updateNameHint(nameHint: String) {
        _generationState.update { it.copy(nameHint = nameHint) }
    }
    
    fun updateAgeMin(ageMin: Int) {
        _generationState.update { it.copy(ageMin = ageMin) }
    }
    
    fun updateAgeMax(ageMax: Int) {
        _generationState.update { it.copy(ageMax = ageMax) }
    }
    
    fun updateProfessionHint(professionHint: String) {
        _generationState.update { it.copy(professionHint = professionHint) }
    }
    
    fun updateBackgroundStoryHint(backgroundStoryHint: String) {
        _generationState.update { it.copy(backgroundStoryHint = backgroundStoryHint) }
    }
    
    fun generateCharacter(onSuccess: (Character) -> Unit) {
        val state = _generationState.value
        
        _generationState.update { it.copy(isLoading = true, error = null) }
        
        viewModelScope.launch {
            val minAge = state.ageMin.takeIf { it > 0 }
            val maxAge = state.ageMax.takeIf { it > 0 && it >= (minAge ?: 0) }
            
            characterRepository.generateCharacter(
                nameHint = state.nameHint.takeIf { it.isNotBlank() },
                minAge = minAge,
                maxAge = maxAge,
                professionHint = state.professionHint.takeIf { it.isNotBlank() },
                backgroundStoryHint = state.backgroundStoryHint.takeIf { it.isNotBlank() }
            )
                .onSuccess { character ->
                    _generationState.update { it.copy(isLoading = false) }
                    onSuccess(character)
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

data class CharacterListState(
    val characters: List<Character> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class CharacterDetailState {
    object Loading : CharacterDetailState()
    data class Success(val character: Character) : CharacterDetailState()
    data class Error(val message: String) : CharacterDetailState()
}

data class CharacterGenerationState(
    val nameHint: String = "",
    val ageMin: Int = 0,
    val ageMax: Int = 0,
    val professionHint: String = "",
    val backgroundStoryHint: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val isFormValid: Boolean get() = true // Generation can work with empty fields
}
