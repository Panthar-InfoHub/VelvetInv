package org.sharad.velvetinvestment.utils

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

class IOSBrowserLauncher : BrowserLauncher {

    override fun launchBrowser(url: String) {
        val nsUrl = NSURL.URLWithString(url) ?: return

        UIApplication.sharedApplication.openURL(nsUrl)
    }
}