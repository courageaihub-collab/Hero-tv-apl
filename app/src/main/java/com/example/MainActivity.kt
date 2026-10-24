package com.example

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SampleData
import com.example.data.model.MediaItem
import com.example.ui.components.TvTopBar
import com.example.ui.components.VirtualTvRemoteOverlay
import com.example.ui.navigation.TvDestination
import com.example.ui.screens.AppsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MediaLibraryScreen
import com.example.ui.screens.MediaPlayerScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.TestingGuideScreen
import com.example.ui.screens.UsbDiagnosticsScreen
import com.example.ui.screens.WebPrototypeScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.TvBackground
import com.example.ui.theme.TvCardBorder
import com.example.ui.theme.TvPrimary
import com.example.ui.theme.TvPrimaryLight
import com.example.ui.theme.TvSurface
import com.example.ui.theme.TvSurfaceHighlight
import com.example.ui.theme.TvTextMuted
import com.example.ui.theme.TvTextPrimary
import com.example.ui.theme.TvTextSecondary

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Keep screen on default for living room smart TV hub experience
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContent {
            MyApplicationTheme {
                var keepScreenOn by remember { mutableStateOf(true) }

                fun updateKeepScreenOn(enabled: Boolean) {
                    keepScreenOn = enabled
                    if (enabled) {
                        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    } else {
                        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    }
                }

                HeroTvApp(
                    keepScreenOn = keepScreenOn,
                    onToggleKeepScreenOn = ::updateKeepScreenOn
                )
            }
        }
    }
}

@Composable
fun HeroTvApp(
    keepScreenOn: Boolean,
    onToggleKeepScreenOn: (Boolean) -> Unit
) {
    var currentDestination by remember { mutableStateOf(TvDestination.HOME) }
    var currentPlayingMedia by remember { mutableStateOf<MediaItem?>(null) }
    var isRemoteVisible by remember { mutableStateOf(false) }
    var isSofaScale by remember { mutableStateOf(true) }
    var isSearchDialogOpen by remember { mutableStateOf(false) }

    val context = androidx.compose.ui.platform.LocalContext.current

    // Remote D-pad triggers
    fun handleRemoteNav(direction: String) {
        val destinations = TvDestination.entries
        val currentIndex = destinations.indexOf(currentDestination)
        when (direction) {
            "NEXT" -> {
                val nextIndex = (currentIndex + 1) % destinations.size
                currentDestination = destinations[nextIndex]
            }
            "PREV" -> {
                val prevIndex = if (currentIndex > 0) currentIndex - 1 else destinations.size - 1
                currentDestination = destinations[prevIndex]
            }
            "HOME" -> {
                currentPlayingMedia = null
                currentDestination = TvDestination.HOME
            }
            "PLAY_FEATURE" -> {
                currentPlayingMedia = SampleData.sampleMediaItems.first()
            }
            "BACK" -> {
                if (currentPlayingMedia != null) {
                    currentPlayingMedia = null
                } else if (currentDestination != TvDestination.HOME) {
                    currentDestination = TvDestination.HOME
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TvBackground)
            .focusable()
            .onKeyEvent { keyEvent ->
                if (keyEvent.type == KeyEventType.KeyDown) {
                    when (keyEvent.key) {
                        Key.DirectionRight -> {
                            handleRemoteNav("NEXT")
                            true
                        }
                        Key.DirectionLeft -> {
                            handleRemoteNav("PREV")
                            true
                        }
                        Key.DirectionCenter, Key.Enter -> {
                            if (currentPlayingMedia == null) {
                                handleRemoteNav("PLAY_FEATURE")
                            }
                            true
                        }
                        Key.Back, Key.Escape -> {
                            handleRemoteNav("BACK")
                            true
                        }
                        else -> false
                    }
                } else {
                    false
                }
            }
    ) {
        // If media player is active, show full-screen player
        if (currentPlayingMedia != null) {
            MediaPlayerScreen(
                media = currentPlayingMedia!!,
                onBack = { currentPlayingMedia = null }
            )
        } else {
            // Adaptive TV Layout: Use NavigationRail on wide/landscape screens, NavigationBar on compact
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val isWide = maxWidth >= 680.dp

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = TvBackground,
                    topBar = {
                        TvTopBar(
                            currentDestinationTitle = currentDestination.title,
                            onRemoteToggleClick = { isRemoteVisible = !isRemoteVisible },
                            isRemoteVisible = isRemoteVisible,
                            onSearchClick = { isSearchDialogOpen = true },
                            modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
                        )
                    },
                    bottomBar = {
                        if (!isWide) {
                            NavigationBar(
                                containerColor = TvSurface,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(androidx.compose.foundation.BorderStroke(1.dp, TvCardBorder))
                                    .windowInsetsPadding(WindowInsets.navigationBars)
                            ) {
                                TvDestination.entries.take(5).forEach { dest ->
                                    val isSelected = currentDestination == dest
                                    NavigationBarItem(
                                        selected = isSelected,
                                        onClick = { currentDestination = dest },
                                        icon = {
                                            Icon(
                                                imageVector = dest.icon,
                                                contentDescription = dest.title,
                                                tint = if (isSelected) TvPrimaryLight else TvTextMuted
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = dest.title,
                                                color = if (isSelected) TvPrimaryLight else TvTextMuted,
                                                fontSize = 10.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            indicatorColor = TvSurfaceHighlight
                                        )
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        // Wide Screen Navigation Rail (Standard for Android TV / Tablets / Landscape)
                        if (isWide) {
                            NavigationRail(
                                containerColor = TvSurface,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .border(androidx.compose.foundation.BorderStroke(1.dp, TvCardBorder))
                            ) {
                                TvDestination.entries.forEach { dest ->
                                    val isSelected = currentDestination == dest
                                    NavigationRailItem(
                                        selected = isSelected,
                                        onClick = { currentDestination = dest },
                                        icon = {
                                            Icon(
                                                imageVector = dest.icon,
                                                contentDescription = dest.title,
                                                tint = if (isSelected) TvPrimaryLight else TvTextMuted
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = dest.title,
                                                color = if (isSelected) TvPrimaryLight else TvTextMuted,
                                                fontSize = 11.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        colors = NavigationRailItemDefaults.colors(
                                            indicatorColor = TvSurfaceHighlight
                                        ),
                                        modifier = Modifier
                                            .padding(vertical = 4.dp)
                                            .testTag("nav_rail_${dest.name}")
                                    )
                                }
                            }
                        }

                        // Main Screen Display Area
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            when (currentDestination) {
                                TvDestination.HOME -> HomeScreen(
                                    onPlayMedia = { currentPlayingMedia = it },
                                    onNavigateToApps = { currentDestination = TvDestination.APPS },
                                    onNavigateToLibrary = { currentDestination = TvDestination.MEDIA_LIBRARY },
                                    onNavigateToUsbDiagnostics = { currentDestination = TvDestination.USB_DIAGNOSTICS }
                                )
                                TvDestination.MEDIA_LIBRARY -> MediaLibraryScreen(
                                    onPlayMedia = { currentPlayingMedia = it }
                                )
                                TvDestination.APPS -> AppsScreen(
                                    onLaunchBuiltinPlayer = {
                                        currentPlayingMedia = SampleData.sampleMediaItems.first()
                                    }
                                )
                                TvDestination.USB_DIAGNOSTICS -> UsbDiagnosticsScreen(
                                    onNavigateToTestingGuide = { currentDestination = TvDestination.TESTING_GUIDE }
                                )
                                TvDestination.TESTING_GUIDE -> TestingGuideScreen()
                                TvDestination.SETTINGS -> SettingsScreen(
                                    isRemoteVisible = isRemoteVisible,
                                    onToggleRemote = { isRemoteVisible = !isRemoteVisible },
                                    keepScreenOn = keepScreenOn,
                                    onToggleKeepScreenOn = onToggleKeepScreenOn,
                                    isSofaScale = isSofaScale,
                                    onToggleSofaScale = { isSofaScale = it },
                                    onOpenWebPrototype = { currentDestination = TvDestination.WEB_PROTOTYPE }
                                )
                                TvDestination.WEB_PROTOTYPE -> WebPrototypeScreen(
                                    onBack = { currentDestination = TvDestination.HOME }
                                )
                            }
                        }
                    }
                }
            }

            // Unified Search Dialog
            if (isSearchDialogOpen) {
                com.example.ui.components.GlobalSearchDialog(
                    onDismiss = { isSearchDialogOpen = false },
                    onSelectMedia = { media ->
                        currentPlayingMedia = media
                        isSearchDialogOpen = false
                    },
                    onLaunchApp = { app ->
                        isSearchDialogOpen = false
                        if (app.launchAction == "INTERNAL_PLAYER") {
                            currentPlayingMedia = SampleData.sampleMediaItems.first()
                        } else if (app.packageName != null) {
                            val launchIntent = context.packageManager.getLaunchIntentForPackage(app.packageName)
                            if (launchIntent != null) {
                                context.startActivity(launchIntent)
                            } else {
                                android.widget.Toast.makeText(context, "${app.name} is not installed.", android.widget.Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                )
            }

            // On-screen floating Virtual TV Remote D-Pad (when enabled)
            AnimatedVisibility(
                visible = isRemoteVisible,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 24.dp, bottom = 24.dp)
            ) {
                VirtualTvRemoteOverlay(
                    onUp = { handleRemoteNav("PREV") },
                    onDown = { handleRemoteNav("NEXT") },
                    onLeft = { handleRemoteNav("PREV") },
                    onRight = { handleRemoteNav("NEXT") },
                    onSelect = { handleRemoteNav("PLAY_FEATURE") },
                    onBack = { handleRemoteNav("BACK") },
                    onHome = { handleRemoteNav("HOME") },
                    onPlayPause = {
                        if (currentPlayingMedia == null) {
                            currentPlayingMedia = SampleData.sampleMediaItems.first()
                        } else {
                            currentPlayingMedia = null
                        }
                    },
                    onVolumeUp = {},
                    onVolumeDown = {},
                    onClose = { isRemoteVisible = false }
                )
            }
        }
    }
}
