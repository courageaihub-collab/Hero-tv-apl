package com.example.data.model

data class UsbCompatibilityVerdict(
    val title: String,
    val canStreamRawVideoOverUsb: Boolean,
    val canTransferFilesMtp: Boolean,
    val summary: String,
    val technicalExplanation: String,
    val recommendation: String,
    val alternativeRating: String // "100% Free / KSh 0", "Needs OTG (KSh 150)", etc.
)

data class HardwareCheckConfig(
    val phoneBrand: String,
    val androidVersion: Int, // e.g. 6, 7, 8, 9, 10, 11+
    val tvBrand: String,
    val tvUsbPortType: String, // "Standard USB-A (Flash drive only)", "USB-C DisplayPort", "Smart TV USB with Android"
    val cableType: String // "Standard Micro-USB charging cable", "USB-C to USB-A cable", "USB-C to HDMI"
)
