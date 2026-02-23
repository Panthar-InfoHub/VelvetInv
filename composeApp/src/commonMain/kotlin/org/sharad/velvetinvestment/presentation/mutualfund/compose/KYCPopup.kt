package org.sharad.velvetinvestment.presentation.mutualfund.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.appRed
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.shared.compose.AppButton
import org.sharad.velvetinvestment.utils.theme.titlesStyle
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.icon_user

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KYCPopup(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onClick: () -> Unit,
){

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxWidth(),
        sheetState=sheetState,
        dragHandle = null,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ){
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 28.dp, horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier.size(94.dp)
                    .clip(CircleShape)
                    .background(appRed.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ){
                Icon(
                    painter = painterResource(Res.drawable.icon_user),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp),
                    tint = appRed
                )
            }

            Text(
                text="Verify Identity (KYC)\n" +
                        "Just Few Steps",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.Black
            )

            Text(
                text="You’re a few steps away from starting your investment journey with us.",
                style = titlesStyle,
                color= titleColor
            )

            AppButton(
                modifier=Modifier.fillMaxWidth(),
                onClick = {
                    onClick()
                },
                text = "Complete your application",
            )

        }
    }

}