package org.sharad.velvetinvestment.presentation.portfolio.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.shared.compose.ContinueBackButtonFooter
import org.sharad.velvetinvestment.utils.theme.titlesStyle
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.delete_box

@Composable
fun CancelSIPConfirmationScreen(
    onConfirmClick: (String) -> Unit,
    onCancelClick: () -> Unit,
    id: String,
    pv: PaddingValues
){

    Column(
        modifier=Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.weight(1f)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(Res.drawable.delete_box),
                    contentDescription = null,
                    tint = Secondary,
                    modifier = Modifier.size(42.dp)
                )
                Column(
                    modifier=Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = "Confirm SIP Cancellation",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "This will stop your future monthly installment place redeem order to withdraw invested amount.",
                        style = titlesStyle,
                        color = titleColor,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        ContinueBackButtonFooter(
            continueText = "Confirm",
            backText ="Don’t Cancel",
            onContinue = { onConfirmClick(id) },
            onBack = onCancelClick,
            pv = pv
        )
    }

}