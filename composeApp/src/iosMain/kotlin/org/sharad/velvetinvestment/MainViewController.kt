package org.sharad.velvetinvestment

import androidx.compose.ui.window.ComposeUIViewController
import org.sharad.velvetinvestment.utils.di.initializeKoin

fun MainViewController() = ComposeUIViewController(
configure ={ initializeKoin() }
) { App() }