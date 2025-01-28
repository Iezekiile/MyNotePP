package com.example.mynotepp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Task(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val isDone: Boolean = false,
    val subtasks: List<Task> = emptyList(),
) : Parcelable

