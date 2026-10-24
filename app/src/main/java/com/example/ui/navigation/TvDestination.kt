package com.example.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Usb
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.ui.graphics.vector.ImageVector

enum class TvDestination(
    val title: String,
    val icon: ImageVector,
    val description: String
) {
    HOME(
        title = "Home",
        icon = Icons.Default.Home,
        description = "Dashboard & Featured"
    ),
    MEDIA_LIBRARY(
        title = "Media",
        icon = Icons.Default.VideoLibrary,
        description = "Videos, Movies & Music"
    ),
    APPS(
        title = "Apps",
        icon = Icons.Default.Apps,
        description = "Installed Phone Applications"
    ),
    USB_DIAGNOSTICS(
        title = "USB Doctor",
        icon = Icons.Default.Usb,
        description = "Hardware & TV Reality"
    ),
    TESTING_GUIDE(
        title = "Free DIY Guide",
        icon = Icons.Default.Build,
        description = "KSh 0 Testing Walkthrough"
    ),
    SETTINGS(
        title = "Settings",
        icon = Icons.Default.Settings,
        description = "Preferences & Launcher Mode"
    ),
    WEB_PROTOTYPE(
        title = "Web Prototype",
        icon = Icons.Default.Code,
        description = "HTML5 / JS Engine View"
    )
}
