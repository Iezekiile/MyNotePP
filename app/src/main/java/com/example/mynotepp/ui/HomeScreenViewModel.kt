package com.example.mynotepp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotepp.data.checklist.ChecklistsRepository
import com.example.mynotepp.data.note.NotesRepository
import com.example.mynotepp.model.BaseItem
import com.example.mynotepp.model.Checklist
import com.example.mynotepp.model.Note
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
    private val notesRepository: NotesRepository,
    private val checklistsRepository: ChecklistsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        StateUI(screenState = ScreenContent.Checklists)
    )

    val uiState: StateFlow<StateUI> = _uiState.asStateFlow()

    val checklists = checklistsRepository.observeChecklists().stateIn(
        viewModelScope,
        WhileSubscribed(5000),
        emptyList()
    )

    val notes = notesRepository.observeNotes().stateIn(
        viewModelScope,
        WhileSubscribed(5000),
        emptyList()
    )

    fun toggleFavourite(id: String) {
        viewModelScope.launch {
            when (uiState.value.screenState) {
                ScreenContent.Notes -> notesRepository.toggleFavorite(id)
                ScreenContent.Checklists -> checklistsRepository.toggleFavorite(id)
            }
        }
    }

    fun addNewItem(title: String) {
        viewModelScope.launch {
            when (uiState.value.screenState) {
                ScreenContent.Notes -> notesRepository.save(notesRepository.create(title))
                ScreenContent.Checklists -> checklistsRepository.save(checklistsRepository.create(title))
            }
        }
    }

    fun deleteItem(item: BaseItem) {
        viewModelScope.launch {
            when (item) {
                is Note -> notesRepository.delete(item.id)
                is Checklist -> checklistsRepository.delete(item.id)
                else -> throw IllegalArgumentException("Unknown item type")
            }
        }
    }

    fun changeScreenContent(screenState: ScreenContent) {
        _uiState.update {
            it.copy(screenState = screenState)
        }
    }

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