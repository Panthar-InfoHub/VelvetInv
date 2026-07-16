package org.sharad.velvetinvestment.shared.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import androidx.navigation.toRoute
import org.sharad.velvetinvestment.presentation.bundle.compose.BundleDetailsScreenRoot
import org.sharad.velvetinvestment.presentation.bundle.compose.BundleReviewScreenRoot
import org.sharad.velvetinvestment.presentation.bundle.compose.ExploreCategoryFundScreenRoot
import org.sharad.velvetinvestment.presentation.bundle.compose.SelectFundScreenRoot
import org.sharad.velvetinvestment.presentation.bundle.viewmodel.BundleDetailsViewModel

@Composable
fun BundleNavGraph(
    id: String,
    onBackClick: () -> Unit,
    navigateToCartScreen: () -> Unit,
){
    val viewModel = koinViewModel<BundleDetailsViewModel> { parametersOf(id) }
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Route.BundleResultScreen
    ){
        composable<Route.BundleResultScreen>{
            BundleDetailsScreenRoot(
                viewModel = viewModel,
                onBackClick = onBackClick,
                onReviewClick = {
                    navController.navigate(Route.BundleReviewScreen)
                },
                onChangeFundClick = { category ->
                    navController.navigate(Route.SelectFundScreen(category.id))
                }
            )
        }
        composable<Route.SelectFundScreen>{ backStackEntry ->
            val route = backStackEntry.toRoute<Route.SelectFundScreen>()
            SelectFundScreenRoot(
                viewModel = viewModel,
                categoryId = route.categoryId,
                onBackClick = { navController.popBackStack() },
                onSaveClick = { navController.popBackStack() },
                onExploreMoreClick = { categoryId, categoryName, slotId, categoryTitle ->
                    navController.navigate(Route.ExploreCategoryFunds(categoryId, categoryName, slotId, categoryTitle))
                }
            )
        }
        composable<Route.ExploreCategoryFunds>{ backStackEntry ->
            val route = backStackEntry.toRoute<Route.ExploreCategoryFunds>()
            ExploreCategoryFundScreenRoot(
                categorySearchName = route.categoryName,
                categorTitle = route.categoryTitle,
                onBackClick = { navController.popBackStack() },
                onFundSelected = { mutualFundId ->
                    viewModel.updateSlotWithMutualFund(route.categoryId, route.slotId, mutualFundId)
                    navController.popBackStack()
                }
            )
        }
        composable<Route.BundleReviewScreen>{
            BundleReviewScreenRoot(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onProceedClick = {
                    navigateToCartScreen()
                },
                onChangeFundClick = { category ->
                    navController.navigate(Route.SelectFundScreen(category.id))
                }
            )
        }
    }
}
