package com.example.data

import com.example.data.model.AppShortcutItem
import com.example.data.model.HardwareCheckConfig
import com.example.data.model.MediaItem
import com.example.data.model.MediaType
import com.example.data.model.UsbCompatibilityVerdict

object SampleData {

    val sampleMediaItems = listOf(
        MediaItem(
            id = "media_1",
            title = "Cosmic Horizon: Deep Space Odyssey",
            subtitle = "Sci-Fi Documentary • 4K HDR",
            durationText = "1h 48m",
            durationSeconds = 6480,
            mediaType = MediaType.MOVIE,
            category = "Sci-Fi",
            rating = "4.9",
            qualityBadge = "4K HDR",
            year = "2024",
            description = "Journey through mysterious purple nebulae, quantum gravitational waves, and distant exoplanets in this visually stunning 10-foot experience.",
            videoUri = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            isFavorite = true,
            progressPercent = 0.42f
        ),
        MediaItem(
            id = "media_2",
            title = "Cyber Synthwave: Live in Neo-Tokyo",
            subtitle = "Electronic Music Concert",
            durationText = "52m",
            durationSeconds = 3120,
            mediaType = MediaType.MUSIC,
            category = "Music",
            rating = "4.8",
            qualityBadge = "Dolby Audio",
            year = "2023",
            description = "Pulsating analog synthesizer basslines, retro-futuristic purple neon lasers, and high-energy 80s drum machines.",
            videoUri = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
            isFavorite = true,
            progressPercent = 0.78f
        ),
        MediaItem(
            id = "media_3",
            title = "The Android Matrix: DIY Embedded Systems",
            subtitle = "Tech Documentary • 1080p",
            durationText = "34m",
            durationSeconds = 2040,
            mediaType = MediaType.LOCAL_VIDEO,
            category = "Tech",
            rating = "4.9",
            qualityBadge = "1080p 60fps",
            year = "2024",
            description = "How to repurpose legacy smartphones running Android 6.0 Marshmallow through Android 9.0 Pie into living room smart media hubs.",
            videoUri = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            isFavorite = true,
            progressPercent = 0.15f
        ),
        MediaItem(
            id = "media_4",
            title = "African Wildlife: Sunset on the Mara",
            subtitle = "Nature Special • 4K Ultra",
            durationText = "1h 15m",
            durationSeconds = 4500,
            mediaType = MediaType.MOVIE,
            category = "Nature",
            rating = "4.7",
            qualityBadge = "4K 60fps",
            year = "2023",
            description = "High-definition footage of Kenya's Maasai Mara savanna during the golden hour, captured with cinema-grade telephoto lenses.",
            videoUri = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
            isFavorite = false,
            progressPercent = 0.0f
        ),
        MediaItem(
            id = "media_5",
            title = "Retro Arcade Legends: 16-Bit Era",
            subtitle = "Gaming History Series",
            durationText = "45m",
            durationSeconds = 2700,
            mediaType = MediaType.SERIES,
            category = "Gaming",
            rating = "4.6",
            qualityBadge = "1080p",
            year = "2022",
            description = "The rise of arcade gaming, side-scrolling platformers, and chiptune sound tracks that shaped modern interactive media.",
            videoUri = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
            isFavorite = true,
            progressPercent = 0.60f
        ),
        MediaItem(
            id = "media_6",
            title = "Lo-Fi Beats for Living Room Relaxing",
            subtitle = "Continuous Ambient Audio Stream",
            durationText = "2h 10m",
            durationSeconds = 7800,
            mediaType = MediaType.MUSIC,
            category = "Music",
            rating = "4.9",
            qualityBadge = "FLAC 24-bit",
            year = "2024",
            description = "Chill beats to study, relax, or have running as an ambient sofa screensaver on your TV.",
            videoUri = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
            isFavorite = false,
            progressPercent = 0.20f
        )
    )

    val defaultAppShortcuts = listOf(
        AppShortcutItem(
            id = "app_media_player",
            packageName = null,
            name = "Hero Player",
            category = "Media",
            isInstalled = true,
            isFavorite = true,
            isTvOptimized = true,
            launchAction = "INTERNAL_PLAYER",
            description = "Built-in hardware accelerated 10-foot media player"
        ),
        AppShortcutItem(
            id = "app_youtube",
            packageName = "com.google.android.youtube",
            name = "YouTube",
            category = "Streaming",
            isInstalled = true,
            isFavorite = true,
            isTvOptimized = true,
            description = "Watch videos and livestreams on the big screen"
        ),
        AppShortcutItem(
            id = "app_netflix",
            packageName = "com.netflix.mediaclient",
            name = "Netflix",
            category = "Streaming",
            isInstalled = false,
            isFavorite = true,
            isTvOptimized = true,
            description = "Movies and TV shows"
        ),
        AppShortcutItem(
            id = "app_files",
            packageName = "com.google.android.documentsui",
            name = "File Explorer",
            category = "Utilities",
            isInstalled = true,
            isFavorite = true,
            isTvOptimized = false,
            description = "Browse phone internal memory and USB OTG storage"
        ),
        AppShortcutItem(
            id = "app_chrome",
            packageName = "com.android.chrome",
            name = "Web Browser",
            category = "Utilities",
            isInstalled = true,
            isFavorite = false,
            isTvOptimized = false,
            description = "Surf the web and stream online media"
        ),
        AppShortcutItem(
            id = "app_gallery",
            packageName = "com.google.android.apps.photos",
            name = "Photo Slideshow",
            category = "Media",
            isInstalled = true,
            isFavorite = false,
            isTvOptimized = true,
            description = "Display personal photos as a digital photo frame"
        ),
        AppShortcutItem(
            id = "app_spotify",
            packageName = "com.spotify.music",
            name = "Spotify Music",
            category = "Streaming",
            isInstalled = false,
            isFavorite = false,
            isTvOptimized = true,
            description = "Stream millions of songs and podcasts"
        ),
        AppShortcutItem(
            id = "app_vlc",
            packageName = "org.videolan.vlc",
            name = "VLC Media",
            category = "Media",
            isInstalled = false,
            isFavorite = true,
            isTvOptimized = true,
            description = "Plays all video codecs, subtitles, and audio"
        ),
        AppShortcutItem(
            id = "app_settings",
            packageName = "com.android.settings",
            name = "System Settings",
            category = "System",
            isInstalled = true,
            isFavorite = false,
            isTvOptimized = false,
            description = "Wi-Fi, Bluetooth, Display, and Developer Options"
        )
    )

    fun evaluateUsbCompatibility(config: HardwareCheckConfig): UsbCompatibilityVerdict {
        val isTypeCDisplayPort = config.tvUsbPortType.contains("DisplayPort", ignoreCase = true)
        val isSmartTvWithApp = config.tvUsbPortType.contains("Smart TV", ignoreCase = true)

        if (isTypeCDisplayPort && config.cableType.contains("HDMI", ignoreCase = true)) {
            return UsbCompatibilityVerdict(
                title = "Hardware Video Out Detected (DisplayPort / MHL)",
                canStreamRawVideoOverUsb = true,
                canTransferFilesMtp = true,
                summary = "Your hardware setup physically routes video signals over USB-C or HDMI adapter.",
                technicalExplanation = "DisplayPort Alternate Mode utilizes high-speed differential pairs in USB Type-C to transmit native uncompressed HDMI/DP digital video signals directly to the TV receiver IC.",
                recommendation = "You have native video out! Hero TV will display full screen at 60fps on your TV.",
                alternativeRating = "Requires USB-C DP Alt Mode Cable (approx. KSh 700 - 1,200)"
            )
        }

        // Standard USB-A TV Port (99% of TVs in Kenya / worldwide)
        return UsbCompatibilityVerdict(
            title = "TV USB is Storage Host Only (No Direct Video Input)",
            canStreamRawVideoOverUsb = false,
            canTransferFilesMtp = true,
            summary = "Crucial Engineering Fact: Standard TV USB-A ports DO NOT accept live video signals from smartphones.",
            technicalExplanation = """
1. THE PHYSICAL REALITY:
A standard TV USB-A port is connected to a budget media decoder SoC (MStar/MediaTek/Realtek). Its firmware is programmed strictly as a "USB Mass Storage / MTP Host". It treats anything plugged in as a static flash thumb drive (reading MP4, MKV, MP3, JPEG).

2. WHY IT CANNOT RECEIVE PHONE SCREEN:
The TV USB port has only 4 pins (VBUS 5V, D-, D+, GND) operating at USB 2.0 (480 Mbps). The TV motherboard lacks an HDMI receiver or video decoder on those pins. Even with custom software, code on the phone cannot force the TV's processor to decode live Android desktop framebuffers over USB-A.

3. WHAT DOES WORK 100% FREE (KSh 0 BUDGET):
• Phone as Media Server (MTP / UMS): Set phone USB Mode to "File Transfer / MTP". The TV's built-in media browser can open and play movies/music stored on the phone!
• Free PC/Laptop Bridge with Scrcpy: If you have an old laptop connected to the TV via HDMI, you run free open-source 'scrcpy' over USB to stream the phone's screen with ultra-low latency!
• Miracast / Wi-Fi Direct: If the TV is a smart TV, cast wire-free with zero additional cables.
            """.trimIndent(),
            recommendation = "Use the KSh 0 Phone Media Hub mode (browse phone files on TV) or run the Hero TV UI directly on an old tablet/phone mounted next to the TV.",
            alternativeRating = "100% Free / KSh 0 (Working Solution)"
        )
    }
}
