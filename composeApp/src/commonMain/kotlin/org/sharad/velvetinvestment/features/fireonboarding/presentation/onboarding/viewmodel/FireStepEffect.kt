package org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel

/** One-off results every FIRE step view model sends to the flow's coordinator. */
sealed interface FireStepEffect {
    /** The step's data was saved; the flow reloads the user and moves to the next pending step. */
    data object Submitted : FireStepEffect
}
