package com.example.mynotepp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotepp.data.checklist.ChecklistRepository
import com.example.mynotepp.data.note.NoteRepository
import com.example.mynotepp.model.BaseItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ScreenContent {
    Checklists, Notes
}

data class StateUI(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val screenState: ScreenContent = ScreenContent.Checklists
)

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val checklistRepository: ChecklistRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        StateUI()
    )
    val uiState: StateFlow<StateUI> = _uiState.asStateFlow()

    val checklists = checklistRepository.observeChecklists().stateIn(
        viewModelScope,
        WhileSubscribed(5000),
        emptyList()
    )

    val notes = noteRepository.observeNotes().stateIn(
        viewModelScope,
        WhileSubscribed(5000),
        emptyList()
    )

    fun toggleFavourite(item: BaseItem) {
        viewModelScope.launch {
            when (uiState.value.screenState) {
                ScreenContent.Notes -> noteRepository.toggleFavorite(item.id)
                ScreenContent.Checklists -> checklistRepository.toggleFavorite(item.id)
            }
        }
    }
    
    fun addNewItem(title: String) {
        viewModelScope.launch {
            when (uiState.value.screenState) {
                ScreenContent.Notes -> noteRepository.save(title)
                ScreenContent.Checklists -> checklistRepository.save(title)
            }
        }
    }

    fun deleteItem(item: BaseItem) {
        viewModelScope.launch {
            when (uiState.value.screenState) {
                ScreenContent.Notes  -> noteRepository.delete(item.id)
                ScreenContent.Checklists -> checklistRepository.delete(item.id)
            }
        }
    }

    fun toggleScreenState() {
        viewModelScope.launch {
            _uiState.update { state ->
                when (uiState.value.screenState) {
                    ScreenContent.Checklists -> {
                        state.copy(screenState = ScreenContent.Notes)
                    }
                    ScreenContent.Notes -> {
                        state.copy(screenState = ScreenContent.Checklists)
                    }
                }
            }
        }
    }


//    private val _uiState = MutableStateFlow(MainUiState())
//    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

//    init {
//        refreshAll()
//    }


//    private fun refreshAll() {
//        //    _uiState.update { it.copy(loading = true) }
//
//        viewModelScope.launch {
//            // Trigger repository requests in parallel
////            val topicsDeferred = async { interestsRepository.getTopics() }
////            val peopleDeferred = async { interestsRepository.getPeople() }
////            val publicationsDeferred = async { interestsRepository.getPublications() }
//
//            // Wait for all requests to finish
////            val topics = topicsDeferred.await().successOr(emptyList())
////            val people = peopleDeferred.await().successOr(emptyList())
////            val publications = publicationsDeferred.await().successOr(emptyList())
//
//
//            _uiState.update {
//                it.copy(
//                    checklists = checklistRepository.getAll()
//                )
//            }
//        }
//    }
}