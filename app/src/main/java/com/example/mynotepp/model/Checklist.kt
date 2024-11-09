package com.example.mynotepp.model

data class Checklist(
    override val id: String = "",
    override val title: String,
    val items: List<ChecklistItem> = emptyList(),
    override val isFavorite: Boolean = false,
    override val createdAt: Long = System.currentTimeMillis()
) : BaseItem
