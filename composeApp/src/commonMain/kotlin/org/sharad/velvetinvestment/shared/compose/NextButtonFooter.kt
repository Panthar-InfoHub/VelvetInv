package org.sharad.velvetinvestment.shared.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/** The white footer with one full-width [AppButton], used at the bottom of most flows. */
@Composable
fun NextButtonFooter(onClick: () -> Unit, pv: PaddingValues = PaddingValues(0.dp), value: String = "Next", enabled: Boolean=true, loading: Boolean=false, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
//            .imePadding()
            .shadow(elevation = 28.dp)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ){
        AppButton(
            modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 16.dp+pv.calculateBottomPadding()),
            onClick = onClick,
            enabled = enabled,
            text = value,
            loading = loading
        )
    }
}
