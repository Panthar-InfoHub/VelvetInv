package org.sharad.velvetinvestment.utils

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

class PdfDownloader(
    private val context: Context
): PdfDownloadManager {

    override fun downloadPdf(
        pdfBytes: ByteArray,
        fileName: String,
        onSuccess: () -> Unit,
        onFailed: (String) -> Unit
    ) {
        try {

            val file = File(
                context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
                "$fileName.pdf"
            )

            FileOutputStream(file).use {
                it.write(pdfBytes)
            }

            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )

            val request = DownloadManager.Request(uri).apply {
                setTitle(fileName)
                setDescription("Downloading PDF")
                setNotificationVisibility(
                    DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
                )
                setDestinationUri(uri)
            }

            val manager =
                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

            manager.enqueue(request)

            onSuccess()

        } catch (e: Exception) {
            onFailed(e.message ?: "Download failed")
        }
    }
}