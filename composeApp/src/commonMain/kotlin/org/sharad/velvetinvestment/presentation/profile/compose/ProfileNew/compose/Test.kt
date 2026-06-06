package org.sharad.velvetinvestment.presentation.profile.compose.ProfileNew.compose

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.sharad.velvetinvestment.shared.theme.Poppins

@Composable
fun DevSignaturePasswordDialog(
    visible: Boolean,
    onDismiss: () -> Unit,
) {
    if (!visible) return

    var password by remember { mutableStateOf("") }
    var showQuote by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
        ) {
            AnimatedContent(
                targetState = showQuote,
                label = "dev_signature_dialog"
            ) { revealed ->

                if (!revealed) {

                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(24.dp), Arrangement.spacedBy(16.dp)
                    ) {

                        Text(
                            text = "Restricted Access",
                            style = MaterialTheme.typography.titleLarge,
                            fontFamily = Poppins,

                        )

                        OutlinedTextField(
                            value = password,
                            onValueChange = {
                                password = it
                                isError = false
                            },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.NumberPassword
                            ),
                            isError = isError,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {

                            TextButton(
                                onClick = onDismiss
                            ) {
                                Text("Cancel")
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = {
                                    if (password == "2907") {
                                        showQuote = true
                                    } else {
                                        isError = true
                                    }
                                }
                            ) {
                                Text("Unlock")
                            }
                        }
                    }

                } else {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 24.dp,
                                vertical = 32.dp
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "\"Powered by caffeine, coroutines and questionable sleep schedules.\"",
                                style = MaterialTheme.typography.titleMedium,
                                fontFamily = Poppins,
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = "— Savy",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "\"Finding joy in steady progress (and sometimes in logs).\"",
                                style = MaterialTheme.typography.titleMedium,
                                fontFamily = Poppins,
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = "— Ayano",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                }
            }
        }
    }
}