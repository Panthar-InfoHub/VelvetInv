package org.sharad.velvetinvestment.shared.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.bgColor4
import org.sharad.emify.core.ui.theme.titleColor
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.search_icon

@Composable
fun AppSearchBar(
    onTextChange: (String) -> Unit,
    value: String,
    modifier: Modifier = Modifier,
    onSearchClick: () -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = value,
        onValueChange = {
            onTextChange(it)
        },
        modifier = modifier.height(52.dp),
        shape = CircleShape,
        singleLine = true,
        placeholder = {
            Text(
                text = "Search For Funds....",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 14.sp),
                color = titleColor
            )
        },
        textStyle = MaterialTheme.typography.labelSmall.copy(fontSize = 14.sp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Primary,
            unfocusedBorderColor = Primary,
            cursorColor = Primary,
            focusedTextColor = Primary,
            unfocusedTextColor = Primary,
            focusedContainerColor = bgColor4.copy(0.1f),
            unfocusedContainerColor = bgColor4.copy(0.1f)
        ),
        trailingIcon = {
            Icon(
                painter = painterResource(Res.drawable.search_icon),
                contentDescription = null,
                tint = if (value.isEmpty()) titleColor.copy(alpha = 0.5f) else Primary,
                modifier = Modifier.padding(end = 4.dp).height(22.dp)
                    .clickable(
                        enabled = value.isNotBlank(),
                    ){
                        onSearchClick()
                        keyboardController?.hide()
                    }
            )
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            onSearchClick()
            keyboardController?.hide()
        }
        )
    )

}