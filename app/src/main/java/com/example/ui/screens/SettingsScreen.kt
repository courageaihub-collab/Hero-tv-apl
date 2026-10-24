package com.example.ui.screens

import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.SettingsRemote
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.TvBadge
import com.example.ui.components.TvSectionHeader
import com.example.ui.theme.TvAccentGold
import com.example.ui.theme.TvBackground
import com.example.ui.theme.TvCardBorder
import com.example.ui.theme.TvPrimary
import com.example.ui.theme.TvPrimaryLight
import com.example.ui.theme.TvSecondary
import com.example.ui.theme.TvSuccess
import com.example.ui.theme.TvSurface
import com.example.ui.theme.TvSurfaceHighlight
import com.example.ui.theme.TvTextMuted
import com.example.ui.theme.TvTextPrimary
import com.example.ui.theme.TvTextSecondary

@Composable
fun SettingsScreen(
    isRemoteVisible: Boolean,
    onToggleRemote: () -> Unit,
    keepScreenOn: Boolean,
    onToggleKeepScreenOn: (Boolean) -> Unit,
    isSofaScale: Boolean,
    onToggleSofaScale: (Boolean) -> Unit,
    onOpenWebPrototype: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(TvBackground),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            TvSectionHeader(
                title = "Hero TV System Settings",
                subtitle = "Living room display configurations, launcher modes, and prototype options"
            )
        }

        // 1. Display & Living Room Ergonomics
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = TvSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, TvCardBorder)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "DISPLAY & LIVING ROOM ERGONOMICS",
                        color = TvTextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )

                    // Keep Screen Awake
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Keep Screen Awake (No Sleep)", color = TvTextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Prevents phone screen from turning off while acting as a living room TV hub", color = TvTextSecondary, fontSize = 12.sp)
                        }
                        Switch(
                            checked = keepScreenOn,
                            onCheckedChange = onToggleKeepScreenOn,
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = TvPrimary)
                        )
                    }

                    // Sofa 10-foot scale
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("10-Foot Sofa Typography Mode", color = TvTextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Enlarges cards, typography, and contrast for viewing from a 3-meter distance", color = TvTextSecondary, fontSize = 12.sp)
                        }
                        Switch(
                            checked = isSofaScale,
                            onCheckedChange = onToggleSofaScale,
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = TvPrimary)
                        )
                    }

                    // Virtual D-pad remote toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("On-Screen TV Remote D-Pad", color = TvTextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Show floating D-pad overlay to test sofa navigation with touch/mouse", color = TvTextSecondary, fontSize = 12.sp)
                        }
                        Switch(
                            checked = isRemoteVisible,
                            onCheckedChange = { onToggleRemote() },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = TvPrimary)
                        )
                    }
                }
            }
        }

        // 2. Kiosk & Default Launcher Setup
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = TvSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, TvCardBorder)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Home, contentDescription = null, tint = TvPrimaryLight)
                            Text(
                                text = "KIOSK & HOME LAUNCHER MODE",
                                color = TvTextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                        TvBadge(text = "ANDROID 6–9 SUPPORTED", color = TvSuccess)
                    }

                    Text(
                        text = "To make an old Android phone behave like a dedicated TV box, set Hero TV as the default Home app. Whenever the device is powered on, it boots straight into this dashboard.",
                        color = TvTextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )

                    Button(
                        onClick = {
                            try {
                                val intent = Intent(Settings.ACTION_HOME_SETTINGS)
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                try {
                                    val intent = Intent(Settings.ACTION_SETTINGS)
                                    context.startActivity(intent)
                                } catch (e2: Exception) {
                                    Toast.makeText(context, "Open Android Settings > Apps > Default Apps > Home App", Toast.LENGTH_LONG).show()
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = TvPrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("set_default_launcher_button")
                    ) {
                        Icon(Icons.Default.Launch, contentDescription = null, modifier = Modifier.padding(end = 6.dp))
                        Text("Configure Default Home App in Android Settings", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 3. Web Prototype Integration
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = TvSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, TvCardBorder)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Code, contentDescription = null, tint = TvSecondary)
                        Text(
                            text = "STANDALONE HTML5/JS WEB PROTOTYPE",
                            color = TvTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    Text(
                        text = "Experience the lightweight web prototype running directly inside the app's integrated WebView. Complete with keyboard event bindings, CSS glassmorphism, and responsive sofa layout.",
                        color = TvTextSecondary,
                        fontSize = 12.sp
                    )

                    Button(
                        onClick = onOpenWebPrototype,
                        colors = ButtonDefaults.buttonColors(containerColor = TvSecondary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("launch_web_prototype_button")
                    ) {
                        Text("Launch Embedded Web Prototype", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        // 4. Android Packaging & Build Recommendation
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = TvSurfaceHighlight),
                border = androidx.compose.foundation.BorderStroke(1.dp, TvCardBorder)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "ANDROID PACKAGING & TARGET ARCHITECTURE",
                        color = TvAccentGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )

                    Text(
                        text = """
• Min SDK: API 24 (Compatible down to Android 7.0 Nougat; can be backported to API 23 Marshmallow).
• Native APK Footprint: Lightweight Jetpack Compose build with zero heavyweight bloat.
• Target Architecture: armeabi-v7a & arm64-v8a to run efficiently on 1GB - 2GB RAM legacy smartphones.
• Zero Cloud Dependency: Operates 100% offline with zero subscription or paid APIs required.
                        """.trimIndent(),
                        color = TvTextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}
