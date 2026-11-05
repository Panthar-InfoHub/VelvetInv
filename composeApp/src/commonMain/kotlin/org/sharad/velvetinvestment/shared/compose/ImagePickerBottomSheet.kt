package org.sharad.velvetinvestment.shared.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.camera
import velvet.composeapp.generated.resources.image_icon

/** Camera / gallery chooser. Moved here from the removed old-KYC `FileUploadScreen`. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagePickerBottomSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit
) {
    if (visible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
            ) {
                Text(
                    "Select Image",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 12.dp, start = 12.dp)
                )

                BottomSheetItem(
                    icon = Res.drawable.camera,
                    text = "Camera",
                    onClick = {
                        onCameraClick()
                    }
                )

                BottomSheetItem(
                    icon = Res.drawable.image_icon,
                    text = "Gallery",
                    onClick = {
                        onGalleryClick()
                    },
                )

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun BottomSheetItem(
    text: String,
    onClick: () -> Unit,
    icon: DrawableResource
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick,
                indication = null,
                interactionSource = remember{ MutableInteractionSource()})
            .padding(vertical = 8.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = Primary,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text,
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black
        )
    }
}
