package com.example.mynotepp.model

data class ChecklistItem(
    val text: String,
    val isChecked: Boolean,
    val createdAt: Long
)