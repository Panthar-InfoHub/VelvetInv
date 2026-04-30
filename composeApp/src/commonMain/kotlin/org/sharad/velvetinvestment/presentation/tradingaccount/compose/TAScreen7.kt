package org.sharad.velvetinvestment.presentation.tradingaccount.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.emify.core.ui.theme.darkBlue
import org.sharad.velvetinvestment.presentation.onboarding.compose.OnBoardingTextField
import org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails.NextButtonFooter
import org.sharad.velvetinvestment.presentation.tradingaccount.viewmodel.TradingAccountViewModel
import org.sharad.velvetinvestment.shared.DatePickerSelector
import org.sharad.velvetinvestment.shared.DropDownSelector
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.GenericDropDownField
import org.sharad.velvetinvestment.shared.compose.LoaderScreen
import org.sharad.velvetinvestment.shared.compose.OnBoardingDateField
import org.sharad.velvetinvestment.utils.DateTimeUtils
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.theme.Poppins
import org.sharad.velvetinvestment.utils.tradingaccount.GuardianRelation
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.gurdian_icon

@Composable
fun GuardianDetail(
    pv: PaddingValues,
    onClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: TradingAccountViewModel
) {

    val state by viewModel.formState.collectAsStateWithLifecycle()
    val enabled by viewModel.guardianScreenButtonEnabled.collectAsStateWithLifecycle()
    var showDateSelector by rememberSaveable{ mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {

        BackHeader(
            "Trading account",
            showBack = true,
            onBackClick = onBackClick
        )

        when (state) {

            is UiState.Error -> {
                ErrorScreen(
                    errorMessage = (state as UiState.Error).message,
                    onRetryClick = { viewModel.getUserData() }
                )
            }

            UiState.Loading -> {
                LoaderScreen()
            }

            is UiState.Success -> {

                val data = (state as UiState.Success).data.data

                Column(modifier = Modifier.fillMaxSize()) {

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        item {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    "Guardian Details",
                                    style = MaterialTheme.typography.headlineLarge
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
                                value = data.guardian_first_name,
                                onValueChange = viewModel::onGuardianFirstNameChange,
                                placeHolder = "Enter Guardian Name",
                                label = "Guardian Name",
                                mandatory = true,
                                keyboardType = KeyboardType.Text
                            )
                        }

                        item {
                            DropDownSelector(
                                value = GuardianRelation.getDisplayName(data.guardian_relation),
                                onValueChange = {
                                    viewModel.onGuardianRelationChange(it.code)
                                },
                                placeHolder = "Select Relationship",
                                mandatory = true,
                                label = "Guardian Relationship",
                                list = GuardianRelation.entries,
                                textConvertor = {
                                    it.displayName
                                }
                            )
                        }

                        item {
                            OnBoardingDateField(
                                value = data.guardian_dob,
                                placeHolder = "DD/MM/YYYY",
                                label = "Guardian DOB",
                                mandatory = true,
                                onClick = {
                                    showDateSelector=true
                                },
                            )
                        }

                        item {
                            OnBoardingTextField(
                                value = data.guardian_pan,
                                onValueChange = { viewModel.onGuardianPanChange(
                                    it.toUpperCase(Locale.current)
                                ) },
                                placeHolder = "ABCDE1234F",
                                label = "Guardian PAN",
                                mandatory = true,
                                keyboardType = KeyboardType.Text
                            )
                        }

                        item {
                            Spacer(
                                modifier = Modifier.height(
                                    pv.calculateBottomPadding()
                                )
                            )
                        }
                    }

                    NextButtonFooter(
                        onClick = onClick,
                        pv = pv,
                        value = "Next",
                        enabled = enabled
                    )
                }
                if (showDateSelector){
                    DatePickerSelector(
                        show = showDateSelector,
                        selectedDate = DateTimeUtils.slashDateToEpochMillis(data.guardian_dob),
                        onDismiss = { showDateSelector=false },
                        onDateSelected = {dob->
                            dob?.let {
                                viewModel.onGuardianDobChange(DateTimeUtils.epochMillisToSlashDate(dob))
                            }
                        }
                    )
                }
            }
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