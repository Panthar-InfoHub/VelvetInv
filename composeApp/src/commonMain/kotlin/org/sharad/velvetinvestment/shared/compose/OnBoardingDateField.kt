package org.sharad.velvetinvestment.shared.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.velvetinvestment.utils.theme.subHeadingMedium
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.icon_callender

@Composable
fun OnBoardingDateField(
    value: String,
    placeHolder: String,
    label: String,
    mandatory: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Row(verticalAlignment = Alignment.Top) {

            Text(
                text = label,
                style = subHeadingMedium,
                color = Color.Black
            )

            if (mandatory) {
                Text(
                    text = "*",
                    color = Color.Red,
                    style = subHeadingMedium
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(15.dp),
                    spotColor = Color.Black.copy(alpha = 0.4f)
                )
                .clip(RoundedCornerShape(15.dp))
                .background(Color.White)
                .border(
                    width = 0.7.dp,
                    shape = RoundedCornerShape(15.dp),
                    color = Color(0xFFC5A572)
                )
                .clickable { onClick() }
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {

            Row(
                modifier=Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = value.ifEmpty { placeHolder },
                    style = MaterialTheme.typography.bodySmall,
                    color = if (value.isEmpty()) Color(0xffC5C5C5) else Color.Black,
                    maxLines = 1,
                    modifier=Modifier.weight(1f)
                )
                Icon(
                    painter = painterResource(Res.drawable.icon_callender),
                    contentDescription = null,
                    modifier=Modifier.padding(horizontal = 8.dp).size(20.dp),
                    tint= Secondary
                )
            }
        }
    }
}