package org.sharad.velvetinvestment.presentation.mutualfund.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.shared.theme.Poppins
import org.sharad.velvetinvestment.shared.theme.VelvetTheme

@Composable
fun BundleCard(
    title: String,
    minAmount: String,
    onClick: () -> Unit,
    modifier: Modifier= Modifier
){
    val shape = remember { RoundedCornerShape(12.dp) }
    Box(
        modifier= modifier.fillMaxWidth()
            .genericDropShadow()
            .clip(shape)
            .border(1.dp, Color(0xffC4C6D1).copy(0.3f),shape)
            .background(Color.White)
            .clickable(onClick = onClick)
    ){
        Column(
            modifier= Modifier.fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                fontFamily = Poppins,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Primary,
                maxLines = 2
            )

            Text(
                text = "MIN . INVESTMENT",
                fontFamily = Poppins,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xff43474F)
            )

            Text(
                text = minAmount,
                fontFamily = Poppins,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xff151C27),
                modifier= Modifier.padding(top = 8.dp)
            )


        }
    }
}

@Composable
@Preview(showBackground = true)
private fun Prev(){
    VelvetTheme {
        BundleCard(title = "Velvet Preserve", minAmount = "1,000", onClick = {})
    }
}