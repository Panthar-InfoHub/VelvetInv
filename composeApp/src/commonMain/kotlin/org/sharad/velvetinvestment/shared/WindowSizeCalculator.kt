package org.sharad.velvetinvestment.shared

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.window.core.layout.WindowSizeClass
import org.sharad.velvetinvestment.utils.WindowSize

@Composable
fun rememberWindowSize(): WindowSize {

    val adaptiveInfo = currentWindowAdaptiveInfo()
    val windowSizeClass = adaptiveInfo.windowSizeClass

    return remember(windowSizeClass) {

        val isTablet =
            windowSizeClass.isWidthAtLeastBreakpoint(
                WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
            )

        val isPhoneLandscape =
            !isTablet &&
                    !windowSizeClass.isHeightAtLeastBreakpoint(
                        WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND
                    )

        when {
            isTablet -> WindowSize.Tablet
            isPhoneLandscape -> WindowSize.PhoneLandscape
            else -> WindowSize.PhonePortrait
        }
    }
}
