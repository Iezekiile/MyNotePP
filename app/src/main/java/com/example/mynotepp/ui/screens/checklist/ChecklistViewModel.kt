package com.example.mynotepp.ui.screens.checklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotepp.data.checklist.ChecklistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChecklistScreenViewModel @Inject constructor(
    private val checklistRepository: ChecklistRepository
) : ViewModel() {

    val checklists = checklistRepository.observeChecklists().stateIn(
        viewModelScope,
        WhileSubscribed(5000),
        checklistRepository.getAll()
    )

    fun toggleFavourite(checklistId: String) {
        viewModelScope.launch {
            checklistRepository.toggleFavorite(checklistId)
        }
    }

    fun addTask(checklistId: String, taskText: String) {
        viewModelScope.launch {
            checklistRepository.addTask(checklistId, taskText)
        }
    }

    fun editTask(checklistId: String, taskId: String, taskText: String) {
        viewModelScope.launch {
            checklistRepository.deleteTask(checklistId, taskId)
            checklistRepository.addTask(checklistId, taskText)
        }
    }

    fun deleteTask(checklistId: String, taskId: String) {
        viewModelScope.launch {
            checklistRepository.deleteTask(checklistId, taskId)
        }
    }

    fun toggleTaskState(checklistId: String, taskId: String, state: Boolean) {
        viewModelScope.launch {
            checklistRepository.toggleTaskState(checklistId, taskId, state)
        }
    }

    fun deleteChecklist(checklistId: String) {
        viewModelScope.launch {
            checklistRepository.delete(checklistId)
        }
    }

    fun editTitle(checklistId: String, newTitle: String) {
        viewModelScope.launch {
            checklistRepository.editChecklistTitle(checklistId, newTitle)
        }
    }
}
