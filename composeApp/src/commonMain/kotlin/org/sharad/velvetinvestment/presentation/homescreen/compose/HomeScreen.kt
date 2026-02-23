package org.sharad.velvetinvestment.presentation.homescreen.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.bgColor4
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.domain.models.home.FireReportSummaryDomain
import org.sharad.velvetinvestment.domain.models.home.GoalsSummaryDomain
import org.sharad.velvetinvestment.domain.models.home.KYCCompletion
import org.sharad.velvetinvestment.domain.models.home.UserWorthCardDomain
import org.sharad.velvetinvestment.presentation.homescreen.HomeScreenViewModel
import org.sharad.velvetinvestment.shared.compose.BarHeader
import org.sharad.velvetinvestment.shared.compose.CircleButton
import org.sharad.velvetinvestment.shared.compose.DotCapCircularProgress
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.GoalEntryCard
import org.sharad.velvetinvestment.shared.compose.GradientBackground
import org.sharad.velvetinvestment.shared.compose.LoaderScreen
import org.sharad.velvetinvestment.utils.UIState
import org.sharad.velvetinvestment.utils.dottedBorder
import org.sharad.velvetinvestment.utils.genericDropShadow
import org.sharad.velvetinvestment.utils.theme.subHeading
import org.sharad.velvetinvestment.utils.theme.titlesStyle
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.arrow_right
import velvet.composeapp.generated.resources.expenses_icon
import velvet.composeapp.generated.resources.fire_icon
import velvet.composeapp.generated.resources.notification_icon
import velvet.composeapp.generated.resources.plus_icon
import velvet.composeapp.generated.resources.settings_icon

@Composable
fun HomeScreenMain(
    viewModel: HomeScreenViewModel,
    pv: PaddingValues
){

    val screenState by viewModel.homeUIState.collectAsStateWithLifecycle()
    val name by viewModel.name.collectAsStateWithLifecycle()
    val netWorth by viewModel.netWorthInfo.collectAsStateWithLifecycle()
    val kyc by viewModel.kycProcess.collectAsStateWithLifecycle()
    val fireReport by viewModel.fireReport.collectAsStateWithLifecycle()
    val goals by viewModel.goalsInfo.collectAsStateWithLifecycle()


    Box(
        modifier=Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        GradientBackground()
        when (screenState) {
            is UIState.Error -> {
                ErrorScreen((screenState as UIState.Error).error)
            }
            UIState.Loading -> {
                LoaderScreen()
            }
            UIState.Success -> {
                HomeScreen(
                    name=name,
                    netWorth=netWorth,
                    kyc=kyc,
                    fireReport=fireReport,
                    goals=goals,
                    onNotificationIconClick={},
                    onSettingsIconClick = {},
                    pv=pv
                )
            }
        }
    }
}


@Composable
fun HomeScreen(
    name: String,
    netWorth: UserWorthCardDomain?,
    kyc: KYCCompletion?,
    fireReport: FireReportSummaryDomain?,
    goals: List<GoalsSummaryDomain>,
    onNotificationIconClick: () -> Unit,
    onSettingsIconClick: () -> Unit,
    pv: PaddingValues
) {

    LazyColumn(
        modifier=Modifier.fillMaxSize()
            .padding( start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item { Spacer(modifier=Modifier.height(16.dp,)) }
        item{ UserSettingsHeader(name = name,onSettingsIconClick=onSettingsIconClick, onNotificationIconClick=onNotificationIconClick) }
        item{ UserWorthCard(netWorth=netWorth, onInvestingRateClick={})}
        item{ BarHeader(heading = "Finish Setting Up Account") }
        item{ KYCCard(kyc=kyc, onClick={}) }
        if (goals.isEmpty()){ item { FirstGoalCard(onClick={}) } }
        item { FireReportHeader(fireReport?.setupStep?:0,fireReport?.totalStep?:8) }
        item { FireReportCard(fireReport, onClick={}) }
        item { BarHeader(heading = "Why Invest with Velvet?") }
        item {
            FeatureNavigationCards(
                onBeatInflationClick = {},
                onExpertPickedClick = {},
                onStartSmallClick = {},
                onDiversifiedClick = {}
            )
        }
        if (goals.isNotEmpty()) {
            item { BarHeader(heading = "Your Goals", showArrow = true, onArrowClick = {}) }
            homeGoalsInfo(goals = goals) { id ->

            }
            item{
                AddCustomGoalCard(onClick={})
            }
        }

        item {
            Spacer(modifier=Modifier.height(pv.calculateBottomPadding()))
        }
    }

}

@Composable
fun AddCustomGoalCard(onClick: () -> Unit) {
    Box(
        modifier=Modifier.fillMaxWidth()
            .genericDropShadow()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .dottedBorder(color = Color(0xff858585), cornerRadius = 10.dp, strokeWidth = 1.dp)
            .clickable(
                onClick=onClick
            )
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp)
        ) {

            Box(modifier = Modifier.size(48.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(bgColor4.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center){
                Icon(
                    painter = painterResource(Res.drawable.plus_icon),
                    contentDescription = null,
                    tint = bgColor4,
                    modifier = Modifier.height(24.dp)
                )
            }

            Column(
                modifier=Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text="Custom Goal",
                    style = subHeading,
                    color = Color.Black
                )
                Text(
                    text="Create your own personalized goal",
                    style=titlesStyle,
                    color = titleColor
                )
            }

        }
    }
}


fun LazyListScope.homeGoalsInfo(goals: List<GoalsSummaryDomain>, onClick:(String)-> Unit) {
    items(goals){
        GoalEntryCard(goal = it, onClick = {onClick(it.goalId)})
    }
}




@Composable
fun FireReportCard(summary: FireReportSummaryDomain?, onClick: () -> Unit) {
    Row(
        modifier=Modifier.fillMaxWidth()
            .height(80.dp)
            .genericDropShadow()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White,RoundedCornerShape(12.dp))
            .clickable(onClick=onClick),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(modifier=Modifier
            .padding(start = 16.dp)
            .size(44.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xff273E71).copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ){
            Icon(
                painter = painterResource(Res.drawable.fire_icon),
                contentDescription = null,
                tint = Color(0xff273E71),
                modifier = Modifier.width(22.dp)
            )
        }

        Column(
            modifier = Modifier.weight(1f)
                .fillMaxWidth()
        ){
            Text(
                text = "Current F.I.R.E Number",
                style = subHeading,
                color = Primary,
            )

            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically){
                Icon(
                    painter = painterResource(Res.drawable.expenses_icon),
                    contentDescription = null,
                    tint = appGreen,
                    modifier = Modifier.padding(end = 4.dp).width(15.dp)
                )
                Text(
                    text="+${summary?.annualGrowth?:0}%",
                    style = titlesStyle,
                    color = appGreen,
                )
                Text(
                    text=" annual growth",
                    style = titlesStyle,
                    color = Color.Black,
                )

            }

        }
        DotCapCircularProgress(
            modifier = Modifier.size(44.dp),
            percentage = summary?.fireNumber?.toFloat()?:0f,
            color = Secondary,
            textColor = Primary
        )

        Icon(
            painter = painterResource(Res.drawable.arrow_right),
            contentDescription = null,
            tint = Primary,
            modifier = Modifier.padding(end = 16.dp).height(24.dp)
        )

    }
}

@Composable
fun FireReportHeader(setupStep: Int, totalStep: Int) {
    Column(
        modifier=Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Box(
                modifier = Modifier.fillMaxHeight()
                    .width(5.dp)
                    .clip(CircleShape)
                    .background(Secondary, CircleShape)
            )

            Column(modifier = Modifier.weight(1f)
                .fillMaxWidth()){
                Text(
                    text = "F.I.R.E Report",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Primary,
                )
                Text(
                    text = "Financial Independence Retire Early",
                    style = titlesStyle,
                    color = Primary,
                )
            }

            Text(
                text = "$setupStep/$totalStep Steps",
                style = titlesStyle,
                color =Color.Black
            )

        }
    }
}


@Composable
fun UserSettingsHeader(
    name: String,
    onSettingsIconClick: () -> Unit,
    onNotificationIconClick: () -> Unit
) {
    Row(
        modifier=Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier=Modifier.weight(1f)
                .fillMaxWidth(),
        ) {
            Text(
                text="Welcome Back",
                style = MaterialTheme.typography.bodyMedium,
                color= Color.Black
            )
            Text(
                text=name,
                style = MaterialTheme.typography.headlineLarge,
                color = Primary
            )
        }

        CircleButton(
            onClick = onNotificationIconClick,
            icon = {
                Icon(
                    painter = painterResource(Res.drawable.notification_icon),
                    contentDescription = null,
                    tint = Secondary,
                    modifier = Modifier.size(24.dp)
                )
            }
        )
        CircleButton(
            onClick = onSettingsIconClick,
            icon = {
                Icon(
                    painter = painterResource(Res.drawable.settings_icon),
                    contentDescription = null,
                    tint = Secondary,
                    modifier = Modifier.size(24.dp)
                )
            }
        )
    }
}