package com.example.mynotepp.data

interface BaseItemRepository<T> {
    fun getAll(): List<T>
    fun getById(id: String): T?
    fun save(item: T)
    fun save(title: String)
    fun delete(id: String)
    fun toggleFavorite(id: String)
}