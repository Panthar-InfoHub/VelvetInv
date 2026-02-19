package org.sharad.velvetinvestment.presentation.palak.fd

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.painterResource
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.image__hdfc_bank_
import velvet.composeapp.generated.resources.rectangle_18

@Composable
fun SetInvestmentDetailsScreen(){
    LazyColumn {
        item{
           TopComposable()
        }
    }
}
@Preview(showBackground = true)
@Composable
fun TopComposable(){
    Box(){
        Row {
            Image(painter = painterResource(Res.drawable.rectangle_18), contentDescription = "hdfc bank")
            Column {
                Text("HDFC Bank")
                Text("Risk")
            }
        }
    }
}