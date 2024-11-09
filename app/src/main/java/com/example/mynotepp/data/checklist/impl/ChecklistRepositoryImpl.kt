package com.example.mynotepp.data.checklist.impl

import com.example.mynotepp.data.FakeDataGenerator
import com.example.mynotepp.data.checklist.ChecklistRepository
import com.example.mynotepp.model.Checklist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChecklistRepositoryImpl @Inject constructor() : ChecklistRepository {

    private val checklistsMutableState =
        MutableStateFlow(FakeDataGenerator.generateFakeChecklists(8))

    override fun observeChecklists(): Flow<List<Checklist>> = checklistsMutableState

    override fun getAll(): List<Checklist> {
        return checklistsMutableState.value
    }

    override fun getById(id: String): Checklist? {
        return checklistsMutableState.value.find { it.id == id }
    }

    override fun save(item: Checklist) {
        checklistsMutableState.update { currentList ->
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
        checklistsMutableState.update { currentList ->
            currentList.filter { it.id != id }
        }
    }

    override fun toggleFavorite(id: String) {
        checklistsMutableState.update { currentList ->
            currentList.map { checklist ->
                if (checklist.id == id) {
                    checklist.copy(isFavorite = !checklist.isFavorite)
                } else {
                    checklist
                }
            }
        }
    }
}