package com.example.ui.screens

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.example.data.SampleData
import com.example.data.model.AppShortcutItem
import com.example.ui.components.TvBadge
import com.example.ui.components.TvFocusCard
import com.example.ui.components.TvSectionHeader
import com.example.ui.theme.TvAccentGold
import com.example.ui.theme.TvBackground
import com.example.ui.theme.TvCardBorder
import com.example.ui.theme.TvError
import com.example.ui.theme.TvPrimary
import com.example.ui.theme.TvPrimaryLight
import com.example.ui.theme.TvSecondary
import com.example.ui.theme.TvSuccess
import com.example.ui.theme.TvSurface
import com.example.ui.theme.TvSurfaceHighlight
import com.example.ui.theme.TvTextMuted
import com.example.ui.theme.TvTextPrimary
import com.example.ui.theme.TvTextSecondary

data class InstalledAppInfo(
    val appName: String,
    val packageName: String,
    val iconDrawable: Drawable?,
    val isSystemApp: Boolean
)

@Composable
fun AppsScreen(
    onLaunchBuiltinPlayer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val pm = context.packageManager

    var installedApps by remember { mutableStateOf<List<InstalledAppInfo>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    var filterTab by remember { mutableStateOf("All Installed") } // "All Installed", "Smart TV Curated"

    LaunchedEffect(Unit) {
        val mainIntent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val resolveInfos = pm.queryIntentActivities(mainIntent, 0)
        val apps = resolveInfos.map { ri ->
            InstalledAppInfo(
                appName = ri.loadLabel(pm).toString(),
                packageName = ri.activityInfo.packageName,
                iconDrawable = ri.loadIcon(pm),
                isSystemApp = (ri.activityInfo.applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_SYSTEM) != 0
            )
        }.sortedBy { it.appName.lowercase() }

        installedApps = apps
    }

    val filteredApps = installedApps.filter {
        searchQuery.isBlank() || it.appName.contains(searchQuery, ignoreCase = true) || it.packageName.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TvBackground)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section Header
        TvSectionHeader(
            title = "Applications & Launcher",
            subtitle = "Directly launch native phone apps or smart-TV shortcuts",
            action = {
                TvBadge(
                    text = "${installedApps.size} Installed Apps Detected",
                    color = TvSuccess
                )
            }
        )

        // Filter Tabs & Search
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search installed phone apps...", color = TvTextMuted, fontSize = 13.sp) },
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
                    .testTag("app_search_input")
            )

            // Mode Tabs
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("All Installed", "Curated TV Hub").forEach { tab ->
                    val isSelected = tab == filterTab
                    Surface(
                        onClick = { filterTab = tab },
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) TvPrimary else TvSurfaceHighlight,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) TvPrimaryLight else TvCardBorder
                        ),
                        modifier = Modifier.testTag("apps_tab_$tab")
                    ) {
                        Text(
                            text = tab,
                            color = if (isSelected) Color.White else TvTextPrimary,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                        )
                    }
                }
            }
        }

        // Grid Content
        if (filterTab == "Curated TV Hub") {
            // Curated shortcuts from SampleData
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 220.dp),
                contentPadding = PaddingValues(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(SampleData.defaultAppShortcuts) { shortcut ->
                    CuratedAppCard(
                        shortcut = shortcut,
                        onLaunch = {
                            if (shortcut.launchAction == "INTERNAL_PLAYER") {
                                onLaunchBuiltinPlayer()
                            } else if (shortcut.packageName != null) {
                                val launchIntent = pm.getLaunchIntentForPackage(shortcut.packageName)
                                if (launchIntent != null) {
                                    context.startActivity(launchIntent)
                                } else {
                                    // Honest notice: Not installed
                                    Toast.makeText(
                                        context,
                                        "${shortcut.name} is not installed on this device.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    // Try opening Play Store
                                    try {
                                        val storeIntent = Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("market://details?id=${shortcut.packageName}")
                                        )
                                        context.startActivity(storeIntent)
                                    } catch (e: Exception) {
                                        // Ignore if no store
                                    }
                                }
                            }
                        }
                    )
                }
            }
        } else {
            // Real Native Installed Apps Grid
            if (filteredApps.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No matching apps found.", color = TvTextSecondary)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 180.dp),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredApps) { app ->
                        InstalledAppCard(
                            app = app,
                            onLaunch = {
                                val launchIntent = pm.getLaunchIntentForPackage(app.packageName)
                                if (launchIntent != null) {
                                    context.startActivity(launchIntent)
                                } else {
                                    Toast.makeText(context, "Cannot launch ${app.appName}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InstalledAppCard(
    app: InstalledAppInfo,
    onLaunch: () -> Unit,
    modifier: Modifier = Modifier
) {
    TvFocusCard(
        onClick = onLaunch,
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp)
            .testTag("installed_app_${app.packageName}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(TvSurfaceHighlight),
                contentAlignment = Alignment.Center
            ) {
                if (app.iconDrawable != null) {
                    val bitmap = remember(app.iconDrawable) {
                        try {
                            app.iconDrawable.toBitmap(96, 96)
                        } catch (e: Exception) {
                            null
                        }
                    }
                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = app.appName,
                            modifier = Modifier.size(42.dp)
                        )
                    } else {
                        Icon(Icons.Default.Apps, contentDescription = null, tint = TvPrimaryLight)
                    }
                } else {
                    Icon(Icons.Default.Apps, contentDescription = null, tint = TvPrimaryLight)
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = app.appName,
                    color = TvTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = if (app.isSystemApp) "System App" else "User Installed",
                    color = TvTextMuted,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
private fun CuratedAppCard(
    shortcut: AppShortcutItem,
    onLaunch: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val pm = context.packageManager
    val isActuallyInstalled = remember(shortcut.packageName) {
        if (shortcut.packageName == null) true
        else pm.getLaunchIntentForPackage(shortcut.packageName) != null
    }

    TvFocusCard(
        onClick = onLaunch,
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .testTag("curated_app_${shortcut.id}")
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
                TvBadge(text = shortcut.category, color = TvSecondary)
                if (isActuallyInstalled) {
                    TvBadge(text = "INSTALLED", color = TvSuccess)
                } else {
                    TvBadge(text = "NOT ON DEVICE", color = TvError)
                }
            }

            Column {
                Text(
                    text = shortcut.name,
                    color = TvTextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = shortcut.description,
                    color = TvTextSecondary,
                    fontSize = 11.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (shortcut.isTvOptimized) "TV Remote Ready" else "Touch/Mouse Required",
                    color = TvTextMuted,
                    fontSize = 10.sp
                )

                Surface(
                    shape = CircleShape,
                    color = if (isActuallyInstalled) TvPrimary else TvSurfaceHighlight,
                    modifier = Modifier.size(28.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (isActuallyInstalled) Icons.Default.Launch else Icons.Default.ErrorOutline,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
