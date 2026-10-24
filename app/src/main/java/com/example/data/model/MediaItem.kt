package com.example.data.model

import android.net.Uri

enum class MediaType {
    MOVIE,
    SERIES,
    MUSIC,
    LOCAL_VIDEO,
    USB_FILE
}

data class MediaItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val durationText: String,
    val durationSeconds: Long = 0L,
    val mediaType: MediaType,
    val category: String,
    val rating: String = "4.8",
    val qualityBadge: String = "1080p",
    val year: String = "2024",
    val description: String = "",
    val videoUri: String? = null,
    val isFavorite: Boolean = false,
    val progressPercent: Float = 0f // 0f to 1f
)

data class AppShortcutItem(
    val id: String,
    val packageName: String?,
    val name: String,
    val category: String,
    val isInstalled: Boolean = true,
    val isFavorite: Boolean = false,
    val isTvOptimized: Boolean = true,
    val launchAction: String? = null,
    val description: String = ""
)
