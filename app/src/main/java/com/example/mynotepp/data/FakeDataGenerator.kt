package com.example.mynotepp.data

import com.example.mynotepp.model.Checklist
import com.example.mynotepp.model.ChecklistItem
import com.example.mynotepp.model.Note

object FakeDataGenerator {

    fun generateFakeNotes(count: Int): List<Note> {
        return List(count) { index ->
            Note(
                id = index.toString(),
                title = "Note #$index",
                content = "Note content #$index",
                isFavorite = index % 2 == 0,
                createdAt = System.currentTimeMillis()
            )
        }
    }

    fun generateFakeChecklists(count: Int): List<Checklist> {
        return List(count) { index ->
            Checklist(
                id = index.toString(),
                title = "Checklist #$index",
                items = generateFakeChecklistItems(5),
                isFavorite = index % 2 == 0,
                createdAt = System.currentTimeMillis()
            )
        }
    }

    private fun generateFakeChecklistItems(count: Int): List<ChecklistItem> {
        return List(count) { index ->
            ChecklistItem(
                text = "Checklist item #$index",
                isChecked = index % 2 == 0,
                createdAt = System.currentTimeMillis()
            )
        }
    }
}