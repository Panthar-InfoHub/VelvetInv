package org.sharad.velvetinvestment.shared.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.DrawableResource
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
    placeholder: String = "Search For Funds....",
    modifier: Modifier = Modifier,
    /** Set when the field must take focus on its own, e.g. the search overlay as it opens. */
    focusRequester: FocusRequester? = null,
    onSearchClick: () -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = value,
        onValueChange = {
            onTextChange(it)
        },
        modifier = modifier
            .height(52.dp)
            .then(if (focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier),
        shape = CircleShape,
        singleLine = true,
        placeholder = {
            Text(
                text = placeholder,
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

/**
 * Looks like [AppSearchBar] but takes no input: tapping it opens the search overlay, which owns
 * the real field. (JN's `AppSearchBarButton`, drawn in Velvet's search-bar style.)
 */
@Composable
fun AppSearchBarButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search For Funds....",
    /**
     * An optional action parked at the end of the bar — the fund list hangs its filter tray
     * here. It has its own click target, so tapping it does not also open the search.
     */
    trailingIcon: DrawableResource? = null,
    onTrailingIconClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(CircleShape)
            .background(bgColor4.copy(0.1f))
            .border(width = 1.dp, color = Primary, shape = CircleShape)
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .padding(start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = placeholder,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 14.sp),
            color = titleColor,
            modifier = Modifier.weight(1f)
        )

        if (trailingIcon != null) {
            Icon(
                painter = painterResource(trailingIcon),
                contentDescription = null,
                tint = Primary,
                modifier = Modifier
                    .size(22.dp)
                    .clickable(
                        onClick = { onTrailingIconClick?.invoke() },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        enabled = onTrailingIconClick != null
                    )
            )
        }

        Icon(
            painter = painterResource(Res.drawable.search_icon),
            contentDescription = null,
            tint = titleColor.copy(alpha = 0.5f),
            modifier = Modifier.height(22.dp)
        )
    }
}
