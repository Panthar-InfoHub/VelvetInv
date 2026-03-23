package org.sharad.velvetinvestment

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.sharad.velvetinvestment.presentation.kyc.compose.FileUploadScreen
import org.sharad.velvetinvestment.shared.Navigation.BaseNavigation
import org.sharad.velvetinvestment.shared.rememberWindowSize
import org.sharad.velvetinvestment.utils.Log
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.SnackBarType
import org.sharad.velvetinvestment.utils.theme.VelvetTheme

@Composable
fun App() {
    val size = rememberWindowSize()
    Log("size", size.toString())
    val snackbarHostState = remember { SnackbarHostState() }
    var currentSnackBar by remember { mutableStateOf<SnackBarType?>(null) }
    LaunchedEffect(Unit) {
        SnackBarController.snackBars.collect { snackBar ->
            currentSnackBar = snackBar
            snackbarHostState.showSnackbar(
                message = when (snackBar) {
                    is SnackBarType.Success -> snackBar.message
                    is SnackBarType.Error -> snackBar.message
                    is SnackBarType.Warning -> snackBar.message
                    is SnackBarType.Info -> snackBar.message
                    is SnackBarType.Neutral -> snackBar.message
                }
            )
        }
    }
    VelvetTheme {
        Scaffold(
            containerColor = Color.White,
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState
                )
                { data ->
                    val containerColor = when (currentSnackBar) {
                        is SnackBarType.Success -> Color(0xFF2E7D32)
                        is SnackBarType.Error -> Color(0xFFC62828)
                        is SnackBarType.Warning -> Color(0xFFF9A825)
                        is SnackBarType.Info -> Color(0xFF1565C0)
                        is SnackBarType.Neutral, null -> Color.DarkGray
                    }
                    Snackbar(
                        containerColor = containerColor,
                        contentColor = Color.White,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp,vertical = 8.dp)
                    ){
                        Text(
                            text = data.visuals.message,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.3.sp
                        )
                    }
                }
            }
        ) { paddingValues ->
//            BaseNavigation(
//                windowSize = size,
//                pv = paddingValues
//            )
            FileUploadScreen(
                onBack = {

                },
                onSuccessfulUpload = {

                }
            )
        }
    }
}