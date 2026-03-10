package org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.bgColor3
import org.sharad.emify.core.ui.theme.bgColor2
import org.sharad.emify.core.ui.theme.bgSecondaryColor
import org.sharad.emify.core.ui.theme.bgColor1
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.presentation.onboarding.compose.OnBoardingTextField
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.PersonalDetailsScreenViewModel
import org.sharad.velvetinvestment.shared.DatePickerSelector
import org.sharad.velvetinvestment.shared.compose.AppButton
import org.sharad.velvetinvestment.shared.compose.OnBoardingDateField
import org.sharad.velvetinvestment.utils.DateTimeUtils
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.icon_callender
import velvet.composeapp.generated.resources.icon_clock
import velvet.composeapp.generated.resources.icon_user

@Composable
fun PersonalDetailScreen(
    modifier: Modifier = Modifier,
    pv: PaddingValues,
    viewModel: PersonalDetailsScreenViewModel,
    onNext: () -> Unit
){

    val details by viewModel.personalDetails.collectAsStateWithLifecycle()
    val showDateSelector by viewModel.showDateSelector.collectAsStateWithLifecycle()
    val buttonEnabled by viewModel.buttonEnabled.collectAsStateWithLifecycle()



    Box(modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter){

        Column(
            modifier=Modifier.fillMaxSize()
        ){
            LazyColumn(
                modifier = Modifier.fillMaxSize().weight(1f).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item(key = "name") {
                    OnBoardingTextField(
                        value = details.fullName,
                        onValueChange = viewModel::onNameChange,
                        placeHolder = "Enter your full Name",
                        label = "Full Name",
                        mandatory = true
                    )
                }
                item(key = "city") {
                    OnBoardingTextField(
                        value = details.city,
                        onValueChange = viewModel::onCityChange,
                        placeHolder = "Enter the city",
                        label = "City",
                        mandatory = true
                    )
                }
                item(key = "mobile") {
                    OnBoardingTextField(
                        value = details.phoneNumber,
                        onValueChange = viewModel::onPhoneChange,
                        placeHolder = "10-digit mobile number",
                        label = "Mobile Number ",
                        mandatory = true
                    )
                }
                item(key = "email") {
                    OnBoardingTextField(
                        value = details.email,
                        onValueChange = viewModel::onEmailChange,
                        placeHolder = "your.email@gmail.com",
                        label = "Email Address ",
                        mandatory = true
                    )
                }
                item(key = "dob") {
                    OnBoardingDateField(
                        value = DateTimeUtils.epochMillisToDate(details.dob),
                        placeHolder = "dd/mm/yy",
                        label = "DOB ",
                        mandatory = true,
                        onClick = {
                            viewModel.showDateSelector()
                        }
                    )
                }

                item(key = "slider") {
                    RetirementYearSlider(
                        onSliderUpdate = viewModel::onSliderChange,
                        selectedYear = details.retirementYear
                    )
                }

                item(key = "card1") {
                    PersonalDetailsFinancialBox(
                        label = "Retirement Year",
                        value = details.retirementYear.toString(),
                        icon = Res.drawable.icon_callender,
                        backgroundColor = bgColor3,
                        labelColor = Secondary
                    )
                }
                item(key = "card2") {
                    PersonalDetailsFinancialBox(
                        label = "Retirement Age",
                        value = details.retirementAge?.toString()?:"Unavailable",
                        icon = Res.drawable.icon_user,
                        backgroundColor = bgSecondaryColor,
                        labelColor = titleColor
                    )
                }
                item(key = "card3") {
                    PersonalDetailsFinancialBox(
                        label = "Years to Save",
                        value = details.savingYears.toString(),
                        icon = Res.drawable.icon_clock,
                        backgroundColor = bgColor2,
                        labelColor = bgColor1
                    )
                }

                item {
                    Spacer(
                        modifier = Modifier.height(80.dp + pv.calculateBottomPadding())
                    )
                }
            }
            NextButtonFooter(
                onClick={onNext()},
                pv=pv,
                enabled = buttonEnabled
            )
        }
        
        if (showDateSelector){
            DatePickerSelector(
                show = showDateSelector,
                selectedDate = details.dob,
                onDismiss = {viewModel.hideDateSelector()},
                onDateSelected = {
                    viewModel.onDobChange(it)
                }
            )
        }

    }
}

@Composable
fun NextButtonFooter(onClick: () -> Unit, pv: PaddingValues, value: String = "Next", enabled: Boolean=true) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .shadow(elevation = 28.dp)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ){
        AppButton(
            modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 16.dp+pv.calculateBottomPadding()),
            onClick = onClick,
            enabled = enabled,
            text = value
        )
    }
}