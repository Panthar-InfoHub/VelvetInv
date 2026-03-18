package org.sharad.velvetinvestment.shared.Navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.sharad.velvetinvestment.presentation.onboarding.compose.OnBoardingConfirmationScreen
import org.sharad.velvetinvestment.presentation.onboarding.compose.currentassets.CurrentAssetScreen
import org.sharad.velvetinvestment.presentation.onboarding.compose.financialflow.FinancialFlowScreen
import org.sharad.velvetinvestment.presentation.onboarding.compose.goals.AddGoalScreen
import org.sharad.velvetinvestment.presentation.onboarding.compose.goals.OnBoardingGoalScreen
import org.sharad.velvetinvestment.presentation.onboarding.compose.insurancecoverage.InsuranceCoverageScreen
import org.sharad.velvetinvestment.presentation.onboarding.compose.loan.AddLoanScreen
import org.sharad.velvetinvestment.presentation.onboarding.compose.loan.OnBoardingLoanScreen
import org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails.PersonalDetailScreen
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.CurrentAssetViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.FinancialFlowScreenViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.GoalScreenViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.InsuranceCoverageViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.LoanScreenViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.PersonalDetailsScreenViewModel
import org.sharad.velvetinvestment.utils.DateTimeUtils

@Composable
fun OnBoardingNavigation(
    personalDetailsViewModel: PersonalDetailsScreenViewModel,
    financialFlowScreenViewModel: FinancialFlowScreenViewModel,
    assetViewModel: CurrentAssetViewModel,
    pv: PaddingValues,
    loanScreenViewModel: LoanScreenViewModel,
    insuranceCoverageViewModel: InsuranceCoverageViewModel,
    goalViewModel: GoalScreenViewModel,
    navController: NavHostController,
    onLoginSuccessNavigation: () -> Unit
) {

    val personalData by personalDetailsViewModel.personalDetails.collectAsStateWithLifecycle()
    val financialData by financialFlowScreenViewModel.financialInfo.collectAsStateWithLifecycle()
    val monthlyEMI by loanScreenViewModel.monthlyEMIBurden.collectAsStateWithLifecycle()
    val loans by loanScreenViewModel.loanList.collectAsStateWithLifecycle()
    val goals by goalViewModel.goalList.collectAsStateWithLifecycle()
    val healthInsurance by insuranceCoverageViewModel.healthInsuranceAmount.collectAsStateWithLifecycle()
    val lifeInsurance by insuranceCoverageViewModel.lifeInsuranceAmount.collectAsStateWithLifecycle()
    val totalAsset by assetViewModel.totalAssets.collectAsStateWithLifecycle()
    val assetsInfo by assetViewModel.assetInfo.collectAsStateWithLifecycle()




    val monthlyIncome = financialData.annualIncome?.div(12) ?: 0
    val housingExpense = financialData.houseExpense ?: 0
    val otherExpenses = (financialData.otherExpense ?: 0) + (financialData.transportExpense ?: 0) + (financialData.foodExpense ?: 0)
    val emiExpense = monthlyEMI
    val netSurplus = monthlyIncome - housingExpense - otherExpenses - emiExpense
    val totalLiabilities= loans.sumOf { it.outstandingAmount }

    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = Route.OnBoardingPersonalDetails,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it }, // From Right
                animationSpec = tween(
                    durationMillis = 500,
                    easing = FastOutSlowInEasing
                )
            )
        },

        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(
                    durationMillis = 500,
                    easing = FastOutSlowInEasing
                )
            )
        },

        // Back navigation animation
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it }, // From Left
                animationSpec = tween(
                    durationMillis = 500,
                    easing = FastOutSlowInEasing
                )
            )
        },

        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it }, // To Right
                animationSpec = tween(
                    durationMillis = 500,
                    easing = FastOutSlowInEasing
                )
            )
        }
    ) {
        composable<Route.OnBoardingPersonalDetails>
        {
            PersonalDetailScreen(
                pv = pv,
                viewModel = personalDetailsViewModel,
                onNext = {
                    personalDetailsViewModel.nextStep()
                    navController.navigate(Route.OnBoardingFinancialFlow)
                }
            )
        }

        composable<Route.OnBoardingFinancialFlow>
        {
            FinancialFlowScreen(
                pv = pv,
                onNext = {
                    personalDetailsViewModel.nextStep()
                    navController.navigate(Route.OnBoardingCurrentAssets)
                },
                onPrev = {
                    personalDetailsViewModel.previousStep()
                    navController.popBackStack()
                },
                viewModel = financialFlowScreenViewModel,
            )
        }

        composable<Route.OnBoardingCurrentAssets> {
            CurrentAssetScreen(
                pv = pv,
                viewModel = assetViewModel,
                onNext = {
                    personalDetailsViewModel.nextStep()
                    navController.navigate(Route.OnBoardingLoan)
                },
                onPrev = {
                    personalDetailsViewModel.previousStep()
                    navController.popBackStack()
                },
            )
        }

        composable<Route.OnBoardingLoan> {
            OnBoardingLoanScreen(
                pv = pv,
                onNext = {
                    personalDetailsViewModel.nextStep()
                    navController.navigate(Route.OnBoardingInsuranceCoverage)
                },
                onPrev = {
                    personalDetailsViewModel.previousStep()
                    navController.popBackStack()
                },
                viewModel = loanScreenViewModel,
                onAddLoanClick = {
                    loanScreenViewModel.clearLoan()
                    navController.navigate(Route.OnBoardingAddLoan)}
            )
        }
        composable<Route.OnBoardingAddLoan> {
            AddLoanScreen(
                pv = pv,
                viewModel = loanScreenViewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable<Route.OnBoardingInsuranceCoverage> {
            InsuranceCoverageScreen(
                pv = pv,
                viewModel = insuranceCoverageViewModel,
                onPrev = {
                    personalDetailsViewModel.previousStep()
                    navController.popBackStack()
                },
                onNext = {
                    navController.navigate(Route.OnBoardingGoal)
                    personalDetailsViewModel.nextStep()
                }
            )
        }
        composable<Route.OnBoardingGoal> {
            OnBoardingGoalScreen(
                pv = pv,
                viewModel = goalViewModel,
                onPrev = {
                    personalDetailsViewModel.previousStep()
                    navController.popBackStack()
                },
                onNext = {
                    personalDetailsViewModel.nextStep()
                    navController.navigate(Route.OnBoardingSummary)
                },
                onAddGoalClick = {
                    navController.navigate(Route.OnBoardingGoalAdd)
                }
            )
        }

        composable<Route.OnBoardingGoalAdd> {
            AddGoalScreen(
                pv = pv,
                viewModel = goalViewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable<Route.OnBoardingSummary> {
            OnBoardingConfirmationScreen(
                // Personal Details
                personalDetails =personalData,
                name = personalData.fullName,
                city = personalData.city,
                annualIncome = financialData.annualIncome?: 0,
                retirementYear = personalData.retirementYear?: DateTimeUtils.getCurrentYear(),

                financiaData =financialData,
                // Monthly Cash Flow
                monthlyIncome = monthlyIncome,
                housingExpense = housingExpense,
                otherExpenses = otherExpenses,
                emiExpense = emiExpense,
                netSurplus = netSurplus,

                // Financial Overview
                totalAssets = totalAsset,
                totalLiabilities = totalLiabilities,
                netWorth = totalAsset-totalLiabilities,

                // Loans
                additionalLoans = loans,

                // Insurance
                termLifeCover = lifeInsurance,
                healthInsurance =healthInsurance,
                totalInsuranceLiabilities = lifeInsurance+healthInsurance,

                // Goals
                goals = goals,
                assetsInfo =assetsInfo,
                pv =pv,
                onPrev = {
                    personalDetailsViewModel.previousStep()
                    navController.popBackStack()
                },
                onNext =onLoginSuccessNavigation
            )
        }

    }

}