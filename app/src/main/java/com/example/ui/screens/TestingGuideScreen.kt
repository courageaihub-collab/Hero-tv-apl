package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Hardware
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.TvBadge
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

data class GuideStep(
    val id: String,
    val stepNumber: Int,
    val title: String,
    val budget: String,
    val instruction: String,
    val technicalNote: String
)

@Composable
fun TestingGuideScreen(
    modifier: Modifier = Modifier
) {
    val completedSteps = remember { mutableStateMapOf<String, Boolean>() }

    val guideSteps = listOf(
        GuideStep(
            id = "step_1",
            stepNumber = 1,
            title = "Prepare Old Android 6–9 Smartphone",
            budget = "KSh 0",
            instruction = "Open Android Settings > About Phone. Tap 'Build Number' 7 times to enable Developer Options. In Developer Options, enable 'Stay Awake' and set 'Default USB Configuration' to 'File Transfer / MTP'.",
            technicalNote = "This guarantees that as soon as you connect the phone to the TV's USB port, the phone exposes its storage file tree rather than locking into 'Charge Only'."
        ),
        GuideStep(
            id = "step_2",
            stepNumber = 2,
            title = "Physical USB Cable Connection",
            budget = "KSh 0 (Use Existing Cable)",
            instruction = "Plug the phone into the TV USB port using your ordinary charging/data cable. Ensure the cable is a 4-wire data cable, not a power-only charging wire.",
            technicalNote = "Look at the TV screen: on most Hisense, TCL, Samsung, and Vitron TVs, an on-screen dialog will pop up saying 'USB Device Connected'."
        ),
        GuideStep(
            id = "step_3",
            stepNumber = 3,
            title = "Browse Phone Media Directly on TV (Media Server Mode)",
            budget = "KSh 0",
            instruction = "Take your television remote control. Switch input source to 'USB' or 'Media Player'. Select 'Movies' or 'Music'. You can now play all local video and audio stored on your phone through the TV speakers and screen!",
            technicalNote = "The TV's onboard MStar SoC plays the files directly. The phone's battery charges from the TV's 5V USB port while acting as an energy-efficient media server."
        ),
        GuideStep(
            id = "step_4",
            stepNumber = 4,
            title = "Living Room Sofa Navigation (KSh 0)",
            budget = "KSh 0",
            instruction = "To control Hero TV on the phone from the sofa, connect an old wired USB mouse to the phone using an OTG adapter, or pair a wireless Bluetooth mouse. Alternatively, use the built-in Hero TV on-screen Virtual D-Pad Remote.",
            technicalNote = "Android natively supports USB mice, keyboards, and game controllers via HID drivers without installing any drivers."
        ),
        GuideStep(
            id = "step_5",
            stepNumber = 5,
            title = "Set Hero TV as Default Home Launcher",
            budget = "KSh 0",
            instruction = "Press the phone's physical Home button. In the Android prompt, select 'Hero TV' and tap 'Always'. Now the old phone behaves like a dedicated Smart TV box every time it turns on.",
            technicalNote = "AndroidManifest declares CATEGORY_HOME and CATEGORY_LEANBACK_LAUNCHER, allowing complete kiosk replacement of the phone's stock launcher."
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(TvBackground),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            TvSectionHeader(
                title = "Step-by-Step DIY Free Testing Guide",
                subtitle = "Complete instructions for converting your old Android 6-9 phone into a living room media hub",
                action = {
                    TvBadge(text = "TOTAL BUDGET: KSh 0", color = TvAccentGold)
                }
            )
        }

        // Hardware Limitations & Unsupported Features Table
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
                    Text(
                        text = "HONEST HARDWARE CAPABILITIES MATRIX",
                        color = TvTextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        CapabilityPill(
                            title = "Local Media Storage Server",
                            status = "100% SUPPORTED (KSh 0)",
                            isSupported = true,
                            modifier = Modifier.weight(1f)
                        )
                        CapabilityPill(
                            title = "Full TV Launcher UI (Sofa)",
                            status = "100% SUPPORTED (KSh 0)",
                            isSupported = true,
                            modifier = Modifier.weight(1f)
                        )
                        CapabilityPill(
                            title = "Raw Video Stream Over USB-A",
                            status = "IMPOSSIBLE (HW LIMIT)",
                            isSupported = false,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Step by Step Checklist
        item {
            Text(
                text = "INTERACTIVE CONVERSION CHECKLIST",
                color = TvTextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        items(guideSteps.size) { index ->
            val step = guideSteps[index]
            val isChecked = completedSteps[step.id] == true

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { completedSteps[step.id] = !isChecked }
                    .testTag("checklist_step_${step.id}"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isChecked) TvSurfaceHighlight else TvCardBackground
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isChecked) TvSuccess else TvCardBorder
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { completedSteps[step.id] = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = TvSuccess,
                            uncheckedColor = TvTextMuted,
                            checkmarkColor = Color.White
                        )
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "STEP ${step.stepNumber}: ${step.title}",
                                color = if (isChecked) TvSuccess else TvTextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            TvBadge(text = step.budget, color = TvAccentGold)
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = step.instruction,
                            color = TvTextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = TvSurfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(0.5.dp, TvCardBorder)
                        ) {
                            Text(
                                text = "Engineering Note: ${step.technicalNote}",
                                color = TvTextMuted,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CapabilityPill(
    title: String,
    status: String,
    isSupported: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = TvSurfaceHighlight,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSupported) TvSuccess.copy(alpha = 0.5f) else TvError.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = if (isSupported) Icons.Default.Check else Icons.Default.Close,
                    contentDescription = null,
                    tint = if (isSupported) TvSuccess else TvError,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = title,
                    color = TvTextPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = status,
                color = if (isSupported) TvSuccess else TvError,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
