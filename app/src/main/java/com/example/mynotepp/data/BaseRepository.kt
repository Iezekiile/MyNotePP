package com.example.mynotepp.data

interface BaseRepository<T> {
    fun getAll(): List<T>
    fun getById(id: String): T?
    fun save(item: T)
    fun delete(id: String)
    fun toggleFavorite(id: String)
    fun create(title: String): T
}