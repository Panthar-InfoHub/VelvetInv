package org.sharad.velvetinvestment.presentation.kyc.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.backgroundGray
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.presentation.kyc.viewmodels.KycContractViewModel
import org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails.NextButtonFooter
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.LoaderScreen
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.pdfutils.PdfViewer
import org.sharad.velvetinvestment.utils.theme.Poppins
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.sign_icon

@Composable
fun KycContractScreen(
    onBack: () -> Unit,
    onSuccessfulUpload: () -> Unit,
    pv: PaddingValues
){

    val viewModel: KycContractViewModel = koinViewModel()
    val uiState by viewModel.contractPdfUrl.collectAsStateWithLifecycle()
//    val uiState = UiState.Success("https://persist.signzy.tech/api/files/1680365517/download/130496b49bb94dd7a28ed6c8ec2e27ef267c3c59dd564758a19653a9910637cd.pdf")
    val finalizeLoading by viewModel.submitLoading.collectAsStateWithLifecycle()
    val successState by viewModel.successState.collectAsStateWithLifecycle()
    val checked by viewModel.markedAsRead.collectAsStateWithLifecycle()

    when(uiState)
    {
        is UiState.Error ->{
            ErrorScreen(
                errorMessage = (uiState as UiState.Error).message,
                onRetryClick = {
                    viewModel.getContractPdf()
                }
            )
        }
        UiState.Loading -> {
            LoaderScreen()
        }
        is UiState.Success -> {
            val data=(uiState as UiState.Success<String>).data
            KycContactScreenMain(
                onBack = onBack,
                pv = pv,
                pfd=data,
                finalizeLoading = finalizeLoading,
                isChecked = checked,
                toggleCheck=viewModel::toggleMark,
                onFinalizeClick={
                    viewModel.finalizeKyc()
                }
            )
        }
    }

    if (successState){

    }

}

@Composable
fun KycContactScreenMain(
    onBack: () -> Unit,
    pv: PaddingValues,
    finalizeLoading: Boolean,
    onFinalizeClick: () -> Unit,
    pfd: String,
    isChecked: Boolean,
    toggleCheck: () -> Unit
) {
    val pdfViewer: PdfViewer= koinInject()
    Column(
        modifier=Modifier.fillMaxSize()
            .background(Color.White)
    ) {
        BackHeader(
            onBackClick = onBack,
            heading = "KYC Confirmation",
            showBack = true
        )
        Box(
            modifier = Modifier.weight(1f).padding(horizontal = 20.dp, vertical = 16.dp)
        ){
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ){
                Box(modifier = Modifier.weight(1f)){
                    PdfPreviewCard(
                        onClick = {
                            pdfViewer.openPdf(pfd)
                        }
                    )
                }

                ConsentSection(
                    isChecked = isChecked,
                    onToggle = toggleCheck
                )
            }
        }

        NextButtonFooter(
            onClick = onFinalizeClick,
            pv = pv,
            value = "Complete KYC",
            loading = finalizeLoading,
            enabled = isChecked
        )

    }
}
@Composable
fun PdfPreviewCard(
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(20.dp))
            .background(Primary.copy(alpha = 0.05f))
            .border(
                width = 1.dp,
                color = Primary.copy(alpha = 0.15f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(6.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .background(backgroundGray)
                .border(
                    width = 1.dp,
                    color = Primary.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onClick() }
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {


                Box(
                    modifier = Modifier
                        .size(84.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.sign_icon),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize().padding(20.dp),
                        tint = Primary
                    )
                }

                Spacer(Modifier.height(16.dp))

                // 🔹 Title
                Text(
                    text = "View KYC Document",
                    fontSize = 16.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    color = Primary
                )

                Spacer(Modifier.height(6.dp))

                // 🔹 Subtitle
                Text(
                    text = "Tap to preview your KYC verification document",
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Normal,
                    color = titleColor,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun ConsentSection(
    isChecked: Boolean,
    onToggle: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onToggle() },
        verticalAlignment = Alignment.Top
    ) {

        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(
                    if (isChecked) Primary else Color.Transparent
                )
                .border(
                    width = 1.dp,
                    color = if (isChecked) Primary else titleColor.copy(0.5f),
                    shape = RoundedCornerShape(6.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isChecked) {
                Text(
                    text = "✓",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        Text(
            text = "I have read and understood the KYC document and confirm that the provided details are accurate.",
            fontSize = 14.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.Normal,
            color = titleColor,
            lineHeight = 20.sp
        )
    }
}