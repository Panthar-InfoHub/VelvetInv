package org.sharad.velvetinvestment.shared.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.ktor.client.plugins.observer.ResponseObserver
import org.jetbrains.compose.resources.getSystemResourceEnvironment
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.greenColor
import org.sharad.emify.core.ui.theme.lightGray
import org.sharad.velvetinvestment.utils.genericDropShadow
import org.sharad.velvetinvestment.utils.theme.Poppins
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.fund_card_icon
import velvet.composeapp.generated.resources.sbi_tree

@Preview(showBackground = true)
@Composable
fun FundCard(modifier: Modifier= Modifier) {
    Box(
        modifier = modifier.height(169.dp).width(193.dp)
            .genericDropShadow(shape =RoundedCornerShape(24.dp)).background(color = Color.White, shape = RoundedCornerShape(24.dp)).padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(Res.drawable.sbi_tree),
                contentDescription = "Sbi icon",
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text("Sbi Gold Fund", fontFamily = Poppins, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(30.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("3Y CAGR", fontFamily = Poppins, color = Color.Gray)
                Text(
                    "6.39%",
                    fontFamily = Poppins,
                    color = greenColor,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

    }
}

