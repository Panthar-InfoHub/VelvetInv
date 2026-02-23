package org.sharad.velvetinvestment.shared.compose

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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
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

    OutlinedTextField(
        value = value,
        onValueChange = {
            onTextChange(it)
        },
        modifier = modifier.height(50.dp),
        shape = CircleShape,
        singleLine = true,
        placeholder = { Text(text="Search For Funds....", style = MaterialTheme.typography.labelSmall, color = titleColor) },
        textStyle = MaterialTheme.typography.labelSmall,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Primary,
            unfocusedBorderColor = Primary,
            cursorColor = Primary,
            focusedTextColor = Primary,
            unfocusedTextColor = Primary,
            focusedContainerColor = bgColor4.copy(0.1f),
            unfocusedContainerColor = bgColor4.copy(0.1f)
        ),
        leadingIcon = { Icon(
            painter = painterResource(Res.drawable.search_icon),
            contentDescription = null,
            tint = Primary,
            modifier = Modifier.padding(start = 4.dp).height(22.dp)
        )
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearchClick() })
    )

}