package org.sharad.velvetinvestment.presentation.profile.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.sharad.velvetinvestment.shared.compose.ScreenHeader
import org.sharad.velvetinvestment.utils.theme.Poppins
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.plain_credit_card_rafiki_1
import velvet.composeapp.generated.resources.push_notifications_rafiki_1

@Preview(showSystemUi = true)
@Composable
fun LottieAnimationScreen(){
    Column (modifier=Modifier.fillMaxSize().padding(16.dp)){
        ScreenHeader(heading = "KYC")
        Image(
            painter = painterResource(Res.drawable.plain_credit_card_rafiki_1),
            contentDescription = "Push Notification Image",
            modifier = Modifier.height(250.dp).fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(30.dp))
        Text(text="Complete your KYC in Under few minutes to get started", fontSize = 20.sp, fontWeight = FontWeight.SemiBold, fontFamily = Poppins)

    Spacer(Modifier.height(10.dp))
    Text("Experience more for no co")}
}
