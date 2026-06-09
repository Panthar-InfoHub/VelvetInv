
package org.sharad.velvetinvestment.presentation.insurance

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.emify.core.ui.theme.LightGreen
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.healthColor
import org.sharad.emify.core.ui.theme.otherColor
import org.sharad.emify.core.ui.theme.termColor
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.presentation.insurance.viewmodel.InsuranceViewModel
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.BarHeader
import org.sharad.velvetinvestment.shared.compose.LoaderScreen
import org.sharad.velvetinvestment.shared.compose.ShadowCard
import org.sharad.velvetinvestment.shared.compose.VelvetLoader
import org.sharad.velvetinvestment.shared.theme.LocalVelvetShapes
import org.sharad.velvetinvestment.shared.theme.Poppins
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.arrow_right
import velvet.composeapp.generated.resources.ic_call
import velvet.composeapp.generated.resources.ic_whatsapp
import velvet.composeapp.generated.resources.icon_cross
import velvet.composeapp.generated.resources.icon_heart
import velvet.composeapp.generated.resources.nav_icon_incurance
import velvet.composeapp.generated.resources.success_icon

@Composable
fun InsuranceNavigationScreen(
    navigateToHealthInsurance: () -> Unit,
    navigateToTermInsurance: () -> Unit,
    navigateToOtherInsurance: () -> Unit,
    viewModel: InsuranceViewModel = koinViewModel()
) {
    var showSuccessDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.showThankYouDialog.collect {
            showSuccessDialog = true
        }
    }

    if (showSuccessDialog) {
        InsuranceThankYouBottomSheet(onDismiss = { showSuccessDialog = false })
    }

    if (viewModel.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize()
                .clickable{},
            contentAlignment = Alignment.Center
        ){
            VelvetLoader()
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        BackHeader("Insurance")
        LazyColumn(
            modifier = Modifier.weight(1f)
                .padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                BarHeader(heading = "Explore Insurance Options")
            }

            item {
                InsuranceEntry(
                    title = "Health Insurance",
                    subTitle = "Health Coverage Progress",
                    color = healthColor,
                    icon = Res.drawable.icon_heart,
                    onClick = navigateToHealthInsurance
                )
            }

            item {
                InsuranceEntry(
                    title = "Term Insurance",
                    subTitle = "Life Coverage Progress",
                    color = termColor,
                    icon = Res.drawable.nav_icon_incurance,
                    onClick = navigateToTermInsurance
                )
            }

            item {
                InsuranceEntry(
                    title = "Other Insurance",
                    subTitle = "Other Coverage Progress",
                    color = otherColor,
                    icon = Res.drawable.nav_icon_incurance,
                    onClick = navigateToOtherInsurance
                )
            }

            item {
                KnowMoreCard(
                    onRequestCallback = { viewModel.requestCallback() },
                    onChatOnWhatsapp = { viewModel.requestWhatsApp() }
                )
            }
        }
    }
}

@Composable
fun KnowMoreCard(
    modifier: Modifier = Modifier,
    onRequestCallback: () -> Unit,
    onChatOnWhatsapp: () -> Unit
) {
    ShadowCard(
        modifier = modifier.fillMaxWidth()
            .border(1.dp, Secondary, LocalVelvetShapes.current.roundedDp15),
        backgroundColor = Color(0xFFFFF9F0),
        shape = LocalVelvetShapes.current.roundedDp15
    ) {
        Column(
            modifier = Modifier
                .drawBehind {
                    drawLine(
                        color = Secondary,
                        start = Offset(0f, 0f),
                        end = Offset(0f, size.height),
                        strokeWidth = 6.dp.toPx()
                    )
                }
                .padding(start = 16.dp, end = 16.dp, top =48.dp, bottom = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Want to Know More?",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Get personalized guidance on your insurance needs and coverage gaps.",
                        style = titlesStyle,
                        color = Color(0xFF5A5E60),
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ContactActionCard(
                    modifier = Modifier.weight(1f),
                    title = "Request Callback",
                    subTitle = "We'll call you back",
                    icon = Res.drawable.ic_call,
                    backgroundColor = Color(0xFF14121E),
                    contentColor = Color.White,
                    onClick = onRequestCallback
                )

                WhatsAppActionCard(
                    modifier = Modifier.weight(1f),
                    title = "Chat on Whatsapp",
                    subTitle = "We'll call you back",
                    icon = Res.drawable.ic_whatsapp,
                    backgroundColor = Color.White,
                    contentColor = Color(0xFF1A1C1E),
                    iconTint = LightGreen,
                    onClick = onChatOnWhatsapp
                )
            }
        }
    }
}

@Composable
fun ContactActionCard(
    modifier: Modifier = Modifier,
    title: String,
    subTitle: String,
    icon: DrawableResource,
    backgroundColor: Color,
    contentColor: Color,
    iconTint: Color? = null,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(LocalVelvetShapes.current.roundedDp12)
            .clickable { onClick() },
        color = backgroundColor,
        shape = LocalVelvetShapes.current.roundedDp12,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (backgroundColor == Color.White) Color(0xFFF5F5F5) else Color.White.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = iconTint ?: contentColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    color = contentColor
                )
                Text(
                    text = subTitle,
                    fontSize = 8.sp,
                    lineHeight = 10.sp,
                    fontFamily = Poppins,
                    color = contentColor.copy(alpha = 0.6f)
                )
            }
        }
    }
}
@Composable
fun WhatsAppActionCard(
    modifier: Modifier = Modifier,
    title: String,
    subTitle: String,
    icon: DrawableResource,
    backgroundColor: Color,
    contentColor: Color,
    iconTint: Color? = null,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(LocalVelvetShapes.current.roundedDp12)
            .border(
                1.dp, Color(0xff22C55E), LocalVelvetShapes.current.roundedDp12
            )
            .clickable { onClick() },
        color = backgroundColor,
        shape = LocalVelvetShapes.current.roundedDp12,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = iconTint ?: contentColor,
                    modifier = Modifier.size(32.dp)
                )

            Column {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    lineHeight = 12.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    color = contentColor
                )
                Text(
                    text = subTitle,
                    fontSize = 8.sp,
                    lineHeight = 10.sp,
                    fontFamily = Poppins,
                    color = contentColor.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsuranceThankYouBottomSheet(
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(
            topStart = 28.dp,
            topEnd = 28.dp
        ),
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 32.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.icon_cross),
                        contentDescription = "Close",
                        tint = Color.Gray
                    )
                }
            }

            Image(
                painter = painterResource(Res.drawable.success_icon),
                contentDescription = null,
                modifier = Modifier.size(64.dp)
            )


            Text(
                text = "Thank you!",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            Text(
                text = "Our expert advisor will connect with you shortly.\nWe appreciate your interest.",
                style = titlesStyle,
                color = titleColor,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary
                )
            ) {
                Text(
                    text = "Got it",
                    fontSize = 18.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Preview
@Composable
fun KnowMoreCardPreview() {
    VelvetTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            KnowMoreCard(
                onRequestCallback = {},
                onChatOnWhatsapp = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InsuranceThankYouDialogPreview() {
    VelvetTheme {
        InsuranceThankYouBottomSheet(onDismiss = {})
    }
}

@Composable
fun InsuranceEntry(
    title:String,
    subTitle: String,
    color: Color,
    icon: DrawableResource,
    onClick:()->Unit
){
    ShadowCard(
        clickable = true,
        onClick = onClick
    ) {
        Row(
            modifier=Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(modifier=Modifier.size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(color.copy(0.1f)),
                contentAlignment = Alignment.Center
            ){
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(
                modifier= Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = subTitle,
                    style = titlesStyle,
                    color= titleColor
                )
            }

            Icon(
                painter = painterResource(Res.drawable.arrow_right),
                contentDescription = null,
                tint = titleColor,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}