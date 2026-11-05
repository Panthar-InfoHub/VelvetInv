package org.sharad.velvetinvestment.features.core.ui.otp

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.TextGray

@Composable
fun OtpInputField(
    otpValue: String,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
    otpLength: Int = OtpUiState.DEFAULT_OTP_LENGTH,
    shape: Shape = CircleShape
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val onBoxClick: () -> Unit = remember {
        {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    val otpTextStyle = MaterialTheme.typography.headlineMedium.copy(
        fontWeight = FontWeight.Medium,
    )

    BasicTextField(
        value = otpValue,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier
            .focusRequester(focusRequester),
        decorationBox = {},
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        repeat(otpLength) { index ->
            val char = otpValue.getOrNull(index)

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(shape)
                    .border(
                        width = 1.dp,
                        color = if (char != null) Secondary else Color(0xFFE4E1E7),
                        shape = shape,
                    )
                    .clickable(onClick = onBoxClick),
                contentAlignment = Alignment.Center,
            ) {
                if (char != null) {
                    Text(
                        text = char.toString(),
                        style = otpTextStyle,
                        color = TextGray,
                    )
                }
            }
        }
    }
}
