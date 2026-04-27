package org.sharad.velvetinvestment.presentation.tradingaccount.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.emify.core.ui.theme.grayColor
import org.sharad.velvetinvestment.presentation.onboarding.compose.OnBoardingTextField
import org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails.NextButtonFooter
import org.sharad.velvetinvestment.presentation.tradingaccount.viewmodel.TradingAccountViewModel
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.GenericDropDownField
import org.sharad.velvetinvestment.utils.theme.Poppins

@Composable
fun TAScreen4(
    pv: PaddingValues,
    onClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: TradingAccountViewModel
) {
    val state by viewModel.formState.collectAsStateWithLifecycle()

    val clientList = listOf("D (Demat)", "P (Physical)")
    val defaultDPList = listOf("CDSL", "NSDL")

    Column(modifier = Modifier.fillMaxSize()) {

        BackHeader(
            "Trading account",
            showBack = true,
            onBackClick = onBackClick
        )

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
                        "Client Information Form",
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Text(
                        "Fields are conditionally mandatory based on your selections",
                        fontFamily = Poppins,
                        fontSize = 14.sp,
                        color = Color(0xff4A5565)
                    )
                }
            }

            item {
                GenericDropDownField(
                    value = state.data.client_type,
                    onValueChange = viewModel::onClientTypeChange,
                    placeHolder = "Client Type",
                    mandatory = true,
                    label = "Client Type",
                    modifier = Modifier.fillMaxWidth(),
                    list = clientList
                )
            }

            item {
                OnBoardingTextField(
                    value = state.data.pms,
                    onValueChange = viewModel::onPmsChange,
                    placeHolder = "Y/N",
                    label = "PMS",
                    mandatory = true
                )
            }

            item {
                Text(
                    "Mandatory if Client Type is D",
                    fontFamily = Poppins,
                    color = grayColor
                )
            }

            item {
                GenericDropDownField(
                    value = state.data.default_dp,
                    onValueChange = viewModel::onDefaultDpChange,
                    placeHolder = "Default DP",
                    mandatory = true,
                    label = "Default DP",
                    modifier = Modifier.fillMaxWidth(),
                    list = defaultDPList
                )
            }

            item {
                Text(
                    "Mandatory if Client Type is D (CDSL/NSDL)",
                    fontFamily = Poppins,
                    color = grayColor
                )
            }

            item {
                OnBoardingTextField(
                    value = state.data.cdsl_dpid,
                    onValueChange = viewModel::onCdslDpidChange,
                    placeHolder = "N/A",
                    label = "CDSL DP ID",
                    mandatory = false
                )
            }

            item {
                OnBoardingTextField(
                    value = state.data.cdslcltid,
                    onValueChange = viewModel::onCdslCltidChange,
                    placeHolder = "N/A",
                    label = "CDSL Client ID",
                    mandatory = false
                )
            }

            item {
                OnBoardingTextField(
                    value = state.data.cmbp_id,
                    onValueChange = viewModel::onCmbpIdChange,
                    placeHolder = "N/A",
                    label = "CMBP ID",
                    mandatory = false
                )
            }

            item {
                OnBoardingTextField(
                    value = state.data.nsdldpid,
                    onValueChange = viewModel::onNsdlDpidChange,
                    placeHolder = "N/A",
                    label = "NSDL DP ID",
                    mandatory = false
                )
            }

            item {
                OnBoardingTextField(
                    value = state.data.nsdlcltid,
                    onValueChange = viewModel::onNsdlCltidChange,
                    placeHolder = "N/A",
                    label = "NSDL Client ID",
                    mandatory = false
                )
            }

            item {
                Spacer(modifier = Modifier.height(pv.calculateBottomPadding()))
            }
        }

        NextButtonFooter(
            onClick = onClick,
            pv = pv,
            value = "Next",
            enabled = true
        )
    }
}