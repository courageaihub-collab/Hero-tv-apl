package com.example

import com.example.data.SampleData
import com.example.data.model.HardwareCheckConfig
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testStandardTvUsbEvaluatesToStorageOnly() {
        val standardConfig = HardwareCheckConfig(
            phoneBrand = "Tecno Spark / Android 7",
            androidVersion = 24,
            tvBrand = "Hisense LED TV",
            tvUsbPortType = "Standard USB-A Port (Media Player)",
            cableType = "Standard USB-A Charging & Data Cable"
        )
        val verdict = SampleData.evaluateUsbCompatibility(standardConfig)

        // Must honestly confirm TV USB port does NOT receive live screen video
        assertFalse(verdict.canStreamRawVideoOverUsb)
        // Must confirm file transfer / MTP works for free
        assertTrue(verdict.canTransferFilesMtp)
        assertTrue(verdict.alternativeRating.contains("KSh 0"))
    }

    @Test
    fun testDisplayPortUsbEvaluatesToVideoOutput() {
        val dpConfig = HardwareCheckConfig(
            phoneBrand = "Samsung Galaxy S9",
            androidVersion = 28,
            tvBrand = "Smart TV with DisplayPort",
            tvUsbPortType = "USB-C with DisplayPort Alt Mode",
            cableType = "USB-C to HDMI Converter Cable"
        )
        val verdict = SampleData.evaluateUsbCompatibility(dpConfig)

        assertTrue(verdict.canStreamRawVideoOverUsb)
        assertTrue(verdict.title.contains("Hardware Video Out Detected"))
    }

    @Test
    fun testSampleMediaIntegrity() {
        val items = SampleData.sampleMediaItems
        assertTrue("Media items must not be empty", items.isNotEmpty())
        for (item in items) {
            assertNotNull(item.id)
            assertTrue(item.title.isNotBlank())
            assertTrue(item.progressPercent in 0f..1f)
        }
    }

    @Test
    fun testAppShortcutsContainHeroPlayer() {
        val shortcuts = SampleData.defaultAppShortcuts
        val heroPlayer = shortcuts.find { it.launchAction == "INTERNAL_PLAYER" }
        assertNotNull("Hero Player internal shortcut should be present", heroPlayer)
    }
}
