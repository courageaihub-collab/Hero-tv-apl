package com.example.ui.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.SampleData
import com.example.data.model.AppShortcutItem
import com.example.data.model.MediaItem
import com.example.data.model.MediaType
import com.example.ui.theme.TvAccentGold
import com.example.ui.theme.TvCardBorder
import com.example.ui.theme.TvPrimary
import com.example.ui.theme.TvPrimaryLight
import com.example.ui.theme.TvSecondary
import com.example.ui.theme.TvSurface
import com.example.ui.theme.TvSurfaceHighlight
import com.example.ui.theme.TvSurfaceVariant
import com.example.ui.theme.TvTextMuted
import com.example.ui.theme.TvTextPrimary
import com.example.ui.theme.TvTextSecondary

@Composable
fun GlobalSearchDialog(
    onDismiss: () -> Unit,
    onSelectMedia: (MediaItem) -> Unit,
    onLaunchApp: (AppShortcutItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var query by remember { mutableStateOf("") }

    val allMedia = SampleData.sampleMediaItems
    val allApps = SampleData.defaultAppShortcuts

    val matchingMedia = remember(query) {
        if (query.isBlank()) emptyList()
        else allMedia.filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.subtitle.contains(query, ignoreCase = true) ||
                    it.category.contains(query, ignoreCase = true)
        }
    }

    val matchingApps = remember(query) {
        if (query.isBlank()) emptyList()
        else allApps.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.category.contains(query, ignoreCase = true) ||
                    it.description.contains(query, ignoreCase = true)
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.88f)
                .clip(RoundedCornerShape(20.dp))
                .background(TvSurface)
                .border(androidx.compose.foundation.BorderStroke(1.5.dp, TvPrimary), RoundedCornerShape(20.dp))
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                // Header & Search Input
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = TvPrimaryLight,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Unified Hero TV Search",
                            color = TvTextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        TvBadge(text = "MEDIA & APPS", color = TvAccentGold)
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TvTextMuted)
                    }
                }

                // Search TextField
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = {
                        Text(
                            "Type movie, song, video, or app name (e.g. 'Cosmic', 'Synthwave', 'YouTube', 'Player')...",
                            color = TvTextMuted,
                            fontSize = 13.sp
                        )
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = TvPrimaryLight)
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TvPrimary,
                        unfocusedBorderColor = TvCardBorder,
                        focusedTextColor = TvTextPrimary,
                        unfocusedTextColor = TvTextPrimary,
                        focusedContainerColor = TvSurfaceHighlight,
                        unfocusedContainerColor = TvSurfaceHighlight
                    ),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("global_search_input")
                )

                // Results Container
                if (query.isBlank()) {
                    // Quick Suggestions
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Search your entire Smart TV system",
                            color = TvTextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Instant 10-foot search across local movies, music files, and installed Android apps.",
                            color = TvTextSecondary,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Cosmic", "Synthwave", "YouTube", "Android", "Wildlife").forEach { tag ->
                                Surface(
                                    onClick = { query = tag },
                                    shape = RoundedCornerShape(16.dp),
                                    color = TvSurfaceHighlight,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, TvCardBorder)
                                ) {
                                    Text(
                                        text = tag,
                                        color = TvPrimaryLight,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                } else if (matchingMedia.isEmpty() && matchingApps.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No media or applications found matching '$query'.",
                            color = TvTextSecondary,
                            fontSize = 14.sp
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Media Results
                        if (matchingMedia.isNotEmpty()) {
                            item {
                                Text(
                                    text = "MEDIA LIBRARY (${matchingMedia.size})",
                                    color = TvSecondary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                                )
                            }

                            items(matchingMedia) { media ->
                                SearchMediaRow(
                                    media = media,
                                    onClick = {
                                        onSelectMedia(media)
                                        onDismiss()
                                    }
                                )
                            }
                        }

                        // App Results
                        if (matchingApps.isNotEmpty()) {
                            item {
                                Text(
                                    text = "APPLICATION SHORTCUTS (${matchingApps.size})",
                                    color = TvAccentGold,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(top = 10.dp, bottom = 4.dp)
                                )
                            }

                            items(matchingApps) { app ->
                                SearchAppRow(
                                    app = app,
                                    onClick = {
                                        onLaunchApp(app)
                                        onDismiss()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchMediaRow(
    media: MediaItem,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = TvSurfaceHighlight,
        border = androidx.compose.foundation.BorderStroke(1.dp, TvCardBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(TvPrimary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (media.mediaType) {
                            MediaType.MUSIC -> Icons.Default.MusicNote
                            else -> Icons.Default.Movie
                        },
                        contentDescription = null,
                        tint = TvPrimaryLight,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column {
                    Text(
                        text = media.title,
                        color = TvTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${media.category} • ${media.durationText}",
                        color = TvTextSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            TvBadge(text = "PLAY ▶", color = TvPrimaryLight)
        }
    }
}

@Composable
private fun SearchAppRow(
    app: AppShortcutItem,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = TvSurfaceHighlight,
        border = androidx.compose.foundation.BorderStroke(1.dp, TvCardBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(TvSurfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Apps,
                        contentDescription = null,
                        tint = TvAccentGold,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column {
                    Text(
                        text = app.name,
                        color = TvTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${app.category} • ${app.description}",
                        color = TvTextSecondary,
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            TvBadge(text = "LAUNCH ↗", color = TvAccentGold)
        }
    }
}
