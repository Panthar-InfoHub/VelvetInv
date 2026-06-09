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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.blueColor
import org.sharad.emify.core.ui.theme.darkBlue
import org.sharad.emify.core.ui.theme.goldenColor
import org.sharad.emify.core.ui.theme.grayColor
import org.sharad.emify.core.ui.theme.redColor
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.presentation.onboarding.compose.OnBoardingTextField
import org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails.NextButtonFooter
import org.sharad.velvetinvestment.presentation.tradingaccount.viewmodel.TradingAccountViewModel
import org.sharad.velvetinvestment.shared.DatePickerSelector
import org.sharad.velvetinvestment.shared.DropDownSelector
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.BarHeader
import org.sharad.velvetinvestment.shared.compose.OnBoardingDateField
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.utils.DateTimeUtils
import org.sharad.velvetinvestment.shared.theme.Poppins
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.velvetinvestment.utils.tradingaccount.Country
import org.sharad.velvetinvestment.utils.tradingaccount.Holding
import org.sharad.velvetinvestment.utils.tradingaccount.NominationAuthentication
import org.sharad.velvetinvestment.utils.tradingaccount.NomineeIdentityType
import org.sharad.velvetinvestment.utils.tradingaccount.NomineeRelationship
import org.sharad.velvetinvestment.utils.tradingaccount.OccupationType
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.add_icon
import velvet.composeapp.generated.resources.tick_icon

@Composable
fun TradingAccountFinancialDetailsScreen(
    pv: PaddingValues,
    onClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: TradingAccountViewModel
) {
    val state by viewModel.formState.collectAsStateWithLifecycle()
    val holderNature by viewModel.holderNature.collectAsStateWithLifecycle()
    val showDateDialog by viewModel.showCalender.collectAsStateWithLifecycle()
    val buttonEnabled by viewModel.financeScreenButtonEnabled.collectAsStateWithLifecycle()
    val thirdHolderEnabled by viewModel.enableThirdHolder.collectAsStateWithLifecycle()
    Column(modifier = Modifier.fillMaxSize()) {

        BackHeader(
            "Trading account",
            showBack = true,
            onBackClick = { onBackClick() }
        )

        UiStateContainer(
            uiState = state,
            onRetry = { viewModel.getUserData() }
        ) { baseResponse ->
            val data = baseResponse.data
            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                "Financial & Trading Details",
                                style = MaterialTheme.typography.headlineLarge
                            )
                            Text(
                                "Help us to understand your trading profile",
                                style = titlesStyle,
                                color = titleColor
                            )
                        }
                    }

                    item {
                        DropDownSelector(
                            value = OccupationType.getDisplayNameFromCode(data.occupation_code)
                                ?: "",
                            onValueChange = {
                                viewModel.onOccupationChange(it.code)
                            },
                            placeHolder = "Select Occupation",
                            mandatory = true,
                            label = "Occupation",
                            modifier = Modifier.fillMaxWidth(),
                            list = OccupationType.entries,
                            textConvertor = {
                                it.displayName
                            }
                        )
                    }

                    item {
                        HolderNature(
                            Selected = holderNature,
                            onHolderNatureChange = viewModel::onHolderNatureChangeUi
                        )
                    }

                    if (holderNature == Holding.JOINT) {
                        item {
                            JointHolder(
                                jointHolder = "Second Holder",

                                secondHolderFirstName = data.second_holder_first_name,
                                onSecondHolderFirstNameChange = viewModel::onSecondFirstNameChange,

                                secondHolderPan = data.second_holder_pan,
                                onSecondHolderPanChange = viewModel::onSecondPanChange,

                                secondHolderEmail = data.second_holder_email,
                                onSecondHolderEmailChange = viewModel::onSecondEmailChange,

                                secondHolderMobile = data.second_holder_mobile,
                                onSecondHolderMobileChange = viewModel::onSecondMobileChange,

                                secondHolderDOB = data.second_holder_dob,
                                onSecondHolderDOBClick = viewModel::onSecondDobChange,
                            )
                        }

                        item {
                            CheckBoxComp(
                                heading = "Add Another Holder",
                                checked = thirdHolderEnabled,
                                onCheckedChange = {
                                    if (it) viewModel.addThirdHolder()
                                    else viewModel.removeThirdHolder()
                                }
                            )
                        }
                        if (thirdHolderEnabled) {
                            item {
                                JointHolder(
                                    jointHolder = "Third Holder",

                                    secondHolderFirstName = data.third_holder_first_name,
                                    onSecondHolderFirstNameChange = viewModel::onThirdFirstNameChange,

                                    secondHolderPan = data.third_holder_pan,
                                    onSecondHolderPanChange = viewModel::onThirdPanChange,

                                    secondHolderEmail = data.third_holder_email,
                                    onSecondHolderEmailChange = viewModel::onThirdEmailChange,

                                    secondHolderMobile = data.third_holder_mobile,
                                    onSecondHolderMobileChange = viewModel::onThirdMobileChange,

                                    secondHolderDOB = data.third_holder_dob,
                                    onSecondHolderDOBClick = viewModel::onThirdDobChange
                                )
                            }
                        }
                    }

                    item {
                        BarHeader(heading = "Nomination Details")
                    }
                    item {
                        DropDownSelector(
                            value = NominationAuthentication.fromCode(data.nomination_authentication)?.displayName
                                ?: "",
                            onValueChange = { viewModel.onNominationAuthChange(it.code) },
                            placeHolder = "Nominee authentication",
                            mandatory = true,
                            label = "Nominee Authentication",
                            modifier = Modifier.fillMaxWidth(),
                            list = NominationAuthentication.getAllowedOptions(data.nomination_opt),
                            textConvertor = {
                                it.displayName
                            }
                        )
                    }

                    item {
                        OnBoardingTextField(
                            value = data.nominee_1_name,
                            onValueChange = viewModel::onNominee1NameChange,
                            placeHolder = "Nominee Name",
                            label = "Nomination Name",
                            mandatory = true,
                            modifier = Modifier.fillMaxWidth(),
                            keyboardType = KeyboardType.Text,
                        )
                    }

                    item {
                        DropDownSelector(
                            value = NomineeRelationship.getDisplayNameFromCode(data.nominee_1_relationship)
                                ?: "",
                            onValueChange = { viewModel.onNominee1RelationChange(it.code) },
                            placeHolder = "Nomination Relation",
                            mandatory = true,
                            label = "Relationship",
                            modifier = Modifier.fillMaxWidth(),
                            list = NomineeRelationship.entries,
                            textConvertor = {
                                it.displayName
                            }
                        )
                    }

                    item {
                        DropDownSelector(
                            value = NomineeIdentityType.getDisplayNameFromCode(data.nominee_1_identity_type)
                                ?: "",
                            onValueChange = { selection ->
                                viewModel.onNominee1IdentityTypeChange(selection.code)
                            },
                            placeHolder = "Nominee identity",
                            mandatory = true,
                            label = "Nomination Identity type",
                            modifier = Modifier.fillMaxWidth(),
                            list = NomineeIdentityType.entries,
                            textConvertor = { it.displayName }
                        )
                    }

                    item {
                        OnBoardingTextField(
                            value = data.nominee_1_identity_number,
                            onValueChange = viewModel::onNominee1IdentityNumberChange,
                            placeHolder = "xxxx xxxx 1234",
                            label = "Nomination Identity number " + if (data.nominee_1_identity_type == NomineeIdentityType.AADHAR.code) "(Last 4 Digits)" else {
                                ""
                            },
                            mandatory = true,
                            modifier = Modifier.fillMaxWidth(),
                            keyboardType = if (data.nominee_1_identity_type == NomineeIdentityType.PAN.code) KeyboardType.Text else KeyboardType.Number,
                        )
                    }

                    item {
                        OnBoardingTextField(
                            value = data.nominee_1_email,
                            onValueChange = viewModel::onNominee1EmailChange,
                            placeHolder = "Nominee Email",
                            label = "Nominee Email",
                            mandatory = true,
                            modifier = Modifier.fillMaxWidth(),
                            keyboardType = KeyboardType.Email,
                        )
                    }

                    item {
                        OnBoardingDateField(
                            value = data.nominee_1_dob,
                            placeHolder = "Select DOB",
                            label = "Nomination DOB",
                            mandatory = true,
                            onClick = {
                                viewModel.showCalender()
                            }
                        )
                    }

                    if (data.nominee_1_minor_flag == "Y") {
                        item {
                            OnBoardingTextField(
                                value = data.nominee_1_guardian,
                                onValueChange = { viewModel.onNominee1GuardianChange(it) },
                                placeHolder = "Enter Guardian Name",
                                label = "Nominee Guardian Name",
                                mandatory = true,
                                modifier = Modifier.fillMaxWidth(),
                                keyboardType = KeyboardType.Text,
                            )
                        }

                        item {
                            OnBoardingTextField(
                                value = data.nominee_1_guardian_pan,
                                onValueChange = viewModel::onNominee1GuardianPanChange,
                                placeHolder = "Enter Guardian PAN",
                                label = "Nominee Guardian PAN",
                                mandatory = true,
                                modifier = Modifier.fillMaxWidth(),
                                keyboardType = KeyboardType.Text,
                            )
                        }
                    }
                    item {
                        PhoneDisplayField(
                            value = data.nominee_1_mobile,
                            onValueChange = viewModel::onNominee1MobileChange,
                            placeHolder = "Nominee Mobile number",
                            label = "Nominee Mobile number",
                            mandatory = true,
                            modifier = Modifier.fillMaxWidth(),
                            keyboardType = KeyboardType.Number,
                        )
                    }

                    item {
                        OnBoardingTextField(
                            value = data.nominee_1_address1,
                            onValueChange = viewModel::onNominee1Address1Change,
                            placeHolder = "House/Flat No.,Building Name",
                            label = "Nominee Address 1",
                            mandatory = true,
                            modifier = Modifier.fillMaxWidth(),
                            keyboardType = KeyboardType.Text,
                        )
                    }

                    item {
                        OnBoardingTextField(
                            value = data.nominee_1_address2,
                            onValueChange = viewModel::onNominee1Address2Change,
                            placeHolder = "Street Name,Area",
                            label = "Nominee Address 2 (optional)",
                            mandatory = false,
                            modifier = Modifier.fillMaxWidth(),
                            keyboardType = KeyboardType.Text,
                        )
                    }

                    item {
                        OnBoardingTextField(
                            value = data.nominee_1_address3,
                            onValueChange = viewModel::onNominee1Address3Change,
                            placeHolder = "Landmark",
                            label = "Nominee Address 3 (optional)",
                            mandatory = false,
                            modifier = Modifier.fillMaxWidth(),
                            keyboardType = KeyboardType.Text,
                        )
                    }

                    item {
                        OnBoardingTextField(
                            value = data.nominee_1_city,
                            onValueChange = viewModel::onNominee1CityChange,
                            placeHolder = "Enter City",
                            label = "City",
                            mandatory = true,
                            modifier = Modifier.fillMaxWidth(),
                            keyboardType = KeyboardType.Text,
                        )
                    }

                    item {
                        OnBoardingTextField(
                            value = data.nominee_1_pin,
                            onValueChange = viewModel::onNominee1PincodeChange,
                            placeHolder = "6-digit pincode",
                            label = "Pincode",
                            mandatory = true,
                            modifier = Modifier.fillMaxWidth(),
                            keyboardType = KeyboardType.Number,
                        )
                    }

                    item {
                        OnBoardingTextField(
                            value = data.nominee_1_country,
                            onValueChange = viewModel::onNominee1CountryChange,
                            placeHolder = "Select Country",
                            mandatory = true,
                            label = "Country",
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

//                        item {
//                            DropDownSelector(
//                                value = data.nominee_soa,
//                                onValueChange = viewModel::onNomineeSoaChange,
//                                placeHolder = "Y/N",
//                                label = "Nomination SOA(Statement of Account)",
//                                mandatory = true,
//                                modifier = Modifier.fillMaxWidth(),
//                                list = listOf("Y", "N"),
//                                textConvertor = { it }
//                            )
//                        }


                    item {
                        Spacer(modifier = Modifier.height(pv.calculateBottomPadding() + 16.dp))
                    }
                }

                NextButtonFooter(
                    onClick = onClick,
                    pv = pv,
                    value = "Next",
                    enabled = buttonEnabled
                )
            }
            if (showDateDialog) {
                DatePickerSelector(
                    show = showDateDialog,
                    selectedDate = DateTimeUtils.slashDateToEpochMillis(data.nominee_1_dob),
                    onDismiss = { viewModel.hideCalender() },
                    onDateSelected = { dob ->
                        dob?.let {
                            viewModel.onDobChange(it)
                        }
                    }
                )
            }
        }
    }
}


@Composable
fun HolderNature(Selected: Holding, onHolderNatureChange: (Holding) -> Unit) {


    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row {

            Text(
                "Holder nature",
                fontFamily = Poppins,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp
            )

            Text(
                "*",
                fontFamily = Poppins,
                color = redColor,
                fontWeight = FontWeight.Medium
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Holding.entries.forEach {
                HoldingCard(
                    cardHeading = it.heading,
                    cardSubHeading = it.subHeading,
                    isSelected = it == Selected,
                    onHolderNatureChange = { onHolderNatureChange(it) },
                    modifier = Modifier.weight(1f)
                )
            }

        }
    }
}

@Composable
fun HoldingCard(
    cardHeading: String,
    cardSubHeading: String,
    isSelected: Boolean,
    onHolderNatureChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.height(150.dp).genericDropShadow(RoundedCornerShape(15.dp)).border(
            if (isSelected) 2.dp else 0.dp,
            color = if (isSelected) goldenColor else Color.White,
            RoundedCornerShape(15.dp)
        ).clip(RoundedCornerShape(15.dp))
            .background(color = Color.White, shape = RoundedCornerShape(10.dp))
            .clickable { onHolderNatureChange() }.padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = cardHeading,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                lineHeight = 18.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(
                    Alignment.CenterHorizontally
                )
            )
            Text(
                text = cardSubHeading,
                color = grayColor,
                fontSize = 12.sp,
                lineHeight = 13.sp,
                fontFamily = Poppins,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun AddMoreNominiee(text: String) {
    Box(modifier = Modifier.fillMaxWidth().drawBehind(onDraw = {
        drawRoundRect(
            cornerRadius = CornerRadius(12.dp.toPx()),
            color = darkBlue,
            style = Stroke(
                2.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            )
        )
    }).padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {},
                modifier = Modifier.background(
                    color = darkBlue.copy(0.1f),
                    shape = RoundedCornerShape(10.dp)
                )
            ) {
                Icon(
                    painter = painterResource(Res.drawable.add_icon),
                    contentDescription = "Add Icon"
                )
            }
            Text(
                text = text,
                fontFamily = Poppins,
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun CheckBoxComp(heading: String= "Nomination", checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedBoxColor = blueColor,
                uncheckedBoxColor = Color.White,
                checkedBorderColor = blueColor, uncheckedBorderColor = blueColor
            ), modifier = Modifier.clip(RoundedCornerShape(4.dp))
        )
        Text(heading, fontFamily = Poppins, fontSize = 16.sp, color = Color(0xff101828))
    }
}

@Composable
fun JointHolder(
    jointHolder: String,

    secondHolderFirstName: String,
    onSecondHolderFirstNameChange: (String) -> Unit,

    secondHolderPan: String,
    onSecondHolderPanChange: (String) -> Unit,

    secondHolderEmail: String,
    onSecondHolderEmailChange: (String) -> Unit,

    secondHolderMobile: String,
    onSecondHolderMobileChange: (String) -> Unit,

    secondHolderDOB: String,
    onSecondHolderDOBClick: (String) -> Unit,
) {
    var showDatePicker by rememberSaveable{mutableStateOf(false)}
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        BarHeader(
            heading = jointHolder
        )

        OnBoardingTextField(
            value = secondHolderFirstName,
            onValueChange = onSecondHolderFirstNameChange,
            placeHolder = "Enter full Name",
            label = "Full Name",
            mandatory = true,
        )

        OnBoardingTextField(
            value = secondHolderPan,
            onValueChange = onSecondHolderPanChange,
            placeHolder = "Enter PAN number",
            label = "PAN number",
            mandatory = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Text,
        )

        OnBoardingTextField(
            value = secondHolderEmail,
            onValueChange = onSecondHolderEmailChange,
            placeHolder = "Enter email address",
            label = "Email",
            mandatory = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Email
        )

        OnBoardingTextField(
            value = secondHolderMobile,
            onValueChange = onSecondHolderMobileChange,
            placeHolder = "Enter Mobile number",
            label = "Mobile number",
            mandatory = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Number,
        )

        OnBoardingDateField(
            value = secondHolderDOB,
            placeHolder = "Select DOB",
            label = "Holder DOB",
            mandatory = true,
            onClick = {
                showDatePicker=true
            }
        )
    }

    if (showDatePicker){
        DatePickerSelector(
            show = showDatePicker,
            selectedDate = DateTimeUtils.slashDateToEpochMillis(secondHolderDOB),
            onDismiss = {
                showDatePicker=false
            },
            onDateSelected = {
                it?.let {
                    onSecondHolderDOBClick(DateTimeUtils.epochMillisToSlashDate(it))
                }
            }
        )
    }
}

@Composable
fun CountrySelectorDialog(
    showDialog: Boolean,
    selectedCode: String?,
    onDismiss: () -> Unit,
    onCountrySelected: (Country) -> Unit
) {
    if (!showDialog) return

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 600.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Text(
                    text = "Select Country",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(Country.entries) { country ->

                        val isSelected = selectedCode == country.code

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    onCountrySelected(country)
                                    onDismiss()
                                }
                                .background(
                                    if (isSelected) {
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                    } else {
                                        Color.Transparent
                                    }
                                )
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = "${country.displayName} (${country.code})",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.weight(1f)
                            )

                            if (isSelected) {
                                Icon(
                                    painter = painterResource(Res.drawable.tick_icon),
                                    contentDescription = null,
                                    tint = appGreen
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}




