package org.sharad.velvetinvestment.shared.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.SnackBarType
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.icon_cross

@Composable
fun AppSnackbarHost() {

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
                },
                withDismissAction = true
            )
        }
    }

    SnackbarHost(hostState = snackbarHostState) { data ->

        val containerColor = when (currentSnackBar) {
            is SnackBarType.Success -> Color(0xFF2E7D32)
            is SnackBarType.Error -> Color(0xFFC62828)
            is SnackBarType.Warning -> Color(0xFFF9A825)
            is SnackBarType.Info -> Color(0xFF1565C0)
            is SnackBarType.Neutral, null -> Color.DarkGray
        }

        Snackbar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            containerColor = containerColor,
            contentColor = Color.White
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = data.visuals.message,
                    modifier = Modifier.weight(1f),
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium,
                    letterSpacing = 0.3.sp
                )

                IconButton(
                    onClick = data::dismiss
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.icon_cross),
                        contentDescription = "Dismiss",
                        tint = Color.White
                    )
                }
            }
        }
    }
}