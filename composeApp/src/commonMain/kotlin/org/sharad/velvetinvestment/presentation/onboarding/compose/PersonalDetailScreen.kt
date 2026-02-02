package org.sharad.velvetinvestment.presentation.onboarding.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import org.sharad.velvetinvestment.presentation.onboarding.models.PersonalDetails
import org.sharad.velvetinvestment.shared.AppButton

@Composable
fun PersonalDetailScreen(
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onCityChange: (String) -> Unit,
    onDobChange: (Long) -> Unit,
    onNext: () -> Unit,
    details: PersonalDetails,
    modifier: Modifier = Modifier,
    pv: PaddingValues
){
    Box(modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter){

        LazyColumn(
            modifier=Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                OnBoardingTextField(
                    value = details.fullName,
                    onValueChange = onNameChange,
                    placeHolder = "Enter your full Name",
                    label = "Full Name",
                    mandatory = true
                )
            }
            item {
                OnBoardingTextField(
                    value = details.city,
                    onValueChange = onCityChange,
                    placeHolder = "Enter the city",
                    label = "City",
                    mandatory = true
                )
            }
            item {
                OnBoardingTextField(
                    value = details.phoneNumber,
                    onValueChange = onPhoneChange,
                    placeHolder = "10-digit mobile number",
                    label = "Mobile Number ",
                    mandatory = true
                )
            }
            item {
                OnBoardingTextField(
                    value = details.email,
                    onValueChange = onEmailChange,
                    placeHolder = "your.email@gmail.com",
                    label = "Email Address ",
                    mandatory = true
                )
            }
            item {
                OnBoardingTextField(
                    value = "",
                    onValueChange = onNameChange,
                    placeHolder = "dd/mm/yy",
                    label = "DOB ",
                    mandatory = true
                )
            }
        }

        NextButtonFooter(
            onClick={},
            pv=pv
        )

    }
}

@Composable
fun NextButtonFooter(onClick: () -> Unit, pv: PaddingValues) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .shadow(elevation = 28.dp)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ){
        AppButton(
            modifier = Modifier.fillMaxWidth().padding(start = 28.dp, end = 28.dp, top = 20.dp, bottom = 16.dp+pv.calculateBottomPadding()),
            onClick = onClick,
            text = "Next"
        )
    }
}