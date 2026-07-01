package org.sharad.velvetinvestment.presentation.kyc.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.ismoy.imagepickerkmp.domain.extensions.loadPainter
import io.github.ismoy.imagepickerkmp.domain.models.PhotoResult
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.bgColor4
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.presentation.kyc.viewmodels.KYCImageUploaderScreenViewModel
import org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails.NextButtonFooter
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.BarHeader
import org.sharad.velvetinvestment.shared.compose.InvertedAppButton
import org.sharad.velvetinvestment.shared.dottedBorder
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.velvetinvestment.utils.SnackBarController
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.image_icon
import velvet.composeapp.generated.resources.sign_icon

@Composable
fun FileUploadScreen(
    onBack: () -> Unit,
    onSuccessfulUpload: () -> Unit,
    viewModel: KYCImageUploaderScreenViewModel = koinViewModel()
) {
    val scope = rememberCoroutineScope()

    val signature by viewModel.signature.collectAsStateWithLifecycle()
    val photo by viewModel.photo.collectAsStateWithLifecycle()
    val showSignatureSelector by viewModel.showSignatureSelector.collectAsStateWithLifecycle()
    val showPhotoSelector by viewModel.showPhotoSelector.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()
    val buttonEnabled by viewModel.buttonEnabled.collectAsStateWithLifecycle()

    FileUploadScreenContent(
        onBack = onBack,
        onSuccessfulUpload = {
            viewModel.uploadImages {
                onSuccessfulUpload()
            }
        },
        signature = signature,
        photo = photo,
        showSignatureSelector = showSignatureSelector,
        showPhotoSelector = showPhotoSelector,
        loading = loading,
        buttonEnabled = buttonEnabled,
        onSignatureClick = { viewModel.showSignatureSelector() },
        onPhotoClick = { viewModel.showPhotoSelector() },
        onSignatureSelected = { viewModel.onSignatureSelected(it) },
        onPhotoSelected = { viewModel.onPhotoSelected(it) },
        onHideSignatureSelector = { viewModel.hideSignatureSelector() },
        onHidePhotoSelector = { viewModel.hidePhotoSelector() },
        showError = { scope.launch { SnackBarController.showError(it) } },
        removeSignature={viewModel.removeSignature()},
        removePhoto={viewModel.removePhoto()}
    )
}

@Composable
fun FileUploadScreenContent(
    onBack: () -> Unit,
    onSuccessfulUpload: () -> Unit,
    signature: PhotoResult?,
    photo: PhotoResult?,
    showSignatureSelector: Boolean,
    showPhotoSelector: Boolean,
    loading: Boolean,
    buttonEnabled: Boolean,
    onSignatureClick: () -> Unit,
    onPhotoClick: () -> Unit,
    onSignatureSelected: (PhotoResult) -> Unit,
    onPhotoSelected: (PhotoResult) -> Unit,
    onHideSignatureSelector: () -> Unit,
    onHidePhotoSelector: () -> Unit,
    showError: (String) -> Unit,
    removeSignature: () -> Unit,
    removePhoto: () -> Unit
) {
    if (showSignatureSelector) {
        ImageUploader(
            showGallery = showSignatureSelector,
            onDismiss = onHideSignatureSelector,
            onSelected = {
                it.fileSize?.let { size ->
                    if (size > 5_242_880L) {
                        showError("File exceeds 5MB limit")
                        onHideSignatureSelector()
                        return@ImageUploader
                    }
                }
                onSignatureSelected(it)
            }
        )
    }

    if (showPhotoSelector) {
        ImageUploader(
            showGallery = showPhotoSelector,
            onDismiss = onHidePhotoSelector,
            onSelected = {
                it.fileSize?.let { size ->
                    if (size > 5_242_880L) {
                        showError("File exceeds 5MB limit")
                        onHidePhotoSelector()
                        return@ImageUploader
                    }
                }
                onPhotoSelected(it)
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.White)
    ) {
        BackHeader(
            onBackClick = onBack,
            heading = "Verification Details",
            showBack = true
        )
        LazyColumn(
            modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { BarHeader(heading = "Upload Signature") }
            item {
                ImageSelectorBox(
                    onClick = onSignatureClick,
                    image = signature,
                    mainText = "Upload Signature",
                    subText = "JPEG, PNG up to 5 MB",
                    icon = Res.drawable.sign_icon,
                    uploadedText = "Signature Uploaded"
                )
            }
            if (signature!=null){
                item {
                    InvertedAppButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = removeSignature,
                        text = "Remove Signature"
                    )
                }
            }
            item { BarHeader(heading = "Upload Photo") }
            item {
                ImageSelectorBox(
                    onClick = onPhotoClick,
                    image = photo,
                    mainText = "Upload Photo",
                    subText = "JPEG, PNG up to 5 MB",
                    icon = Res.drawable.image_icon,
                    uploadedText = "Image Uploaded"
                )
            }
            if (photo!=null){
                item {
                    InvertedAppButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = removePhoto,
                        text = "Remove Photo"
                    )
                }
            }
            item {
                Spacer(Modifier.height(8.dp))
            }
        }
        NextButtonFooter(
            onClick = onSuccessfulUpload,
            value = "Save and Continue",
            loading = loading,
            enabled = buttonEnabled
        )
    }
}

@Composable
fun ImageSelectorBox(onClick: () -> Unit, image: PhotoResult?, mainText: String, subText: String, icon: DrawableResource, uploadedText:String){

    Box(
        modifier = Modifier.fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(15.dp))
            .dottedBorder(
                color = Color.Black,
                strokeWidth = 1.dp,
                cornerRadius = 15.dp,
                on = 5f,
                off = 5f
            )
            .clickable(
                onClick=onClick
            ),
        contentAlignment = Alignment.Center
    ){
        if (image!=null){
            val painter=image.loadPainter()
            if (painter!=null){
                Image(
                    painter=painter,
                    contentDescription = null,
                    modifier = Modifier.fillMaxHeight().padding(8.dp)
                )
            }
            else{
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.size(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(bgColor4.copy(0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(icon),
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Text(
                        text = uploadedText,
                        color = Secondary,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = "Tap to change",
                        style = titlesStyle,
                        color = titleColor
                    )
                }
            }
        }
        else{
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.size(44.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(bgColor4.copy(0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = mainText,
                    color = Secondary,
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    text = subText,
                    style = titlesStyle,
                    color = titleColor
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun FileUploadScreenPreview() {
    VelvetTheme {
        FileUploadScreenContent(
            onBack = {},
            onSuccessfulUpload = {},
            signature = null,
            photo = null,
            showSignatureSelector = false,
            showPhotoSelector = false,
            loading = false,
            buttonEnabled = false,
            onSignatureClick = {},
            onPhotoClick = {},
            onSignatureSelected = {},
            onPhotoSelected = {},
            onHideSignatureSelector = {},
            onHidePhotoSelector = {},
            {},
            {}
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
fun ImageSelectorBoxPreview() {
    VelvetTheme {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            ImageSelectorBox(
                onClick = {},
                image = null,
                mainText = "Upload Signature",
                subText = "JPEG, PNG up to 5 MB",
                icon = Res.drawable.sign_icon,
                uploadedText = "Signature Uploaded"
            )
        }
    }
}
