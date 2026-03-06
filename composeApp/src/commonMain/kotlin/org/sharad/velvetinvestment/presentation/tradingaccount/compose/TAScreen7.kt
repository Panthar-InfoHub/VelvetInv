package org.sharad.velvetinvestment.presentation.tradingaccount.compose

import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.emify.core.ui.theme.blueColor
import org.sharad.emify.core.ui.theme.darkBlue
import org.sharad.velvetinvestment.presentation.onboarding.compose.OnBoardingTextField
import org.sharad.velvetinvestment.presentation.tradingaccount.viewmodel.TradingAccountViewModel
import org.sharad.velvetinvestment.shared.compose.GenericDropDownField
import org.sharad.velvetinvestment.utils.theme.Poppins
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.gurdian_icon
@Preview(showSystemUi = true)
@Composable
fun GuardianDetail() {
    val viewModel : TradingAccountViewModel = koinViewModel()
    val state by viewModel.gurdianDetailModel.collectAsStateWithLifecycle()
    val listOfRelationships =listOf("Mother","Father","Son","Daughter","Brother","Sister")
    LazyColumn(
        modifier = Modifier.fillMaxWidth().padding(16.dp).safeDrawingPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    "Guardian Details",
                    fontSize = 24.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Text(
                    "Provide guardian information for the minor account holder",
                    fontFamily = Poppins,
                    fontSize = 14.sp,
                    color = Color(0xff4A5565)
                )
            }
        }

        item {
           WhyThisNeeded()
        }

        item {
            OnBoardingTextField(
                value =state.guardianName ,
                onValueChange =viewModel::onGurdianNameChange,
                placeHolder = "Enter guardian's full name",
                label = "Guardian Name"
                , mandatory = true,
                keyboardType = KeyboardType.Text,
            )
        }
        item {
            GenericDropDownField(
                value = state.minorRelation,
                onValueChange = viewModel::onMinorRelationChange,
                placeHolder = "Minor Relationship",
                mandatory = true,
                list = listOfRelationships,
                label = "Minor Relationship"
            )
        }

        item {
            OnBoardingTextField(
                value =state.guardianEmail,
                onValueChange = viewModel::onGurdianEmailChange,
                placeHolder = "Enter Email Address",
                label = "Email Address",
                mandatory = true,
                keyboardType = KeyboardType.Email
            )
        }
        item {
            OnBoardingTextField(
                value = state.guardianNumber,
                onValueChange =viewModel::onGurdianNumberChange,
                placeHolder = "Enter Mobile Number",
                label = "Guardian Mobile Number",
                mandatory = true,
                keyboardType = KeyboardType.Phone
            )
        }
    }
}

@Composable
fun WhyThisNeeded(){
    Box(modifier = Modifier.fillMaxWidth().border(0.7.dp, shape = RoundedCornerShape(10.dp), color = Color(0xffBEDBFF)).clip(
        RoundedCornerShape(10.dp)).background(color = Color(0xffEFF6FF)).padding(16.dp)){
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)){
            Icon(painter = painterResource( Res.drawable.gurdian_icon), contentDescription = "guardian icon", tint = Color(0xff155DFC))
            Column {
                Text("Why is this needed?", fontWeight =  FontWeight.SemiBold,color = darkBlue, fontFamily = Poppins, fontSize = 16.sp)
                Text("Guardian details are required because the account holder is under 18 years of age. The guardian will have legal authority over the account until the minor turns 18."
                , fontSize = 14.sp, fontFamily = Poppins, color = Color(0xff1447E6))
            }
        }
    }
}