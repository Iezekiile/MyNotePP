package com.example.mynotepp.utils

import java.util.UUID

object IdUtils {
    fun generateUniqueId(): String {
        return UUID.randomUUID().toString()
    }
}