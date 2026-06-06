package org.sharad.velvetinvestment.presentation.cart.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.domain.models.usercart.CartType
import org.sharad.velvetinvestment.domain.models.usercart.SipDetails
import org.sharad.velvetinvestment.domain.models.usercart.SipItemDomain
import org.sharad.velvetinvestment.domain.models.usercart.UserCartDomain
import org.sharad.velvetinvestment.presentation.cart.viewmodel.CartScreenViewModel
import org.sharad.velvetinvestment.presentation.cart.viewmodel.CartSideEffects
import org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails.NextButtonFooter
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.shared.rememberBrowserReturnLauncher
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.velvetinvestment.utils.AppEventsController
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.formatMoneyAfterL
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.back_arrow
import velvet.composeapp.generated.resources.nav_icon_full_screener
import velvet.composeapp.generated.resources.wallet_icon

@Composable
fun CartScreen(
    pv: PaddingValues,
    onBack: () -> Unit
){

    val viewModel: CartScreenViewModel= koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val total by viewModel.totalAmount.collectAsStateWithLifecycle()
    val popupVisible by viewModel.confirmationPopupVisible.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()
    val isPurchaseEnabled by viewModel
        .isPurchaseEnabled
        .collectAsStateWithLifecycle()
    val browserLauncher = rememberBrowserReturnLauncher()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit){
        viewModel.cartSideEffect.collect{
            when(it){
                is CartSideEffects.OpenForInitiation -> {
                    browserLauncher.launch(it.url){
                        viewModel.checkPurchaseStatus(it.mandateId)
                    }
                }
                is CartSideEffects.OpenForPurchase ->{
                    browserLauncher.launch(it.url){
                        viewModel.reloadFund()
                    }
                }

                is CartSideEffects.OpenForLumpSumPurchase -> {
                    browserLauncher.launch(it.url){
                        viewModel.reloadFund()
                        scope.launch{ AppEventsController.sendPortfolioRefreshEvent() }
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit){
        if (uiState is UiState.Success){
            viewModel.reloadFund()
        }
    }

    UiStateContainer(
        uiState = uiState,
        onRetry = { viewModel.loadCart() },
        modifier = Modifier.fillMaxSize()
    ) { data ->
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CartHeader(
                onBack = onBack
            )

            MFTypeSelector(
                selected = data.selectedCartType,
                lumpsumSize = data.cartData.lumpSumItems.size,
                sipSize = data.cartData.sipItems.size,
                onSelect = viewModel::onCartTypeSelected
            )

            HorizontalDivider(
                thickness = 0.7.dp,
                color = titleColor.copy(0.2f),
                modifier = Modifier.padding(top = 16.dp)
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                CartScreenContent(
                    data= data.cartData,
                    selectedType = data.selectedCartType,
                    onRemoveClick = viewModel::removeItem,
                    onRefresh = { viewModel.loadCart() },
                    onStepUpEnabled = {viewModel.enableStepUp(it)},
                    onStepUpDisabled = {viewModel.disableStepUp(it)},
                    onStepUpAmountChange = {item, amount->
                        viewModel.updateStepUpAmount(item,amount)
                    }
                )
            }

            NextButtonFooter(
                onClick = {
                    viewModel.showPopup()
                },
                pv = pv,
                value = if (total == 0L) "No Fund Added to the Cart" else "Pay ₹${formatMoneyAfterL(total)}",
                loading = loading,
                enabled = isPurchaseEnabled
            )
        }
        if (popupVisible) {
            CartCutOffPopup(
                visible = popupVisible,
                onDismiss = { viewModel.hidePopup() },
                onPurchase = { viewModel.purchase() }
            )
        }
    }

}

@Composable
fun MFTypeSelector(selected: CartType, onSelect: (CartType) -> Unit, lumpsumSize: Int, sipSize: Int, ) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        CartTypeChip(
            text = "Lumpsum ($lumpsumSize)",
            onClick = { onSelect(CartType.LUMPSUM) },
            selected = selected == CartType.LUMPSUM,
            icon = Res.drawable.wallet_icon
        )
        CartTypeChip(
            text = "SIP ($sipSize)",
            onClick = { onSelect(CartType.SIP) },
            selected = selected == CartType.SIP,
            icon = Res.drawable.nav_icon_full_screener
        )
    }
}

@Composable
fun CartScreenContent(
    onRemoveClick: (String) -> Unit,
    onRefresh: () -> Unit,
    selectedType: CartType,
    data: UserCartDomain,
    onStepUpEnabled: (SipItemDomain) -> Unit,
    onStepUpDisabled: (SipItemDomain) -> Unit,
    onStepUpAmountChange: (SipItemDomain, String) -> Unit
) {
    when(selectedType){
        CartType.SIP -> {
            SIPScreenContent(
                items = data.sipItems,
                onRefresh=onRefresh,
                onRemoveClick=onRemoveClick,
                onStepUpEnabled = onStepUpEnabled,
                onStepUpDisabled = onStepUpDisabled,
                onStepUpAmountChange = onStepUpAmountChange
            )
        }
        CartType.LUMPSUM -> {
            LumpSumScreenContent(
                items = data.lumpSumItems,
                onRefresh=onRefresh,
                onRemoveClick=onRemoveClick
            )
        }
    }
}

@Composable
fun CartHeader(onBack: () -> Unit) {
    Row(
        modifier=Modifier.fillMaxWidth()
            .padding(vertical = 16.dp, horizontal =  16.dp ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        Icon(
            painter = painterResource(Res.drawable.back_arrow),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
                .clickable(
                    onClick = onBack,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
        )

        Text(
            text = "Cart",
            color = Primary,
            style = MaterialTheme.typography.headlineLarge
        )
    }
}


@Composable
fun CartTypeChip(text: String, onClick: () -> Unit, selected: Boolean, icon: DrawableResource) {
    val selectedColor= Primary
    val unselectedColor= Color(0xffF3F4F6)
    val selectedTextColor= Color.White
    val unselectedTextColor= Color(0xff6A7282)

    Box(
        modifier = Modifier
            .height(40.dp)
            .clip(CircleShape)
            .background(if (selected) selectedColor else unselectedColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(horizontal = 16.dp)
        ){
            Icon(
                painter=painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = if (selected) selectedTextColor else unselectedTextColor
            )
            Text(
                text = text,
                style = titlesStyle.copy(fontWeight = FontWeight.Medium, fontSize = 14.sp),
                color = if (selected) selectedTextColor else unselectedTextColor,
            )
        }
    }
}


@Preview
@Composable
fun CartScreenContentEmptyPreview() {
    VelvetTheme {
        Box(modifier = Modifier.background(Color.White).fillMaxSize()) {
            CartScreenContent(
                onRemoveClick = {},
                onRefresh = {},
                selectedType = CartType.LUMPSUM,
                data = UserCartDomain(emptyList(), emptyList()),
                onStepUpEnabled = {},
                onStepUpDisabled = {},
                onStepUpAmountChange = {_,_->},
            )
        }
    }
}


@Preview
@Composable
fun CartScreenContentPreview() {
    VelvetTheme {
        Box(modifier = Modifier.background(Color.White)) {
            CartScreenContent(
                onRemoveClick = {},
                onRefresh = {},
                selectedType = CartType.SIP,
                data = UserCartDomain(
                    listOf(
                        SipItemDomain(
                            id = "1",
                            amcName = "SBI Mutual Fund",
                            productName = "SBI Bluechip Fund Direct Growth",
                            amount = 5000,
                            type = CartType.LUMPSUM,
                            date = "2023-10-27",
                            sipDetails = SipDetails(
                                startDate = "2023-11-01",
                                endDate = "2028-11-01",
                                frequency = "Monthly",
                                day = 5,
                                sipAmount = 2000
                            ),
                            imageUrl = "",
                            inv_id = "",
                            prodCode = "",
                            stepUpRequired = true,
                            minStepUpAmount = 500,
                            amcCode = "",
                            minStepUpPercent = 10.0,
                        ),
                        SipItemDomain(
                            id = "2",
                            amcName = "HDFC Mutual Fund",
                            productName = "HDFC Mid-Cap Opportunities Fund",
                            amount = 2000,
                            type = CartType.SIP,
                            date = "2023-10-27",
                            sipDetails = SipDetails(
                                startDate = "2023-11-01",
                                endDate = "2028-11-01",
                                frequency = "Monthly",
                                day = 5,
                                sipAmount = 2000
                            ),
                            "",
                            inv_id = "",
                            prodCode = "",
                            minStepUpAmount = 500,
                            amcCode = "",
                            minStepUpPercent = 10.0,
                        )
                    ), emptyList()
                ),
                onStepUpEnabled = {},
                onStepUpDisabled ={},
                onStepUpAmountChange = {_,_->},
            )
        }
    }
}
