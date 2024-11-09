package com.example.mynotepp.model

data class Note(
    override val id: String,
    override val title: String,
    val content: String,
    override val isFavorite: Boolean = false,
    override val createdAt: Long
) : BaseItem
