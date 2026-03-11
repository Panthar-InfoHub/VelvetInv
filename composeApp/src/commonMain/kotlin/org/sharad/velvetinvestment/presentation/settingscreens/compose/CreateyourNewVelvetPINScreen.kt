package org.sharad.velvetinvestment.presentation.settingscreens.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.serialization.generateRouteWithArgs
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.darkBlue
import org.sharad.emify.core.ui.theme.grayColor
import org.sharad.velvetinvestment.utils.theme.Poppins
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.arrowback_elements
import kotlin.coroutines.ContinuationInterceptor




@Composable
fun CreateyourNewVelvetPINScreen(){
    Column (modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp).statusBarsPadding().navigationBarsPadding()){
        Row (modifier = Modifier.padding(vertical = 32.dp).fillMaxWidth()){
            Icon(painter = painterResource(Res.drawable.arrowback_elements), contentDescription = "arrowback")
        }

        LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            item {
                Text(
                    "Create your new Velvet PIN",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    fontFamily = Poppins
                )
            }

            item {
                Text("For enhanced security of your finances, please set a new PIN. You’ll need to enter this PIN each time you access the app.", fontFamily = Poppins, fontSize = 14.sp, color = Color(0xff12110D))
            }

            item {
                val otpLength = 4

                val otpValues = remember {
                    List(otpLength) { mutableStateOf("") }
                }

                val focusRequesters = remember {
                    List(otpLength) { FocusRequester() }
                }

                    OtpGrid(modifier = Modifier,
                        otpLength = otpLength,
                        otpValues = otpValues,
                        focusRequesters = focusRequesters,
                    )


                }
            }
        TextButton(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 10.dp).fillMaxWidth().height(50.dp),
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary,
                contentColor = Color.White
            )
        ) {
            Text(
                "Get OTP",
                fontWeight = FontWeight.Medium,
                fontFamily = Poppins,
                fontSize = 18.sp
            )
        }
            }
        }


@Composable
fun OtpGrid(
    modifier: Modifier,
    otpLength: Int,
    otpValues: List<MutableState<String>>,
    focusRequesters: List<FocusRequester>
){


    Row(modifier=modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        otpValues.forEachIndexed{index, state->

            val appColor = Primary
            Surface(modifier=modifier.size(56.dp),
                color = Color.Transparent,
                shape = RoundedCornerShape(20),
                border = BorderStroke(1.dp, if(state.value.isEmpty()){ Color(0xff12110D).copy(alpha = 0.4f) } else Primary)
            ){
                val RobotoFontFamily = null
                BasicTextField(
                    modifier=modifier.focusRequester(focusRequesters[index])
                        .onKeyEvent{
                            if(it.key == Key.Backspace){
                                if(state.value.isEmpty() && index>0){
                                    otpValues[index-1].value=""
                                    focusRequesters[index-1].requestFocus()
                                    true
                                }
                                else{
                                    state.value=""
                                    true
                                }
                            }
                            false
                        },
                    value = state.value,
                    onValueChange = {newInput->
                        if (newInput.length == otpLength && newInput.all { it.isDigit() }) {
                            // Paste handling: only accept numeric input
                            newInput.forEachIndexed { i, c ->
                                if (i < otpLength) otpValues[i].value = c.toString()
                            }

                            focusRequesters[otpLength-1].requestFocus()
                        } else if (newInput.length == 1 && newInput[0].isDigit()) {
                            state.value = newInput
                            if (index < otpLength - 1)
                                focusRequesters[index + 1].requestFocus()
                        }
                    },
                    textStyle = TextStyle(
                        fontFamily = RobotoFontFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone)
                ){
                    Box(modifier=modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                        it()
                    }
                }
            }
        }
    }
}