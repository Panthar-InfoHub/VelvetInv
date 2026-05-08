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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.sharad.velvetinvestment.presentation.onboarding.compose.OnBoardingTextField
import org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails.NextButtonFooter
import org.sharad.velvetinvestment.presentation.tradingaccount.viewmodel.TradingAccountViewModel
import org.sharad.velvetinvestment.shared.DropDownSelector
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.theme.Poppins
import org.sharad.velvetinvestment.utils.tradingaccount.ClientType
import org.sharad.velvetinvestment.utils.tradingaccount.DefaultDp
import org.sharad.velvetinvestment.utils.tradingaccount.FatcaOccupationType
import org.sharad.velvetinvestment.utils.tradingaccount.SourceOfWealth
import org.sharad.velvetinvestment.utils.tradingaccount.YesNo

@Composable
fun TradingAccountClientInfoScreen(
    pv: PaddingValues,
    onClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: TradingAccountViewModel
) {
    val state by viewModel.formState.collectAsStateWithLifecycle()
    val buttonEnabled by viewModel.clientScreenButtonEnabled.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {

        BackHeader(
            "Trading account",
            showBack = true,
            onBackClick = { onBackClick() }
        )

        UiStateContainer(
            uiState = state,
            onRetry = { viewModel.getUserData() },
            modifier = Modifier.fillMaxSize()
        ) { uiData ->
            val data = uiData.data
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
                                    "Client Information Form",
                                    style = MaterialTheme.typography.headlineLarge
                                )
                                Text(
                                    "Fields are conditionally mandatory based on your selections",
                                    fontFamily = Poppins,
                                    fontSize = 14.sp,
                                    lineHeight = 14.sp,
                                    color = Color(0xff4A5565)
                                )
                            }
                        }

                        item {
                            DropDownSelector(
                                value = ClientType.getDisplayName(data.client_type),
                                onValueChange = { viewModel.onClientTypeChangeUi(it) },
                                placeHolder = "Client Type",
                                mandatory = true,
                                label = "Client Type",
                                modifier = Modifier.fillMaxWidth(),
                                list = ClientType.entries,
                                textConvertor = {
                                    it.displayName
                                }
                            )
                        }

                        if (data.client_type == ClientType.DEMAT.code){
                            item {
                                DropDownSelector(
                                    value = YesNo.displayNameFromCode(data.pms),
                                    onValueChange = { viewModel.onPmsChange(it.code) },
                                    placeHolder = "Y/N",
                                    label = "PMS",
                                    mandatory = true,
                                    list = YesNo.entries,
                                    textConvertor = {
                                        it.displayName
                                    }
                                )
                            }
                            item {
                                DropDownSelector(
                                    value = DefaultDp.getDisplayName(data.default_dp),
                                    onValueChange = { viewModel.onDefaultDpChangeUi(it) },
                                    placeHolder = "Select Default DP",
                                    label = "Default DP",
                                    mandatory = true,
                                    list = DefaultDp.entries,
                                    textConvertor = {
                                        it.displayName
                                    }
                                )
                            }
                        }

                        if (data.default_dp == DefaultDp.CDSL.code){
                            item {
                                OnBoardingTextField(
                                    value = data.cdsl_dpid,
                                    onValueChange = viewModel::onCdslDpidChange,
                                    placeHolder = "Enter CDSL DP ID",
                                    label = "CDSL DP ID",
                                    mandatory = true,
                                    keyboardType = KeyboardType.Number
                                )
                            }

                            item {
                                OnBoardingTextField(
                                    value = data.cdslcltid,
                                    onValueChange = viewModel::onCdslCltidChange,
                                    placeHolder = "Enter CDSL Client ID",
                                    label = "CDSL Client ID",
                                    mandatory = true,
                                    keyboardType = KeyboardType.Number
                                )
                            }
                        }

                        if (data.default_dp == DefaultDp.NSDL.code){
                            item {
                                OnBoardingTextField(
                                    value = data.cmbp_id,
                                    onValueChange = viewModel::onCmbpIdChange,
                                    placeHolder = "Enter CMBP ID",
                                    label = "CMBP ID",
                                    mandatory = true,
                                    keyboardType = KeyboardType.Text
                                )
                            }

                            item {
                                OnBoardingTextField(
                                    value = data.nsdldpid,
                                    onValueChange = viewModel::onNsdlDpidChange,
                                    placeHolder = "Enter NSDL DP ID",
                                    label = "NSDL DP ID",
                                    mandatory = true,
                                    keyboardType = KeyboardType.Text
                                )
                            }
                            item {
                                OnBoardingTextField(
                                    value = data.nsdlcltid,
                                    onValueChange = viewModel::onNsdlCltidChange,
                                    placeHolder = "Enter NSDL Client ID",
                                    label = "NSDL Client ID",
                                    mandatory = true,
                                    keyboardType = KeyboardType.Number
                                )
                            }
                        }

                        item {
                            DropDownSelector(
                                value = SourceOfWealth.getDisplayName(data.srce_wealt),
                                onValueChange = { viewModel.onSourceWealthChange(it.code) },
                                placeHolder = "Source Of Wealth",
                                label = "Source Of Wealth",
                                mandatory = true,
                                list = SourceOfWealth.entries,
                                textConvertor = {
                                    it.displayName
                                }
                            )
                        }
                        item {
                            DropDownSelector(
                                value = FatcaOccupationType.getDisplayName(data.occ_type),
                                onValueChange = { viewModel.onOccTypeChange(it.code) },
                                placeHolder = "Occupation Category",
                                label = "Occupation Category",
                                mandatory = true,
                                list = FatcaOccupationType.entries,
                                textConvertor = {
                                    it.displayName
                                }
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
                        enabled = buttonEnabled
                    )
                }
        }
    }
}
