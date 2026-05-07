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
import androidx.compose.runtime.LaunchedEffect
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
import org.sharad.emify.core.ui.theme.appRed
import org.sharad.emify.core.ui.theme.bgColor4
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.domain.models.home.GoalsSummaryDomain
import org.sharad.velvetinvestment.domain.models.home.UserWorthCardDomain
import org.sharad.velvetinvestment.presentation.homescreen.HomeScreenViewModel
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.shared.compose.BarHeader
import org.sharad.velvetinvestment.shared.compose.CircleButton
import org.sharad.velvetinvestment.shared.compose.GoalEntryCard
import org.sharad.velvetinvestment.shared.compose.GradientBackground
import org.sharad.velvetinvestment.shared.dottedBorder
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.utils.AppEvents
import org.sharad.velvetinvestment.utils.RefreshEvents
import org.sharad.velvetinvestment.utils.theme.subHeading
import org.sharad.velvetinvestment.utils.theme.titlesStyle
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.arrow_right
import velvet.composeapp.generated.resources.expenses_icon
import velvet.composeapp.generated.resources.fire_icon
import velvet.composeapp.generated.resources.notification_icon
import velvet.composeapp.generated.resources.plus_icon

@Composable
fun HomeScreenMain(
    viewModel: HomeScreenViewModel,
    pv: PaddingValues,
    navigateToFireReportScreen: () -> Unit,
    navigateToKYCScreen: () -> Unit,
    navigateToGoalScreen: () -> Unit,
    navigateToNotification: () -> Unit,
    navigateToAddGoal: () -> Unit,
    navigateToSpecificGoalProjection: (String) -> Unit,
    navigateToInsurance: () -> Unit,
    navigateToFd: () -> Unit,
    navigateToMutualFund: () -> Unit,
    navigateToTradingAccountSetup: () -> Unit
){

    LaunchedEffect(Unit){
        AppEvents.refreshEvents.collect {
            when(it){
                RefreshEvents.HomeEventRefresh -> {
                    viewModel.loadHome()
                    AppEvents.clear()
                }

                RefreshEvents.GoalEventRefresh -> {
                    viewModel.loadHome()
                    AppEvents.clear()
                }

                else -> {}
            }
        }
    }

    val homeState by viewModel.homeState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        GradientBackground()

        UiStateContainer(
            uiState = homeState,
            onRetry = { viewModel.loadHome() }
        ) { data ->
            HomeScreen(
                name = data.name,
                netWorth = data.userWorth,
                kyc = data.kycCompletion,
                tradingKyc = data.tradingAccountCompletion,
                fireReport = data.fireReport,
                goals = data.goals,
                hidden = data.hidden,
                onHiddenToggle = { viewModel.toggleHidden() },
                onNotificationIconClick = { navigateToNotification() },
                onSettingsIconClick = navigateToInsurance,
                pv = pv,
                onFireReportClick = navigateToFireReportScreen,
                navigateToKYCScreen = navigateToKYCScreen,
                navigateToGoalScreen = navigateToGoalScreen,
                navigateToAddGoal = navigateToAddGoal,
                navigateToSpecificGoalProjection = navigateToSpecificGoalProjection,
                navigateToMutualFund = navigateToMutualFund,
                navigateToFd = navigateToFd,
                navigateToInsurance = navigateToInsurance,
                navigateToTradingAccountSetup = navigateToTradingAccountSetup
            )
        }
    }
}


@Composable
fun HomeScreen(
    name: String,
    netWorth: UserWorthCardDomain?,
    kyc: Boolean,
    fireReport: Double,
    goals: List<GoalsSummaryDomain>,
    onNotificationIconClick: () -> Unit,
    onSettingsIconClick: () -> Unit,
    pv: PaddingValues,
    onFireReportClick: () -> Unit,
    navigateToKYCScreen: () -> Unit,
    navigateToGoalScreen: () -> Unit,
    hidden: Boolean,
    onHiddenToggle: () -> Unit,
    tradingKyc: Boolean,
    navigateToAddGoal: () -> Unit,
    navigateToSpecificGoalProjection: (String) -> Unit,
    navigateToFd: () -> Unit,
    navigateToMutualFund: () -> Unit,
    navigateToInsurance: () -> Unit,
    navigateToTradingAccountSetup: () -> Unit
) {

    LazyColumn(
        modifier=Modifier.fillMaxSize()
            .padding( start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier=Modifier.height(16.dp,)) }
        item{ UserSettingsHeader(name = name,onSettingsIconClick=onSettingsIconClick, onNotificationIconClick=onNotificationIconClick) }
        item{ UserWorthCard(netWorth=netWorth, onInvestingRateClick={}, hidden=hidden, onHiddenToggle=onHiddenToggle)}
        if (!kyc || !tradingKyc){ item { BarHeader(heading = "Finish Setting Up Account") } }
        if (!kyc){
            item { KYCCard(onClick = { navigateToKYCScreen() }, text = "Complete the KYC Process") }
        }
        else{
            if (!tradingKyc){
                item { KYCCard(onClick = { navigateToTradingAccountSetup() }, text = "Setup Your Trading Account") }
            }
        }
        if (goals.isEmpty()){ item { FirstGoalCard(onClick={navigateToAddGoal()}) } }
        item { FireReportHeader()}
        item { FireReportCard(fireReport, onClick ={onFireReportClick()}) }
        item { BarHeader(heading = "Why Invest with Velvet?") }
        item {
            FeatureNavigationCards(
                onBeatInflationClick = {
                    navigateToMutualFund()
                },
                onExpertPickedClick = {
                    navigateToFd()
                },
                onStartSmallClick = {
                    onFireReportClick()
                },
                onDiversifiedClick = {
                    navigateToInsurance()
                }
            )
        }
        if (goals.isNotEmpty()) {
            item { BarHeader(heading = "Your Goals", showArrow = true, onArrowClick = navigateToGoalScreen) }
            homeGoalsInfo(goals = goals) { id ->
                navigateToSpecificGoalProjection(id)
            }
            item{
                AddCustomGoalCard(onClick={navigateToAddGoal()})
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
fun FireReportCard(summary: Double, onClick: () -> Unit) {
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
                text = "Current F.I.R.E Percentage",
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
                    text="${summary}%",
                    style = titlesStyle,
                    color =if (summary>0) appGreen else appRed,
                )

            }

        }
//        DotCapCircularProgress(
//            modifier = Modifier.size(44.dp),
//            percentage = summary?.fireNumber?.toFloat()?:0f,
//            color = Secondary,
//            textColor = Primary
//        )

        Icon(
            painter = painterResource(Res.drawable.arrow_right),
            contentDescription = null,
            tint = Primary,
            modifier = Modifier.padding(end = 16.dp).height(24.dp)
        )

    }
}

@Composable
fun FireReportHeader() {
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
//        CircleButton(
//            onClick = onSettingsIconClick,
//            icon = {
//                Icon(
//                    painter = painterResource(Res.drawable.settings_icon),
//                    contentDescription = null,
//                    tint = Secondary,
//                    modifier = Modifier.size(24.dp)
//                )
//            }
//        )
    }
}