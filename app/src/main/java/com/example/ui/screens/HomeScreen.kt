package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Usb
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.SampleData
import com.example.data.model.AppShortcutItem
import com.example.data.model.MediaItem
import com.example.ui.components.TvBadge
import com.example.ui.components.TvFocusCard
import com.example.ui.components.TvSectionHeader
import com.example.ui.theme.TvAccentGold
import com.example.ui.theme.TvAccentViolet
import com.example.ui.theme.TvBackground
import com.example.ui.theme.TvCardBackground
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
fun HomeScreen(
    onPlayMedia: (MediaItem) -> Unit,
    onNavigateToApps: () -> Unit,
    onNavigateToLibrary: () -> Unit,
    onNavigateToUsbDiagnostics: () -> Unit,
    modifier: Modifier = Modifier
) {
    val featuredMedia = SampleData.sampleMediaItems.first()
    val continueWatchingList = SampleData.sampleMediaItems.filter { it.progressPercent > 0f }
    val quickApps = SampleData.defaultAppShortcuts.take(6)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(TvBackground),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 1. Cinematic Hero Banner
        item {
            HeroBanner(
                media = featuredMedia,
                onPlayClick = { onPlayMedia(featuredMedia) },
                onDiagnosticsClick = onNavigateToUsbDiagnostics
            )
        }

        // 2. Hardware Engineering Notice Card (USB Reality in Living Room)
        item {
            TvFocusCard(
                onClick = onNavigateToUsbDiagnostics,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("usb_reality_notice_card")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(TvAccentViolet.copy(alpha = 0.2f))
                            .border(1.dp, TvAccentViolet, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Usb,
                            contentDescription = null,
                            tint = TvPrimaryLight,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "USB TO TV HARDWARE REALITY",
                                color = TvTextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            TvBadge(text = "CRITICAL FACT", color = TvAccentGold)
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Standard TV USB ports are Flash Storage decoders only, not HDMI video inputs. Tap to view the KSh 0 Media Server solution.",
                            color = TvTextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }

                    TvBadge(text = "DIAGNOSE >", color = TvPrimaryLight)
                }
            }
        }

        // 3. Quick Apps Row
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                TvSectionHeader(
                    title = "Quick Applications",
                    subtitle = "Installed on phone & TV utilities",
                    action = {
                        OutlinedButton(
                            onClick = onNavigateToApps,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TvPrimaryLight)
                        ) {
                            Text("All Apps", fontSize = 12.sp)
                        }
                    }
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(quickApps) { app ->
                        QuickAppCard(
                            app = app,
                            onClick = {
                                if (app.launchAction == "INTERNAL_PLAYER") {
                                    onPlayMedia(featuredMedia)
                                } else {
                                    onNavigateToApps()
                                }
                            }
                        )
                    }
                }
            }
        }

        // 4. Continue Watching Row
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                TvSectionHeader(
                    title = "Recently Played & Continue Watching",
                    subtitle = "Resume local videos & audio streams",
                    action = {
                        OutlinedButton(
                            onClick = onNavigateToLibrary,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TvSecondary)
                        ) {
                            Text("Full Library", fontSize = 12.sp)
                        }
                    }
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(continueWatchingList) { media ->
                        ContinueWatchingCard(
                            media = media,
                            onClick = { onPlayMedia(media) }
                        )
                    }
                }
            }
        }

        // 5. Featured Library Row
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                TvSectionHeader(
                    title = "Featured Entertainment",
                    subtitle = "High quality 1080p and 4K HDR media"
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(SampleData.sampleMediaItems.drop(1)) { media ->
                        MediaItemCard(
                            media = media,
                            onClick = { onPlayMedia(media) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HeroBanner(
    media: MediaItem,
    onPlayClick: () -> Unit,
    onDiagnosticsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp),
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, TvCardBorder),
        colors = CardDefaults.cardColors(containerColor = TvSurface)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.img_hero_banner),
                contentDescription = "Hero Wallpaper",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Gradient Overlays for readable text
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                TvBackground.copy(alpha = 0.95f),
                                TvBackground.copy(alpha = 0.75f),
                                Color.Transparent
                            )
                        )
                    )
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                TvBackground.copy(alpha = 0.85f)
                            )
                        )
                    )
            )

            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TvBadge(text = "HERO SPOTLIGHT", color = TvPrimaryLight)
                    TvBadge(text = media.qualityBadge, color = TvAccentGold)
                    TvBadge(text = "★ ${media.rating}", color = TvSecondary)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = media.title,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = media.description,
                    color = TvTextSecondary,
                    fontSize = 13.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp, bottom = 14.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onPlayClick,
                        colors = ButtonDefaults.buttonColors(containerColor = TvPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("hero_banner_play_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Play Feature",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    OutlinedButton(
                        onClick = onDiagnosticsClick,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, TvCardBorder)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = TvTextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "USB Compatibility",
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickAppCard(
    app: AppShortcutItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TvFocusCard(
        onClick = onClick,
        modifier = modifier
            .width(130.dp)
            .height(115.dp)
            .testTag("app_card_${app.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(TvSurfaceHighlight)
                    .border(1.dp, TvCardBorder, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when {
                        app.name.contains("Player", ignoreCase = true) -> Icons.Default.PlayArrow
                        app.name.contains("Files", ignoreCase = true) -> Icons.Default.VideoLibrary
                        else -> Icons.Default.Apps
                    },
                    contentDescription = app.name,
                    tint = TvPrimaryLight,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = app.name,
                color = TvTextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = app.category,
                color = TvTextMuted,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun ContinueWatchingCard(
    media: MediaItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TvFocusCard(
        onClick = onClick,
        modifier = modifier
            .width(220.dp)
            .height(150.dp)
            .testTag("continue_card_${media.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TvBadge(text = media.qualityBadge, color = TvAccentGold)
                Text(text = media.durationText, color = TvTextSecondary, fontSize = 11.sp)
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
                    text = media.subtitle,
                    color = TvTextSecondary,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                LinearProgressIndicator(
                    progress = { media.progressPercent },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = TvPrimary,
                    trackColor = TvSurfaceHighlight
                )
                Text(
                    text = "${(media.progressPercent * 100).toInt()}% watched",
                    color = TvTextMuted,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
private fun MediaItemCard(
    media: MediaItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TvFocusCard(
        onClick = onClick,
        modifier = modifier
            .width(190.dp)
            .height(170.dp)
            .testTag("media_card_${media.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TvBadge(text = media.category, color = TvSecondary)
                TvBadge(text = "★ ${media.rating}", color = TvAccentGold)
            }

            Column {
                Text(
                    text = media.title,
                    color = TvTextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
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
                TvBadge(text = media.qualityBadge, color = TvPrimaryLight)
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = TvPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
