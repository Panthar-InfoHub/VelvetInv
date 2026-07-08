package org.sharad.velvetinvestment.domain.webview

import kotlinx.serialization.Serializable

@Serializable
enum class WebViewUrlMatchType {
    CONTAINS,
    STARTS_WITH,
    EXACT
}
