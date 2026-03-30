package org.sharad.velvetinvestment.utils.pdfutils

import platform.Foundation.NSURL
import platform.UIKit.UIDocumentInteractionController

 class PdfViewerIos: PdfViewer {
     override fun openPdf(url: String) {
         val nsUrl = NSURL.URLWithString(url)
         val controller = UIDocumentInteractionController.interactionControllerWithURL(nsUrl!!)
         controller.presentPreviewAnimated(true)
     }
}