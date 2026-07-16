package org.sharad.velvetinvestment.presentation.portfolio.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.ic_empty_notification

@Composable
fun EmptyFundScreen(onBrowseClick: () -> Unit, text:String, buttonText:String) {

    Box(
        modifier=Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){

        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Image(
                painter = painterResource(Res.drawable.ic_empty_notification),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                contentScale = ContentScale.FillWidth
            )


        }

    }

}