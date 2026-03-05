package org.sharad.velvetinvestment.presentation.fixeddeposits.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.appRed
import org.sharad.emify.core.ui.theme.appYellow
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.domain.models.fixeddeposits.RiskLevel
import org.sharad.velvetinvestment.presentation.fixeddeposits.uimodels.FixedDepositTenureUIModel
import org.sharad.velvetinvestment.presentation.fixeddeposits.uimodels.FixedDepositUIModel
import org.sharad.velvetinvestment.shared.compose.ShadowCard
import org.sharad.velvetinvestment.utils.theme.subHeading
import org.sharad.velvetinvestment.utils.theme.titlesStyle
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.fd_placeholder

@Composable
fun FDCard(
    fd: FixedDepositUIModel,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    ShadowCard(
        modifier=modifier
    ) {

        Column(
            modifier=Modifier.fillMaxWidth().padding(16.dp)
        ) {

            FDCardHeader(fd)
            Spacer(Modifier.height(16.dp))
            HorizontalDivider()
            Text(text="Interest rates", style = MaterialTheme.typography.headlineSmall,color= Primary, modifier=Modifier.padding(vertical = 12.dp))
            FDTableHeader()
            Spacer(Modifier.height(16.dp))
            fd.tenures.forEach {
                FDTenureRow(it)
            }
        }
    }
}

@Composable
private fun FDCardHeader(fd: FixedDepositUIModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AsyncImage(
            modifier = Modifier.size(48.dp),
            contentDescription = null,
            model=fd.bankLogoUrl,
            fallback = painterResource(Res.drawable.fd_placeholder),
            error = painterResource(Res.drawable.fd_placeholder),
            placeholder = painterResource(Res.drawable.fd_placeholder),
        )
        Column(
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            Row(
                modifier=Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(text = fd.bankName, style = MaterialTheme.typography.labelLarge)
                Text(text = fd.interest, style = MaterialTheme.typography.displayLarge, color = appGreen)
            }
            Text(
                text = fd.riskText.name.toLowerCase(Locale.current).capitalize(Locale.current) + " Risk",
                style = titlesStyle,
                color = when (fd.riskText) {
                    RiskLevel.LOW -> appGreen
                    RiskLevel.MODERATE -> appYellow
                    RiskLevel.HIGH -> appRed
                }
            )
        }
    }
}

@Composable
private fun FDTableHeader() {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Tenure",
            style = subHeading,
            color = Secondary,
            modifier = Modifier.weight(1f),
        )

        Text(
            text = "Interest",
            style = subHeading,
            color = Secondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 4.dp).width(60.dp)
        )

        Text(
            text = "You receive",
            style = subHeading,
            color = Secondary,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1.4f),
        )
    }
}

@Composable
private fun FDTenureRow(
    tenure: FixedDepositTenureUIModel
) {

    Row {

        Text(
            text = tenure.tenureText,
            style = titlesStyle,
            color = titleColor,
            modifier = Modifier.weight(1f),
        )

        Text(
            text = tenure.interestText,
            style = titlesStyle,
            color = titleColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 4.dp).width(60.dp)
        )

        Text(
            text = tenure.returnText,
            style = titlesStyle,
            color = titleColor,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1.4f),
        )
    }

    Spacer(Modifier.height(16.dp))
}