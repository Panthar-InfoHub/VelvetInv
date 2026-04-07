package org.sharad.velvetinvestment.presentation.insurance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.BarHeader
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.pl5
import velvet.composeapp.generated.resources.pl6
import velvet.composeapp.generated.resources.pl7

@Composable
fun OtherInsuranceScreen(
    onBackClick: () -> Unit,
){
    Column(
        modifier=Modifier.fillMaxSize()
    ) {
        BackHeader(heading = "Other Insurances", showBack = true, onBackClick = onBackClick)
        LazyColumn(
            modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                BarHeader(heading="Term Insurance")
            }

            item {
                ImageCard(
                    x0 = Res.drawable.pl5
                )
            }

            item {
                BarHeader(heading="Health Insurance")
            }


            item {
                ImageCard(
                    x0 = Res.drawable.pl6
                )
            }

            item {
                BarHeader(heading="Disability Insurance")
            }

            item {
                ImageCard(
                    x0 = Res.drawable.pl7
                )
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}