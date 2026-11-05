package org.sharad.velvetinvestment.features.mutualfund.ui.compose

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.bgColor4
import org.sharad.velvetinvestment.shared.compose.AppButton
import org.sharad.velvetinvestment.shared.compose.AppButtonDefaults
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.shared.theme.Poppins
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.back_arrow
import velvet.composeapp.generated.resources.ic_callended_filled
import velvet.composeapp.generated.resources.ic_ruppee_filled

/**
 * Where the MF buying flow starts: the user picks a monthly SIP or a one-time lumpsum, and that
 * choice ([org.sharad.velvetinvestment.utils.FundTypeSelector], set by the caller) is the default
 * mode for every screen after it, down to the purchase screen.
 *
 * TODO(top-up): JN loads the portfolio here and, when the user already holds funds, offers a
 * "Top Up existing Funds / Explore New Funds" sheet first. The top-up flow is parked for later
 * (its screens are in `composeApp/later/`), so the portfolio load and the sheet are commented out
 * below and both buttons go straight on to the fund list.
 */
@Composable
fun InvestmentMethodScreen(
    onStartSipClick: () -> Unit = {},
    onLumpsumClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    // TODO(top-up): onExistingSIPFundClick: () -> Unit = {},
    // TODO(top-up): onExistingLumpSumFundClick: () -> Unit = {}
) {
    // TODO(top-up): the portfolio only decided whether to show the top-up sheet.
    // val vm: PortfolioScreenViewModel = koinViewModel()
    // val uiState by vm.uiState.collectAsStateWithLifecycle()

    InvestmentMethodContent(
        onStartSipClick = onStartSipClick,
        onLumpsumClick = onLumpsumClick,
        onBackClick = onBackClick,
        modifier = modifier
    )
}

@Composable
fun InvestmentMethodContent(
    onStartSipClick: () -> Unit = {},
    onLumpsumClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // TODO(top-up): wrapped in UiStateContainer(uiState = portfolio) in JN; with the portfolio
    // load parked there is nothing to wait for.
    // var showSIPBottomSheet by remember { mutableStateOf(false) }
    // var showLumpSumBottomSheet by remember { mutableStateOf(false) }
    // val sheetStateSIP = rememberModalBottomSheetState()
    // val sheetStateLumpSum = rememberModalBottomSheetState()
    // val hasFunds = portfolio.mutualFunds.isNotEmpty()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 8.dp)
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.back_arrow),
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
            }
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(
                start = 24.dp,
                end = 24.dp,
                top = 8.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            item {
                Column {
                    Text(
                        text = "Invest Your Way",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = Poppins,
                        color = Color.Black
                    )

                    Text(
                        text = "Pick an option to start your investment journey today. ",
                        fontFamily = Poppins,
                        fontSize = 14.sp,
                        lineHeight = 16.sp,
                        color = Color.Gray
                    )
                }
            }

            item {
                InvestmentOptionCard(
                    title = "Start a SIP",
                    description = "Automated monthly investments to build wealth steadily over time.",
                    buttonText = "Start SIP",
                    icon = Res.drawable.ic_callended_filled,
                    onButtonClick = {
                        // TODO(top-up): if (hasFunds) showSIPBottomSheet = true else
                        onStartSipClick()
                    }
                )
            }

            item {
                InvestmentOptionCard(
                    title = "Invest a Lump-Sum",
                    description = "Make a one-time investment with your available funds today.",
                    buttonText = "Invest Lump Sum",
                    icon = Res.drawable.ic_ruppee_filled,
                    onButtonClick = {
                        // TODO(top-up): if (hasFunds) showLumpSumBottomSheet = true else
                        onLumpsumClick()
                    }
                )
            }
        }

        // TODO(top-up): the "Top Up existing Funds / Explore New Funds" sheets.
        // if (showSIPBottomSheet) {
        //     SIPSelectionBottomSheet(
        //         sheetState = sheetStateSIP,
        //         onDismiss = { showSIPBottomSheet = false },
        //         onExistingFundClick = {
        //             showSIPBottomSheet = false
        //             onExistingSIPFundClick()
        //         },
        //         onExploreNewFundsClick = {
        //             showSIPBottomSheet = false
        //             onStartSipClick()
        //         }
        //     )
        // }
        // if (showLumpSumBottomSheet) {
        //     SIPSelectionBottomSheet(
        //         sheetState = sheetStateLumpSum,
        //         onDismiss = { showLumpSumBottomSheet = false },
        //         onExistingFundClick = {
        //             showLumpSumBottomSheet = false
        //             onExistingLumpSumFundClick()
        //         },
        //         onExploreNewFundsClick = {
        //             showLumpSumBottomSheet = false
        //             onLumpsumClick()
        //         }
        //     )
        // }
    }
}

// TODO(top-up): the sheet offered when the user already holds funds. Kept, commented, for when
// the top-up flow comes back.
//
// @OptIn(ExperimentalMaterial3Api::class)
// @Composable
// fun SIPSelectionBottomSheet(
//     sheetState: SheetState,
//     onDismiss: () -> Unit,
//     onExistingFundClick: () -> Unit,
//     onExploreNewFundsClick: () -> Unit
// ) {
//     ModalBottomSheet(
//         onDismissRequest = onDismiss,
//         sheetState = sheetState,
//         containerColor = Color.White,
//         shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
//     ) {
//         Column(
//             modifier = Modifier
//                 .fillMaxWidth()
//                 .padding(bottom = 32.dp)
//         ) {
//             BottomSheetOptionItem(
//                 title = "Top Up existing Funds",
//                 subtitle = "Invest more in existing funds",
//                 onClick = onExistingFundClick
//             )
//
//             HorizontalDivider(
//                 modifier = Modifier.padding(horizontal = 24.dp),
//                 color = Color.LightGray.copy(alpha = 0.5f)
//             )
//
//             BottomSheetOptionItem(
//                 title = "Explore New Funds",
//                 subtitle = "Discover new funds and start a fresh investment.",
//                 onClick = onExploreNewFundsClick
//             )
//         }
//     }
// }
//
// @Composable
// fun BottomSheetOptionItem(
//     title: String,
//     subtitle: String,
//     onClick: () -> Unit
// ) {
//     Column(
//         modifier = Modifier
//             .fillMaxWidth()
//             .clickable(onClick = onClick)
//             .padding(horizontal = 24.dp, vertical = 20.dp)
//     ) {
//         Text(
//             text = title,
//             style = buttonTextStyle,
//             color = titleColor
//         )
//         Spacer(modifier = Modifier.height(4.dp))
//         Text(
//             text = subtitle,
//             style = MaterialTheme.typography.titleSmall,
//             color = Color(0xffAAAAAA)
//         )
//     }
// }

@Composable
fun InvestmentOptionCard(
    title: String,
    description: String,
    buttonText: String,
    icon: DrawableResource,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .genericDropShadow(RoundedCornerShape(12.dp))
            .border(width = 1.dp, color = bgColor4.copy(alpha = 0.2f), shape = RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = bgColor4.copy(0.1f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = Poppins,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = description,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontFamily = Poppins,
            color = Color.Gray,
            modifier = Modifier.padding(end = 40.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        AppButton(
            modifier = Modifier.fillMaxWidth(),
            text = buttonText,
            onClick = onButtonClick,
            style = AppButtonDefaults.style(shape = RoundedCornerShape(12.dp)),
        )
    }
}

@Composable
@Preview
fun InvestmentMethodScreenPreview() {
    VelvetTheme {
        InvestmentMethodContent()
    }
}
