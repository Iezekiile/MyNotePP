package com.example.mynotepp.data.note

import com.example.mynotepp.data.BaseRepository
import com.example.mynotepp.model.Note
import kotlinx.coroutines.flow.Flow


interface NotesRepository : BaseRepository<Note> {
    fun observeNotes() : Flow<List<Note>>
}
