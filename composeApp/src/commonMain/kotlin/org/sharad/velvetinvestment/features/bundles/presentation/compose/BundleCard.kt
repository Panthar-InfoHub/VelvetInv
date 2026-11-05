package org.sharad.velvetinvestment.features.bundles.presentation.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.features.bundles.domain.models.AssetAllocationDomain
import org.sharad.velvetinvestment.features.bundles.domain.models.BundleCategorySummaryDomain
import org.sharad.velvetinvestment.features.bundles.domain.models.BundleMetaDataDomain
import org.sharad.velvetinvestment.features.bundles.domain.models.BundleSummaryDomain
import org.sharad.velvetinvestment.features.core.domain.models.PurchaseMode
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.shared.theme.LocalVelvetShapes
import org.sharad.velvetinvestment.shared.theme.Poppins
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.theme.buttonTextStyle
import org.sharad.velvetinvestment.shared.theme.tinyLabel
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.ic_bundle_card_fund
import velvet.composeapp.generated.resources.ic_jagged_arrow
import velvet.composeapp.generated.resources.ic_pointer_right
import velvet.composeapp.generated.resources.rupee_icon

/** Velvet's bundle-card outline, used when the API sends no border colour. */
private val BundleCardDefaultBorder = Color(0xFFE5E7EB)

/** The bundle's border colour from the API, or Velvet's neutral card outline when it sent none. */
val BundleSummaryDomain.cardBorderColor: Color
    get() = borderColor?.let { Color(it) } ?: BundleCardDefaultBorder

/**
 * The frame every bundle card shares: Velvet's white card with the bundle's banner drawn over it
 * at [BANNER_ALPHA], bordered in the bundle's colour. A blank or failed image leaves it plain white.
 */
@Composable
fun BundleCardContainer(
    bundle: BundleSummaryDomain,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = LocalVelvetShapes.current.roundedDp24

    Box(
        modifier = modifier
            .genericDropShadow(shape)
            .clip(shape)
            .background(Color.White)
            .border(1.dp, bundle.cardBorderColor, shape)
            .clickable(onClick = onClick)
    ) {
        if (bundle.imageUrl.isNotBlank()) {
            AsyncImage(
                model = bundle.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alpha = BANNER_ALPHA,
                modifier = Modifier.matchParentSize()
            )
        }
        Column(
            modifier = Modifier.padding(20.dp),
            content = content
        )
    }
}

private const val BANNER_ALPHA = 0.5f

@Composable
fun BundleCard(
    bundle: BundleSummaryDomain,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BundleCardContainer(
        bundle = bundle,
        onClick = onClick,
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(
                text = bundle.name,
                style = MaterialTheme.typography.headlineLarge,
                color = Color.Black
            )
            if (bundle.description.isNotBlank()) {
                Text(
                    text = bundle.description,
                    style = tinyLabel,
                    color = titleColor
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            BundleInfoRow(
                icon = painterResource(Res.drawable.ic_bundle_card_fund),
                iconTint = appGreen,
                iconBackground = appGreen.copy(alpha = 0.1f)
            ) {
                Text(
                    text = "${bundle.fundCount} Mutual Funds",
                    style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black
                )
                if (bundle.assetClassLabel.isNotBlank()) {
                    Text(
                        text = bundle.assetClassLabel,
                        style = tinyLabel,
                        color = titleColor
                    )
                }
            }

            val monthlyStartAmount = bundle.startAmountFor(PurchaseMode.MONTHLY)
            if (monthlyStartAmount != null) {
                BundleInfoRow(
                    icon = painterResource(Res.drawable.rupee_icon),
                    iconTint = Primary,
                    iconBackground = Primary.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = "Start From",
                        style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                        color = titleColor
                    )
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = Color.Black
                                )
                            ) {
                                append("₹")
                                append(monthlyStartAmount.toString())
                            }
                            withStyle(SpanStyle(fontSize = 11.sp)) {
                                append("/month")
                            }
                        },
                        style = tinyLabel,
                        color = titleColor
                    )
                }
            }

            BundleInfoRow(
                icon = painterResource(Res.drawable.ic_jagged_arrow),
                iconTint = appGreen,
                iconBackground = appGreen.copy(alpha = 0.1f)
            ) {
                Text(
                    text = "3Y RETURN",
                    style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                    color = titleColor
                )
                Text(
                    text = bundle.metaData.investmentGrowth,
                    style = tinyLabel,
                    color = appGreen
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 12.dp)
                .height(1.dp)
                .background(bundle.cardBorderColor.copy(alpha = 0.4f))
        )

        Button(
            onClick = onClick,
            shape = LocalVelvetShapes.current.roundedDp12,
            colors = ButtonDefaults.buttonColors(containerColor = Primary, contentColor = Color.White),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Text(
                text = "View Bundle",
                style = buttonTextStyle.copy(fontSize = 14.sp)
            )
            Icon(
                painter = painterResource(Res.drawable.ic_pointer_right),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.padding(start = 10.dp).size(16.dp)
            )
        }
    }
}

@Composable
private fun BundleInfoRow(
    icon: Painter,
    iconTint: Color,
    iconBackground: Color,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(LocalVelvetShapes.current.circle)
                .background(iconBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(12.dp)
            )
        }
        Column { content() }
    }
}

internal val previewBundleSummary = BundleSummaryDomain(
    id = "1",
    name = "Equity + (Gold & Silver)",
    description = "Velvet Equity + (Gold & Silver)",
    imageUrl = "",
    borderColor = 0xFFD2B077,
    assetAllocation = AssetAllocationDomain(equity = 70.0, debt = 0.0, hybrid = 5.0, commodity = 25.0),
    metaData = BundleMetaDataDomain(
        riskLevel = "High",
        investmentTime = "",
        investmentGrowth = "6-8% p.a.",
        startAmount = 1000L,
        dailyStartAmount = 50L,
        monthlyStartAmount = 500L
    ),
    categories = listOf(
        BundleCategorySummaryDomain("c1", "mid_cap", "Mid Cap", 25.0, slotCount = 2),
        BundleCategorySummaryDomain("c2", "multi_cap", "Multi Cap", 10.0, slotCount = 1),
        BundleCategorySummaryDomain("c3", "global_others", "Commodity", 25.0, slotCount = 2)
    )
)

@Preview(showBackground = true)
@Composable
private fun BundleCardPreview() {
    VelvetTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            listOf(0xFFD2B077, 0xFF273E71, null).forEach { border ->
                BundleCard(
                    bundle = previewBundleSummary.copy(borderColor = border),
                    onClick = {}
                )
            }
        }
    }
}
