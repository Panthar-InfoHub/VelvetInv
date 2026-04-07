package org.sharad.velvetinvestment.presentation.insurance

import androidx.compose.runtime.Composable
import org.sharad.emify.core.ui.theme.healthColor
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.icon_heart
import velvet.composeapp.generated.resources.pl3

@Composable
fun HealthInsuranceScreen(
    onBackClick: () -> Unit,
){
   SharedInsuranceScreen(
       heading = "Health Insurance",
       onBack = onBackClick,
       subHeading = "Coverage Progress",
       icon = Res.drawable.icon_heart,
       tint = healthColor,
       status = "Active",
       coverage = "₹ 5,00,000",
       recommended ="₹ 10,00,000",
       gap = "₹ 5,00,000",
       infoText = "A CAS (Consolidate Account Statement) Provides an automated way to track all your Mutual Fund holdings across different brokers in one place.",
       image = Res.drawable.pl3
   ) 
}