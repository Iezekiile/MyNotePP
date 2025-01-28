package com.example.mynotepp.data.note

import com.example.mynotepp.data.BaseItemRepository
import com.example.mynotepp.model.Note
import kotlinx.coroutines.flow.Flow


interface NoteRepository : BaseItemRepository<Note> {
    fun observeNotes() : Flow<List<Note>>
    fun editNoteTitle(noteId: String, newTitle: String)
    fun editNoteContent(noteId: String, newContent: String)
}
