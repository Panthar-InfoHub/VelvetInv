package org.sharad.velvetinvestment.presentation.onboarding.compose.financialflow

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.sharad.velvetinvestment.utils.theme.Poppins
import org.sharad.velvetinvestment.utils.theme.subHeadingMedium

@Composable
fun MoneyTextField(
    value:String,
    onValueChange:(String)->Unit,
    placeHolder:String,
    label:String,
    mandatory: Boolean=false,
    modifier: Modifier = Modifier
){

    Column(
        modifier=modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row (
            verticalAlignment = Alignment.Top
        ){
            Text(
                text=label,
                style = subHeadingMedium,
                color = Color.Black
            )
            if (mandatory){
                Text(
                    text = "*",
                    color = Color.Red,
                    style = subHeadingMedium
                )
            }
        }

        BasicTextField(
            value = value,
            onValueChange = {it-> onValueChange(it) },
            singleLine = true,
            textStyle = TextStyle(
                fontFamily = Poppins,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = modifier.fillMaxWidth()
                .height(54.dp)
                .shadow(elevation = 8.dp,RoundedCornerShape(15.dp), spotColor = Color.Black.copy(alpha = 0.4f))
                .clip(RoundedCornerShape(15.dp))
                .background(Color.White, RoundedCornerShape(15.dp))
                .border(
                    width = 0.7.dp,
                    shape = RoundedCornerShape(15.dp),
                    color = Color(0xFFC5A572)
                ),
        ) {
            Row(
                modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
               horizontalArrangement  = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "₹",
                    fontFamily = Poppins,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier=Modifier.padding(start = 8.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 12.dp)
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeHolder,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xffC5C5C5)
                        )
                    }
                    it()
                }
            }
        }
    }
}