package com.example.mynotepp.model

interface BaseItem {
    val id: String
    val title: String
    val isFavorite: Boolean
    val createdAt: Long
}
