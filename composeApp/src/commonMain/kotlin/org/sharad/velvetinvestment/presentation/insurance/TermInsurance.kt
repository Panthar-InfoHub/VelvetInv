package org.sharad.velvetinvestment.presentation.insurance

import androidx.compose.runtime.Composable
import org.sharad.emify.core.ui.theme.healthColor
import org.sharad.emify.core.ui.theme.termColor
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.icon_heart
import velvet.composeapp.generated.resources.nav_icon_incurance
import velvet.composeapp.generated.resources.pl2
import velvet.composeapp.generated.resources.pl3

@Composable
fun TermInsuranceScreen(
    onBackClick: () -> Unit,
){
    SharedInsuranceScreen(
        heading = "Term Insurance",
        onBack = onBackClick,
        subHeading = "Life Coverage Progress",
        icon = Res.drawable.nav_icon_incurance,
        tint = termColor,
        status = "Active",
        coverage = "₹ 5,00,000",
        recommended ="₹ 10,00,000",
        gap = "₹ 5,00,000",
        infoText = "A CAS (Consolidate Account Statement) Provides an automated way to track all your Mutual Fund holdings across different brokers in one place.",
        image = Res.drawable.pl2
    )
}