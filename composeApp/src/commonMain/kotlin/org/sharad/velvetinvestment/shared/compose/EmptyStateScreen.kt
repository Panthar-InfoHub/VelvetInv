package org.sharad.velvetinvestment.shared.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.theme.subHeading
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.ic_empty_notification

/**
 * JN's `EmptyFundScreen` (title, subtitle, one action) in Velvet's look: Velvet's empty-state
 * illustration — the one `presentation/portfolio/compose/EmptyFundScreen` shows — with Velvet text
 * styles and button underneath. JN's icon-on-a-halo badge is not carried over.
 *
 * [buttonText] blank hides the button.
 */
@Composable
fun EmptyFundScreen(
    title: String,
    subtitle: String,
    buttonText: String,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_empty_notification),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 40.dp),
                contentScale = ContentScale.FillWidth
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = title,
                style = subHeading,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = subtitle,
                style = titlesStyle,
                color = titleColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            if (buttonText.isNotBlank()) {
                Spacer(Modifier.height(32.dp))
                AppButton(
                    text = buttonText,
                    onClick = onActionClick,
                    modifier = Modifier.fillMaxWidth(),
                    style = AppButtonDefaults.style(shape = RoundedCornerShape(12.dp)),
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
private fun EmptyFundScreenPreview() {
    VelvetTheme {
        EmptyFundScreen(
            title = "No orders yet",
            subtitle = "Every purchase and redemption you place will appear here, with its status and units.",
            buttonText = "Go Back",
            onActionClick = {}
        )
    }
}
