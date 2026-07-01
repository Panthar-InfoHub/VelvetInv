package org.sharad.velvetinvestment.presentation.kyc.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.bgColor4
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.presentation.kyc.viewmodels.KYCScreenViewModel
import org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails.NextButtonFooter
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.ShadowCard
import org.sharad.velvetinvestment.shared.theme.Poppins
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.velvetinvestment.utils.withInterRupee
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.plain_credit_card_rafiki_1

@Composable
fun KYCScreen(
    onBackClick: () -> Unit,
    onKYCInitSuccess: () -> Unit,
    viewModel: KYCScreenViewModel = koinViewModel()
) {
    val loading by viewModel.isLoading.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current

    var hasLaunchedBrowser by remember { mutableStateOf(false) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START && hasLaunchedBrowser) {
                onKYCInitSuccess()
                hasLaunchedBrowser = false
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    KYCScreenContent(
        loading = loading,
        onBackClick = onBackClick,
        onCompleteKYCClick = {
            viewModel.startKyc {
                hasLaunchedBrowser = true
            }
        }
    )
}

@Composable
fun KYCScreenContent(
    loading: Boolean,
    onBackClick: () -> Unit,
    onCompleteKYCClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        BackHeader(heading = "KYC", onBackClick = onBackClick, showBack = true)
        LazyColumn(
            modifier = Modifier.fillMaxSize().weight(1f).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                ){
                    Image(
                        painter = painterResource(Res.drawable.plain_credit_card_rafiki_1),
                        contentDescription = "Push Notification Image",
                        modifier = Modifier.fillMaxWidth(0.8f),
                        contentScale = ContentScale.FillWidth
                    )
                }
            }
            item {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Complete your KYC in Under few minutes to get started",
                        fontSize = 20.sp, fontWeight = FontWeight.SemiBold, fontFamily = Poppins
                    )
                    Text(
                        "Experience more for no cost",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = Poppins
                    )
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ZeroPriceCard(
                        text = "Account Maintenance Charges",
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                    ZeroPriceCard(
                        text = "Account Opening Fees",
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                }
            }
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                SpanStyle(fontWeight = FontWeight.SemiBold)
                            ) {
                                append("Disclaimer: ")
                            }
                            append("During DigiLocker verification, please select both your Aadhaar and PAN documents, as both are required to complete your KYC verification successfully.")                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = titleColor
                    )
                }
            }
        }
        NextButtonFooter(
            value = "Complete your KYC",
            onClick = onCompleteKYCClick,
            loading = loading
        )

    }
}

@Composable
fun ZeroPriceCard(text: String, modifier: Modifier) {

    ShadowCard(
        modifier=modifier
    ) {
        Column(
            modifier=Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier=Modifier.size(64.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(bgColor4.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text="₹0".withInterRupee(),
                    style = MaterialTheme.typography.headlineMedium,
                    color= Primary
                )
            }
            Text(
                text=text,
                style = titlesStyle,
                color = titleColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun KYCScreenPreview() {
    VelvetTheme {
        KYCScreenContent(
            loading = false,
            onBackClick = {},
            onCompleteKYCClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ZeroPriceCardPreview() {
    VelvetTheme {
        ZeroPriceCard(
            text = "Account Maintenance Charges",
            modifier = Modifier.padding(16.dp)
        )
    }
}
