package org.sharad.velvetinvestment.presentation.profile.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.velvetinvestment.presentation.profile.compose.ProfileNew.UImodel.PersonalInfoUiData
import org.sharad.velvetinvestment.presentation.profile.compose.ProfileNew.viewModel.PersonalInfoViewModel
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.utils.theme.Poppins

@Composable
fun PersonalInformationScreen(
    onBack: () -> Unit,
    pv: PaddingValues,
    viewModel: PersonalInfoViewModel = koinViewModel()
) {
    val state by viewModel.personalInfoState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.White),
    ) {
        BackHeader(
            heading = "Personal Information",
            showBack = true,
            onBackClick = onBack
        )

        UiStateContainer(
            uiState = state,
            onRetry = { viewModel.loadPersonalInfo() }
        ) { data ->
            PersonalInfoContent(data)
        }
    }
}

@Composable
fun PersonalInfoContent(data: PersonalInfoUiData) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        item { Spacer(modifier = Modifier.height(16.dp)) }
        
        item {
            InfoSectionHeader(title = "Profile Details")
        }

        item {
            DisplayRow(label = "Full Name", value = data.fullName)
        }
        item {
            DisplayRow(label = "Date of Birth", value = data.dob)
        }
        item {
            DisplayRow(label = "Email", value = data.email)
        }
        item {
            DisplayRow(label = "Mobile Number", value = data.mobileNumber)
        }
        item {
            DisplayRow(label = "City", value = data.city)
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }

        item {
            InfoSectionHeader(title = "Identity & Status")
        }

        item {
            DisplayRow(label = "Trading Account Status", value = data.kycStatusTrading)
        }
        item {
            DisplayRow(label = "KYC Status", value = data.kycStatusMf)
        }

        item { Spacer(modifier = Modifier.height(32.dp)) }
    }
}

@Composable
fun InfoSectionHeader(title: String) {
    Column {
        Text(
            text = title,
            fontFamily = Poppins,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(thickness = 1.dp, color = Color.LightGray.copy(alpha = 0.5f))
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun DisplayRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        Text(
            text = label,
            fontFamily = Poppins,
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value.ifBlank { "Not Provided" },
            fontFamily = Poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}