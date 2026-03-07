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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.emify.core.ui.theme.blueColor
import org.sharad.emify.core.ui.theme.darkBlue
import org.sharad.emify.core.ui.theme.goldenColor
import org.sharad.emify.core.ui.theme.grayColor
import org.sharad.emify.core.ui.theme.redColor
import org.sharad.velvetinvestment.presentation.onboarding.compose.OnBoardingTextField
import org.sharad.velvetinvestment.presentation.tradingaccount.uimodel.FinancialTradingDetailsModel
import org.sharad.velvetinvestment.presentation.tradingaccount.viewmodel.TradingAccountViewModel
import org.sharad.velvetinvestment.shared.compose.GenericDropDownField
import org.sharad.velvetinvestment.utils.genericDropShadow
import org.sharad.velvetinvestment.utils.theme.Poppins
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.add_icon

@Preview(showSystemUi = true)
@Composable
fun TradingScreen3() {
    val tradingDetailViewModel: TradingAccountViewModel = koinViewModel()
    val state by tradingDetailViewModel.financialTradingDetailsModel.collectAsStateWithLifecycle()
    val list = listOf(
        "Business",
        "Service",
        "Professional",
        "Agriculture",
        "Retired",
        "Housewife",
        "Student",
        "Other"
    )
    val nomineeauthenticationlist = listOf(
        "W-WET",
        "E-Sign",
        "O-OTP"
    )

    val listofRelations = listOf(
        "Mother",
        "Father",
        "Son",
        "Daughter",
        "Brother",
        "Sister"

    )
    val listOfNomineeIdentity = listOf(
        "PAN", "Aadhaar", "Driving License", "OCI/Pasport"
    )
    val listOfCountry = listOf(
        "India", "Dubai", "US", "UK", "France", "Germany", "Japan"
    )
    LazyColumn(
        modifier = Modifier.fillMaxWidth().padding(16.dp).safeDrawingPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    "Financial & Trading Details",
                    fontSize = 24.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Text(
                    "Help us to understand your trading profile",
                    fontFamily = Poppins,
                    fontSize = 14.sp,
                    color = Color(0xff4A5565)
                )
            }
        }

        item {
            GenericDropDownField(
                value = state.occupation,
                onValueChange = tradingDetailViewModel::onOccupationChange,
                placeHolder = "Select Occupation",
                mandatory = true,
                label = "Occupation",
                modifier = Modifier.fillMaxWidth(),
                list = list
            )
        }
        item {
            HolderNature(
                Selected = state.holderNature,
                onHolderNatureChange = tradingDetailViewModel::onHolderNatureChange
            )
        }
        if (state.holderNature == Holding.JOINT) {
            item {
                JointHolder(jointHolder ="Joint Holder 2",list=listofRelations, list2 = listOfCountry,state,tradingDetailViewModel)
            }
            item {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)){
                        JointHolder(
                            jointHolder = "Joint Holder 3",
                            list = listofRelations,
                            list2 = listOfCountry,state,tradingDetailViewModel
                        )
                        AddMoreNominiee("Add More Joint Holder")

                    }
                }
        }
            item {
                CheckBoxComp(
                    checked = state.checked,
                    onCheckedChange = tradingDetailViewModel::onCheckedChange
                )
            }
            item {
                OnBoardingTextField(
                    value = state.nominationOPT,
                    onValueChange = tradingDetailViewModel::onNominationChange,
                    placeHolder = "Y/N",
                    label = "Nomination OPT",
                    mandatory = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardType = KeyboardType.Text
                )
            }

            item {
                GenericDropDownField(
                    value = state.nomineeAuthentication, onValueChange = tradingDetailViewModel::onNomineeAuthenticationChange,
                    placeHolder = "Nominee authentication",
                    mandatory = true,
                    label = "Nominee Authentication",
                    modifier = Modifier.fillMaxWidth(),
                    list = nomineeauthenticationlist,
                )
            }

            item {
                OnBoardingTextField(
                    value = state.nomineeName, onValueChange = tradingDetailViewModel::onNomineeNameChange,
                    placeHolder = "Nominee Name",
                    label = "Nomination Name",
                    mandatory = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardType = KeyboardType.Text,
                )
            }

            item {
                GenericDropDownField(
                    value = state.nomineeRelation,
                    onValueChange = tradingDetailViewModel::onNomineeRelationChange,
                    placeHolder = "Nomination Relation",
                    mandatory = true,
                    label = "Relationship",
                    modifier = Modifier.fillMaxWidth(),
                    list = listofRelations,
                )
            }
            //Nomination dob
            item {
                GenericDropDownField(
                    value = state.nomineeIdentityType,
                    onValueChange = tradingDetailViewModel::onNomineeIdentityTypeChange,
                    placeHolder = "Nominee identity",
                    mandatory = true,
                    label = "Nomination Identity type",
                    modifier = Modifier.fillMaxWidth(),
                    list = listOfNomineeIdentity,


                    )
            }

            item {
                OnBoardingTextField(
                    value = state.nomineeAadhar,
                    onValueChange =tradingDetailViewModel::onNomineeAadharChange,
                    placeHolder = "xxxx xxxx 1234",
                    label = "Nomination Aadhar number",
                    mandatory = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardType = KeyboardType.Number,
                )

            }

            item {
                OnBoardingTextField(
                    value = state.nomineeEmail,
                    onValueChange = tradingDetailViewModel::onNomineeEmailChange,
                    placeHolder = "Nominee  Email",
                    label = "Nominee Email",
                    mandatory = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardType = KeyboardType.Email,
                )

            }
            item {
                OnBoardingTextField(
                    value = state.nomineeMobile,
                    onValueChange = tradingDetailViewModel::onNomineeMobileChange,
                    placeHolder = "Nominee Mobile number",
                    label = "Nominee Mobile number",
                    mandatory = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardType = KeyboardType.Number,
                )

            }

            item {
                OnBoardingTextField(
                    value = state.nomineeAddress1,
                    onValueChange = tradingDetailViewModel::onNomineeAddress1Change,
                    placeHolder = "House/Flat No.,Building Name",
                    label = "Nominee Address 1",
                    mandatory = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardType = KeyboardType.Text,
                )

            }
            item {
                OnBoardingTextField(
                    value = state.nomineeAddress2,
                    onValueChange = tradingDetailViewModel::onNomineeAddress2Change,
                    placeHolder = "Street Name,Area",
                    label = "Nominee Address 2 (optional)",
                    mandatory = false,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardType = KeyboardType.Text,
                )

            }

            item {
                OnBoardingTextField(
                    value = state.nomineeAddress3,
                    onValueChange = tradingDetailViewModel::onNomineeAddress3Change,
                    placeHolder = "Landmark",
                    label = "Nominee Address 3 (optional)",
                    mandatory = false,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardType = KeyboardType.Text,
                )
            }
            item {
                OnBoardingTextField(
                    value = state.nomineeCity,
                    onValueChange = tradingDetailViewModel::onNomineeCityChange,
                    placeHolder = "Enter City",
                    label = "City",
                    mandatory = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardType = KeyboardType.Text,
                )

            }
            item {
                OnBoardingTextField(
                    value = state.nomineePincode,
                    onValueChange = tradingDetailViewModel::onNomineePincodeChange,
                    placeHolder = "6-digit pincode",
                    label = "Pincode",
                    mandatory = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardType = KeyboardType.Number,
                )

            }

            item {
                GenericDropDownField(
                    value = state.nomineeCity,
                    onValueChange = {},
                    placeHolder = "Select Country",
                    mandatory = true,
                    label = "Country",
                    modifier = Modifier.fillMaxWidth(),
                    list = listOfCountry,

                    )
            }
            item {
                OnBoardingTextField(
                    value = state.nomineeSOA,
                    onValueChange = tradingDetailViewModel::onNomineeSOAChange,
                    placeHolder = "Y/N",
                    label = "Nomination SOA(Statement of Account)",
                    mandatory = false,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardType = KeyboardType.Text,

                    )
            }
            item {
                OnBoardingTextField(
                    value = state.nomineeOptRefNo,
                    onValueChange = tradingDetailViewModel::onNomineeOptRefNoChange,
                    placeHolder = "opt ref no.",
                    label = "Nominee opt ref no.",
                    mandatory = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardType = KeyboardType.Number,
                )
            }
            item {
                AddMoreNominiee("Add More Nominee")

                Spacer(modifier = Modifier.height(16.dp))
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
            Holding.entries.forEach { it ->
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

@Preview(showBackground = true)
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
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = cardHeading,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(
                    Alignment.CenterHorizontally
                )
            )
            Text(
                text = cardSubHeading,
                color = grayColor,
                fontSize = 14.sp,
                fontFamily = Poppins,
                textAlign = TextAlign.Center
            )
        }
    }
}


enum class Holding(
    val heading: String = "",
    val subHeading: String = ""
) {
    SINGLE(heading = "Single Holding", subHeading = "One account holder - you alone"),
    JOINT(
        heading = "Joint Holding",
        subHeading = "Multiple account holders (additional details required)"
    )
}


@Composable
fun AddMoreNominiee(text:String) {
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
                text=text,
                fontFamily = Poppins,
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun CheckBoxComp(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
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
        Text("Nomination", fontFamily = Poppins, fontSize = 16.sp, color = Color(0xff101828))
    }
}

@Composable
fun JointHolder(jointHolder: String, list: List<String>, list2: List<String>, state: FinancialTradingDetailsModel,
                viewModel: TradingAccountViewModel) {
    Column(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(color = Color(0xffF9F9F9), shape = RoundedCornerShape(16.dp)),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            jointHolder,
            color = darkBlue,
            fontWeight = FontWeight.SemiBold,
            fontFamily = Poppins,
            fontSize = 22.sp
        )

        OnBoardingTextField(
            value = state.jointFullName,
            onValueChange = viewModel::onJointFullNameChange,
            placeHolder = "Enter full Name",
            label = "Full Name",
            mandatory = true,
        )


        GenericDropDownField(
            value = state.jointRelationship,
            onValueChange = viewModel::onJointRelationshipChange,
            placeHolder = "Select Relationship",
            mandatory = true,
            label = "Relationship with Primary Holder",
            modifier = Modifier.fillMaxWidth(),
            list = list
        )

        OnBoardingTextField(
            value = state.jointPan,
            onValueChange = viewModel::onJointPanChange,
            placeHolder = "Enter PAN number",
            label = "PAN number",
            mandatory = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Text,
        )


        OnBoardingTextField(
            value = state.jointEmail,
            onValueChange = viewModel::onJointEmailChange,
            placeHolder = "Enter email address",
            label = "Email",
            mandatory = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Email

        )

        OnBoardingTextField(
            value = state.jointMobile,
            onValueChange = viewModel::onJointMobileChange,
            placeHolder = "Enter Mobile number",
            label = "Mobile number",
            mandatory = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Number,
        )


        OnBoardingTextField(
            value = state.jointPercentage,
            onValueChange = viewModel::onJointPercentageChange,
            placeHolder = "Enter percentage (e.g., 30%)",
            label = "Percentage of Holding",
            mandatory = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Number,
        )

        Text(
            "Address Details",
            fontFamily = Poppins,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = darkBlue
        )

        OnBoardingTextField(
            value = state.jointAddress1,
            onValueChange =viewModel::onJointAddress1Change,
            placeHolder = "House/Flat No.,Building Name",
            label = "Address Line 1",
            mandatory = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Text,
        )

        OnBoardingTextField(
            value = state.jointAddress2,
            onValueChange = viewModel::onJointAddress2Change,
            placeHolder = "Street Name,Area",
            label = "Address Line 2",
            mandatory = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Text,
        )

        OnBoardingTextField(
            value = state.jointAddress3,
            onValueChange = viewModel::onJointAddress3Change,
            placeHolder = "Landmark",
            label = "Address Line 3 (optional)",
            mandatory = false,
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Text,
        )

        OnBoardingTextField(
            value = state.jointCity,
            onValueChange = viewModel::onJointCityChange,
            placeHolder = "Enter City",
            label = "City",
            mandatory = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Text,
        )

        OnBoardingTextField(
            value = state.jointPincode, onValueChange = viewModel::onJointPincodeChange,
            placeHolder = "6-digit pincode",
            label = "Pincode",
            mandatory = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Number,
        )

        GenericDropDownField(
            value = state.jointCountry, onValueChange = viewModel::onJointCountryChange,
            placeHolder = "Select Country",
            mandatory = true,
            label = "Country",
            modifier = Modifier.fillMaxWidth(),
            list = list2
        )
    }
}




