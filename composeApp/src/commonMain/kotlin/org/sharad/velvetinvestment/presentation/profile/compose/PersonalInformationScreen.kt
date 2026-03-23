package org.sharad.velvetinvestment.presentation.profile.compose

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.utils.theme.Poppins
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.penvector

@Composable
fun PersonalInformationScreen(onBack: () -> Unit, pv: PaddingValues) {
    var name by remember { mutableStateOf("Pooja Sharma") }
    var dateOfBirth by remember { mutableStateOf("**/**/2002") }
    var mobileNumber by remember { mutableStateOf("*****90909") }
    var emailAddress by remember { mutableStateOf("poo*****01@gmail.com") }
    var panNumber by remember { mutableStateOf("******701H") }
    var incomeRange by remember { mutableStateOf("Below 1 Lac") }
    Column(
        modifier = Modifier.fillMaxSize(),
    ){
        BackHeader(
            heading = "Personal Information",
            showBack = true,
            onBackClick = onBack
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {

            item {
                CommonFormField(
                    label = "Full Name (as on PAN card)",
                    value = name,
                    onValueChange = { name = it }
                )
            }
            item {
                CommonFormField(
                    label = "Date of Birth",
                    value = dateOfBirth,
                    onValueChange = { dateOfBirth = it },
                    keyboardType = KeyboardType.Number
                )
            }

            item {
                CommonFormField(
                    label = "Mobile Number",
                    value = mobileNumber,
                    onValueChange = { mobileNumber = it },
                    keyboardType = KeyboardType.Phone,
                    showTrailingIcon = true
                )
            }

            item {
                CommonFormField(
                    label = "Email Address",
                    value = emailAddress,
                    onValueChange = { emailAddress = it },
                    keyboardType = KeyboardType.Email,
                    showTrailingIcon = true
                )
            }

            item {
                CommonFormField(
                    label = "PAN Number",
                    value = panNumber,
                    onValueChange = { panNumber = it }
                )
            }

            item {
                CommonFormField(
                    label = "Income Range",
                    value = incomeRange,
                    onValueChange = { incomeRange = it },
                    showTrailingIcon = true
                )
            }
        }
    }

}

@Composable
fun CommonFormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    showTrailingIcon: Boolean = false
) {
    Column(modifier = modifier) {

        Text(
            text = label,
            fontFamily = Poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            textStyle = TextStyle(
                fontFamily = Poppins,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            ),
            trailingIcon = {
                if (showTrailingIcon) {
                    Icon(
                        painter = painterResource(Res.drawable.penvector),
                        contentDescription = "Edit Icon"
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .genericDropShadow(RoundedCornerShape(14.dp))
                .border(
                    1.dp,
                    color = Secondary,
                    shape = RoundedCornerShape(14.dp)
                )
                .clip(RoundedCornerShape(14.dp))
        )

        Spacer(modifier = Modifier.height(18.dp))
    }
}