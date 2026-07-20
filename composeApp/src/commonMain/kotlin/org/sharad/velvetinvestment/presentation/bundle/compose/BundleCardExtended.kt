package org.sharad.velvetinvestment.presentation.bundle.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.domain.models.mutualfunds.BundledMutualFundDomain
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.shared.theme.LocalVelvetShapes
import org.sharad.velvetinvestment.shared.theme.Poppins
import org.sharad.velvetinvestment.shared.theme.VelvetTheme

/* ---------------------------------------------------------- */
/* Premium Palette */
/* ---------------------------------------------------------- */

object BundlePalette {

    val Equity = Primary
    val Hybrid = Color(0xFF2196F3)
    val Debt = Color(0xFF4CAF50)
    val Commodity = Secondary

    fun color(type: AssetType): Color = when (type) {
        AssetType.EQUITY -> Equity
        AssetType.HYBRID -> Hybrid
        AssetType.DEBT -> Debt
        AssetType.COMMODITY -> Commodity
    }
}

enum class AssetType(val title: String) {
    EQUITY("Equity"),
    HYBRID("Hybrid"),
    DEBT("Debt"),
    COMMODITY("Commodities")
}

data class AssetAllocationUi(
    val type: AssetType,
    val percentage: Int
)

fun BundledMutualFundDomain.assetAllocations(): List<AssetAllocationUi> {

    return listOf(
        AssetAllocationUi(AssetType.EQUITY, equityPercentage),
        AssetAllocationUi(AssetType.HYBRID, hybridPercentage),
        AssetAllocationUi(AssetType.DEBT, debtPercentage),
        AssetAllocationUi(AssetType.COMMODITY, commodityPercentage)
    ).filter { it.percentage > 0 }
}

/* ---------------------------------------------------------- */

@Composable
fun BundleCardExtended(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    bundleData: BundledMutualFundDomain
) {

    val allocations = bundleData.assetAllocations()
    val shapes = LocalVelvetShapes.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .genericDropShadow(shapes.roundedDp24)
            .clip(shapes.roundedDp24)
            .background(Color.White)
            .border(
                width = 1.dp,
                color = Color(0xFFE5E7EB),
                shape = shapes.roundedDp24
            )
            .clickable(onClick = onClick)
            .padding(20.dp)
    ) {

        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                SubcomposeAsyncImage(
                    modifier = Modifier.size(42.dp)
                        .shadow(
                            elevation = 4.dp, shape = shapes.roundedDp12
                        ),
                    model = bundleData.imgUrl,
                    contentDescription = null,
                    loading = {
                        BundleFundIcon(
                            schemeName = "V"+bundleData.bundleName.take(1),
                            size = 38.dp
                        )
                    },
                    error = {
                        BundleFundIcon(
                            schemeName = "V"+bundleData.bundleName.take(1),
                            size = 38.dp
                        )
                    },
                    success = {
                        SubcomposeAsyncImageContent()
                    }
                )

                Column(
                    modifier = Modifier.weight(1f)
                        .padding(start = 10.dp)
                ) {

                    Text(
                        text = bundleData.bundleName,
                        style = MaterialTheme.typography.labelSmall,
                        color = titleColor,
                        maxLines = 1
                    )

                    Text(
                        text = bundleData.bundleDescription,
                        style = MaterialTheme.typography.displaySmall,
                        color = titleColor,
                        maxLines = 1
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {

                    Text(
                        text = "Risk",
                        style = MaterialTheme.typography.bodySmall,
                        color = titleColor
                    )

                    Text(
                        text = bundleData.riskLevel,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
            ) {

                drawLine(
                    color = titleColor,
                    start = Offset(0f, size.height / 2),
                    end = Offset(size.width, size.height / 2),
                    strokeWidth = 2f,
                    pathEffect = PathEffect.dashPathEffect(
                        intervals = floatArrayOf(12f, 8f)
                    )
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Text(
                    text = "Distribution",
                    style = MaterialTheme.typography.displaySmall,
                    color = titleColor
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(shapes.circle)
                ) {

                    allocations.forEach {

                        Box(
                            modifier = Modifier
                                .weight(it.percentage.toFloat())
                                .fillMaxHeight()
                                .background(
                                    BundlePalette.color(it.type)
                                )
                        )
                    }
                }

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(18.dp)
                ) {

                    allocations.forEach {

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(shapes.circle)
                                    .background(
                                        BundlePalette.color(it.type)
                                    )
                            )

                            Text(
                                text = "${it.type.title} ${it.percentage}%",
                                fontFamily = Poppins,
                                fontSize = 10.sp,
                                color = titleColor
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun BundleFundIcon(
    schemeName: String,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    cornerRadius: Dp = 12.dp,
    backgroundColor: Color = Primary,
    textColor: Color = Color.White
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(cornerRadius))
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = schemeName,
            style = MaterialTheme.typography.headlineSmall,
            color = textColor
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewBundleCard() {

    VelvetTheme {

        BundleCardExtended(
            onClick = {},
            bundleData = BundledMutualFundDomain(
                id = "1",
                bundleName = "Velvet Preserve",
                bundleDescription = "Balanced wealth with lower volatility",

                equityPercentage = 70,
                hybridPercentage = 15,
                debtPercentage = 10,
                commodityPercentage = 5,

                riskLevel = "Moderate",
                investmentTime = "5+ Years",
                investmentGrowth = "Balanced"
            )
        )
    }
}
