package org.sharad.velvetinvestment.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.reinterpret
import platform.Foundation.NSData
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.Foundation.dataWithContentsOfURL
import platform.UIKit.UIApplication
import platform.UIKit.UIDocumentPickerDelegateProtocol
import platform.UIKit.UIDocumentPickerMode
import platform.UIKit.UIDocumentPickerViewController
import platform.darwin.NSObject

@Composable
actual fun rememberDocumentManager(onResult: (SharedDocument?) -> Unit): DocumentManager {
    val documentPicker = UIDocumentPickerViewController(
        documentTypes = listOf("public.item"),
        inMode = UIDocumentPickerMode.UIDocumentPickerModeOpen
    )
    val documentDelegate = remember {
        object : NSObject(), UIDocumentPickerDelegateProtocol {
            override fun documentPicker(controller: UIDocumentPickerViewController, didPickDocumentAtURL: NSURL) {
                didPickDocumentAtURL.startAccessingSecurityScopedResource()
                val data = NSData.dataWithContentsOfURL(didPickDocumentAtURL)
                didPickDocumentAtURL.stopAccessingSecurityScopedResource()
                onResult.invoke(SharedDocument(data))
                controller.dismissViewControllerAnimated(true, null)
            }
        }
    }

    return remember {
        DocumentManager {
            documentPicker.setDelegate(documentDelegate)
            UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(documentPicker, true, null)
        }
    }
}

actual class DocumentManager actual constructor(private val onLaunch: () -> Unit) {
    actual fun launch() {
        onLaunch()
    }
}

actual class SharedDocument(private val data: NSData?) {
    @OptIn(ExperimentalForeignApi::class)
    actual fun toByteArray(): ByteArray? {
        val d = data ?: return null
        val ptr = d.bytes ?: return null

        val length = d.length.toInt()
        if (length <= 0) return ByteArray(0)

        return ptr.readBytes(length)
    }

    @OptIn(BetaInteropApi::class)
    actual fun toText(): String? {
        return data?.let {
            NSString.create(it, NSUTF8StringEncoding)?.toString()
        }
    }

    actual fun fileName(): String? {
        return null
    }
}