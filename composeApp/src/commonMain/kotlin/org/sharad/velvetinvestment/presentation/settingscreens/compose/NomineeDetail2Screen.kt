package org.sharad.velvetinvestment.presentation.settingscreens.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.velvetinvestment.presentation.onboarding.compose.OnBoardingTextField
import org.sharad.velvetinvestment.presentation.settingscreens.viewmodels.SettingViewModel
import org.sharad.velvetinvestment.shared.compose.GenericDropDownField
@Preview(showSystemUi = true)
@Composable
fun NomineeDetail2Screen() {
        val viewModel:SettingViewModel = koinViewModel()
    val status by viewModel.nomineeDetail2Model.collectAsStateWithLifecycle()

        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp).navigationBarsPadding().statusBarsPadding(), verticalArrangement = Arrangement.spacedBy(20.dp)) {
            item {
                OnBoardingTextField(
                    value =status.fullName,
                    onValueChange = viewModel::updateFullName,
                    placeHolder = "Full Name",
                    mandatory = false,
                    keyboardType = KeyboardType.Text,
                    label = "Nominee Full Name"
                )
            }

            item {
                GenericDropDownField(
                    status.relationship,
                    onValueChange = viewModel::updateRelationship,
                    placeHolder = "Relationship",
                    label = "Relationship",
                    list = listOf()
                )
            }

            item {
                GenericDropDownField(
                    status.nomineeId,
                    onValueChange = viewModel::updateNomineeId,
                    placeHolder = "Nominee Identity",
                    label = "Nominee ID",
                    list = listOf()
                )
            }

            item {
                OnBoardingTextField(
                    value = status.mobileNumber,
                    onValueChange = viewModel::updateMobileNumber,
                    placeHolder = "Enter Mobile number",
                    label = "Mobile Number",
                    mandatory = false,
                    keyboardType = KeyboardType.Phone
                )
            }
            item {
                OnBoardingTextField(
                    value = status.email,
                    onValueChange = viewModel::updateEmail,
                    placeHolder = "Enter Email address",
                    label = "Email",
                    mandatory = false,
                    keyboardType = KeyboardType.Email
                )
            }
        }

    }
