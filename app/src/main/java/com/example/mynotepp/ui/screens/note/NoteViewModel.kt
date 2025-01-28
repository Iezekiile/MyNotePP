package com.example.mynotepp.ui.screens.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotepp.data.note.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    val notes = noteRepository.observeNotes().stateIn(
        viewModelScope,
        WhileSubscribed(5000),
        noteRepository.getAll()
    )

    fun editContent(noteId: String, newContent: String) {
        viewModelScope.launch {
            noteRepository.editNoteContent(
                noteId,
                newContent
            )
        }
    }

    fun editTitle(noteId: String, newTitle: String) {
        viewModelScope.launch {
            noteRepository.editNoteTitle(
                noteId,
                newTitle
            )
        }
    }

    fun deleteNote(noteId: String) {
        viewModelScope.launch {
            noteRepository.delete(noteId)
        }
    }

    fun toggleFavorite(noteId: String) {
        viewModelScope.launch {
            noteRepository.toggleFavorite(noteId)
        }
    }
}