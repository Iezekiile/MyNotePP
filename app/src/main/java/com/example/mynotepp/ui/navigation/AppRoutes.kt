package com.example.mynotepp.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
data object HomeScreenRoute

@Serializable
data class NoteScreenRoute(
    val id: String
)

@Serializable
data class ChecklistScreenRoute(
    val id: String

)