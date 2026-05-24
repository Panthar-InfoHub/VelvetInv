package org.sharad.velvetinvestment.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import org.sharad.velvetinvestment.shared.theme.Inter


@Composable
fun String.withInterRupee(): AnnotatedString {
    val inter = Inter
    return buildAnnotatedString {
        this@withInterRupee.forEach { char ->

            if (char == '₹') {
                withStyle(
                    SpanStyle(
                        fontFamily = inter
                    )
                ) {
                    append(char)
                }
            } else {
                append(char)
            }
        }
    }
}