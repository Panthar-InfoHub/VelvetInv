package org.sharad.velvetinvestment.presentation.insurance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.sharad.velvetinvestment.presentation.SplashScreen.ExpandingDots
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.BarHeader
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.banner_child_1
import velvet.composeapp.generated.resources.banner_child_2
import velvet.composeapp.generated.resources.banner_child_3
import velvet.composeapp.generated.resources.banner_family_1
import velvet.composeapp.generated.resources.banner_family_2
import velvet.composeapp.generated.resources.banner_family_3
import velvet.composeapp.generated.resources.banner_senior_1
import velvet.composeapp.generated.resources.banner_senior_2
import velvet.composeapp.generated.resources.banner_senior_3

@Composable
fun OtherInsuranceScreen(
    onBackClick: () -> Unit,
){

    val familyBanner = listOf(Res.drawable.banner_family_1, Res.drawable.banner_family_2, Res.drawable.banner_family_3)
    val childBanner = listOf(Res.drawable.banner_child_1, Res.drawable.banner_child_2, Res.drawable.banner_child_3)
    val seniorBanner = listOf(Res.drawable.banner_senior_1, Res.drawable.banner_senior_2, Res.drawable.banner_senior_3)

    val familyBannerPagerState = rememberPagerState{familyBanner.size}
    val childBannerPagerState = rememberPagerState{childBanner.size}
    val seniorBannerPagerState = rememberPagerState{seniorBanner.size}


    Column(
        modifier=Modifier.fillMaxSize()
    ) {
        BackHeader(heading = "Other Insurances", showBack = true, onBackClick = onBackClick)
        LazyColumn(
            modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                BarHeader(heading="Family Floater Insurance")
            }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    HorizontalPager(
                        state = familyBannerPagerState,
                    ){
                        ImageCard(
                            x0 = familyBanner[it]
                        )
                    }
                    ExpandingDots(
                        numberOfDots = familyBanner.size,
                        currentIndexOfDot = familyBannerPagerState.currentPage
                    )
                }
            }

            item {
                BarHeader(heading="Child Insurance")
            }


            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    HorizontalPager(
                        state = childBannerPagerState,
                    ){
                        ImageCard(
                            x0 = childBanner[it]
                        )
                    }
                    ExpandingDots(
                        numberOfDots = childBanner.size,
                        currentIndexOfDot = childBannerPagerState.currentPage
                    )
                }
            }

            item {
                BarHeader(heading="Senior Citizen Health Insurance")
            }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    HorizontalPager(
                        state = seniorBannerPagerState,
                    ){
                        ImageCard(
                            x0 = seniorBanner[it]
                        )
                    }
                    ExpandingDots(
                        numberOfDots = seniorBanner.size,
                        currentIndexOfDot = seniorBannerPagerState.currentPage
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}