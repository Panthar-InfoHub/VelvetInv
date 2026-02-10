package org.sharad.velvetinvestment.shared.Navigation

import kotlinx.serialization.Serializable

object Route {

    @Serializable
    data object SplashScreen

    @Serializable
    data object LoginScreen

    @Serializable
    data object LoginFlow

    @Serializable
    data object MainAppFlow

    @Serializable
    data class OnBoardingFlow(val index: Int)
    @Serializable
    data object OnBoardingPersonalDetails
    @Serializable
    data object OnBoardingFinancialFlow
    @Serializable
    data object OnBoardingCurrentAssets
    @Serializable
    data object OnBoardingLoan
    @Serializable
    data object OnBoardingAddLoan
    @Serializable
    data object OnBoardingInsuranceCoverage
    @Serializable
    data object OnBoardingGoal
    @Serializable
    data object OnBoardingGoalAdd
    @Serializable
    data object OnBoardingSummary

    @Serializable
    data object ApplicationFLow
    @Serializable
    data object BottomNav
    @Serializable
    data object Home
    @Serializable
    data object FundScreener
    @Serializable
    data object PortFolio
    @Serializable
    data object Profile


}