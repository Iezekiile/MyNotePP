package com.example.mynotepp.data.note.impl

import com.example.mynotepp.data.FakeDataGenerator
import com.example.mynotepp.data.note.NoteRepository
import com.example.mynotepp.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepositoryImpl @Inject constructor() : NoteRepository {

    private val notesMutableState =
        MutableStateFlow(FakeDataGenerator.generateFakeNotes(8))

    override fun observeNotes(): Flow<List<Note>> = notesMutableState

    override fun getAll(): List<Note> {
        return notesMutableState.value
    }

    override fun getById(id: String): Note? {
        return notesMutableState.value.find { it.id == id }
    }

    override fun save(item: Note) {
        notesMutableState.update { currentList ->
            val updatedList = currentList.toMutableList()
            val existingIndex = updatedList.indexOfFirst { it.id == item.id }
            if (existingIndex >= 0) {
                updatedList[existingIndex] = item
            } else {
                updatedList.add(item)
            }
            updatedList
        }
    }

    override fun delete(id: String) {
        notesMutableState.update { currentList ->
            currentList.filter { it.id != id }
        }
    }

    override fun toggleFavorite(id: String) {
        notesMutableState.update { currentList ->
            currentList.map { note ->
                if (note.id == id) {
                    note.copy(isFavorite = !note.isFavorite)
                } else {
                    note
                }
            }
        }
    }
}