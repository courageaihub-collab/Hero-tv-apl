package com.example.ui.screens

import android.media.MediaPlayer
import android.net.Uri
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.VideoView
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.data.model.MediaItem
import com.example.ui.components.TvBadge
import com.example.ui.theme.TvAccentGold
import com.example.ui.theme.TvBackground
import com.example.ui.theme.TvPrimary
import com.example.ui.theme.TvPrimaryLight
import com.example.ui.theme.TvSecondary
import com.example.ui.theme.TvSurface
import com.example.ui.theme.TvTextPrimary
import com.example.ui.theme.TvTextSecondary
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun MediaPlayerScreen(
    media: MediaItem,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(true) }
    var showControls by remember { mutableStateOf(true) }
    var currentPositionMs by remember { mutableIntStateOf(0) }
    var durationMs by remember { mutableIntStateOf(0) }
    var isMuted by remember { mutableStateOf(false) }
    var aspectMode by remember { mutableStateOf("16:9 Fit") }

    var videoViewRef by remember { mutableStateOf<VideoView?>(null) }

    // Intercept hardware Back button to exit player gracefully
    BackHandler {
        onBack()
    }

    // Auto-hide controls after 4 seconds of playback
    LaunchedEffect(showControls, isPlaying) {
        if (showControls && isPlaying) {
            delay(4000L)
            showControls = false
        }
    }

    // Continuous timeline ticker
    LaunchedEffect(videoViewRef, isPlaying) {
        while (true) {
            videoViewRef?.let { vv ->
                if (vv.isPlaying) {
                    currentPositionMs = vv.currentPosition
                    if (durationMs <= 0 && vv.duration > 0) {
                        durationMs = vv.duration
                    }
                }
            }
            delay(500L)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            videoViewRef?.stopPlayback()
        }
    }

    fun formatTime(ms: Int): String {
        val totalSeconds = (ms / 1000).coerceAtLeast(0)
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(TvBackground)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                showControls = !showControls
            }
            .testTag("media_player_container")
    ) {
        // Video View Hardware Host
        AndroidView(
            factory = { ctx ->
                VideoView(ctx).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    media.videoUri?.let { uriStr ->
                        setVideoURI(Uri.parse(uriStr))
                    }
                    setOnPreparedListener { mp ->
                        durationMs = mp.duration
                        mp.isLooping = true
                        start()
                        isPlaying = true
                    }
                    setOnErrorListener { _, _, _ ->
                        // In case network stream is unavailable in sandbox, continue graceful UI
                        true
                    }
                    videoViewRef = this
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // TV Player Controls HUD Overlay
        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Black.copy(alpha = 0.85f),
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.90f)
                            )
                        )
                    )
            ) {
                // Top Navigation Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Surface(
                            onClick = onBack,
                            shape = CircleShape,
                            color = TvSurface.copy(alpha = 0.8f),
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Exit Player",
                                    tint = TvTextPrimary
                                )
                            }
                        }

                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = media.title,
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                TvBadge(text = media.qualityBadge, color = TvAccentGold)
                            }
                            Text(
                                text = media.subtitle,
                                color = TvTextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }

                    TvBadge(text = "USB PLAYBACK ENGINE", color = TvSecondary)
                }

                // Center Play/Pause Indicator
                Surface(
                    onClick = {
                        videoViewRef?.let { vv ->
                            if (vv.isPlaying) {
                                vv.pause()
                                isPlaying = false
                            } else {
                                vv.start()
                                isPlaying = true
                            }
                        }
                    },
                    shape = CircleShape,
                    color = TvPrimary.copy(alpha = 0.85f),
                    modifier = Modifier
                        .size(72.dp)
                        .align(Alignment.Center)
                        .testTag("center_play_pause_button")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = Color.White,
                            modifier = Modifier.size(38.dp)
                        )
                    }
                }

                // Bottom Timeline and Transport Controls
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 32.dp, vertical = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Timeline Slider & Counters
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = formatTime(currentPositionMs),
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Slider(
                            value = if (durationMs > 0) currentPositionMs.toFloat() / durationMs else 0f,
                            onValueChange = { fraction ->
                                val targetMs = (fraction * durationMs).toInt()
                                currentPositionMs = targetMs
                                videoViewRef?.seekTo(targetMs)
                            },
                            colors = SliderDefaults.colors(
                                thumbColor = TvPrimaryLight,
                                activeTrackColor = TvPrimary,
                                inactiveTrackColor = Color.White.copy(alpha = 0.25f)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 16.dp)
                                .testTag("player_seek_slider")
                        )

                        Text(
                            text = if (durationMs > 0) formatTime(durationMs) else media.durationText,
                            color = TvTextSecondary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Transport Actions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Aspect Ratio & Mute
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Surface(
                                onClick = {
                                    aspectMode = when (aspectMode) {
                                        "16:9 Fit" -> "Fill Screen"
                                        "Fill Screen" -> "Original"
                                        else -> "16:9 Fit"
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                color = TvSurface.copy(alpha = 0.8f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AspectRatio,
                                        contentDescription = null,
                                        tint = TvTextPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = aspectMode,
                                        color = TvTextPrimary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            IconButton(
                                onClick = { isMuted = !isMuted }
                            ) {
                                Icon(
                                    imageVector = if (isMuted) Icons.Default.VolumeMute else Icons.Default.VolumeUp,
                                    contentDescription = "Mute",
                                    tint = if (isMuted) Color.Red else Color.White
                                )
                            }
                        }

                        // Playback Buttons: -10s, Play/Pause, +10s
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            // Rewind 10s
                            IconButton(
                                onClick = {
                                    val newPos = (currentPositionMs - 10000).coerceAtLeast(0)
                                    videoViewRef?.seekTo(newPos)
                                    currentPositionMs = newPos
                                },
                                modifier = Modifier.size(44.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FastRewind,
                                    contentDescription = "Rewind 10s",
                                    tint = Color.White,
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            // Play / Pause
                            Surface(
                                onClick = {
                                    videoViewRef?.let { vv ->
                                        if (vv.isPlaying) {
                                            vv.pause()
                                            isPlaying = false
                                        } else {
                                            vv.start()
                                            isPlaying = true
                                        }
                                    }
                                },
                                shape = CircleShape,
                                color = TvPrimary,
                                modifier = Modifier.size(54.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = "Play/Pause",
                                        tint = Color.White,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }

                            // Forward 10s
                            IconButton(
                                onClick = {
                                    val newPos = (currentPositionMs + 10000).coerceAtMost(durationMs)
                                    videoViewRef?.seekTo(newPos)
                                    currentPositionMs = newPos
                                },
                                modifier = Modifier.size(44.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FastForward,
                                    contentDescription = "Fast Forward 10s",
                                    tint = Color.White,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }

                        // Close Player Button
                        Surface(
                            onClick = onBack,
                            shape = RoundedCornerShape(12.dp),
                            color = TvPrimary.copy(alpha = 0.2f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, TvPrimary)
                        ) {
                            Text(
                                text = "Exit Player",
                                color = TvPrimaryLight,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
