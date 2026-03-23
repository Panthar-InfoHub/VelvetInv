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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.emify.core.ui.theme.goldenColor
import org.sharad.velvetinvestment.presentation.onboarding.compose.OnBoardingTextField
import org.sharad.velvetinvestment.presentation.tradingaccount.viewmodel.TradingAccountViewModel
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.GenericDropDownField
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.utils.theme.Poppins
import org.sharad.velvetinvestment.utils.theme.subHeadingMedium


@Preview(showSystemUi = true)
@Composable
fun TradingAccountScreen1(
    pv: PaddingValues
) {
    val viewModel: TradingAccountViewModel = koinViewModel()
    val state by viewModel.basicDetailsModel.collectAsStateWithLifecycle()

    val taxStatusList = listOf("Resident Individual", "Non-Resident Individual (NRI)", "HUF")


    Column(modifier = Modifier.fillMaxSize()) {
        BackHeader("Trading account", showBack = true, onBackClick = {})
        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        "Basic Details",
                        fontSize = 24.sp,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
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
                    value = state.firstName,
                    onValueChange = viewModel::onFirstNameChange,
                    placeHolder = "Enter First Name",
                    label = "First Name",
                    mandatory = true,
                    keyboardType = KeyboardType.Text
                )
            }
            item {
                OnBoardingTextField(
                    value = state.middleName,
                    onValueChange = viewModel::onMiddleNameChange,
                    placeHolder = "Enter Middle Name",
                    label = "Middle Name (Optional)",
                    mandatory = false,
                    keyboardType = KeyboardType.Text
                )
            }
            item {
                OnBoardingTextField(
                    value = state.lastName,
                    onValueChange = viewModel::onLastNameChange,
                    placeHolder = "Enter Last Name",
                    label = "Last Name",
                    mandatory = false,
                    keyboardType = KeyboardType.Text
                )
            }

            item {
                GenderBoxComposable(
                    "Gender", selected = state.gender, onSelect = {it->
                        viewModel.onGenderChange(it)}
                )
            }


            item {
                GenericDropDownField(
                    value = state.taxStatus,
                    onValueChange = viewModel::onTaxStatusChange,
                    placeHolder = "Tax Status",
                    mandatory = true,
                    label = "Tax Status",
                    modifier = Modifier.fillMaxWidth(),
                    list = taxStatusList
                )
            }



            item {
                OnBoardingTextField(
                    value = state.email,
                    onValueChange = viewModel::onEmailChange,
                    placeHolder = "Enter Email",
                    label = "Email Address ",
                    mandatory = true,
                    keyboardType = KeyboardType.Email
                )
            }

            item {
                OnBoardingTextField(
                    value = state.phone,
                    onValueChange = viewModel::onPhoneChange,
                    placeHolder = "Enter Phone Number",
                    label = "Phone Number",
                    mandatory = true,
                    keyboardType = KeyboardType.Phone
                )

            }

            item { Spacer(modifier = Modifier.height(pv.calculateBottomPadding())) }

        }
    }
}



@Composable
fun GenderBoxComposable(
    label: String, selected: String, onSelect: (String) -> Unit
) {
    val genderList = listOf(
        "Male", "Female", "Other"
    )
    Column(
        modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label, style = subHeadingMedium, color = Color.Black
        )
    }

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        genderList.forEach { text ->
            GenderBox(modifier = Modifier.weight(1f).fillMaxWidth(),
                gender = text,
                isSelected = (selected == text),
                onSelect = {
                    onSelect(text)
                }
            )
        }
    }

}


@Preview(showBackground = true)
@Composable
fun GenderBox(
    modifier: Modifier = Modifier, gender: String, isSelected: Boolean,
    onSelect: () -> Unit,
) {
    Box(
        modifier = modifier.height(92.dp).genericDropShadow(RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp)).border(
                if (isSelected) {
                    2.dp
                } else {
                    0.dp
                }, color = if(isSelected)goldenColor else Color.White, shape = RoundedCornerShape(24.dp)
            ).background(Color.White)
            .clickable {onSelect()}, contentAlignment = Alignment.Center
    )
    {
        Text(text = gender, fontFamily = Poppins)
    }
}