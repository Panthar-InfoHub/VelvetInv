package org.sharad.velvetinvestment.shared.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.velvetinvestment.utils.theme.Poppins

@Composable
fun OtpGrid(
    modifier: Modifier,
    otpLength: Int,
    otp: String,
    onOtpChange: (String) -> Unit
) {

    val focusRequesters = remember {
        List(otpLength) { FocusRequester() }
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        repeat(otpLength) { index ->

            val char = otp.getOrNull(index)?.toString() ?: ""

            Surface(
                modifier = Modifier.size(52.dp),
                color = Color.Transparent,
                shape = RoundedCornerShape(20),
                border = BorderStroke(
                    1.dp,
                    if (char.isEmpty())
                        Color(0xff12110D).copy(alpha = 0.4f)
                    else Secondary
                )
            ) {

                BasicTextField(
                    modifier = Modifier
                        .focusRequester(focusRequesters[index])
                        .onKeyEvent {
                            if (it.key == Key.Backspace) {
                                if (otp.isNotEmpty()) {
                                    onOtpChange(otp.dropLast(1))
                                }
                                true
                            } else false
                        },
                    value = char,
                    onValueChange = { input ->

                        if (input.all { it.isDigit() }) {
                            val newOtp =
                                if (index < otp.length)
                                    otp.replaceRange(index, index + 1, input)
                                else
                                    otp + input

                            if (newOtp.length <= otpLength) {
                                onOtpChange(newOtp)

                                if (index < otpLength - 1) {
                                    focusRequesters[index + 1].requestFocus()
                                }
                            }
                        }
                    },
                    textStyle = TextStyle(
                        fontFamily = Poppins,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        it()
                    }
                }
            }
        }
    }
}