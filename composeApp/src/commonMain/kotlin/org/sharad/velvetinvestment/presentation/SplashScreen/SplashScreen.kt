package org.sharad.velvetinvestment.presentation.SplashScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.velvetinvestment.shared.AppButton
import org.sharad.velvetinvestment.utils.WindowSize
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.background_mesh
import velvet.composeapp.generated.resources.logo_app
import velvet.composeapp.generated.resources.splash_cover_1

@Composable
fun SplashScreen(windowSize: WindowSize, onGetStarted: () -> Unit) {

    Box(
        modifier=Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopStart
    ){

        LightGradient()


        when (windowSize) {
            WindowSize.PhoneLandscape -> {
                SplashScreenLandscape(
                    onGetStarted=onGetStarted
                )
            }

            WindowSize.PhonePortrait -> {
                SplashScreenPortrait(
                    onGetStarted=onGetStarted
                )
            }

            WindowSize.Tablet -> {
                SplashScreenPortrait(
                    onGetStarted=onGetStarted
                )

            }
        }
    }
}

@Composable
fun LightGradient() {
    Box(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.5f)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF91B6FF).copy(alpha = 0.4f), Color.Transparent)
                )
            ),
    )
}

@Composable
fun SplashScreenPortrait(onGetStarted: () -> Unit) {
    Box(
        modifier=Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Image(
            painter = painterResource(Res.drawable.background_mesh),
            contentDescription = "Background Mesh",
            modifier=Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SplashPane1()
            SplashPane2(
                onGetStarted=onGetStarted
            )
        }
    }
}

@Composable
fun SplashScreenLandscape(onGetStarted: () -> Unit) {

    Box(
        modifier = Modifier.fillMaxSize()
    ){

        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(Res.drawable.background_mesh),
                contentDescription = "Background Mesh",
                modifier=Modifier.weight(1f),
                contentScale = ContentScale.FillBounds
            )
            Image(
                painter = painterResource(Res.drawable.background_mesh),
                contentDescription = "Background Mesh",
                modifier = Modifier.weight(1f),
                contentScale = ContentScale.FillBounds
            )
        }

        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SplashPane1(
                modifier = Modifier.weight(1f),
                size= WindowSize.PhoneLandscape
            )
            SplashPane2(
                modifier = Modifier.weight(1f),
                onGetStarted=onGetStarted
            )
        }
    }
}

@Composable
fun SplashScreenTablet(){

}

@Composable
fun SplashPane1(
    modifier: Modifier = Modifier,
    size: WindowSize.PhoneLandscape?=null
){
    Column(
        modifier=modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(Res.drawable.logo_app),
            contentDescription ="App_Logo",
            modifier=Modifier.size(if(size== WindowSize.PhoneLandscape) 100.dp else 75.dp)
        )
        Spacer(modifier=Modifier.height(12.dp))
        Text(
            text = "Velvet Investing",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
        if (size!=WindowSize.PhoneLandscape){
            Image(
                painter = painterResource(Res.drawable.splash_cover_1),
                modifier = Modifier.fillMaxWidth().height(300.dp),
                contentDescription = "Splash_Cover",
            )
        }
    }
}

@Composable
fun SplashPane2(
    modifier: Modifier = Modifier,
    onGetStarted: () -> Unit
){
    val textList= remember{
        listOf(
            "Goals-based planning, direct mutual funds, fixed deposit all in one place .",
            "Discover a platform that merges investment strategies, savings options, and tailored financial advice.",
            "Explore a service that combines investment tactics, savings plans, and personalized financial guidance."
        )
    }

    val pagerState= rememberPagerState { textList.size }

    LaunchedEffect(Unit){
        while (true){
            delay(3000)
            val nextPage = (pagerState.currentPage + 1) % textList.size
            pagerState.animateScrollToPage(nextPage)
        }
    }


    Column(
        modifier=modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text="Master your Financial\nFuture",
            textAlign = TextAlign.Center,
            color = Secondary,
            style = MaterialTheme.typography.headlineLarge
        )

        Column(
            modifier=Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ){

            HorizontalPager(
                state = pagerState,
            ){
                Text(
                    text = textList[it],
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall
                )
            }


            ExpandingDots(
                currentIndexOfDot = pagerState.currentPage,
            )

        }

        Spacer(
            modifier=Modifier.height(8.dp)
        )

        AppButton(
            onClick = {
                onGetStarted()
            },
            text = "Get Started",
        )

    }
}

