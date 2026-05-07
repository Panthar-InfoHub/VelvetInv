package org.sharad.velvetinvestment.presentation.goals.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.velvetinvestment.domain.models.home.GoalsSummaryDomain
import org.sharad.velvetinvestment.presentation.goals.viewmodel.GoalInfoScreenViewModel
import org.sharad.velvetinvestment.presentation.homescreen.compose.homeGoalsInfo
import org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails.NextButtonFooter
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.utils.AppEvents
import org.sharad.velvetinvestment.utils.RefreshEvents

@Composable
fun GoalScreen(
    onBack:()->Unit,
    onAddClick:()->Unit,
    pv: PaddingValues,
    onGoalClick:(String)->Unit
){
    val viewModel: GoalInfoScreenViewModel= koinViewModel()
    val uiState by viewModel.goalsInfo.collectAsStateWithLifecycle()

    LaunchedEffect(Unit){
        AppEvents.refreshEvents.collect{
            when(it){
                RefreshEvents.GoalEventRefresh ->{
                    viewModel.loadGoals()
                    AppEvents.clear()

                }
                RefreshEvents.HomeEventRefresh -> {

                }

                else -> {}
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
    ){

        BackHeader(heading = "Your Goals", showBack = true, onBackClick = onBack)
        Box(
            modifier = Modifier.weight(1f).fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            UiStateContainer(
                uiState = uiState,
                onRetry = { viewModel.loadGoals() }
            ) { data ->
                GoalScreenContent(data = data, onGoalClick = onGoalClick)
            }
        }
        NextButtonFooter(
            value = "Add Goals",
            onClick = onAddClick,
            pv=pv
        )
    }
}

@Composable
fun GoalScreenContent(data: List<GoalsSummaryDomain>, onGoalClick: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        homeGoalsInfo(goals = data) { id ->
            onGoalClick(id)
        }
    }
}