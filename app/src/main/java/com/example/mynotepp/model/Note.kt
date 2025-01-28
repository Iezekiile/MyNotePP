package com.example.mynotepp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Note(
    override val id: String = UUID.randomUUID().toString(),
    override val title: String,
    val content: String = "",
    override val isFavorite: Boolean = false,
    override val createdAt: Long = System.currentTimeMillis()
) : BaseItem, Parcelable
