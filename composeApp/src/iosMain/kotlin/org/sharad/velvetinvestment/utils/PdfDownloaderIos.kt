package org.sharad.velvetinvestment.utils

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.dataWithBytes
import platform.Foundation.writeToURL
import platform.UIKit.UIApplication
import platform.UIKit.UIDocumentInteractionController
import platform.UIKit.UIDocumentInteractionControllerDelegateProtocol
import platform.UIKit.UIViewController
import platform.darwin.NSObject

class PdfDownloaderIos : PdfDownloadManager {

    @OptIn(ExperimentalForeignApi::class)
    override fun downloadPdf(
        pdfBytes: ByteArray,
        fileName: String,
        onSuccess: (String?) -> Unit,
        onFailed: (String) -> Unit
    ) {
        try {
            val data = pdfBytes.toNSData()

            val fileManager = NSFileManager.defaultManager

            val documentsURL = fileManager.URLForDirectory(
                directory = NSDocumentDirectory,
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = false,
                error = null
            ) ?: throw Exception("Cannot access documents directory")

            val fileURL = documentsURL.URLByAppendingPathComponent("$fileName.pdf")
            if (fileURL==null)
            {
                onFailed("Failed to save PDF")
                return
            }
            val success = data.writeToURL(fileURL, true)

            if (success) {
                onSuccess(documentsURL.path ?: "")
                openPdf(fileURL.path ?: "")

            } else {
                onFailed("Failed to save PDF")
            }

        } catch (e: Exception) {
            onFailed(e.message ?: "Unknown error")
        }
    }



    private var documentController: UIDocumentInteractionController? = null

    fun openPdf(filePath: String) {

        val url = NSURL.fileURLWithPath(filePath)

        val controller = UIDocumentInteractionController.interactionControllerWithURL(url)

        val rootVC = UIApplication.sharedApplication.keyWindow?.rootViewController

            ?: return

        val delegate = object : NSObject(), UIDocumentInteractionControllerDelegateProtocol {

            override fun documentInteractionControllerViewControllerForPreview(

                controller: UIDocumentInteractionController

            ): UIViewController {

                return rootVC

            }

        }

        controller.delegate = delegate

        documentController = controller

        controller.presentPreviewAnimated(true)

    }
}


@OptIn(ExperimentalForeignApi::class)
fun ByteArray.toNSData(): NSData {
    return this.usePinned { pinned ->
        NSData.dataWithBytes(
            bytes = pinned.addressOf(0),
            length = this.size.toULong()
        )
    }
}
