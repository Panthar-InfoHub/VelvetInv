package org.sharad.velvetinvestment.utils

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.onDownload
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readRawBytes
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.create
import platform.Foundation.dataWithBytes
import platform.Foundation.writeToURL
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIDocumentInteractionController
import platform.UIKit.UIDocumentInteractionControllerDelegateProtocol
import platform.UIKit.UIViewController
import platform.darwin.NSObject

class PdfDownloaderIos(
    private val client: HttpClient
) : PdfDownloadManager {

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

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    override suspend fun downloadUrlPdf(
        url: String,
        fileName: String,
        onProgress: (progress: Int) -> Unit,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        try {
            println("[DEBUG_LOG] DownloadPdf iOS: Starting download from: $url")

            withContext(Dispatchers.Main) {
                onProgress(0)
            }

            val response: HttpResponse = withContext(Dispatchers.IO) {
                client.get(url) {
                    onDownload { bytesSentTotal, contentLength ->
                        val totalBytes = contentLength ?: 0L
                        if (totalBytes > 0) {
                            val progress = ((bytesSentTotal * 100) / totalBytes).toInt()
                            println("[DEBUG_LOG] DownloadPdf iOS: Progress: $progress% ($bytesSentTotal/$totalBytes bytes)")
                            CoroutineScope(Dispatchers.Main).launch {
                                onProgress(progress)
                            }
                        }
                    }
                }
            }

            if (response.status.value in 200..299) {
                val bytes = response.readRawBytes()
                println("[DEBUG_LOG] DownloadPdf iOS: Downloaded ${bytes.size} bytes")

                withContext(Dispatchers.Main) {
                    onProgress(100)
                }

                val documentsPath = NSSearchPathForDirectoriesInDomains(
                    NSDocumentDirectory,
                    NSUserDomainMask,
                    true
                ).firstOrNull() as? String

                if (documentsPath == null) {
                    println("[DEBUG_LOG] DownloadPdf iOS: Could not get documents directory")
                    client.close()
                    onFailure()
                    return
                }

                val filePath = "$documentsPath/$fileName.pdf"
                val fileUrl = NSURL.fileURLWithPath(filePath)

                // Save file - convert ByteArray to NSData
                val data = bytes.usePinned { pinned ->
                    NSData.create(
                        bytes = pinned.addressOf(0),
                        length = bytes.size.toULong()
                    )
                }
                val writeSuccess = data.writeToURL(fileUrl, atomically = true) ?: false

                client.close()

                if (writeSuccess) {
                    println("[DEBUG_LOG] DownloadPdf iOS: File saved at: $filePath")

                    // Show share sheet on main thread
                    withContext(Dispatchers.Main) {
                        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
                        if (rootViewController != null) {
                            val activityVC = UIActivityViewController(
                                activityItems = listOf(fileUrl),
                                applicationActivities = null
                            )
                            rootViewController.presentViewController(
                                activityVC,
                                animated = true,
                                completion = null
                            )
                        }
                        onSuccess()
                        openPdf(filePath)
                    }
                } else {
                    println("[DEBUG_LOG] DownloadPdf iOS: Failed to write file")
                    onFailure()
                }
            } else {
                println("[DEBUG_LOG] DownloadPdf iOS: HTTP error: ${response.status}")
                client.close()
                onFailure()
            }
        } catch (e: Exception) {
            println("[DEBUG_LOG] DownloadPdf iOS: Exception: ${e.message}")
            e.printStackTrace()
            onFailure()
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
