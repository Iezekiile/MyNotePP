package com.example.mynotepp.data.checklist

import com.example.mynotepp.data.BaseItemRepository
import com.example.mynotepp.model.Checklist
import kotlinx.coroutines.flow.Flow

interface ChecklistRepository : BaseItemRepository<Checklist> {
    fun observeChecklists() : Flow<List<Checklist>>
    fun deleteTask(checklistId: String, taskId: String)
    fun addTask(checklistId: String, title: String)
    fun toggleTaskState(checklistId: String, taskId: String, state: Boolean)
    fun editChecklistTitle(checklistId: String, newTitle: String)
}