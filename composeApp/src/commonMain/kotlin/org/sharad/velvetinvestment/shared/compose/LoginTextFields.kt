package org.sharad.velvetinvestment.shared.compose

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LoginTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String? = null,
    placeHolder: String,
    modifier: Modifier = Modifier,
) {

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = MaterialTheme.typography.displayMedium,
        modifier = modifier.fillMaxWidth()
            .height(54.dp)
            .clip(RoundedCornerShape(15.dp))
            .border(
                width = 0.7.dp,
                shape = RoundedCornerShape(15.dp),
                color = Color(0xFFC5A572)
            ),
    ) {

        Box(
            modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (value.isEmpty()) {
                Text(
                    text = placeHolder,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xffC5C5C5)
                )
            }
            it()
        }

    }
}