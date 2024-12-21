package com.example.mynotepp.data.note.impl

import com.example.mynotepp.data.FakeDataGenerator
import com.example.mynotepp.data.note.NotesRepository
import com.example.mynotepp.model.Note
import com.example.mynotepp.utils.IdUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotesRepositoryImpl @Inject constructor() : NotesRepository {

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

    override fun create(title: String): Note {
        return Note(
            id = IdUtils.generateUniqueId(),
            title = title,
            content = "",
            isFavorite = false,
            createdAt = System.currentTimeMillis()
        )
    }
}