package org.sharad.velvetinvestment.utils

import androidx.compose.runtime.Composable

@Composable
expect fun AppBackHandler(enabled: Boolean, onBack: () -> Unit)