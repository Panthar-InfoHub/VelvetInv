package org.sharad.velvetinvestment.presentation.profile.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.darkBlue
import org.sharad.emify.core.ui.theme.goldenColor
import org.sharad.velvetinvestment.shared.compose.GoalsCard
import org.sharad.velvetinvestment.shared.compose.ScreenHeader
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.calender
import velvet.composeapp.generated.resources.elements
import velvet.composeapp.generated.resources.rupeesign
import velvet.composeapp.generated.resources.vector__6_
@Preview(showSystemUi = true)
@Composable
fun FinancialMarriageScreen() {
    var textFieldValue by remember { mutableStateOf("") }
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
    ) {
        // 1. Header Section
        item {
            ScreenHeader("Custom Goal")
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 2. Goal Selection Section
        item {
            Text("Goal Name", fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(20.dp))
            SelectionDisplay(
                icon = Res.drawable.elements,
                label = "Retirement",
                color = goldenColor
            )
        }

        // 3. Input Fields
        item {
            TextFieldComposable(
                "Target Year *",
                value = "2030",
                icon = Res.drawable.calender,
                color = goldenColor
            )
            TextFieldComposable(
                "Target Amount *",
                value = "100000",
                icon = Res.drawable.rupeesign,
                color = goldenColor
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        // 4. Inflation Section
        item {
            Text(
                "Default Inflation Rate(% per annum)",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(20.dp))
            SelectionDisplay(
                icon = Res.drawable.vector__6_,
                label = "6 %",
                color = goldenColor,
                labelColor = goldenColor
            )
            Spacer(modifier = Modifier.height(30.dp))
        }

        // 5. Projection Section
        item {
            HorizontalDivider(thickness = 2.dp, color = goldenColor)
            Spacer(modifier = Modifier.height(15.dp))
            Text("Projected Impact", fontSize = 20.sp, fontWeight = FontWeight.W600)
            Spacer(modifier = Modifier.height(15.dp))
            GoalsCard(color =goldenColor )
            Spacer(modifier = Modifier.height(20.dp))
        }

        // 6. Action Button
        item {
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(darkBlue),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp)
            ) {
                Text("Set Your First Goal", fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }

}
    @Composable
    fun SelectionDisplay(
        icon: DrawableResource,
        label: String,
        color: Color,
        labelColor: Color = Color.Unspecified
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, color = color, shape = RoundedCornerShape(12.dp))
                .height(70.dp)
                .background(color = Color.White, shape = RoundedCornerShape(12.dp))
                .padding(15.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier
                        .background(color.copy(0.1f), RoundedCornerShape(8.dp))
                        .padding(8.dp)
                )
                Spacer(Modifier.width(15.dp))
                Text(label, fontWeight = FontWeight.Medium, fontSize = 16.sp, color = labelColor)
            }
        }
    }
