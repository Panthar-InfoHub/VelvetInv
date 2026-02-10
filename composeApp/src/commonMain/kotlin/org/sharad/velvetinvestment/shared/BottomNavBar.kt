package org.sharad.velvetinvestment.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.shadowColor
import org.sharad.velvetinvestment.shared.Navigation.Route
import org.sharad.velvetinvestment.utils.genericDropShadow
import org.sharad.velvetinvestment.utils.theme.Poppins
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.nav_icon_full_screener
import velvet.composeapp.generated.resources.nav_icon_home
import velvet.composeapp.generated.resources.nav_icon_portfolio
import velvet.composeapp.generated.resources.nav_icon_profile


@Composable
fun BottomNavBar(navController: NavController) {

    val entry by navController.currentBackStackEntryAsState()
    val currentDestination=entry?.destination

    val bottomBarItems = listOf(Route.Home, Route.FundScreener, Route.PortFolio, Route.Profile)
    val itemsLabels= listOf("Home","Fund Screener","Portfolio","Profile")
    val icons= listOf(Res.drawable.nav_icon_home,
        Res.drawable.nav_icon_full_screener,Res.drawable.nav_icon_portfolio,Res.drawable.nav_icon_profile)

    Box(modifier=Modifier.fillMaxWidth()
        .dropShadow(
            shadow = Shadow(
                radius = 16.dp,
                color = Color(0xffDEE2F6)
            ),
            shape = RectangleShape
        )
        .background(Color.Red),
        contentAlignment = Alignment.Center) {

        NavigationBar(containerColor = Color.White,
            modifier = Modifier.fillMaxWidth()
        ){
            bottomBarItems.forEachIndexed { index, item ->
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any{
                        it.hasRoute(item::class)
                    }==true,
                    onClick = {navController.navigate(item){
                        popUpTo(navController.graph.startDestinationId){
                            saveState=true
                        }
                        launchSingleTop=true
                        restoreState=true
                    } },
                    icon = {
                        Icon(
                            painter = painterResource(icons[index]),
                            contentDescription = itemsLabels[index],
                            modifier = Modifier.size(28.dp),
                        )
                    },
                    label = {
                        Text(
                            text=itemsLabels[index],
                            fontFamily = Poppins,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = Primary,
                        unselectedTextColor = Primary,
                        indicatorColor = Color.Transparent,
                        selectedIconColor = Secondary,
                        selectedTextColor = Secondary
                    )
                )
            }
        }
    }
}