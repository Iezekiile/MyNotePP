package com.example.mynotepp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotepp.data.checklist.ChecklistRepository
import com.example.mynotepp.data.note.NoteRepository
import com.example.mynotepp.model.BaseItem
import com.example.mynotepp.model.Checklist
import com.example.mynotepp.model.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

//data class MainUiState(
//    val checklists: List<Checklist> = emptyList(),
//    val notes: List<Note> = emptyList()
//)

@HiltViewModel
class MainScreenViewModel @Inject constructor (
    private val noteRepository: NoteRepository,
    private val checklistRepository: ChecklistRepository
) : ViewModel() {


    val checklists = checklistRepository.observeChecklists().stateIn(
        viewModelScope,
        WhileSubscribed(5000),
        emptySet()
    )

    fun toggleFavourite(item: BaseItem) {
        viewModelScope.launch {
            when (item) {
                is Note -> noteRepository.toggleFavorite(item.id)
                is Checklist -> checklistRepository.toggleFavorite(item.id)
                else -> throw IllegalArgumentException("Unknown item type")
            }
        }
    }

    fun addNewItem(item: BaseItem){
        viewModelScope.launch {
            when (item) {
                is Note -> noteRepository.save(item)
                is Checklist -> checklistRepository.save(item)
                else -> throw IllegalArgumentException("Unknown item type")
            }
        }
    }

    fun deleteItem(item: BaseItem){
        viewModelScope.launch {
            when (item) {
                is Note -> noteRepository.delete(item.id)
                is Checklist -> checklistRepository.delete(item.id)
                else -> throw IllegalArgumentException("Unknown item type")
            }
        }
    }



//    private val _uiState = MutableStateFlow(MainUiState())
//    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

//    init {
//        refreshAll()
//    }

//    fun <T> toggleFavorite(item: T) {
//        viewModelScope.launch {
//
//        }
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