package org.sharad.velvetinvestment.presentation.fixeddeposits.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.sharad.velvetinvestment.shared.theme.Poppins
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.blostom_logo

@Composable
fun BlostemFooter(
    modifier: Modifier = Modifier,
){
    Row(
        modifier= modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text="Powered By   ",
            fontSize = 10.sp,
            fontFamily = Poppins,
            color = Color(0xff656565)
        )

        Icon(
            painter = painterResource(Res.drawable.blostom_logo),
            contentDescription = "Blostem Logo",
            modifier= Modifier.size(24.dp)
        )

        Text(
            text="   Blostem",
            fontFamily = Poppins,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

    }
}

@Preview(showBackground = true)
@Composable
fun BlostemFooterPreview() {
    VelvetTheme {
        BlostemFooter()
    }
}