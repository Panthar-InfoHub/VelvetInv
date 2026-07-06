package org.sharad.velvetinvestment.presentation.cart.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.appRed
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDetailsDomain
import org.sharad.velvetinvestment.presentation.mutualfund.CartBottomSheetState
import org.sharad.velvetinvestment.presentation.mutualfund.Duration
import org.sharad.velvetinvestment.presentation.mutualfund.MFPurchaseTypes
import org.sharad.velvetinvestment.shared.compose.AppButton
import org.sharad.velvetinvestment.shared.theme.Inter
import org.sharad.velvetinvestment.utils.FundTypeSelector
import org.sharad.velvetinvestment.utils.SelectedFundType
import org.sharad.velvetinvestment.utils.formatMoneyAfterL
import org.sharad.velvetinvestment.shared.theme.Poppins
import org.sharad.velvetinvestment.shared.theme.subHeadingMedium
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.velvetinvestment.utils.clearFocusOnTap
import org.sharad.velvetinvestment.utils.withInterRupee
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.arrow_down

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartPopup(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onAmountChange: (String) -> Unit,
    onTypeChange: (MFPurchaseTypes) -> Unit,
    detailState: MutualFundDetailsDomain,
    cartState: CartBottomSheetState,
    onAddClick: () -> Unit,
    showFrequencyDropDown: () -> Unit,
    showDateDropDown: () -> Unit,
    showDurationDropDown: () -> Unit,

    ) {
    val scrollState = rememberScrollState()
    val fundType by FundTypeSelector.fundType.collectAsStateWithLifecycle()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxWidth(),
        sheetState=sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ){
        Column(
            modifier = Modifier.fillMaxWidth()
                .verticalScroll(state = scrollState)
                .imePadding()
                .padding(vertical = 20.dp, horizontal = 16.dp)
                .clearFocusOnTap(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Column(
                modifier=Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "Add to Cart",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        modifier = Modifier.padding(top = 8.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    FundBadge(
                        text = when(fundType){
                            SelectedFundType.SIP -> "SIP"
                            SelectedFundType.LUMSUM -> "Lump Sum"
                        }
                    )
                }

                Text(
                    text= detailState.scheme_name,
                    style = titlesStyle,
                    color = Color.Black,
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis
                )
            }

            when(fundType){
                SelectedFundType.LUMSUM -> LumpSumCart(
                    amount = cartState.amount,
                    onAmountChange = onAmountChange,
                    onChipClick = { onAmountChange(it.toString()) },
                    minAmount = cartState.minLumpSumAmount,
                    onAddClick=onAddClick,
                    loading = cartState.loading,


                )
                SelectedFundType.SIP -> SIPCart(
                    amount = cartState.amount,
                    onAmountChange = onAmountChange,
                    onChipClick = { onAmountChange(it.toString()) },
                    minAmount = cartState.minSipAmount,
                    onAddClick=onAddClick,
                    loading = cartState.loading,
                    frequency = cartState.selectedFrequency?.label,
                    duration = cartState.selectedDuration,
                    date = cartState.selectedSIPDate,
                    showFrequencyDropDown = showFrequencyDropDown,
                    showDateDropDown = showDateDropDown,
                    showDurationDropDown = showDurationDropDown
                )
            }
        }
    }

}

@Composable
fun SIPCart(
    onChipClick: (Long) -> Unit,
    onAmountChange: (String) -> Unit,
    amount: Long?,
    minAmount: Long,
    onAddClick: () -> Unit,
    loading: Boolean,
    frequency: String?,
    duration: Duration?,
    date: String?,
    showFrequencyDropDown: () -> Unit,
    showDateDropDown: () -> Unit,
    showDurationDropDown: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val chips = generateInvestmentChips(
        minAmount = minAmount,
        isSip = true
    )
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ){
            ShadowlessTextField(
                value = amount?.toString() ?: "",
                onValueChange = onAmountChange,
                placeHolder = "Enter amount (min. ₹${minAmount})",
                label = "Investment Amount",
                mandatory = false,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (amount != null && amount < minAmount) {
                    Text(
                        text = "Amount less than min ₹$minAmount".withInterRupee(),
                        color = appRed,
                        style = titlesStyle.copy(fontSize = 12.sp),
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
                Text(
                    text = "Min ₹$minAmount".withInterRupee(),
                    style = titlesStyle.copy(fontSize = 14.sp),
                    color = titleColor
                )
            }
        }

        AmountChipsGrid(
            amounts = chips,
            onChipClick = { onChipClick(it) },
            currentAmount=amount
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){

            DropDownField(
                text = date ?: "",
                placeholder = "Select Date",
                onClick = {
                    keyboardController?.hide()
                    showDateDropDown()
                },
                label = "SIP Day",
                modifier = Modifier.weight(1f)
            )
        }

        AppButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Start SIP " + ((amount?.let {"of ₹" +formatMoneyAfterL(it)+"/month" })?:""),
            onClick = {
                onAddClick()
            },
            loading = loading,
            enabled = amount != null && amount >= minAmount
        )
    }
}

@Composable
fun LumpSumCart(
    onChipClick: (Long) -> Unit,
    onAmountChange: (String) -> Unit,
    amount: Long?,
    minAmount: Long,
    onAddClick: () -> Unit,
    loading: Boolean,

) {
    val chips = generateInvestmentChips(
        minAmount = minAmount,
        isSip = false
    )
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ){
            ShadowlessTextField(
                value = amount?.toString() ?: "",
                onValueChange = onAmountChange,
                placeHolder = "Enter amount (min. ₹${minAmount})",
                label = "Investment Amount",
                mandatory = false,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (amount != null && amount < minAmount) {
                    Text(
                        text = "Amount less than min ₹$minAmount".withInterRupee(),
                        color = appRed,
                        style = titlesStyle.copy(fontSize = 12.sp),
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
                Text(
                    text = "Min ₹$minAmount".withInterRupee(),
                    style = titlesStyle.copy(fontSize = 14.sp),
                    color = titleColor
                )
            }
        }

        AmountChipsGrid(
            amounts = chips,
            onChipClick = { onChipClick(it) },
            currentAmount=amount
        )

        AppButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Add " + ((amount?.let {"of ₹" +formatMoneyAfterL(it) })?:"")+" to Cart" ,
            onClick = {
                onAddClick()
            },
            loading = loading,
            enabled = amount != null && amount >= minAmount
        )
    }
}

@Composable
fun AmountChipsGrid(
    amounts: List<Long>,
    onChipClick: (Long) -> Unit,
    currentAmount: Long?,
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        amounts.forEach { amount ->
            ChipItem(
                text = ("₹" + formatMoneyAfterL(amount)),
                onClick = { onChipClick(amount) },
                selected = amount == currentAmount
            )
        }
    }
}

@Composable
fun ChipItem(text: String, onClick: () -> Unit, selected: Boolean) {
    Box(
        modifier = Modifier
            .widthIn(min=52.dp)
            .clip(CircleShape)
            .background(if (selected) Secondary else Color.White)
            .clickable(onClick = onClick)
            .border(
                width = 1.dp,
                color = if (selected) Secondary else titleColor.copy(0.2f),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = text.withInterRupee(),
            style = titlesStyle.copy(fontWeight = FontWeight.Medium, fontSize = 14.sp),
            color = if (selected) Color.White else Primary,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
        )
    }
}

@Composable
fun ShadowlessTextField(
    value:String,
    onValueChange:(String)->Unit,
    placeHolder:String,
    label:String?=null,
    mandatory: Boolean=false,
    modifier: Modifier = Modifier,
){

    Column(
        modifier=modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        label?.let{
            Row(
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = label,
                    style = subHeadingMedium,
                    color = Color.Black
                )
                if (mandatory) {
                    Text(
                        text = "*",
                        color = Color.Red,
                        style = subHeadingMedium
                    )
                }
            }
        }

        BasicTextField(
            value = value,
            onValueChange = {it-> onValueChange(it) },
            singleLine = true,
            textStyle = TextStyle(
                fontFamily = Poppins,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = modifier.fillMaxWidth()
                .height(54.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(Color.White, RoundedCornerShape(15.dp))
                .border(
                    width = 0.7.dp,
                    shape = RoundedCornerShape(15.dp),
                    color = Secondary
                ),
        ) {
            Row(
                modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                horizontalArrangement  = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "₹",
                    fontFamily = Inter,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier=Modifier.padding(start = 8.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(end = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeHolder.withInterRupee(),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xffC5C5C5)
                        )
                    }
                    it()
                }
            }
        }
    }
}

@Composable
fun DropDownField(
    text: String,
    placeholder: String,
    onClick: () -> Unit,
    label: String? = null,
    mandatory: Boolean = false,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        label?.let {
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text = label,
                    style = subHeadingMedium,
                    color = Color.Black
                )
                if (mandatory) {
                    Text(
                        text = "*",
                        color = Color.Red,
                        style = subHeadingMedium
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(Color.White)
                .border(
                    width = 0.7.dp,
                    shape = RoundedCornerShape(15.dp),
                    color = Secondary
                )
                .clickable { onClick() }
        ) {

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = text.ifEmpty { placeholder },
                    style = if (text.isEmpty())
                        MaterialTheme.typography.bodySmall.copy(color = Color(0xffC5C5C5))
                    else
                        TextStyle(
                            fontFamily = Poppins,
                            fontSize = 16.sp,
                            color = Color.Black,
                        ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Icon(
                    painter = painterResource(Res.drawable.arrow_down),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = titleColor
                )
            }
        }
    }
}

@Composable
fun FundBadge(text: String){
    Box(
        modifier = Modifier.clip(CircleShape).background(Primary)
    ){
        Text(
            text = text,
            fontSize = 12.sp,
            color = Color.White,
            fontWeight = FontWeight.Medium,
            fontFamily = Poppins,
            modifier = Modifier.padding(vertical = 2.dp, horizontal = 12.dp)
        )
    }
}

fun generateInvestmentChips(
    minAmount: Long,
    isSip: Boolean
): List<Long> {

    val baseAmount = if (minAmount > 0) minAmount else 500L

    val multipliers = if (isSip) {
        listOf(1L, 2L, 5L, 10L, 20L)
    } else {
        listOf(1L, 2L, 5L, 10L)
    }

    return multipliers
        .map { baseAmount * it }
        .distinct()
}