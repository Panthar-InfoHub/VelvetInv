package org.sharad.velvetinvestment.presentation.fixeddeposits.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.velvetinvestment.presentation.fixeddeposits.uimodels.CategoryFixedDepositUIModel
import org.sharad.velvetinvestment.presentation.fixeddeposits.uimodels.FDTenureSort
import org.sharad.velvetinvestment.presentation.fixeddeposits.viewmodel.FDCategoryViewModel
import org.sharad.velvetinvestment.shared.compose.AppSearchBar
import org.sharad.velvetinvestment.shared.compose.BarHeader
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.LoaderScreen
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.genericDropShadow
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.back_arrow
import velvet.composeapp.generated.resources.mf_haeder_icon

@Composable
fun FDCategoryScreenRoot(
    onBackClick: () -> Unit,
    onIconClick: () -> Unit,
    onFundClick:(String)->Unit,
    pv: PaddingValues,
    onSearchClick:(String)->Unit,
    onCategoryClick:(String,String)->Unit
){

    val viewModel: FDCategoryViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchText by viewModel.searchText.collectAsStateWithLifecycle()


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ScreenHeader(
            onBackClick = { onBackClick() },
            onIconClick = { onIconClick() }
        )
        Box(
            modifier = Modifier.weight(1f)
                .fillMaxSize()
        ){
            when(uiState){
                is UiState.Error->{
                    ErrorScreen((uiState as UiState.Error).message, onRetryClick = {})
                }
                UiState.Loading -> {
                    LoaderScreen()
                }

                is UiState.Success -> {
                    val data= (uiState as UiState.Success<List<CategoryFixedDepositUIModel>>).data
                    CategoryFDScreen(
                        categoryList = data,
                        onCategoryClick = onCategoryClick,
                        onFundClick = {onFundClick(it)},
                        searchText =searchText,
                        onTextChange = { viewModel.onSearchTextChange(it) },
                        pv =pv,
                        onSearchClick = {onSearchClick(searchText)},
                        onSortClick={id,sort->
                            viewModel.reorderTenures(id,sort)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryFDScreen(
    categoryList: List<CategoryFixedDepositUIModel>,
    onCategoryClick: (String, String) -> Unit,
    onFundClick: (String) -> Unit,
    searchText: String,
    onTextChange: (String) -> Unit,
    pv: PaddingValues,
    onSearchClick: (String) -> Unit,
    onSortClick: (String, FDTenureSort) -> Unit
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            AppSearchBar(
                value = searchText,
                onTextChange = { onTextChange(it) },
                onSearchClick = { onSearchClick(searchText) },
                modifier=Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )
        }
        categoryList.forEach { category ->
            item{
                BarHeader(
                    heading = category.categoryName,
                    showArrow = true,
                    onArrowClick = {onCategoryClick(category.categoryId, category.categoryName)},
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = category.fds,
                        key = { it.id }
                    ) { fund ->

                        Box(
                            modifier = Modifier
                                .width(348.dp)
                                .height(IntrinsicSize.Max)
                        ) {
                            FDDetailCard(
                                fd = fund,
                                onClick = { onFundClick(fund.id) },
                                modifier = Modifier.fillMaxHeight(),
                                onSortClick = onSortClick
                            )
                        }
                    }
                }
            }
        }
        item(){
            Spacer(Modifier.height(pv.calculateBottomPadding()+20.dp))
        }
    }

}


@Composable
private fun ScreenHeader(onIconClick: () -> Unit, onBackClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp, horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Fixed Deposits",
            style = MaterialTheme.typography.headlineLarge,
            color = Primary
        )
        Icon(
            painter = painterResource(Res.drawable.back_arrow),
            contentDescription = null,
            modifier = Modifier.size(22.dp).clickable(
                onClick = onBackClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ).align(Alignment.CenterStart)
        )

        Box(
            modifier=Modifier
                .size(52.dp)
                .genericDropShadow(CircleShape)
                .clip(CircleShape)
                .background(Color.White)
                .clickable(
                    onClick = onIconClick
                ).align(Alignment.CenterEnd)
        ){
            Icon(
                painter = painterResource(Res.drawable.mf_haeder_icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp).align(Alignment.Center),
                tint= Secondary
            )
        }
    }

}