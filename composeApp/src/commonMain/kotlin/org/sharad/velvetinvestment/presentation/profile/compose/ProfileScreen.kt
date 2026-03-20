package org.sharad.velvetinvestment.presentation.profile.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.data.remote.repository.UserAuthenticationRepo
import org.sharad.velvetinvestment.domain.repository.UserAuth
import org.sharad.velvetinvestment.shared.compose.BarHeader
import org.sharad.velvetinvestment.shared.compose.ShadowCard
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.SnackBarType
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess
import org.sharad.velvetinvestment.utils.theme.Poppins
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.back_icon
import velvet.composeapp.generated.resources.fd_icon
import velvet.composeapp.generated.resources.notification_icon
import velvet.composeapp.generated.resources.profile_icon
import velvet.composeapp.generated.resources.security
import velvet.composeapp.generated.resources.settings_icon
import velvet.composeapp.generated.resources.signupelement
import velvet.composeapp.generated.resources.termcondition

@Composable
fun ProfileScreen(
    navigateToNotification: () -> Unit,
    navigateToPersonalInfo: () -> Unit,
    onSignOut: () -> Unit
) {
    val authRepo = koinInject<UserAuth>()
    val scope= rememberCoroutineScope()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(bottom = 60.dp)
    ) {


        item {
            ProfileTopBar()
        }

        item {
            ProfileHeader("")
        }

        item{ Spacer(Modifier) }

        item {
            BarHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                "Account Setting"
            )
        }

        item {
            AccountSettingsCard(
                onPersonalInfoClick=navigateToPersonalInfo
            )
        }

        item {
            BarHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                "Preferences"
            )
        }

        item {
            PreferencesCard(
                onNotificationClick = navigateToNotification
            )
        }

        item {
            BarHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                "Legal"
            )
        }

        item {
            LegalCard()
        }

        item {
            SignOutButton(
                onClick={
                    scope.launch {
                        authRepo.signOut()
                            .onSuccess {
                                onSignOut()
                            }
                            .onError {
                                SnackBarController.showSnackBar(SnackBarType.Error(it.message))
                            }
                    }
                }
            )
        }
    }
  }

@Composable
fun ProfileTopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {


        Text(
            text = "Profile",
            fontWeight = FontWeight.SemiBold,
            fontFamily = Poppins,
            fontSize = 22.sp,
            color = Color(0xFF273E71)
        )

        ShadowCard(
            modifier = Modifier
                .size(52.dp)
                .align( Alignment.CenterEnd ),
            shape = CircleShape,
            onClick = {}
        ) {
            Icon(
                painter = painterResource(Res.drawable.settings_icon),
                contentDescription = "Setting Icon",
                tint = Color(0xFFD8AF6B)
            )
        }
    }
}

@Composable
fun ProfileHeader(image:String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(120.dp)
                .background(color = Color.LightGray, shape = CircleShape),
            contentAlignment = Alignment.Center
        ){
//            AsyncImage(
//                modifier = Modifier.fillMaxSize(),
//                contentDescription = null,
//                model = image,
//                fallback = painterResource(Res.drawable.profile_icon),
//                error = painterResource(Res.drawable.profile_icon),
//                placeholder = painterResource(Res.drawable.profile_icon)
//            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "Pooja Sharma",
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp
        )
    }
}

@Composable
fun AccountSettingsCard(onPersonalInfoClick: () -> Unit) {
    ShadowCard(
        modifier = Modifier.padding(horizontal = 16.dp),
    ) {
        Column(modifier = Modifier)
        {

            // Personal Information
            RowItem(
                icon = Res.drawable.profile_icon,
                title = "Personal Information",
                subtitle = "Your details and contact info",
                onCLick = onPersonalInfoClick
            )

            HorizontalDivider(color = Color.LightGray)

            // Bank Account
            RowItem(
                icon = Res.drawable.fd_icon,
                title = "Bank Account",
                subtitle = "Manage Linked funding source"
            )
        }
    }
}

@Composable
fun PreferencesCard(onNotificationClick: () -> Unit) {
    ShadowCard(
        modifier = Modifier.padding(horizontal = 16.dp)
        ) {
        Column(modifier = Modifier) {
            RowItem(
                icon = Res.drawable.notification_icon,
                title = "Notifications",
                subtitle = "Alert, updates & news",
                onCLick = onNotificationClick
            )
        }
    }
}

@Composable
fun LegalCard() {
    ShadowCard(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier) {

            RowItem(
                icon = Res.drawable.termcondition,
                title = "Terms & Condition"
            )

            HorizontalDivider(color = Color.LightGray)

            RowItem(
                icon = Res.drawable.security,
                title = "Privacy Policy"
            )
        }
    }
}

@Composable
fun RowItem(
    icon: DrawableResource,
    title: String,
    subtitle: String? = null,
    onCLick: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable{onCLick()},
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal=16.dp, vertical = 12.dp))
        {

            Box(
                modifier=Modifier.size(44.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(
                        color = Color(0xFFDEE2F6),
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ){
                Icon(
                    painter = painterResource(icon),
                    contentDescription = title,
                    tint= Primary,
                    modifier = Modifier
                        .size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            Column(
                modifier=Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontFamily = Poppins,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                subtitle?.let {
                    Text(
                        text = it,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = Poppins,
                        color = titleColor
                    )
                }
            }
            Icon(
                painter = painterResource(Res.drawable.back_icon),
                contentDescription = "Forward Icon",
                tint = Color(0xFFD2B077)
            )
        }
    }
}

@Composable
fun SignOutButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 35.dp)
    ) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF0600).copy(0.1f),
                contentColor = Color(0xFFFF0600)
            ),
            modifier = Modifier
                .weight(1f)
                .height(50.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.signupelement),
                contentDescription = "Signup Button Icon"
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text("Sign Out",
                fontFamily = Poppins,
                fontSize = 20.sp)
        }
    }
}