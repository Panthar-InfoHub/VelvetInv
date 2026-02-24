package org.sharad.velvetinvestment.presentation.profile.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.blueColor
import org.sharad.emify.core.ui.theme.darkBlue
import org.sharad.velvetinvestment.shared.compose.GoalsCard
import org.sharad.velvetinvestment.shared.compose.ScreenHeader
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.calender
import velvet.composeapp.generated.resources.elements
import velvet.composeapp.generated.resources.rupeesign
import velvet.composeapp.generated.resources.vector__6_

@Preview(showSystemUi = true)
@Composable
fun FinancialRetirementScreen() {
    var textFieldValue by remember { mutableStateOf("") }
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)// Handles outer padding for all items
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

            GoalDisplayBox(
                icon = Res.drawable.elements,
                label = "Retirement",
                color = blueColor
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 3. Target Inputs Section
        item {
            TextFieldComposable(
                text = "Target Year *",
                value = "2030",
                icon = Res.drawable.calender,
                color = blueColor
            )
            TextFieldComposable(
                text = "Target Amount *",
                value = "100000",
                icon = Res.drawable.rupeesign,
                color = blueColor
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

            GoalDisplayBox(
                icon = Res.drawable.vector__6_,
                label = "6 %",
                color = blueColor,
                textColor = blueColor
            )
            Spacer(modifier = Modifier.height(30.dp))
        }

        // 5. Impact Analysis Section
        item {
            HorizontalDivider(thickness = 2.dp, color = blueColor)
            Spacer(modifier = Modifier.height(15.dp))

            Text("Projected Impact", fontSize = 20.sp, fontWeight = FontWeight.W600)
            Spacer(modifier = Modifier.height(15.dp))

            GoalsCard(color=blueColor)
            Spacer(modifier = Modifier.height(20.dp))
        }

        // 6. Final Action Section
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { /* Handle Save */ },
                    colors = ButtonDefaults.buttonColors(darkBlue),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Set Your First Goal", fontSize = 18.sp)
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

