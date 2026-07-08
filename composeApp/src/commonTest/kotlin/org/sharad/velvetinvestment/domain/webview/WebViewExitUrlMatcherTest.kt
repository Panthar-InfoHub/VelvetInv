package org.sharad.velvetinvestment.domain.webview

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class WebViewExitUrlMatcherTest {

    @Test
    fun containsMatchesWhenPatternIsSubstring() {
        assertTrue(
            WebViewExitUrlMatcher.matches(
                currentUrl = "https://partner.example.com/callback?status=success",
                patterns = listOf("status=success"),
                matchType = WebViewUrlMatchType.CONTAINS
            )
        )
    }

    @Test
    fun containsIsCaseInsensitive() {
        assertTrue(
            WebViewExitUrlMatcher.matches(
                currentUrl = "https://partner.example.com/CALLBACK",
                patterns = listOf("callback"),
                matchType = WebViewUrlMatchType.CONTAINS
            )
        )
    }

    @Test
    fun startsWithRequiresPrefix() {
        val patterns = listOf("https://partner.example.com/callback")

        assertTrue(
            WebViewExitUrlMatcher.matches(
                "https://partner.example.com/callback?id=1",
                patterns,
                WebViewUrlMatchType.STARTS_WITH
            )
        )
        assertFalse(
            WebViewExitUrlMatcher.matches(
                "https://partner.example.com/other/callback",
                patterns,
                WebViewUrlMatchType.STARTS_WITH
            )
        )
    }

    @Test
    fun exactRequiresFullMatch() {
        val patterns = listOf("https://partner.example.com/done")

        assertTrue(
            WebViewExitUrlMatcher.matches(
                "https://partner.example.com/done",
                patterns,
                WebViewUrlMatchType.EXACT
            )
        )
        assertFalse(
            WebViewExitUrlMatcher.matches(
                "https://partner.example.com/done?extra=1",
                patterns,
                WebViewUrlMatchType.EXACT
            )
        )
    }

    @Test
    fun noMatchWhenPatternsEmpty() {
        assertFalse(
            WebViewExitUrlMatcher.matches(
                "https://partner.example.com/anything",
                emptyList(),
                WebViewUrlMatchType.CONTAINS
            )
        )
    }
}
