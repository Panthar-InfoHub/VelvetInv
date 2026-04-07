package org.sharad.velvetinvestment.presentation.mutualfund

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.models.usercart.CartItemDomain
import org.sharad.velvetinvestment.domain.models.usercart.CartType
import org.sharad.velvetinvestment.domain.models.usercart.SipDetails
import org.sharad.velvetinvestment.domain.models.usercart.UserCartDomain
import org.sharad.velvetinvestment.domain.usecases.LaunchBrowserUseCase
import org.sharad.velvetinvestment.domain.usecases.fundusecases.GetUserCartUseCase
import org.sharad.velvetinvestment.domain.usecases.fundusecases.PurchaseLumpsumFundUseCase
import org.sharad.velvetinvestment.domain.usecases.fundusecases.PurchaseSipFundUseCase
import org.sharad.velvetinvestment.utils.Log
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.SnackBarType
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess

data class CartUiModel(
    val cartData: UserCartDomain,
    val selectedCartType: CartType = CartType.LUMPSUM
)

class CartScreenViewModel(
    private val getUserCartUseCase: GetUserCartUseCase,
    private val purchaseSipUseCase: PurchaseSipFundUseCase,
    private val purchaseLumpSumUseCase: PurchaseLumpsumFundUseCase,
    private val browserLauncher: LaunchBrowserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<CartUiModel>>(UiState.Loading)
    val uiState: StateFlow<UiState<CartUiModel>> = _uiState.asStateFlow()

    private val _loading= MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _confirmationPopupVisible= MutableStateFlow(false)
    val confirmationPopupVisible: StateFlow<Boolean> = _confirmationPopupVisible.asStateFlow()


    val visibleItems: StateFlow<List<CartItemDomain>> =
        uiState
            .map { state ->
                when (state) {
                    is UiState.Success -> {
                        when (state.data.selectedCartType) {
                            CartType.SIP -> state.data.cartData.sipItems
                            CartType.LUMPSUM -> state.data.cartData.lumpSumItems
                        }
                    }
                    else -> emptyList()
                }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalAmount: StateFlow<Long> =
        visibleItems
            .map { items ->
                items.sumOf {
                    if (it.type == CartType.SIP) {
                        it.sipDetails?.sipAmount ?: 0
                    } else {
                        it.amount
                    }
                }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)


    init {
        loadCart()
    }


    fun onCartTypeSelected(type: CartType) {
        val current = _uiState.value
        if (current is UiState.Success) {
            _uiState.value = UiState.Success(
                current.data.copy(selectedCartType = type)
            )
        }
    }

    fun removeItem(itemId: String) {
        val current = _uiState.value
        if (current is UiState.Success) {

            val updatedSip = current.data.cartData.sipItems.filterNot { it.id == itemId }
            val updatedLump = current.data.cartData.lumpSumItems.filterNot { it.id == itemId }

            _uiState.value = UiState.Success(
                current.data.copy(
                    cartData = current.data.cartData.copy(
                        sipItems = updatedSip,
                        lumpSumItems = updatedLump
                    )
                )
            )
        }
    }

    fun loadCart() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            getUserCartUseCase()
                .onSuccess {
                    _uiState.value=UiState.Success(
                        CartUiModel(
                            cartData = it,
                        )
                    )
                }
                .onError {
                    _uiState.value=UiState.Error(it.message)
                }

            val dummyData = UserCartDomain(

                sipItems = listOf(
                    CartItemDomain(
                        id = "sip_1",
                        amcName = "Sector/Thematic Fund",
                        productName = "ICICI Technology Fund",
                        amount = 0,
                        type = CartType.SIP,
                        date = "2026-04-01",
                        sipDetails = SipDetails(
                            startDate = "2026-04-05",
                            endDate = "2036-04-05",
                            frequency = "Monthly",
                            day = 5,
                            sipAmount = 2000
                        )
                    ),
                    CartItemDomain(
                        id = "sip_2",
                        amcName = "Large Cap Fund",
                        productName = "HDFC Top 100 Fund",
                        amount = 0,
                        type = CartType.SIP,
                        date = "2026-04-01",
                        sipDetails = SipDetails(
                            startDate = "2026-04-10",
                            endDate = "2036-04-10",
                            frequency = "Monthly",
                            day = 10,
                            sipAmount = 3000
                        )
                    )
                ),

                lumpSumItems = listOf(
                    CartItemDomain(
                        id = "lump_1",
                        amcName = "Sector/Thematic Fund",
                        productName = "Baroda Bnp Paribas Energy Opportunities Fund",
                        amount = 5000,
                        type = CartType.LUMPSUM,
                        date = "2026-04-01"
                    ),
                    CartItemDomain(
                        id = "lump_2",
                        amcName = "Commodities • Gold",
                        productName = "SBI Gold Fund",
                        amount = 3000,
                        type = CartType.LUMPSUM,
                        date = "2026-04-01"
                    )
                )
            )

            _uiState.value = UiState.Success(
                CartUiModel(
                    cartData = dummyData,
                    selectedCartType = CartType.LUMPSUM
                )
            )
        }
    }

    fun reloadFund(){
        viewModelScope.launch {
            getUserCartUseCase()
                .onSuccess {
                    _uiState.value=UiState.Success(
                        CartUiModel(
                            cartData = it,
                        )
                    )
                }
                .onError {
                    SnackBarController.showSnackBar(SnackBarType.Info(it.message))
                }
        }
    }

    fun purchase(){
        if (_uiState.value is UiState.Success){
            val data= (_uiState.value as UiState.Success).data
            hidePopup()
            if (data.selectedCartType == CartType.SIP){
                purchaseSip()
            }else {
                purchaseLumpSum()
            }
        }
    }

    fun purchaseSip() {
        viewModelScope.launch {
            _loading.value = true
            purchaseSipUseCase()
                .onSuccess {
                    _loading.value = false
                    browserLauncher(it)
                }
                .onError {
                    _loading.value = false
                    Log("SNACK", "ERROR: ${it.message}")
                    SnackBarController.showSnackBar(SnackBarType.Error(it.message))
                }
        }
    }

    fun purchaseLumpSum() {
        viewModelScope.launch {
            _loading.value = true
            purchaseLumpSumUseCase()
                .onSuccess {
                    _loading.value = false
                    browserLauncher(it)
                }
                .onError {
                    _loading.value = false
                    Log("SNACK", "ERROR: ${it.message}")
                    SnackBarController.showSnackBar(SnackBarType.Error(it.message))
                }
        }

    }

    fun showPopup(){
        _confirmationPopupVisible.value=true
    }

    fun hidePopup(){
        _confirmationPopupVisible.value=false
    }

}