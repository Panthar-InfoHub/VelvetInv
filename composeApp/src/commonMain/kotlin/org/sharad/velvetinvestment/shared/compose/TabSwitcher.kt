package org.sharad.velvetinvestment.shared.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.sharad.emify.core.ui.theme.Secondary

@Composable
fun <T> GenericTabSwitcher(
    tabs: List<T>,
    selectedTab: T,
    onTabSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    tabContent: @Composable (tab: T, selected: Boolean) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        tabs.forEach { tab ->

            val selected = tab == selectedTab

            Column(
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .clickable { onTabSelected(tab) },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                tabContent(tab, selected)

                Spacer(Modifier.height(6.dp))

                Box(
                    Modifier
                        .height(2.dp)
                        .fillMaxWidth()
                        .background(
                            if (selected) Secondary else Color.Transparent,
                            RoundedCornerShape(50)
                        )
                )
            }

        }
    }
}

