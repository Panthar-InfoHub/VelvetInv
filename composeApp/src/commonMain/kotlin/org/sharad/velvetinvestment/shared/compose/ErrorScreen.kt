package org.sharad.velvetinvestment.shared.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.sharad.velvetinvestment.shared.theme.Typography
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.error_img

@Composable
fun ErrorScreen(
    errorMessage: String,
    onRetryClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.error_img),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 28.dp)
        )

        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = errorMessage,
                style = Typography.displayLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            AppButton(
                text = "Try again",
                onClick = onRetryClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Prev(){
    VelvetTheme {
        ErrorScreen(errorMessage = "Something went wrong",{})
    }
}
