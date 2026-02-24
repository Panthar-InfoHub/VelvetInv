package org.sharad.velvetinvestment.presentation.profile.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.darkBlue
import org.sharad.emify.core.ui.theme.greenColor
import org.sharad.emify.core.ui.theme.redColor
import org.sharad.velvetinvestment.shared.compose.BarHeader
import org.sharad.velvetinvestment.shared.compose.FundCard
import org.sharad.velvetinvestment.utils.genericDropShadow
import org.sharad.velvetinvestment.utils.theme.Poppins
import org.sharad.velvetinvestment.utils.theme.VelvetTheme
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.arrow_down
import velvet.composeapp.generated.resources.arrowback_elements
import velvet.composeapp.generated.resources.axis_bank
import velvet.composeapp.generated.resources.coin_finger
import velvet.composeapp.generated.resources.down_stock
import velvet.composeapp.generated.resources.sbi_tree
import velvet.composeapp.generated.resources.search2
import velvet.composeapp.generated.resources.up_stock


@Composable
fun MutualFundsScreen() {
    var text by remember { mutableStateOf("") }
        Column(Modifier.fillMaxSize().padding(16.dp)) {
            Spacer(modifier = Modifier.height(30.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.arrowback_elements),
                    contentDescription = "ArrowBackIcon",
                    Modifier.size(24.dp)
                )
                Text(
                    "Mutual Funds",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color(0xFF273E71)
                )
                Box(
                    modifier = Modifier.genericDropShadow(CircleShape)
                        .background(color = Color.White, shape = CircleShape)
                        .padding(10.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.coin_finger),
                        contentDescription = "coin Icon",
                        tint = Color(0xFFD8AF6B)
                    )
                }

            }
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = text,
                onValueChange = { text = it },
                leadingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.search2),
                        contentDescription = "search"
                    )
                },
                placeholder = { Text("Search For Funds....") },
                shape = RoundedCornerShape(50.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF4881FF).copy(0.1f),
                    unfocusedContainerColor = Color(0xFF4881FF).copy(0.1f)
                ),
                modifier = Modifier.fillMaxWidth()
                    .border(1.dp, color = darkBlue, shape = RoundedCornerShape(50.dp))
            )
            Spacer(modifier = Modifier.height(16.dp))
            MutualFundCategory()
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn {
                item { BarHeader(modifier = Modifier.fillMaxWidth(), heading = "MF Holdings")
                }
                item {
                    Spacer(modifier=Modifier.height(20.dp))
                    MFholdingSummary(
                        Res.drawable.sbi_tree,
                        "SBI Gold Fund",
                        "Commodities Gold",
                        "5000",
                        "22.4",
                        Res.drawable.up_stock,
                        greenColor
                    )
                }

            item {
                Spacer(Modifier.height(20.dp))

                MFholdingSummary(
                    Res.drawable.axis_bank, "Axis Small Cap Fund", "Small Cap", "10000", "2.4",
                    Res.drawable.down_stock, redColor
                )
            }
                item {
                    Spacer(Modifier.height(20.dp))
                    BarHeader(modifier = Modifier.fillMaxWidth(), "Popular Fund")
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            FundCard(modifier = Modifier.weight(1f))
                            FundCard(modifier = Modifier.weight(1f))
                        }
                    }
                }
                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            FundCard(modifier = Modifier.weight(1f))
                            FundCard(modifier = Modifier.weight(1f))
                        }
                    }

           item {
               Spacer(modifier=Modifier.height(20.dp))
               BarHeader(modifier = Modifier.fillMaxWidth(), "NIFTY 50 Returns")
           }
                item {
                    Spacer(Modifier.height(20.dp))
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            FundCard(modifier = Modifier.weight(1f))
                            FundCard(modifier = Modifier.weight(1f))
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        FundCard(modifier = Modifier.weight(1f))
                        FundCard(modifier = Modifier.weight(1f))
                    }
                }
            item {
                Spacer(modifier = Modifier.height(20.dp))
                BarHeader(modifier = Modifier.fillMaxWidth(), "NIFTY 50 Returns")

            }
             item {
                 Spacer(modifier = Modifier.height(16.dp))
                 Column {
                     Row(
                         modifier = Modifier.fillMaxWidth(),
                         horizontalArrangement = Arrangement.spacedBy(16.dp)
                     ) {
                         FundCard(modifier = Modifier.weight(1f))
                         FundCard(modifier = Modifier.weight(1f))
                     }
                 }
             }
                 item {
                     Spacer(modifier = Modifier.height(20.dp))
                     Row(
                         modifier = Modifier.fillMaxWidth(),
                         horizontalArrangement = Arrangement.spacedBy(16.dp)
                     ) {
                         FundCard(modifier = Modifier.weight(1f))
                         FundCard(modifier = Modifier.weight(1f))
                     }
                 }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    BarHeader(modifier = Modifier.fillMaxWidth(), "Explore NFOs")

                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            FundCard(modifier = Modifier.weight(1f))
                            FundCard(modifier = Modifier.weight(1f))
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        FundCard(modifier = Modifier.weight(1f))
                        FundCard(modifier = Modifier.weight(1f))
                    }
                }

            }
        }
    }


@Composable
fun FilterBox(text:String, selected: Boolean, onClick:()->Unit){
    Row (modifier = Modifier.height(32.dp).width(104.dp).padding(horizontal = 1.dp).background(color = Color(0xFFDEE2F6).copy(0.75f), shape = RoundedCornerShape(16.dp)).padding(horizontal = 8.dp), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically){
        Text(text=text, fontSize = 14.sp, fontFamily = Poppins, color = Color.Black.copy(0.87f))
        if(selected) {
            Icon(
                painter = painterResource(Res.drawable.arrow_down),
                contentDescription = "Arrow Down Icon", modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Composable
fun MutualFundCategory(){
     val category =listOf(
        "Sort by",
        "Index Only",
        "Flexi Cap",
        "Sectoral",
        "Large Cap"
    )
    var selected by remember { mutableStateOf(category.first())}

    LazyRow (modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        items(category) { it ->
            FilterBox(text = it, selected = selected == it, onClick = { selected = it })

        }
    }

}

@Composable
fun MFholdingSummary(icon: DrawableResource, title: String, subtitle: String, amount: String, percent: String, subIcon: DrawableResource, color: Color){

    Box(
        modifier= Modifier.fillMaxWidth()
            .height(80.dp)

            .genericDropShadow()
            .clip(shape = RoundedCornerShape(10.dp))
            .background(color = Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(icon),
                    contentDescription = "sbi icon",
                    modifier = Modifier.size(44.dp)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        title,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        fontFamily = Poppins
                    )

                    Text(subtitle, color = Color.Gray)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Text(
                        "₹" + amount,
                        fontSize = 16.sp,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(subIcon),
                            contentDescription = "upstock icon",
                            tint = color
                        )
                        Text("$percent %", fontSize = 14.sp, color = color)
                    }

                }


            }
            Spacer(Modifier.height(10.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Prev(){
    VelvetTheme{
        MutualFundsScreen()
    }
}
