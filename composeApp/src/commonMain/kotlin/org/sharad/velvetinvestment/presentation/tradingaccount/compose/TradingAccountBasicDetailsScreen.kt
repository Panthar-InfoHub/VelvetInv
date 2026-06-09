package org.sharad.velvetinvestment.presentation.tradingaccount.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.sharad.emify.core.ui.theme.goldenColor
import org.sharad.velvetinvestment.presentation.onboarding.compose.OnBoardingTextField
import org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails.NextButtonFooter
import org.sharad.velvetinvestment.presentation.tradingaccount.viewmodel.TradingAccountViewModel
import org.sharad.velvetinvestment.shared.DropDownSelector
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.OnBoardingDateField
import org.sharad.velvetinvestment.shared.theme.Poppins
import org.sharad.velvetinvestment.shared.theme.subHeadingMedium
import org.sharad.velvetinvestment.utils.tradingaccount.Gender
import org.sharad.velvetinvestment.utils.tradingaccount.TaxStatus

@Composable
fun TradingAccountBasicDetailsScreen(
    pv: PaddingValues,
    onClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: TradingAccountViewModel
) {
    val state by viewModel.formState.collectAsStateWithLifecycle()
    val buttonEnabled by viewModel.basicDetailsNextEnabled.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        BackHeader("Trading account", showBack = true, onBackClick = { onBackClick() })
        UiStateContainer(
            uiState = state,
            onRetry = { viewModel.getUserData() },
            modifier = Modifier.fillMaxSize()
        ) { uiData ->
            val data = uiData.data
            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxSize().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                )
                {
                        item {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    "Basic Details",
                                    style = MaterialTheme.typography.headlineLarge,
                                )
                                Text(
                                    "Let's start with your basic information",
                                    fontFamily = Poppins,
                                    fontSize = 14.sp,
                                    color = Color(0xff4A5565)
                                )
                            }
                        }
                        item {
                            OnBoardingTextField(
                                value = data.primary_holder_first_name,
                                onValueChange = viewModel::onFirstNameChange,
                                placeHolder = "Enter First Name",
                                label = "First Name",
                                mandatory = true,
                                keyboardType = KeyboardType.Text
                            )
                        }
                        item {
                            OnBoardingTextField(
                                value = data.primary_holder_middle_name,
                                onValueChange = viewModel::onMiddleNameChange,
                                placeHolder = "Enter Middle Name",
                                label = "Middle Name (Optional)",
                                mandatory = false,
                                keyboardType = KeyboardType.Text
                            )
                        }
                        item {
                            OnBoardingTextField(
                                value = data.primary_holder_last_name,
                                onValueChange = viewModel::onLastNameChange,
                                placeHolder = "Enter Last Name",
                                label = "Last Name",
                                mandatory = false,
                                keyboardType = KeyboardType.Text
                            )
                        }
                        item {
                            OnBoardingTextField(
                                value = data.po_bir_inc,
                                onValueChange = viewModel::onPlaceOfBirthChange,
                                placeHolder = "Enter Place of Birth",
                                label = "Place of Birth",
                                mandatory = true,
                                keyboardType = KeyboardType.Text
                            )
                        }

                        item {
                            GenderBoxComposable(
                                "Gender",
                                selected = data.gender,
                                onSelect = {
                                    viewModel.onGenderChange(it)
                                }
                            )
                        }


                        item {
                            DropDownSelector(
                                value = TaxStatus.fromCode(data.tax_status)?.displayName ?: "",
                                onValueChange = {
                                    viewModel.onTaxStatusChange(it.code)
                                },
                                placeHolder = "Tax Status",
                                mandatory = true,
                                label = "Tax Status",
                                modifier = Modifier.fillMaxWidth(),
                                list = TaxStatus.entries,
                                textConvertor = {
                                    it.displayName+ " (${it.code})"
                                }
                            )
                        }


                        item {
                            OnBoardingTextField(
                                value = data.email,
                                onValueChange = viewModel::onEmailChange,
                                placeHolder = "Enter Email",
                                label = "Email Address ",
                                mandatory = true,
                                keyboardType = KeyboardType.Email
                            )
                        }

                        item {
                            PhoneDisplayField(
                                value = data.indian_mobile_no,
                                onValueChange = viewModel::onPhoneChange,
                                placeHolder = "Enter Phone Number",
                                label = "Phone Number",
                                mandatory = true,
                                keyboardType = KeyboardType.Phone
                            )

                        }

                        item {
                            OnBoardingDateField(
                                value = data.primary_holder_dob_incorporation,
                                placeHolder = "dd/mm/yy",
                                label = "DOB ",
                                mandatory = true,
                                onClick = {
                                },
                            )
                        }

                        item { Spacer(modifier = Modifier.height(pv.calculateBottomPadding())) }

                    }
                    NextButtonFooter(
                        onClick = onClick,
                        pv = pv,
                        value = "Next",
                        enabled = buttonEnabled,
                    )
                }
        }
    }
}



@Composable
fun GenderBoxComposable(
    label: String, selected: String, onSelect: (String) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row{
            Text(
                text = label, style = subHeadingMedium, color = Color.Black
            )
            Text(
                text = "*", color = Color.Red, style = subHeadingMedium
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Gender.entries.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowItems.forEach { gender ->
                        GenderBox(
                            modifier = Modifier.weight(1f),
                            gender = gender.displayLabel,
                            isSelected = selected == gender.code,
                            onSelect = {
                                onSelect(gender.code)
                            }
                        )
                    }
                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }

}

@Composable
fun PhoneDisplayField(
    value:String,
    onValueChange:(String)->Unit,
    placeHolder:String,
    label:String,
    mandatory: Boolean=false,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType= KeyboardType.Text,
    enabled:Boolean=true
){

    Column(
        modifier=modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row (
            verticalAlignment = Alignment.Top
        ){
            Text(
                text=label,
                style = subHeadingMedium,
                color = Color.Black
            )
            if (mandatory){
                Text(
                    text = "*",
                    color = Color.Red,
                    style = subHeadingMedium
                )
            }
        }

        BasicTextField(
            enabled=enabled,
            value = value,
            onValueChange = {it-> onValueChange(it) },
            singleLine = true,
            textStyle = MaterialTheme.typography.bodySmall,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = modifier.fillMaxWidth()
                .height(54.dp)
                .shadow(elevation = 8.dp,RoundedCornerShape(15.dp), spotColor = Color.Black.copy(alpha = 0.4f))
                .clip(RoundedCornerShape(15.dp))
                .background(Color.White, RoundedCornerShape(15.dp))
                .border(
                    width = 0.7.dp,
                    shape = RoundedCornerShape(15.dp),
                    color = Color(0xFFC5A572)
                ),
        ) {

            Box(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    Text(
                        style = MaterialTheme.typography.bodySmall,
                        text = "+91 | "
                    )
                    if (value.isEmpty()) {
                        Text(
                            text = placeHolder,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xffC5C5C5),
                            maxLines = 1
                        )
                    }
                    it()
                }
            }

        }

    }

}



@Composable
fun GenderBox(
    modifier: Modifier = Modifier, gender: String, isSelected: Boolean,
    onSelect: () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(CircleShape).border(
                if (isSelected) {
                    2.dp
                } else {
                    0.dp
                }, color = if(isSelected)goldenColor else Color.LightGray, shape = CircleShape
            ).background(Color.White)
            .clickable {onSelect()}, contentAlignment = Alignment.Center
    )
    {
        Text(text = gender, fontFamily = Poppins, modifier= Modifier.padding(vertical = 12.dp))
    }
}