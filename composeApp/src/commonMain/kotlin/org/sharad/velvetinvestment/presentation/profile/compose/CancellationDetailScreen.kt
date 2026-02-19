package org.sharad.velvetinvestment.presentation.profile.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.darkBlue
import org.sharad.emify.core.ui.theme.grayColor
import org.sharad.emify.core.ui.theme.greenColor
import org.sharad.emify.core.ui.theme.orangeColor
import org.sharad.velvetinvestment.utils.genericDropShadow
import org.sharad.velvetinvestment.utils.theme.Poppins
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.arrowback_elements

@Preview(showSystemUi = true)
@Composable
fun CancellationDetailScreen() {
    val investmentDetail = InvestmentDetails(
        fundName = "SBI Gold Fund",
        investmentType = "Lumpsum",
        redemptionAmount = 5000,
        nav = 29.189,
        unitsAlloted = 171.30,
        folioNumber = "SBI123456789"
    )
    val cancellationDetail = CancellationDetailClass(
        date = "20 Dec 2025",
        id = "CAN45678901234"
    )
    var cancellationstatus by remember { mutableStateOf<TransactionStatus>(TransactionStatus.FAILED) }
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            CancellationHeader("Cancellation Details", "MF1703345678901")
        }
        item {
            when (cancellationstatus) {
                TransactionStatus.PENDING -> PendingComposable()
                TransactionStatus.SUCCESSFUL -> SuccessfulComposable()
                TransactionStatus.FAILED -> FailedComposable()
            }
        }
        item {
            InvestmentDetailsCard(
                fundName = investmentDetail.fundName,
                investmentType = investmentDetail.investmentType,
                redemptionAmount = investmentDetail.redemptionAmount,
                nav = investmentDetail.nav,
                unitsAlloted = investmentDetail.unitsAlloted,
                folioNumber = investmentDetail.folioNumber

            )
        }
        item {
            CancellationTransactionDetail(
                status = cancellationstatus,
                cancellationDate =cancellationDetail.date,
                cancellationId =cancellationDetail.id
            )
        }
    }

}

@Composable
fun CancellationTransactionDetail(
    status: TransactionStatus,
    cancellationDate: String,
    cancellationId: String
) {
    val status = status
    Box(
        modifier = Modifier.fillMaxWidth().genericDropShadow(RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp)).background(color = Color.White).padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Transaction Details",
                color = darkBlue,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = Poppins
            )
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text("Cancellation Date", fontFamily = Poppins, fontSize = 14.sp, color = grayColor)
                Text(
                    cancellationDate,
                    fontFamily = Poppins,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        cancellationId,
                        fontFamily = Poppins,
                        fontSize = 14.sp,
                        color = grayColor
                    )

                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "CAN45678901234",
                        fontFamily = Poppins,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text("Status", fontFamily = Poppins, fontSize = 14.sp, color = grayColor)

                Text(
                    text = when (status) {
                        TransactionStatus.FAILED -> "Failed"
                        TransactionStatus.PENDING -> "In Progress"
                        TransactionStatus.SUCCESSFUL -> "Succeed"
                    },
                    fontFamily = Poppins,
                    fontSize = 16.sp,
                    color = when (status) {
                        TransactionStatus.FAILED -> Color.Red
                        TransactionStatus.PENDING -> orangeColor
                        TransactionStatus.SUCCESSFUL -> greenColor
                    },
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}


@Composable
fun CancellationHeader(heading: String, id: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            painter = painterResource(Res.drawable.arrowback_elements),
            contentDescription = "ArrowBack"
        )
        Column {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = heading,
                color = darkBlue,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                fontFamily = Poppins
            )
            Text(id, color = grayColor, fontSize = 14.sp, fontFamily = Poppins)
        }
    }
}


data class CancellationDetailClass(
    val date: String,
    val id: String
)