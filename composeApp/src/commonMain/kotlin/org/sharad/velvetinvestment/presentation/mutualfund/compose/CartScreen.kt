package org.sharad.velvetinvestment.presentation.mutualfund.compose

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.appRed
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.domain.models.usercart.CartItemDomain
import org.sharad.velvetinvestment.domain.models.usercart.CartType
import org.sharad.velvetinvestment.presentation.mutualfund.CartScreenViewModel
import org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails.NextButtonFooter
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.formatMoneyAfterL
import org.sharad.velvetinvestment.shared.theme.Poppins
import org.sharad.velvetinvestment.shared.theme.titlesStyle
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
    val visibleItems by viewModel.visibleItems.collectAsStateWithLifecycle()
    val popupVisible by viewModel.confirmationPopupVisible.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()

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
                    items = visibleItems,
                    onRemoveClick = viewModel::removeItem,
                    onRefresh = { viewModel.loadCart() }
                )
            }

            NextButtonFooter(
                onClick = {
                    viewModel.showPopup()
                },
                pv = pv,
                value = if (total == 0L) "No Fund Added to the Cart" else "Pay ₹${formatMoneyAfterL(total)}",
                loading = loading,
                enabled = total != 0L
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
fun CartScreenContent(items: List<CartItemDomain>, onRemoveClick: (String) -> Unit, onRefresh: () -> Unit) {
    PullToRefreshBox(
        isRefreshing = false,
        onRefresh = onRefresh
    ){
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(
                items = items,
                key = { _, it -> it.id }
            ) { _, it ->
                CartItem(
                    item = it,
                    onRemoveClick = onRemoveClick
                )
                HorizontalDivider(
                    thickness = 0.5.dp,
                    color = titleColor,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
fun CartItem(
    item: CartItemDomain,
    onRemoveClick: (String) -> Unit
) {

    val initials = remember(item.productName) {
        item.productName
            .split(" ")
            .take(2)
            .mapNotNull { it.firstOrNull()?.uppercase() }
            .joinToString("")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initials,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        fontFamily = Poppins
                    )
                }

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = item.productName,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = item.amcName,
                        style = titlesStyle,
                        color = titleColor
                    )
                }
            }

            Text(
                text = "Remove",
                color = appRed,
                style = titlesStyle,
                modifier = Modifier.padding(start = 8.dp)
                    .clickable { onRemoveClick(item.id) }
            )
        }


        Row(
            verticalAlignment = Alignment.CenterVertically
        )
        {

            val amount = if (item.type == CartType.SIP) {
                item.sipDetails?.sipAmount ?: 0
            } else {
                item.amount
            }

            Text(
                text = "₹${amount}",
                color = appGreen,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = if (item.type == CartType.SIP) "Monthly" else "One-time",
                style = titlesStyle,
                color = Color(0xff4A5565)
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