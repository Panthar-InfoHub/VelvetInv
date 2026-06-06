package org.sharad.velvetinvestment.presentation.insurance

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.bgColor3
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.presentation.SplashScreen.ExpandingDots
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.ShadowCard
import org.sharad.velvetinvestment.shared.theme.Poppins
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.velvetinvestment.utils.withInterRupee
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.icon_warning

@Composable
fun SharedInsuranceScreen(
    heading: String,
    onBack: () -> Unit,
    subHeading: String,
    icon: DrawableResource,
    tint: Color,
    status: String,
    coverage: String,
    recommended: String,
    gap: String,
    infoText: String,
    image: List<DrawableResource>
) {
    val state= rememberPagerState { image.size }
    Column(
        modifier=Modifier.fillMaxSize()
    ) {
        BackHeader(heading=heading, showBack = true, onBackClick = onBack)
        LazyColumn(
            modifier=Modifier.weight(1f).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            item {
                CoverageCard(
                    heading=heading,
                    subHeading = subHeading,
                    icon = icon,
                    color = tint,
                    status = status,
                    coverage = coverage,
                    recommended = recommended,
                    gap = gap,
                )
            }
            item {
                RiskCard(infoText)
            }
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ){
                    HorizontalPager(
                        modifier = Modifier.fillMaxWidth(),
                        state = state
                    ) {
                        ImageCard(image[it])
                    }
                    ExpandingDots(
                        currentIndexOfDot = state.currentPage,
                        numberOfDots = image.size
                    )
                }
            }
        }
    }
}

@Composable
fun ImageCard(x0: DrawableResource) {
   Image(
       painter = painterResource(x0),
       contentDescription = null,
       modifier = Modifier.fillMaxWidth()
           .height(196.dp),
       contentScale = ContentScale.FillWidth
   )
}

@Composable
fun CoverageCard(
    heading: String,
    subHeading: String,
    icon: DrawableResource,
    color: Color,
    status: String,
    coverage: String,
    recommended: String,
    gap: String,
) {
    ShadowCard(
        modifier = Modifier.border(
            width = 0.75.dp,
            color = Color(0xFFE0E0E0),
            shape = RoundedCornerShape(15.dp)
        )
    )
    {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier.size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(color.copy(0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = heading,
                        fontSize = 16.sp,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = subHeading,
                        style = titlesStyle,
                        color = titleColor
                    )
                }
            }

            CoverageStatus(
                status=status,
                coverage = coverage,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ){
                RecommendedCard(modifier=Modifier.weight(1f), title = "Recommended", value=recommended,color = Color(0xffF57C00))
                RecommendedCard(modifier=Modifier.weight(1f), title = "Coverage Gap", value=gap,color = Color(0xffE53935))
            }
        }
    }
}

@Composable
fun RecommendedCard(modifier: Modifier, title: String, value: String, color: Color) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFFFF3E0))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = titlesStyle,
                color = color
            )
            Text(
                text = value.withInterRupee(),
                fontWeight = FontWeight.SemiBold,
                fontFamily = Poppins,
                fontSize = 16.sp,
                color = Primary
            )
        }
    }
}

@Composable
fun CoverageStatus(coverage: String, status: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF5F9FF))
    ){
        Column(
            modifier=Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Coverage Status",
                    color = Color(0xff273E71).copy(0.7f),
                    style = titlesStyle
                )

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(appGreen)
                ){
                    Text(
                        text = status,
                        color = Color.White,
                        style = titlesStyle.copy(fontSize = 10.sp),
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }

            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = coverage.withInterRupee(),
                    color = Primary,
                    fontFamily = Poppins,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = " Current Coverage",
                    color = Color(0xff273E71).copy(0.7f),
                    style = titlesStyle
                )
            }
        }
    }
}

@Composable
fun RiskCard(
    text:String
) {
    ShadowCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = bgColor3.copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.icon_warning),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint=Secondary
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {


                Text(
                    text =  text.withInterRupee(),
                    style = titlesStyle.copy(fontSize = 12.sp),
                    color = Primary
                )

            }
        }
    }
}