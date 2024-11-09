package com.example.mynotepp.data.checklist

import com.example.mynotepp.data.BaseRepository
import com.example.mynotepp.model.Checklist
import kotlinx.coroutines.flow.Flow

interface ChecklistRepository : BaseRepository<Checklist> {
    fun observeChecklists() : Flow<List<Checklist>>
}