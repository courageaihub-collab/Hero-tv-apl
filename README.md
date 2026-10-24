# HERO TV — USB Android Smart TV Project

**Transform an old Android smartphone (Android 6.0–9.0) into a living room Smart TV entertainment hub.**

Target Budget: **KSh 0** (Free tools, existing cables, zero paid subscriptions).

---

## 1. Project Overview & Main Objective

HERO TV is an experimental embedded DIY project designed to give a second life to legacy Android smartphones (running Android 6.0 Marshmallow, Android 7.0 Nougat, Android 8.0 Oreo, or Android 9.0 Pie) by turning them into dedicated smart TV media systems connected to a television via USB.

### The Intended Setup:
- **Computing Unit**: Repurposed Android 6–9 smartphone.
- **Physical Link**: Standard USB cable connected to the TV's USB port.
- **Cost**: KSh 0 (using cables and phone already in the household).
- **Interface**: 10-foot sofa-viewable dark UI with deep violet accents, readable typography, and remote/D-pad navigation.

---

## 2. Critical Engineering Reality: USB-to-TV Capabilities

> ⚠️ **CRITICAL EMBEDDED NOTICE**:
> Software **cannot** magically force a standard television USB-A port to act as an uncompressed HDMI video input.

### The Physical & Electrical Reality:
1. **Pinout Limitations**: A standard USB-A port on 99% of consumer TVs (Hisense, TCL, Vitron, Samsung, LG) has only 4 pins: `VBUS (+5V)`, `D-`, `D+`, and `GND` operating at USB 2.0 (maximum theoretical 480 Mbps). It has **no TMDS / differential HDMI receiver lines**.
2. **TV Processor Firmware**: The TV's onboard decoder SoC (e.g. MStar or MediaTek) runs fixed firmware designed exclusively to read file systems (FAT32/NTFS) from flash drives. It does not possess a video sink or display driver.
3. **MTP / UMS vs Screen Mirroring**: Connecting a phone via USB mounts the phone as a **Mass Storage Device** or **MTP Media Device**. The TV reads video and music files directly from the phone's memory.

### Working Solutions for KSh 0 Budget:
- **Mode A (Direct KSh 0 - Phone as TV Media Server)**: Set the phone's USB configuration to *Transfer Files (MTP)*. The TV's built-in media browser accesses and plays all video and audio files stored on the phone, while the TV powers the phone.
- **Mode B (Zero-Cost Scrcpy Bridge)**: If you have an old laptop connected to the TV via HDMI, run open-source `scrcpy` over USB ADB to mirror the phone's display with ultra-low latency.
- **Mode C (Sofa Console Mode)**: Prop the phone on an entertainment console running HERO TV, pair an inexpensive or old USB mouse via OTG (or Bluetooth mouse), and control the 10-foot interface.

---

## 3. HERO TV System Features

1. **Dashboard & Hero Banner**: Cinematic 4K/1080p featured titles, live digital sofa clock, quick app row, continue-watching row with progress tracking.
2. **Media Player**: Full 10-foot player with custom transport HUD, timeline scrubbing, 10s seek forward/back, aspect ratio toggle (16:9, Fill, Fit), and volume controls.
3. **Local File Integration**: Storage Access Framework (SAF) integration to select and play real MP4, MKV, MP3, and WAV files from internal phone memory or USB OTG drives.
4. **Native App Launcher**: Real `PackageManager` scanning to list and launch installed Android applications on the device, with distinct indicators for TV-optimized vs touch-only apps.
5. **USB Diagnostics & Hardware Doctor**: Telemetry reporting battery, charging, and OS level; plus an interactive compatibility simulator assessing TV models, cable types, and physical protocols.
6. **Virtual TV Remote Controller**: On-screen floating D-pad with UP, DOWN, LEFT, RIGHT, OK, BACK, HOME, and volume controls for touchscreens and browser emulators.
7. **Hardware Keyboard / Remote Support**: Full key event binding for D-Pad, Arrow keys, Enter, Back, and Home keys.
8. **Kiosk / Default Launcher Mode**: `CATEGORY_HOME` and `CATEGORY_LEANBACK_LAUNCHER` declarations allowing Hero TV to replace the device's stock launcher.
9. **Standalone HTML5/JS Prototype**: Bundled directly in `app/src/main/assets/web_prototype/index.html` and launchable via the in-app Web Prototype viewer.

---

## 4. Step-by-Step Free Testing Guide

1. **Enable Developer Options**: Go to *Settings > About Phone*. Tap *Build Number* 7 times.
2. **Configure USB**: In Developer Options, enable *Stay Awake* while charging and set *Default USB Configuration* to *File Transfer (MTP)*.
3. **Connect to TV**: Plug the phone into the TV USB port using your charging cable.
4. **Select TV Input**: On your TV remote, press *Source / Input* and select *USB* or *Media Player*. Browse your phone's *DCIM*, *Movies*, or *Download* folders.
5. **Set Hero TV as Launcher**: Press the phone's Home button and choose *Hero TV > Always*.
6. **Launch & Enjoy**: Enjoy your KSh 0 repurposed smart TV media center!
