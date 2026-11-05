# Parked code (not compiled)

Files here are **not** part of any source set. They were taken out of
`composeApp/src/...` during the Janta Nivesh migration because the new feature-based code
replaced them in the live flow, but they may be wanted again. Each keeps its original path
under `src/`, so it can be moved back with `git mv`.

Expect them not to compile as they are: they still reference old Velvet types and composables
(for example `MutualFundIcon` now lives in `shared/compose/`).

| Path | Why it's parked | Replaced by |
|---|---|---|
| `presentation/portfolio/compose/ExistingFundScreen.kt`, `ExistingFundsLumpSumScreen.kt`, `presentation/portfolio/viewmodel/ExistingFundsLumpSumViewModel.kt` | **Top-up of existing funds.** It's off for now; search the code for `TODO(top-up)` to find the spots to re-enable (routes, nav destinations, InvestmentMethodScreen sheet) | Not replaced yet |
| `presentation/mutualfund/compose/CategoryMutualFundScreenRoot.kt`, `MutualFundSearchResultRoot.kt`, `MutualFundDetailsScreen.kt`, `BundledFundCard.kt` and `presentation/mutualfund/viewmodel/*` | The old MF list, search and details screens | `features/mutualfund/ui/...` |
| `presentation/explorefunds/compose/ExploreFundScreen.kt`, `presentation/explorefunds/viewmodel/ExploreFundScreenViewModel.kt` | The old explore tab | `features/bottomNavigation/ui/...` |
| `utils/GraphChartPruning.kt` | Worked on the old MF graph model | `features/core/utils/GraphChartPruning.kt` |
| `presentation/portfolio/compose/{PortfolioScreen, SIPDetailsScreen, FDDetailsScreen, FolioFundsMFScreen, RedemptionBottomSheet, HistoryTimelineCard, CancelSIPScreen}.kt`, `presentation/portfolio/viewmodel/{PortfolioScreenViewModel, MFPortfolioDetailsViewModel, FolioFundsMFViewModel, FDPortFolioDetailsViewModel}.kt`, `presentation/portfolio/models/{SelectedPortfolio, FDDeatilsUIModels, MutualFundDashBoardData, FundListCardData}.kt` | The old portfolio (incl. Velvet's withdraw bottom sheet + web view) | `features/portfolio/...` |
| `features/portfolio/ui/screens/{ExistingFundScreen, ExistingFundsLumpSumScreen}.kt`, `features/portfolio/ui/viewmodel/ExistingFundsLumpSumViewModel.kt` | **JN's top-up** screens, parked like Velvet's | Not replaced yet |
| `presentation/cart/compose/*` (CartScreen, CartPopup, CartCutOffPopup, CartFab, LumpSum/SIPCartContent), `presentation/cart/viewmodel/CartScreenViewModel.kt` | The old (v1) cart | `features/cart/presentation/...` |
| `presentation/fixeddeposits/viewmodel/{FDSearchResultViewModel, FDDetailsViewModel, FDPurchaseViewModel}.kt`, `presentation/fixeddeposits/uimodels/{FDPurchaseUiModel, Mapper, FixedDeposits}.kt` | The old FD view models (Velvet's FD screens now run on JN's) | `features/fd/ui/viewmodels/*` |
