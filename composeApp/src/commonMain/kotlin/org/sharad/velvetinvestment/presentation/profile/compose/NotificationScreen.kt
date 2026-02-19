package org.sharad.velvetinvestment.presentation.profile.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ktor.websocket.Frame
import org.jetbrains.compose.resources.painterResource
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.arrowback_elements
import velvet.composeapp.generated.resources.push_notifications_rafiki_1

@Preview(showSystemUi = true)
@Composable
fun NotificationScreen() {
    var selected by remember { mutableStateOf("Alert") }
    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(30.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(Res.drawable.arrowback_elements),
                contentDescription = "ArrowBackIcon"
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(
                    text = "Notification centre",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF273E71)
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextBox(
                text = "Alert",
                isSelected = selected == "Alert",
                onSelect = { selected = "Alert" })
            TextBox(
                text = "Transaction",
                isSelected = selected == "Transaction",
                onSelect = { selected = "Transaction"})

        }

        Spacer(Modifier.height(100.dp))
        Image(
            painter = painterResource(Res.drawable.push_notifications_rafiki_1),
            contentDescription = "Push Notification Image",
            modifier = Modifier.height(300.dp).fillMaxWidth()
        )
    }
    }




@Composable
fun TextBox(text: String, isSelected: Boolean, onSelect: () -> Unit) {
    Box(modifier = Modifier.clickable{onSelect()}){
        Text(text = text, fontSize = 20.sp, color = if (isSelected) Color.Black else Color.Gray)

    }
}