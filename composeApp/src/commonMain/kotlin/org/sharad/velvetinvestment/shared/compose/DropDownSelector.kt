package org.sharad.velvetinvestment.shared.compose

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.dropdown_icon

@Composable
fun <T> DropDownSelector(
    title: String = "",
    value: String,
    onValueChange: (T) -> Unit,
    placeholder: String,
    mandatory: Boolean = false,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    list: List<T>,
    textConvertor: (T) -> String,
    highlighted: Boolean = false
) {
    var extended by remember { mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.spacedBy(4.dp), modifier = modifier) {
        if (title.isNotEmpty()) {
            Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xff434843)
                )
                if (mandatory) {
                    Text(
                        text = "*",
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                .background(Color.White, RoundedCornerShape(15.dp))
                .border(
                    width = if (highlighted) OutlinedTextFieldDefaults.FocusedBorderThickness else 0.7.dp,
                    shape = RoundedCornerShape(15.dp),
                    color = if (highlighted) Secondary else Color(0xFFE4E1E7)
                )
                .animateContentSize()
        ) {
            GenericDropDownHeader(
                value = value,
                placeHolder = placeholder,
                onClick = { extended = !extended },
                extended = extended,
                leadingIcon = leadingIcon
            )
            if (extended) {
                GenericDropDownContent(
                    list = list,
                    textConvertor = textConvertor,
                    onSelected = { item ->
                        onValueChange(item)
                        extended = false
                    }
                )
            }
        }
    }
}

@Composable
private fun <T> GenericDropDownContent(
    onSelected: (T) -> Unit,
    list: List<T>,
    textConvertor: (T) -> String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
    ) {
        list.forEach { item ->
            Text(
                text = textConvertor(item),
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    .clickable(
                        onClick = { onSelected(item) }
                    )
            )
        }
    }
}

@Composable
private fun GenericDropDownHeader(
    value: String,
    placeHolder: String,
    onClick: () -> Unit,
    extended: Boolean,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    val animatedIcon by animateFloatAsState(
        targetValue = if (extended) 180f else 0f,
        label = "arrow"
    )
    Row(
        modifier = Modifier.fillMaxWidth()
            .height(54.dp)
            .clickable(
                onClick = { onClick() }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingIcon != null) {
            Row(modifier = Modifier.padding(start = 16.dp)) { leadingIcon() }
        }
        Text(
            text = (value.ifEmpty { placeHolder }).capitalize(Locale.current),
            style = MaterialTheme.typography.labelSmall,
            color = if (value.isBlank()) Color(0xFF9E9E9E) else Primary,
            modifier = Modifier
                .weight(1f)
                .padding(start = if (leadingIcon != null) 8.dp else 16.dp)
        )
        Icon(
            painter = painterResource(Res.drawable.dropdown_icon),
            contentDescription = null,
            modifier = Modifier.padding(end = 24.dp).rotate(animatedIcon),
            tint = Primary
        )
    }
}
