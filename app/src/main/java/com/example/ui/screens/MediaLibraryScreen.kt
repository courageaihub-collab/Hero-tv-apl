package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Usb
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SampleData
import com.example.data.model.MediaItem
import com.example.data.model.MediaType
import com.example.ui.components.TvBadge
import com.example.ui.components.TvFocusCard
import com.example.ui.components.TvSectionHeader
import com.example.ui.theme.TvAccentGold
import com.example.ui.theme.TvBackground
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
fun MediaLibraryScreen(
    onPlayMedia: (MediaItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var userImportedMedia by remember { mutableStateOf<List<MediaItem>>(emptyList()) }

    // SAF file picker for playing actual user files on device / USB OTG!
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            val fileName = uri.lastPathSegment?.substringAfterLast('/') ?: "Local File"
            val isAudio = fileName.endsWith(".mp3", true) || fileName.endsWith(".wav", true)
            val newMedia = MediaItem(
                id = "custom_${System.currentTimeMillis()}",
                title = fileName,
                subtitle = "User Selected Local Media",
                durationText = "Local File",
                mediaType = if (isAudio) MediaType.MUSIC else MediaType.LOCAL_VIDEO,
                category = if (isAudio) "Music" else "Local Storage",
                rating = "5.0",
                qualityBadge = "Device Storage",
                year = "Local",
                description = "File selected from device internal memory or USB OTG storage.",
                videoUri = uri.toString(),
                isFavorite = true
            )
            userImportedMedia = listOf(newMedia) + userImportedMedia
            onPlayMedia(newMedia)
        }
    }

    val allMedia = userImportedMedia + SampleData.sampleMediaItems

    val filteredMedia = allMedia.filter { item ->
        val matchesCategory = when (selectedCategory) {
            "All" -> true
            "Movies" -> item.mediaType == MediaType.MOVIE || item.mediaType == MediaType.SERIES
            "Music" -> item.mediaType == MediaType.MUSIC
            "Local / USB" -> item.mediaType == MediaType.LOCAL_VIDEO || item.mediaType == MediaType.USB_FILE || item.videoUri?.startsWith("content://") == true
            else -> true
        }
        val matchesSearch = searchQuery.isBlank() ||
                item.title.contains(searchQuery, ignoreCase = true) ||
                item.subtitle.contains(searchQuery, ignoreCase = true) ||
                item.category.contains(searchQuery, ignoreCase = true)

        matchesCategory && matchesSearch
    }

    val categories = listOf("All", "Movies", "Music", "Local / USB")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TvBackground)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header & File Picker Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TvSectionHeader(
                title = "Media Library",
                subtitle = "Explore movies, music, and local device videos",
                modifier = Modifier.weight(1f)
            )

            // Open Local Storage / USB Button
            Button(
                onClick = {
                    filePickerLauncher.launch(arrayOf("video/*", "audio/*"))
                },
                colors = ButtonDefaults.buttonColors(containerColor = TvPrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("open_local_file_button")
            ) {
                Icon(
                    imageVector = Icons.Default.FolderOpen,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Pick Local File (SAF)",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }

        // Search Bar & Filter Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search media, movies, songs...", color = TvTextMuted, fontSize = 13.sp) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = TvPrimaryLight)
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TvPrimary,
                    unfocusedBorderColor = TvCardBorder,
                    focusedTextColor = TvTextPrimary,
                    unfocusedTextColor = TvTextPrimary,
                    focusedContainerColor = TvSurface,
                    unfocusedContainerColor = TvSurface
                ),
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .testTag("media_search_input")
            )

            // Categories
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    val isSelected = category == selectedCategory
                    Surface(
                        onClick = { selectedCategory = category },
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) TvPrimary else TvSurfaceHighlight,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) TvPrimaryLight else TvCardBorder
                        ),
                        modifier = Modifier.testTag("filter_chip_$category")
                    ) {
                        Text(
                            text = category,
                            color = if (isSelected) Color.White else TvTextPrimary,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                        )
                    }
                }
            }
        }

        // Grid of Media Items
        if (filteredMedia.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.VideoLibrary,
                        contentDescription = null,
                        tint = TvTextMuted,
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        text = "No matching media found",
                        color = TvTextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Tap 'Pick Local File (SAF)' to select an MP4 or MP3 from your phone storage",
                        color = TvTextSecondary,
                        fontSize = 13.sp
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 220.dp),
                contentPadding = PaddingValues(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredMedia) { media ->
                    MediaLibraryCard(
                        media = media,
                        onClick = { onPlayMedia(media) }
                    )
                }
            }
        }
    }
}

@Composable
private fun MediaLibraryCard(
    media: MediaItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TvFocusCard(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .testTag("library_item_${media.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = when (media.mediaType) {
                            MediaType.MOVIE, MediaType.SERIES -> Icons.Default.Movie
                            MediaType.MUSIC -> Icons.Default.Audiotrack
                            MediaType.USB_FILE -> Icons.Default.Usb
                            MediaType.LOCAL_VIDEO -> Icons.Default.FolderOpen
                        },
                        contentDescription = null,
                        tint = TvPrimaryLight,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(text = media.category, color = TvSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                TvBadge(text = media.qualityBadge, color = TvAccentGold)
            }

            Column {
                Text(
                    text = media.title,
                    color = TvTextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = media.subtitle,
                    color = TvTextSecondary,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = media.durationText,
                    color = TvTextMuted,
                    fontSize = 11.sp
                )

                Surface(
                    shape = CircleShape,
                    color = TvPrimary,
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
