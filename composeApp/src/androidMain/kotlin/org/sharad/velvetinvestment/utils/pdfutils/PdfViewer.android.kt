package org.sharad.velvetinvestment.utils.pdfutils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.net.toUri


class PdfViewerAndroid(private val context: Context) : PdfViewer {
    override fun openPdf(url: String) {
        val uri = url.toUri()

        // Try a dedicated PDF viewer first.
        val viewPdfIntent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            flags = Intent.FLAG_ACTIVITY_NO_HISTORY or
                Intent.FLAG_GRANT_READ_URI_PERMISSION or
                Intent.FLAG_ACTIVITY_NEW_TASK
        }

        try {
            context.startActivity(viewPdfIntent)
            return
        } catch (_: ActivityNotFoundException) {
            // No app registered to handle application/pdf for this URI — fall through.
        }

        // Fallback: open the URL in a browser, which can render/download the PDF.
        val browserIntent = Intent(Intent.ACTION_VIEW, uri).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        try {
            context.startActivity(browserIntent)
        } catch (_: ActivityNotFoundException) {
            Toast.makeText(context, "No app found to open the PDF", Toast.LENGTH_SHORT).show()
        }
    }
}
