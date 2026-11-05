package org.sharad.velvetinvestment.shared.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.velvetinvestment.shared.theme.VelvetTheme

/**
 * The logo of a fund or bundle, with an initials tile while it loads, when it fails, or when
 * there is no [iconUrl] at all — a blank url skips the request and draws [FundIconPlaceholder].
 *
 * Ported from Janta Nivesh; styled like Velvet's fund cards (soft shadow, white tile, navy initials tile).
 */
@Composable
fun FundIcon(
    iconUrl: String?,
    name: String,
    modifier: Modifier = Modifier,
    size: Dp = 38.dp,
    cornerRadius: Dp = 12.dp,
    /** How many of [name]'s leading characters the placeholder shows. */
    letterNum: Int = 2,
    /** Overrides the letters taken from [name]. */
    initials: String? = null,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Fit
) {
    val fallback: @Composable () -> Unit = {
        FundIconPlaceholder(
            name = name,
            size = size,
            cornerRadius = cornerRadius,
            letterNum = letterNum,
            initials = initials
        )
    }

    if (iconUrl.isNullOrBlank()) {
        Box(modifier = modifier) { fallback() }
        return
    }

    SubcomposeAsyncImage(
        model = iconUrl,
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier
            .size(size)
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(cornerRadius))
            .clip(RoundedCornerShape(cornerRadius))
            .background(Color.White),
        loading = { fallback() },
        error = { fallback() },
        success = { SubcomposeAsyncImageContent() }
    )
}

/** The initials tile [FundIcon] shows while there is no logo to show. */
@Composable
fun FundIconPlaceholder(
    name: String,
    modifier: Modifier = Modifier,
    size: Dp = 38.dp,
    cornerRadius: Dp = 12.dp,
    letterNum: Int = 2,
    initials: String? = null
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(cornerRadius))
            .background(Primary),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = (initials ?: name.take(letterNum)).capitalize(Locale.current),
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FundIconPreview() {
    VelvetTheme {
        FundIcon(iconUrl = null, name = "Parag Parikh Flexi Cap")
    }
}
