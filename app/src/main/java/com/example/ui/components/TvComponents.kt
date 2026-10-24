package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SettingsRemote
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TvAccentGold
import com.example.ui.theme.TvCardBackground
import com.example.ui.theme.TvCardBorder
import com.example.ui.theme.TvFocusBorder
import com.example.ui.theme.TvFocusBorderGlow
import com.example.ui.theme.TvPrimary
import com.example.ui.theme.TvPrimaryDark
import com.example.ui.theme.TvPrimaryLight
import com.example.ui.theme.TvSecondary
import com.example.ui.theme.TvSurface
import com.example.ui.theme.TvSurfaceHighlight
import com.example.ui.theme.TvSurfaceVariant
import com.example.ui.theme.TvTextMuted
import com.example.ui.theme.TvTextPrimary
import com.example.ui.theme.TvTextSecondary
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TvFocusCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(14.dp),
    backgroundColor: Color = TvCardBackground,
    isExternalFocused: Boolean = false,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocusedByInteraction by interactionSource.collectIsFocusedAsState()
    val isFocused = isFocusedByInteraction || isExternalFocused

    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.04f else 1.0f,
        animationSpec = spring(dampingRatio = 0.75f, stiffness = 400f),
        label = "card_scale"
    )

    Card(
        modifier = modifier
            .scale(scale)
            .focusable(interactionSource = interactionSource)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = BorderStroke(
            width = if (isFocused) 2.5.dp else 1.dp,
            color = if (isFocused) TvFocusBorder else TvCardBorder
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isFocused) 10.dp else 2.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    if (isFocused) {
                        Brush.verticalGradient(
                            listOf(
                                TvSurfaceHighlight.copy(alpha = 0.7f),
                                TvCardBackground
                            )
                        )
                    } else {
                        Brush.linearGradient(
                            listOf(
                                backgroundColor,
                                backgroundColor.copy(alpha = 0.95f)
                            )
                        )
                    }
                )
        ) {
            content()
        }
    }
}

@Composable
fun TvBadge(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = TvSecondary,
    backgroundColor: Color = color.copy(alpha = 0.15f)
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(backgroundColor)
            .border(0.5.dp, color.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = text,
            color = color,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun TvSectionHeader(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    action: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                color = TvTextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.2.sp
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    color = TvTextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
        action?.invoke()
    }
}

@Composable
fun TvTopBar(
    currentDestinationTitle: String,
    onRemoteToggleClick: () -> Unit,
    isRemoteVisible: Boolean,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentTimeString by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val formatter = SimpleDateFormat("HH:mm • EEE, MMM d", Locale.getDefault())
        while (true) {
            currentTimeString = formatter.format(Date())
            delay(1000L)
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(TvSurface.copy(alpha = 0.9f))
            .border(BorderStroke(1.dp, TvCardBorder))
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // App Identity & Destination
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(TvPrimary, TvPrimaryDark)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Tv,
                    contentDescription = "Hero TV Logo",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "HERO TV",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        letterSpacing = 1.sp
                    )
                    TvBadge(text = "KSh 0 SMART TV", color = TvAccentGold)
                }
                Text(
                    text = "Section: $currentDestinationTitle",
                    color = TvTextSecondary,
                    fontSize = 11.sp
                )
            }
        }

        // Live Clock & TV Remote Toggle
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Live Sofa Clock
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = TvSurfaceVariant,
                border = BorderStroke(1.dp, TvCardBorder)
            ) {
                Text(
                    text = currentTimeString.ifEmpty { "10-Foot Living Room Mode" },
                    color = TvTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            // Unified Search Pill
            Surface(
                onClick = onSearchClick,
                shape = RoundedCornerShape(20.dp),
                color = TvSurfaceHighlight,
                border = BorderStroke(1.dp, TvCardBorder),
                modifier = Modifier.testTag("top_bar_search_button")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = TvPrimaryLight,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Search",
                        color = TvTextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Virtual Remote Toggle Pill
            Surface(
                onClick = onRemoteToggleClick,
                shape = RoundedCornerShape(20.dp),
                color = if (isRemoteVisible) TvPrimary else TvSurfaceHighlight,
                border = BorderStroke(1.dp, if (isRemoteVisible) TvPrimaryLight else TvCardBorder),
                modifier = Modifier.testTag("toggle_virtual_remote_button")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SettingsRemote,
                        contentDescription = "Remote Controller",
                        tint = if (isRemoteVisible) Color.White else TvTextPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = if (isRemoteVisible) "Hide D-Pad" else "TV Remote D-Pad",
                        color = if (isRemoteVisible) Color.White else TvTextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

/**
 * On-screen TV Remote Control simulation.
 * Allows testing full D-pad controls, OK/Select, Back, Home, and volume on touch devices or web emulator!
 */
@Composable
fun VirtualTvRemoteOverlay(
    onUp: () -> Unit,
    onDown: () -> Unit,
    onLeft: () -> Unit,
    onRight: () -> Unit,
    onSelect: () -> Unit,
    onBack: () -> Unit,
    onHome: () -> Unit,
    onPlayPause: () -> Unit,
    onVolumeUp: () -> Unit,
    onVolumeDown: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .width(230.dp)
            .shadow(16.dp, RoundedCornerShape(24.dp))
            .border(BorderStroke(2.dp, TvPrimary), RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        color = TvSurface.copy(alpha = 0.96f)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Remote Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SettingsRemote,
                        contentDescription = null,
                        tint = TvPrimaryLight,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "TV REMOTE D-PAD",
                        color = TvTextPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(
                    onClick = onClose,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Remote",
                        tint = TvTextMuted,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            // Directional Pad (Circular D-pad)
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(TvSurfaceHighlight)
                    .border(BorderStroke(1.5.dp, TvCardBorder), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                // Up button
                IconButton(
                    onClick = onUp,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 4.dp)
                        .testTag("remote_dpad_up")
                ) {
                    Icon(Icons.Default.ArrowUpward, contentDescription = "D-Pad Up", tint = TvTextPrimary)
                }

                // Down button
                IconButton(
                    onClick = onDown,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 4.dp)
                        .testTag("remote_dpad_down")
                ) {
                    Icon(Icons.Default.ArrowDownward, contentDescription = "D-Pad Down", tint = TvTextPrimary)
                }

                // Left button
                IconButton(
                    onClick = onLeft,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 4.dp)
                        .testTag("remote_dpad_left")
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "D-Pad Left", tint = TvTextPrimary)
                }

                // Right button
                IconButton(
                    onClick = onRight,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 4.dp)
                        .testTag("remote_dpad_right")
                ) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "D-Pad Right", tint = TvTextPrimary)
                }

                // Center OK / SELECT
                Surface(
                    onClick = onSelect,
                    shape = CircleShape,
                    color = TvPrimary,
                    modifier = Modifier
                        .size(46.dp)
                        .testTag("remote_dpad_select")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "OK",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }

            // Quick Actions: Back, Home, Play
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                RemoteActionPill(
                    icon = Icons.Default.ArrowBack,
                    label = "BACK",
                    onClick = onBack,
                    testTag = "remote_btn_back"
                )
                RemoteActionPill(
                    icon = Icons.Default.Home,
                    label = "HOME",
                    onClick = onHome,
                    testTag = "remote_btn_home"
                )
                RemoteActionPill(
                    icon = Icons.Default.PlayArrow,
                    label = "PLAY",
                    onClick = onPlayPause,
                    testTag = "remote_btn_play"
                )
            }

            // Volume Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RemoteActionPill(
                    icon = Icons.Default.VolumeDown,
                    label = "VOL -",
                    onClick = onVolumeDown,
                    testTag = "remote_btn_voldown"
                )
                RemoteActionPill(
                    icon = Icons.Default.VolumeUp,
                    label = "VOL +",
                    onClick = onVolumeUp,
                    testTag = "remote_btn_volup"
                )
            }
        }
    }
}

@Composable
private fun RemoteActionPill(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    testTag: String
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = TvSurfaceHighlight,
        border = BorderStroke(1.dp, TvCardBorder),
        modifier = Modifier.testTag(testTag)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = label, tint = TvTextPrimary, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.height(2.dp))
            Text(label, color = TvTextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        }
    }
}
