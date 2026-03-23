package org.sharad.velvetinvestment.presentation.kyc.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.velvetinvestment.presentation.kyc.uistate.Gender
import org.sharad.velvetinvestment.presentation.kyc.uistate.MaritalStatus
import org.sharad.velvetinvestment.presentation.kyc.uistate.OccupationType
import org.sharad.velvetinvestment.presentation.kyc.viewmodels.KYCFormScreenViewModel
import org.sharad.velvetinvestment.presentation.onboarding.compose.OnBoardingTextField
import org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails.NextButtonFooter
import org.sharad.velvetinvestment.shared.DropDownSelector
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.LoaderScreen

@Composable
fun KYCFormScreen(pv: PaddingValues, onNext: () -> Unit, onBack: () -> Unit){
    val viewModel: KYCFormScreenViewModel = koinViewModel()
    val loading by viewModel.isLoading.collectAsStateWithLifecycle()
    val isDataLoaded by viewModel.isDataLoaded.collectAsStateWithLifecycle()

    LaunchedEffect(Unit){
        if (!isDataLoaded){
            viewModel.loadData(
                onSuccess = {},
                onFailure = {
                    onBack()
                }
            )
        }
    }

    if (loading){
        LoaderScreen()
    }
    else{
        FormContent(pv=pv, onNext=onNext, onBack=onBack, viewModel=viewModel)
    }
}

@Composable
fun FormContent(
    pv: PaddingValues,
    onNext: () -> Unit,
    onBack: () -> Unit,
    viewModel: KYCFormScreenViewModel
) {
    val loading by viewModel.formSubmissionLoading.collectAsStateWithLifecycle()
    val buttonEnabled by viewModel.buttonEnabled.collectAsStateWithLifecycle()
    val state by viewModel.formState.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        BackHeader(
            heading = "KYC Form",
            showBack = true,
            onBackClick =onBack
        )
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                OnBoardingTextField(
                    value = state.name,
                    onValueChange = {},
                    placeHolder = "Full Name",
                    label = "Full Name",
                    enabled = false
                )
            }

            item {
                OnBoardingTextField(
                    value = state.dob,
                    onValueChange = {},
                    placeHolder = "DOB",
                    label = "Date of Birth",
                    enabled = false
                )
            }

            item {
                OnBoardingTextField(
                    value = state.aadhaarNumber,
                    onValueChange = {},
                    placeHolder = "Aadhaar",
                    label = "Aadhaar Number",
                    enabled = false
                )
            }
            item {
                OnBoardingTextField(
                    value = state.emailId,
                    onValueChange = {},
                    placeHolder = "Email",
                    label = "Email",
                    enabled = false
                )
            }
            item {
                OnBoardingTextField(
                    value = state.mobileNumber,
                    onValueChange = {},
                    placeHolder = "Mobile Number",
                    label = "Mobile Number",
                    enabled = false
                )
            }


            item {
                DropDownSelector(
                    value = state.gender,
                    onValueChange = {
                        viewModel.updateGender(it.code)
                    },
                    placeHolder = "M/F",
                    label = "Gender",
                    list = Gender.entries,
                    textConvertor = {
                        it.name
                    },
                )
            }

            item {
                DropDownSelector(
                    value = state.maritalStatus,
                    onValueChange ={
                        viewModel.updateMaritalStatus(it.code)
                    },
                    placeHolder = "Marital Status",
                    label = "Marital Status",
                    list = MaritalStatus.entries,
                    textConvertor = {
                        it.name
                    },
                )
            }

            item {
                OnBoardingTextField(
                    value = state.panNumber,
                    onValueChange = viewModel::updatePANNumber,
                    placeHolder = "PAN Number",
                    label = "PAN Number"
                )
            }

//            item {
//                OnBoardingTextField(
//                    value = state.fatherTitle,
//                    onValueChange = viewModel::updateFatherTitle,
//                    placeHolder = "Mr.",
//                    label = "Father Title"
//                )
//            }

            item {
                OnBoardingTextField(
                    value = state.fatherName,
                    onValueChange = viewModel::updateFatherName,
                    placeHolder = "Father Name",
                    label = "Father Name",
                    mandatory = true
                )
            }

//            item {
//                OnBoardingTextField(
//                    value = state.motherTitle,
//                    onValueChange = viewModel::updateMotherTitle,
//                    placeHolder = "Mrs.",
//                    label = "Mother Title"
//                )
//            }

            item {
                OnBoardingTextField(
                    value = state.motherName,
                    onValueChange = viewModel::updateMotherName,
                    placeHolder = "Mother Name",
                    label = "Mother Name",
                    mandatory = true
                )
            }

            item {
                OnBoardingTextField(
                    value = state.placeOfBirth,
                    onValueChange = viewModel::updatePlaceOfBirth,
                    placeHolder = "Place of Birth",
                    label = "Place of Birth",
                    mandatory = true
                )
            }
            item {
                DropDownSelector(
                    value = state.occupationCode,
                    onValueChange ={
                        viewModel.updateOccupationCode(it.code)
                    },
                    placeHolder = "Occupation",
                    label = "Occupation Code",
                    list = OccupationType.entries,
                    textConvertor = {
                        it.code+":" +it.name
                    },
                    mandatory = true
                )
            }
            item {
                OnBoardingTextField(
                    value = state.occupationDescription,
                    onValueChange = viewModel::updateOccupationDesciption,
                    placeHolder = "Occupation Description",
                    label = "Occupation Description",
                    mandatory = true
                )
            }
        }
        NextButtonFooter(
            onClick = {
                viewModel.submitForm {
                    onNext()
                }
            },
            pv=pv,
            value = "Confirm",
            loading=loading,
            enabled = buttonEnabled
        )
    }
}