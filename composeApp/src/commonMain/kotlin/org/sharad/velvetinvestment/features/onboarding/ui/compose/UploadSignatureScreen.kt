package org.sharad.velvetinvestment.features.onboarding.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.ismoy.imagepickerkmp.domain.config.CameraCaptureConfig
import io.github.ismoy.imagepickerkmp.domain.config.CropConfig
import io.github.ismoy.imagepickerkmp.domain.config.GalleryConfig
import io.github.ismoy.imagepickerkmp.domain.extensions.loadPainter
import io.github.ismoy.imagepickerkmp.domain.models.PhotoResult
import io.github.ismoy.imagepickerkmp.features.imagepicker.config.ImagePickerKMPConfig
import io.github.ismoy.imagepickerkmp.features.imagepicker.model.ImagePickerResult
import io.github.ismoy.imagepickerkmp.features.imagepicker.ui.rememberImagePickerKMP
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Black
import org.sharad.emify.core.ui.theme.GreyText
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.UploadBoxBackground
import org.sharad.emify.core.ui.theme.UploadBoxBorder
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.UploadSignatureEvent
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.UploadSignatureUiState
import org.sharad.velvetinvestment.shared.compose.ImagePickerBottomSheet
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.InvertedAppButton
import org.sharad.velvetinvestment.shared.compose.NextButtonFooter
import org.sharad.velvetinvestment.shared.theme.Spacing
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.dashedBorder
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.upload_signature_icon
import kotlin.time.Duration.Companion.milliseconds

private const val MAX_FILE_SIZE_BYTES = 5_242_880L
private const val SHEET_DISMISS_DELAY_MS = 300L

/**
 * Signature-only variant of the older KYC upload screen — the new onboarding KYC form collects the
 * photo from DigiLocker, so only the signature is still asked for here.
 */
@Composable
fun UploadSignatureScreen(
    state: UploadSignatureUiState,
    onEvent: (UploadSignatureEvent) -> Unit,
    onBack: () -> Unit
) {
    val fileLimitError = "File exceeds 5MB limit"
    val picker = rememberImagePickerKMP(
        config = ImagePickerKMPConfig(
            galleryConfig = GalleryConfig(
                allowMultiple = false
            ),
            cameraCaptureConfig = CameraCaptureConfig(
                cropConfig = CropConfig(
                    enabled = true,
                    circularCrop = false,
                    freeformCrop = true
                )
            )
        ),
    )
    val result = picker.result

    LaunchedEffect(result) {
        when (val r = result) {
            is ImagePickerResult.Success -> {
                val photo = r.photos.firstOrNull()
                photo?.fileSize?.let { size ->
                    if (size > MAX_FILE_SIZE_BYTES) {
                        // Close the sheet first, otherwise the snackbar renders behind it.
                        onEvent(UploadSignatureEvent.HideSignatureSelector)
                        delay(SHEET_DISMISS_DELAY_MS.milliseconds)
                        SnackBarController.showError(fileLimitError)
                    } else {
                        onEvent(UploadSignatureEvent.OnSignatureSelected(photo))
                    }
                }
            }

            is ImagePickerResult.Error -> {
                onEvent(UploadSignatureEvent.HideSignatureSelector)
                delay(SHEET_DISMISS_DELAY_MS.milliseconds)
                SnackBarController.showError(r.exception.message ?: "An Error Occurred")
            }

            else -> Unit
        }
    }

    Scaffold(
        containerColor = White
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            BackHeader(
                title = "Verification Details",
                onBack = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.dp24)
            )
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .padding(horizontal = Spacing.dp24),
                verticalArrangement = Arrangement.spacedBy(Spacing.dp24)
            ) {
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(Spacing.dp8),
                    ) {
                        Text(
                            text = "Upload Signature",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Please provide a clear image of your signature to complete your verification.",
                            style = MaterialTheme.typography.labelSmall,
                            color = GreyText
                        )
                    }
                }

                item {
                    Column {
                        UploadDocumentCard(
                            title = "Upload Signature",
                            instructionText = "Click to Upload Signature",
                            icon = Res.drawable.upload_signature_icon,
                            topLineColor = Primary.copy(alpha = 0.5f),
                            isSelected = state.signature != null,
                            image = state.signature,
                            onClick = { onEvent(UploadSignatureEvent.ShowSignatureSelector) }
                        )
                        if (state.signature != null) {
                            InvertedAppButton(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = Spacing.dp16),
                                onClick = { onEvent(UploadSignatureEvent.RemoveSignature) },
                                text = "Remove Signature"
                            )
                        }
                    }
                }
            }
            NextButtonFooter(
                value = "Save & Continue" + " →",
                onClick = { onEvent(UploadSignatureEvent.OnUploadClicked) },
                loading = state.isLoading,
                enabled = state.signature != null,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    ImagePickerBottomSheet(
        visible = state.showSignatureSelector,
        onDismiss = { onEvent(UploadSignatureEvent.HideSignatureSelector) },
        onCameraClick = { picker.launchCamera() },
        onGalleryClick = { picker.launchGallery() }
    )
}

@Composable
fun UploadDocumentCard(
    title: String,
    icon: DrawableResource,
    instructionText: String,
    topLineColor: Color,
    isSelected: Boolean,
    image: PhotoResult?,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(Spacing.dp12))
    )
    {
        Column {
            // Top Accent Line
            HorizontalDivider(thickness = Spacing.dp4, color = topLineColor)

            Column(
                modifier = Modifier.padding(all = Spacing.dp24),
                verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    color = Black
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(Spacing.dp8))
                        .background(
                            if (isSelected) Primary.copy(alpha = 0.05f)
                            else UploadBoxBackground
                        )
                        .dashedBorder(
                            color = if (isSelected) Primary else UploadBoxBorder,
                            strokeWidth = Spacing.dp2,
                            cornerRadius = Spacing.dp8,
                            dashLength = Spacing.dp6,
                            gapLength = Spacing.dp4,
                        )
                        .clickable(onClick = onClick),
                    contentAlignment = Alignment.Center
                ) {
                    if (image != null) {

                        val painter = image.loadPainter()

                        if (painter != null) {
                            Image(
                                painter = painter,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(Spacing.dp8)
                            )
                        } else {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(Spacing.dp8),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Box(
                                    modifier = Modifier
                                        .size(Spacing.dp48)
                                        .clip(RoundedCornerShape(Spacing.dp10))
                                        .background(Primary.copy(alpha = 0.1f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(icon),
                                        contentDescription = null,
                                        tint = Primary,
                                        modifier = Modifier.size(Spacing.dp24)
                                    )
                                }

                                Text(
                                    text = "Document Selected",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Primary
                                )

                                Text(
                                    text = "Tap to change",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = GreyText
                                )
                            }
                        }

                    } else {

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(Spacing.dp8),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(Spacing.dp48)
                                    .clip(RoundedCornerShape(Spacing.dp10))
                                    .background(Primary.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(icon),
                                    contentDescription = null,
                                    tint = Primary,
                                    modifier = Modifier.size(Spacing.dp24)
                                )
                            }

                            Text(
                                text = instructionText,
                                style = MaterialTheme.typography.labelMedium,
                                color = Primary
                            )

                            Text(
                                text = "JPEG, PNG up to 5 MB",
                                style = MaterialTheme.typography.titleSmall.copy(fontSize = 12.sp),
                                color = GreyText
                            )
                        }
                    }
                }
            }
        }

    }
}

@Preview
@Composable
private fun UploadSignatureScreenPreview() {
    VelvetTheme {
        UploadSignatureScreen(
            state = UploadSignatureUiState(),
            onEvent = {},
            onBack = {}
        )
    }
}
