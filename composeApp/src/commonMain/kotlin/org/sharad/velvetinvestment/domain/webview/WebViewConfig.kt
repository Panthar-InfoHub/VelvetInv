package org.sharad.velvetinvestment.domain.webview

import kotlinx.serialization.Serializable

@Serializable
data class WebViewConfig(
    val url: String,
    val exitUrlPatterns: List<String> = emptyList(),
    val matchType: WebViewUrlMatchType = WebViewUrlMatchType.CONTAINS,
    val title: String? = null
)
