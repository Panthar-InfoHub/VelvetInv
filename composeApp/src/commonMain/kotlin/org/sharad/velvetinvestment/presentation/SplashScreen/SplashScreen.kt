package org.sharad.velvetinvestment.presentation.SplashScreen

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.velvetinvestment.shared.compose.AppButton
import org.sharad.velvetinvestment.utils.PermissionCallback
import org.sharad.velvetinvestment.utils.PermissionStatus
import org.sharad.velvetinvestment.utils.PermissionType
import org.sharad.velvetinvestment.utils.WindowSize
import org.sharad.velvetinvestment.utils.createPermissionsManager
import org.sharad.velvetinvestment.utils.storage.AuthPrefs
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.background_mesh
import velvet.composeapp.generated.resources.logo_app
import velvet.composeapp.generated.resources.splash1
import velvet.composeapp.generated.resources.splash2
import velvet.composeapp.generated.resources.splash3
import velvet.composeapp.generated.resources.splash4
import kotlin.collections.listOf

@Composable
fun SplashScreen(windowSize: WindowSize, onGetStarted: () -> Unit) {

    val prefs: AuthPrefs = koinInject()
    var shouldAskPermission by remember {
        mutableStateOf(prefs.isFirstLaunch())
    }

    val permissionManager = createPermissionsManager(
        callback = object : PermissionCallback {
            override fun onPermissionStatus(
                permissionType: PermissionType,
                status: PermissionStatus
            ) {
                when (status) {
                    PermissionStatus.GRANTED -> {
                        prefs.setFirstLaunch(true)
                        shouldAskPermission = false
                    }
                    PermissionStatus.DENIED -> {
                        prefs.setFirstLaunch(true)
                        shouldAskPermission = false
                    }
                    PermissionStatus.SHOW_RATIONALE -> {
                    }
                }
            }
        }
    )

    if (shouldAskPermission) {
        permissionManager.askPermission(PermissionType.NOTIFICATION)
    }

    SplashScreenContent(
        windowSize = windowSize,
        onGetStarted = onGetStarted
    )
}

@Composable
fun SplashScreenContent(
    windowSize: WindowSize,
    onGetStarted: () -> Unit
) {

    val headList = remember {
        listOf(

            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(color = Primary)
                ) {
                    append("F.I.R.E")
                }
                append(" Report.")
            },

            buildAnnotatedString {
                append("Track your ")
                withStyle(
                    style = SpanStyle(color = Primary)
                ) {
                    append("Net worth")
                }
                append(" real time")
            },

            buildAnnotatedString {
                append("Understand ")
                withStyle(
                    style = SpanStyle(color = Primary)
                ) {
                    append("your Financial Reality")
                }
            },

            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(color = Primary)
                ) {
                    append("Save More.")
                }
                append(" Invest Better.")
            }
        )
    }
    val textList= remember{
        listOf(
            "Know when you can became financially independent with Velvet’s personalised F.I.R.E Report",
            "Velvet Show you what to focus on  and how to close you F.I.R.E gap",
            "Track your net worth, savings rate, goals, retirement path and financial gaps in one Place",
            "Take your right actions today to create the life you dream of tomorrow"
           )
    }

    val imageList= remember {
        listOf(
            Res.drawable.splash1,
            Res.drawable.splash2,
            Res.drawable.splash3,
            Res.drawable.splash4
        )
    }

    val pagerState= rememberPagerState { textList.size }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopStart
    ) {

        LightGradient()


        when (windowSize) {
            WindowSize.PhoneLandscape -> {
                SplashScreenLandscape(
                    onGetStarted = onGetStarted,
                    headList =headList,
                    pagerState =pagerState,
                    textList =textList,
                    imageList =imageList
                )
            }

            WindowSize.PhonePortrait -> {
                SplashScreenPortrait(
                    onGetStarted = onGetStarted,
                    headList =headList,
                    pagerState =pagerState,
                    textList =textList,
                    imageList =imageList
                )
            }

            WindowSize.Tablet -> {
                SplashScreenPortrait(
                    onGetStarted = onGetStarted,
                    headList =headList,
                    pagerState =pagerState,
                    textList =textList,
                    imageList =imageList
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
fun SplashScreenPortrait(
    onGetStarted: () -> Unit,
    headList: List<AnnotatedString>,
    pagerState: PagerState,
    textList: List<String>,
    imageList: List<DrawableResource>
) {
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
            SplashPane1(
                pagerState=pagerState,
                imageList=imageList
            )
            SplashPane2(
                onGetStarted = onGetStarted,
                headList =headList,
                pagerState =pagerState,
                textList =textList
            )
        }
    }
}

@Composable
fun SplashScreenLandscape(
    onGetStarted: () -> Unit,
    headList: List<AnnotatedString>,
    pagerState: PagerState,
    textList: List<String>,
    imageList: List<DrawableResource>
) {

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
                size= WindowSize.PhoneLandscape,
                imageList=imageList,
                pagerState=pagerState
            )
            SplashPane2(
                modifier = Modifier.weight(1f),
                onGetStarted =onGetStarted,
                headList =headList,
                pagerState =pagerState,
                textList =textList
            )
        }
    }
}


@Composable
fun SplashPane1(
    modifier: Modifier = Modifier,
    size: WindowSize.PhoneLandscape? = null,
    imageList: List<DrawableResource>,
    pagerState: PagerState
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
            AnimatedContent(
                targetState = pagerState.currentPage
            ){page->
                Image(
                    painter = painterResource(imageList[page]),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp).height(300.dp),
                    contentDescription = "Splash_Cover",
                )
            }
        }
    }
}

@Composable
fun SplashPane2(
    modifier: Modifier = Modifier,
    onGetStarted: () -> Unit,
    headList: List<AnnotatedString>,
    pagerState: PagerState,
    textList: List<String>
){

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

        AnimatedContent(
            targetState = pagerState.currentPage
        ){page->
            Text(
                text = headList[page],
                textAlign = TextAlign.Center,
                color = Secondary,
                style = MaterialTheme.typography.headlineLarge
            )
        }

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
                numberOfDots = 4
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
