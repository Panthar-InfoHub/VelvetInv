package org.sharad.velvetinvestment.presentation.fixeddeposits.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.data.remote.mapper.PayoutType
import org.sharad.velvetinvestment.domain.models.fd.FDDetailsDomain
import org.sharad.velvetinvestment.domain.models.fd.FDFaqDomain
import org.sharad.velvetinvestment.domain.models.fd.FDTenureDomain
import org.sharad.velvetinvestment.domain.models.fixeddeposits.RiskLevel
import org.sharad.velvetinvestment.presentation.fixeddeposits.uimodels.toUIModel
import org.sharad.velvetinvestment.presentation.fixeddeposits.viewmodel.FDDetailsViewModel
import org.sharad.velvetinvestment.presentation.fixeddeposits.viewmodel.FDModalType
import org.sharad.velvetinvestment.presentation.onboarding.compose.financialflow.IconMoneyTextField
import org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails.NextButtonFooter
import org.sharad.velvetinvestment.shared.compose.AppButton
import org.sharad.velvetinvestment.shared.compose.BarHeader
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.LoaderScreen
import org.sharad.velvetinvestment.shared.compose.ShadowCard
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.formatMoneyAfterL
import org.sharad.velvetinvestment.utils.theme.Poppins
import org.sharad.velvetinvestment.utils.theme.subHeading
import org.sharad.velvetinvestment.utils.theme.titlesStyle
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.arrow_down
import velvet.composeapp.generated.resources.back_arrow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FDDetailsScreenRoot(
    id: String,
    onBackClick: () -> Unit,
    pv: PaddingValues,
    onPurchaseClick: () -> Unit,
) {
    val viewModel: FDDetailsViewModel = koinViewModel { parametersOf(id) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {

        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
            Icon(
                painter = painterResource(Res.drawable.back_arrow),
                contentDescription = null,
                modifier = Modifier
                    .padding(16.dp)
                    .size(24.dp)
                    .clickable { onBackClick() }
            )
        }

        Box(modifier = Modifier.weight(1f)) {
            when (val state = uiState) {

                UiState.Loading -> LoaderScreen()

                is UiState.Error -> ErrorScreen(state.message)

                is UiState.Success -> {
                    val data= state.data
                    val sheet by viewModel.activeSheet.collectAsStateWithLifecycle()
                    if (sheet != null) {
                        ModalBottomSheet(
                            onDismissRequest = { viewModel.closeSheet() },
                            containerColor = Color.White,
                        ) {
                            when (sheet) {
                                FDModalType.PAYOUT -> FDOptionsBottomSheet(
                                    title="Interest Payout",
                                    options=data.payoutOptions,
                                    onOptionCLick = viewModel::updateInterestPayout,
                                    onDismiss = viewModel::closeSheet,
                                    selectedOption = data.selectedPayout ?: PayoutType.Custom("UNAVAILABLE"),
                                    optionDisplayString = {option->
                                        option.displayName
                                    }
                                )

                                FDModalType.APPLICABLE ->   FDOptionsBottomSheet(
                                    "Applicable For",
                                    options = data.applicableFor,
                                    onOptionCLick = viewModel::updateApplicable,
                                    onDismiss = viewModel::closeSheet,
                                    selectedOption = data.applicable,
                                    optionDisplayString = {option->
                                        option
                                    }
                                )

                                FDModalType.INVEST -> {
                                    InvestAmountBottomSheet(
                                        invest = state.data.invest,
                                        onConfirmClick = viewModel::updateInvest
                                    )
                                }
                                null -> {}
                            }
                        }
                    }
                    FDDetailsScreen(
                        data = state.data,
                        pv = pv,
                        showPayout = { viewModel.openSheet(FDModalType.PAYOUT) },
                        showApplicable = { viewModel.openSheet(FDModalType.APPLICABLE) },
                        showInvestAmount= {viewModel.openSheet(FDModalType.INVEST)},
                        onPurchaseClick=onPurchaseClick
                    )
                }
            }
        }
    }
}

@Composable
fun FDDetailsScreen(
    data: FDDetailsDomain,
    pv: PaddingValues,
    showPayout: () -> Unit,
    showApplicable: () -> Unit,
    showInvestAmount: () -> Unit,
    onPurchaseClick: () -> Unit,
) {

    Column(
        modifier=Modifier.fillMaxSize()
    ){
        LazyColumn(
            modifier = Modifier.weight(1f)
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {

            item { FDHeader(
                bankLogo = data.bankLogo,
                bankName = data.bankName,
                riskLevel = data.riskLabel,
                maxInterestRate = data.maxInterestRate
            ) }

            item { Spacer(Modifier.height(16.dp)) }

            item { FDInvestSection(data, onPayoutClick =showPayout, onApplicableClick = showApplicable, showInvestAmount=showInvestAmount) }

            item { Spacer(Modifier.height(16.dp)) }

            item {
                FDInterestRatesCard(
                    list = data.interestRates,
                    invest = data.invest,
                    selectedPayoutMode = data.selectedPayout,
                    selectedApplicable = data.applicable
                )
            }

            item { Spacer(Modifier.height(16.dp)) }

            item { FDLockCard(data) }

            item { Spacer(Modifier.height(16.dp)) }

            item { FDAboutCard(data.about) }

            item { Spacer(Modifier.height(16.dp)) }

            item { FDFaqSection(data.faqs) }
        }

        NextButtonFooter(
            pv = pv,
            onClick = {onPurchaseClick()},
            value = "Invest Now",
            enabled = true,
        )
    }
}

@Composable
fun FDHeader(bankLogo: String, bankName: String, riskLevel: RiskLevel, maxInterestRate: Double) {

    Column(
        modifier=Modifier.fillMaxWidth()
    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                AsyncImage(
                    model = bankLogo,
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                )

                Spacer(Modifier.width(12.dp))

                Column(modifier=Modifier.weight(1f)) {

                    Text(
                        text = bankName,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold
                    )

                   RiskLevelIndicator(risk = riskLevel)
                }
                Text(
                    text = "${maxInterestRate}% p.a.",
                    style = MaterialTheme.typography.titleMedium,
                    color = appGreen
                )
            }
        }
        HorizontalDivider(color = titleColor.copy(0.2f),modifier=Modifier.padding(horizontal = 16.dp))
    }
}

@Composable
fun FDInvestSection(
    data: FDDetailsDomain,
    onPayoutClick: () -> Unit,
    onApplicableClick: () -> Unit,
    showInvestAmount: () -> Unit
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 220.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues( 16.dp)
    ) {

        item {
            FDInfoCard(
                title = "Invest",
                value = "₹ ${formatMoneyAfterL(data.invest)}",
                onClick = showInvestAmount
            )
        }

        item {
            FDInfoCard(
                title = "Interest Payout",
                value = data.selectedPayout?.displayName?:"N/A",
                onClick = onPayoutClick
            )
        }

        item {
            FDInfoCard(
                title = "Applicable For",
                value = data.applicableFor.firstOrNull() ?: "-",
                onClick = onApplicableClick
            )
        }

        item {
            FDInfoCard(
                title = "Min. investment",
                value = "₹ ${data.minDeposit}"
            )
        }
    }
}

@Composable
fun FDInfoCard(
    title: String,
    value: String,
    onClick: (() -> Unit)? = null
) {
    ShadowCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable(enabled = onClick != null) { onClick?.invoke() }
    ) {

        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                title,
                style = MaterialTheme.typography.labelSmall,
                color = Primary
            )

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {

                Text(
                    value,
                    style = MaterialTheme.typography.labelSmall,
                    color = Secondary
                )

                if (onClick != null) {
                    Icon(
                        painter = painterResource(Res.drawable.arrow_down),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun FDInterestRatesCard(
    list: List<FDTenureDomain>,
    invest: Long,
    selectedPayoutMode: PayoutType?,
    selectedApplicable: String,
) {

    val uiList = remember(list, invest, selectedPayoutMode?.id, selectedApplicable) {
        list
            .filter { it.payoutFrequency == selectedPayoutMode }
            .map { it.toUIModel(invest) }
    }

    ShadowCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Text(
                text = "Interest rates",
                fontSize = 20.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                color = Primary,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            FDTableHeader()

            Spacer(Modifier.height(16.dp))

            uiList.forEach {
                FDTenureRow(it)
            }
        }
    }
}

@Composable
fun FDLockCard(data: FDDetailsDomain) {

    ShadowCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            LockRow("Lock In", "${data.lockInDays} Days")
            LockRow("Withdrawal penalty", "${data.prematurePenalty}%")
            LockRow("RBI Insurance", data.insuranceAmount)
        }
    }
}
@Composable
fun LockRow(
    title: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = title,
            style = subHeading,
            fontWeight = FontWeight.SemiBold,
            color = Primary
        )

        Text(
            text = value,
            fontSize = 16.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.Medium,
            color = Secondary
        )
    }
}
@Composable
fun <T> FDOptionsBottomSheet(
    title: String,
    options: List<T>,
    onOptionCLick: (T) -> Unit,
    optionDisplayString: (T) -> String ,
    onDismiss: () -> Unit,
    selectedOption: T
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Text(title, style = MaterialTheme.typography.titleLarge, color = Primary)

        Spacer(Modifier.height(12.dp))

        if (options.isEmpty()){
            Text(
                text = "No Available Options",
                style = MaterialTheme.typography.titleMedium,
                color = Secondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDismiss() }
                    .padding(vertical = 12.dp)
            )
        }

        else{
            options.forEach {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ){

                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .border(
                                width = 2.dp,
                                color = Secondary,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        if (selectedOption==it) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(
                                        color = Secondary,
                                        shape = CircleShape
                                    )
                            )
                        }
                    }

                    Text(
                        text = optionDisplayString(it),
                        style = MaterialTheme.typography.titleMedium,
                        color = Secondary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOptionCLick(it) }
                            .padding(vertical = 12.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun InvestAmountBottomSheet(
    invest:Long,
    onConfirmClick: (Long) -> Unit,
){
    var money by remember { mutableStateOf(invest.toString()) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Text("Enter Investment Amount", style = MaterialTheme.typography.titleLarge, color = Primary)

        Spacer(Modifier.height(16.dp))

        IconMoneyTextField(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            value = money,
            onValueChange = {money=it},
            placeHolder = "",
            icon = {}
        )

        Spacer(Modifier.height(16.dp))

        AppButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {onConfirmClick(money.toLong())},
            text = "Confirm"
        )
    }
}


@Composable
fun FDAboutCard(about: String) {

    ShadowCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Text("About",
                fontFamily = Poppins,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Primary,
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = about,
                fontFamily = Poppins,
                fontSize = 14.sp,
                color=Color.Black
            )
        }
    }
}

@Composable
fun FDFaqSection(faqs: List<FDFaqDomain>) {

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {

        BarHeader(modifier = Modifier.fillMaxWidth(), "FAQs")

        Spacer(Modifier.height(8.dp))

        faqs.forEach { faq ->

            var expanded by remember { mutableStateOf(false) }
            val animatedIcon by animateFloatAsState(
                targetValue = if (expanded) 180f else 0f,)

            ShadowCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable(
                        indication = null,
                        interactionSource = MutableInteractionSource()
                    ) { expanded = !expanded }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            faq.question,
                            fontSize = 16.sp,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Medium,
                            color = Primary,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            painter = painterResource(Res.drawable.arrow_down),
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                                .graphicsLayer{ rotationZ = animatedIcon }
                        )
                    }

                    AnimatedVisibility (expanded) {
                        Spacer(Modifier.padding(horizontal = 4.dp).height(8.dp))
                        Text(
                            faq.answer,
                            style = titlesStyle.copy(fontSize = 14.sp),
                            color =Color.Black
                        )
                    }
                }
            }
        }
    }
}