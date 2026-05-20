package org.sharad.velvetinvestment.presentation.firereport.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.ShadowCard
import org.sharad.velvetinvestment.shared.theme.Poppins

@Composable
fun UpdateDetailsScreen(
    onBackClick: () -> Unit,
    onFinancialFlowClick: () -> Unit,
    onLoanClick: () -> Unit,
    onInsuranceClick: () -> Unit,
    onGoalsClick: () -> Unit,
    onAssetClick: () -> Unit
){
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        BackHeader(heading = "Update Details", onBackClick = onBackClick, showBack = true)
        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(Modifier.height(12.dp))
            }

            item {
                UpdateCard(
                    heading = "Assets",
                    subHeading = "Current Assets",
                    onClick = onAssetClick
                )
            }

            item {
                UpdateCard(
                    heading = "Financial Flow",
                    subHeading = "Expenses",
                    onClick = onFinancialFlowClick
                )
            }

            item {
                UpdateCard(
                    heading = "Loan & Debts",
                    subHeading = "Liabilities",
                    onClick = onLoanClick
                )
            }

            item {
                UpdateCard(
                    heading = "Insurance Coverage",
                    subHeading = "Health/Term Insurance",
                    onClick = onInsuranceClick
                )
            }

            item {
                UpdateCard(
                    heading = "Goals",
                    subHeading = "Financial Goals",
                    onClick = onGoalsClick
                )
            }

        }
    }
}

@Composable
fun UpdateCard(
    heading: String,
    subHeading: String,
    onClick: () -> Unit
){
    val color= Color(0xff273E71)
    ShadowCard(
        clickable = true,
        onClick=onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
            ) {
                Text(
                    text=heading,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Poppins,
                    color=color
                )
                Text(
                    text = subHeading,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = Poppins,
                    color=color.copy(0.6f)
                )
            }

            Box(
                modifier = Modifier.clip(CircleShape)
                    .background(Color(0xffF1F5F9))
            ){
                Text(
                    "Update",
                    fontSize = 12.sp,
                    fontFamily = Poppins,
                    color=Color(0xff4C3198),
                    modifier = Modifier.padding(vertical = 1.dp, horizontal = 8.dp)
                )
            }
        }
    }
}