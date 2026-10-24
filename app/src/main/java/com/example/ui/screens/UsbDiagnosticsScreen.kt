package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbManager
import android.os.BatteryManager
import android.os.Build
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Cable
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Usb
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SampleData
import com.example.data.model.HardwareCheckConfig
import com.example.ui.components.TvBadge
import com.example.ui.components.TvFocusCard
import com.example.ui.components.TvSectionHeader
import com.example.ui.theme.TvAccentGold
import com.example.ui.theme.TvBackground
import com.example.ui.theme.TvCardBackground
import com.example.ui.theme.TvCardBorder
import com.example.ui.theme.TvError
import com.example.ui.theme.TvPrimary
import com.example.ui.theme.TvPrimaryLight
import com.example.ui.theme.TvSecondary
import com.example.ui.theme.TvSuccess
import com.example.ui.theme.TvSurface
import com.example.ui.theme.TvSurfaceHighlight
import com.example.ui.theme.TvSurfaceVariant
import com.example.ui.theme.TvTextMuted
import com.example.ui.theme.TvTextPrimary
import com.example.ui.theme.TvTextSecondary
import com.example.ui.theme.TvWarning

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsbDiagnosticsScreen(
    onNavigateToTestingGuide: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Live Device Diagnostics
    val deviceManufacturer = Build.MANUFACTURER.replaceFirstChar { it.uppercase() }
    val deviceModel = Build.MODEL
    val androidRelease = Build.VERSION.RELEASE
    val sdkInt = Build.VERSION.SDK_INT
    val isLegacyTarget = sdkInt in 23..28 // Android 6.0 (Marshmallow) to Android 9.0 (Pie)

    // Battery & USB Power check
    val batteryStatusIntent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    val chargePlug = batteryStatusIntent?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) ?: -1
    val isUsbPlugged = chargePlug == BatteryManager.BATTERY_PLUGGED_USB
    val isAcPlugged = chargePlug == BatteryManager.BATTERY_PLUGGED_AC

    val usbManager = context.getSystemService(Context.USB_SERVICE) as? UsbManager
    val attachedDevicesCount = usbManager?.deviceList?.size ?: 0

    // Interactive Compatibility Simulator State
    var selectedPhone by remember { mutableStateOf("$deviceManufacturer $deviceModel (Detected)") }
    var selectedTvBrand by remember { mutableStateOf("Hisense / TCL / Vitron LED TV") }
    var selectedTvPort by remember { mutableStateOf("Standard USB-A Port (Media Player)") }
    var selectedCable by remember { mutableStateOf("Standard USB-A Charging & Data Cable") }

    val currentConfig = HardwareCheckConfig(
        phoneBrand = selectedPhone,
        androidVersion = sdkInt,
        tvBrand = selectedTvBrand,
        tvUsbPortType = selectedTvPort,
        cableType = selectedCable
    )

    val verdict = remember(selectedPhone, selectedTvBrand, selectedTvPort, selectedCable) {
        SampleData.evaluateUsbCompatibility(currentConfig)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(TvBackground),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Top Header
        item {
            TvSectionHeader(
                title = "USB Hardware & Electrical Diagnostics",
                subtitle = "Engineering facts, protocol inspection, and honest compatibility analysis",
                action = {
                    Button(
                        onClick = onNavigateToTestingGuide,
                        colors = ButtonDefaults.buttonColors(containerColor = TvPrimary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("View KSh 0 Setup Guide", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        // Live Phone Hardware State Card
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
                            Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = TvPrimaryLight)
                            Text(
                                text = "HOST PHONE TELEMETRY",
                                color = TvTextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        TvBadge(
                            text = if (isLegacyTarget) "TARGET HW: ANDROID 6–9 OK" else "RUNNING ON API $sdkInt",
                            color = if (isLegacyTarget) TvSuccess else TvSecondary
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        InfoMetricTile(
                            label = "DEVICE MODEL",
                            value = "$deviceManufacturer $deviceModel",
                            modifier = Modifier.weight(1f)
                        )
                        InfoMetricTile(
                            label = "ANDROID OS",
                            value = "Android $androidRelease (API $sdkInt)",
                            modifier = Modifier.weight(1f)
                        )
                        InfoMetricTile(
                            label = "USB POWER",
                            value = when {
                                isUsbPlugged -> "USB Connected (5V)"
                                isAcPlugged -> "AC Charger"
                                else -> "Battery Powered"
                            },
                            modifier = Modifier.weight(1f)
                        )
                        InfoMetricTile(
                            label = "ATTACHED OTG",
                            value = "$attachedDevicesCount Devices",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // The Harsh Technical Reality (USB-to-TV Breakdown)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = TvCardBackground),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, TvWarning.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = TvWarning)
                        Text(
                            text = "THE PHYSICAL REALITY OF TV USB PORTS",
                            color = TvWarning,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp
                        )
                    }

                    Text(
                        text = "Why standard TV USB-A ports CANNOT be turned into live video inputs by software:",
                        color = TvTextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(TvSurfaceHighlight)
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        RealityBullet(
                            title = "1. Electrical Pinout Limit",
                            body = "A standard USB-A port on a TV contains only 4 conductors (Pin 1: VBUS 5V, Pin 2: D-, Pin 3: D+, Pin 4: GND). It has no high-speed differential TMDS pairs needed for HDMI video signals."
                        )
                        RealityBullet(
                            title = "2. TV Processor Architecture",
                            body = "The budget decoder chip on the TV motherboard (MStar/MediaTek) only runs embedded storage firmware. It mounts the USB device as a Mass Storage (FAT32/NTFS) drive to read raw files. It lacks a video sink or display driver."
                        )
                        RealityBullet(
                            title = "3. Zero-Cost (KSh 0) Real Solution",
                            body = "Do NOT despair! Set your Android phone's USB mode to 'File Transfer (MTP)'. The TV can browse and play all videos, music, and movies directly from the phone. Alternatively, use Hero TV on the phone screen with a wireless mouse or old laptop ADB bridge."
                        )
                    }
                }
            }
        }

        // Comprehensive Protocol Feasibility & Standards Analysis
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
                            Icon(Icons.Default.Memory, contentDescription = null, tint = TvSecondary)
                            Text(
                                text = "USB DISPLAY PROTOCOL FEASIBILITY MATRIX",
                                color = TvTextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                        TvBadge(text = "STANDARDS AUDIT", color = TvSecondary)
                    }

                    Text(
                        text = "Technical evaluation of mobile-to-TV display technologies across Android 6–9 era hardware:",
                        color = TvTextSecondary,
                        fontSize = 12.sp
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        ProtocolCardItem(
                            name = "MHL (Mobile High-Definition Link)",
                            portType = "Micro-USB 5-pin / 11-pin",
                            status = "DEPRECATED & UNSUPPORTED ON USB-A",
                            isViable = false,
                            details = "Transmitted HDMI video over Micro-USB pins. Required MHL-licensed TV HDMI port (never TV USB). Discontinued by Qualcomm and Samsung after 2015 (Galaxy S5 era). Budget Tecno/Infinix never possessed MHL silicon."
                        )

                        ProtocolCardItem(
                            name = "SlimPort (Mobility DisplayPort)",
                            portType = "Micro-USB to DisplayPort IC",
                            status = "OBSOLETE",
                            isViable = false,
                            details = "Proprietary Analogix chip used on Nexus 4, 5, 7 and LG G2/G3/G4. Required active external powered HDMI adapter. Discontinued by all manufacturers by Android 6.0."
                        )

                        ProtocolCardItem(
                            name = "DisplayPort Alt Mode (DP Alt Mode)",
                            portType = "USB Type-C (USB 3.1+ only)",
                            status = "FLAGSHIPS ONLY (S8/S9) • NOT BUDGET PHONES",
                            isViable = false,
                            details = "Physically re-routes USB-C high-speed lanes to transmit uncompressed DisplayPort video. Available on Samsung S8/S9+, Note 8/9. NOT supported on budget Android 6-9 devices (Tecno Spark, Infinix Hot, Redmi) which have USB 2.0 controllers."
                        )

                        ProtocolCardItem(
                            name = "USB DisplayLink / Virtual Display IC",
                            portType = "USB 2.0 / USB 3.0 via External Silicon",
                            status = "COST PROHIBITIVE (KSh 5,000+)",
                            isViable = false,
                            details = "Compresses screen buffers into USB packets via DisplayLink Presenter app. Requires an expensive active powered USB hub with DisplayLink DL-3xxx chip. Violates the KSh 0 DIY budget."
                        )

                        ProtocolCardItem(
                            name = "Phone MTP Media Server (Hero TV Solution)",
                            portType = "Standard USB-A Data Cable",
                            status = "100% WORKING • KSh 0 BUDGET",
                            isViable = true,
                            details = "The TV mounts the phone as a high-capacity media drive. TV plays all local 1080p movies and music directly through its remote and speakers. Zero adapters, zero lag, 100% free."
                        )
                    }
                }
            }
        }

        // Interactive TV Compatibility Inspector
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Tv, contentDescription = null, tint = TvPrimaryLight)
                            Text(
                                text = "INTERACTIVE COMPATIBILITY INSPECTOR",
                                color = TvTextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                        TvBadge(text = "SIMULATE YOUR TV", color = TvAccentGold)
                    }

                    // Selectors Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        DropdownSelector(
                            label = "TV Brand / Model",
                            options = listOf(
                                "Hisense / TCL / Vitron LED TV",
                                "Samsung / LG Smart TV",
                                "Sony Bravia Android TV",
                                "Generic CRT / Analogue TV with USB Box"
                            ),
                            selectedOption = selectedTvBrand,
                            onOptionSelected = { selectedTvBrand = it },
                            modifier = Modifier.weight(1f)
                        )

                        DropdownSelector(
                            label = "TV USB Port Type",
                            options = listOf(
                                "Standard USB-A Port (Media Player)",
                                "USB-C with DisplayPort Alt Mode",
                                "Smart TV Android USB Host"
                            ),
                            selectedOption = selectedTvPort,
                            onOptionSelected = { selectedTvPort = it },
                            modifier = Modifier.weight(1f)
                        )

                        DropdownSelector(
                            label = "Connecting Cable",
                            options = listOf(
                                "Standard USB-A Charging & Data Cable",
                                "Micro-USB to USB-A Cable",
                                "USB-C to HDMI Converter Cable"
                            ),
                            selectedOption = selectedCable,
                            onOptionSelected = { selectedCable = it },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Live Verdict Box
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = if (verdict.canStreamRawVideoOverUsb) TvSuccess.copy(alpha = 0.12f) else TvSurfaceVariant,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (verdict.canStreamRawVideoOverUsb) TvSuccess else TvCardBorder
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
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
                                    Icon(
                                        imageVector = if (verdict.canStreamRawVideoOverUsb) Icons.Default.Check else Icons.Default.Close,
                                        contentDescription = null,
                                        tint = if (verdict.canStreamRawVideoOverUsb) TvSuccess else TvError
                                    )
                                    Text(
                                        text = verdict.title,
                                        color = if (verdict.canStreamRawVideoOverUsb) TvSuccess else TvPrimaryLight,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }

                                TvBadge(text = verdict.alternativeRating, color = TvAccentGold)
                            }

                            Text(
                                text = verdict.summary,
                                color = TvTextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Text(
                                text = verdict.technicalExplanation,
                                color = TvTextSecondary,
                                fontSize = 12.sp,
                                lineHeight = 18.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoMetricTile(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = TvSurfaceHighlight,
        border = androidx.compose.foundation.BorderStroke(1.dp, TvCardBorder)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(label, color = TvTextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, color = TvTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun RealityBullet(title: String, body: String) {
    Column {
        Text(title, color = TvPrimaryLight, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(2.dp))
        Text(body, color = TvTextSecondary, fontSize = 11.sp, lineHeight = 16.sp)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownSelector(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text(label, color = TvTextMuted, fontSize = 11.sp) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TvTextPrimary,
                unfocusedTextColor = TvTextPrimary,
                focusedBorderColor = TvPrimary,
                unfocusedBorderColor = TvCardBorder,
                focusedContainerColor = TvSurfaceHighlight,
                unfocusedContainerColor = TvSurfaceHighlight
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(TvSurface)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, color = TvTextPrimary, fontSize = 12.sp) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun ProtocolCardItem(
    name: String,
    portType: String,
    status: String,
    isViable: Boolean,
    details: String
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = TvSurfaceHighlight,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isViable) TvSuccess.copy(alpha = 0.5f) else TvCardBorder
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name,
                    color = TvTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                TvBadge(
                    text = status,
                    color = if (isViable) TvSuccess else TvError
                )
            }
            Text(
                text = "Hardware Interface: $portType",
                color = TvSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = details,
                color = TvTextSecondary,
                fontSize = 11.sp,
                lineHeight = 15.sp
            )
        }
    }
}
