package org.sharad.velvetinvestment.presentation.LoginScreen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.backgroundGray
import org.sharad.velvetinvestment.utils.AuthMode

@Composable
fun AuthSwitch(
    authMode: AuthMode,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
) {

    val animatedOffset by animateDpAsState(
        targetValue = if (authMode==AuthMode.Login) 0.dp else 140.dp,
        label = "Sliding Window"
    )

    val animatedLoginColor by animateColorAsState(
        targetValue = if (authMode==AuthMode.Login) Primary else Primary.copy(0.6f),
    )

    val animatedSignUpColor by animateColorAsState(
        targetValue = if (authMode==AuthMode.SignUp) Primary else Primary.copy(0.6f),
    )

    Box(
        modifier=Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundGray),
        contentAlignment = Alignment.CenterStart
    ){
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.5f)
                .offset(animatedOffset)
                .padding(vertical = 6.dp, horizontal = 6.dp)
                .shadow(2.dp,RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White),
        )
        Row(
            modifier=Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(
                contentAlignment = Alignment.Center,
                modifier=Modifier.weight(1f).fillMaxHeight()
                    .clickable(
                        onClick={onLoginClick()},
                        interactionSource = remember{ MutableInteractionSource() },
                        indication = null
                    )
            ){
                Text(
                    text="Log In",
                    style = MaterialTheme.typography.headlineSmall,
                    color = animatedLoginColor,
                    textAlign = TextAlign.Center
                )
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier=Modifier.weight(1f).fillMaxHeight()
                    .clickable(
                        onClick={onSignUpClick()},
                        interactionSource = remember{ MutableInteractionSource() },
                        indication = null
                    )
            ){
                Text(
                    text="Sign Up",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    color = animatedSignUpColor
                )
            }
        }
    }
}