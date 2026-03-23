package org.sharad.velvetinvestment.domain.usecases

import org.sharad.velvetinvestment.utils.BrowserLauncher

class LaunchBrowserUseCase(
    private val browserLauncher: BrowserLauncher
) {
    operator fun invoke(url: String) {
        browserLauncher.launchBrowser(url)
    }
}