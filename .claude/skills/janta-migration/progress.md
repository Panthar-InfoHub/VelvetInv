# Migration progress

Update this file at the end of every porting session. It is the only memory between sessions.

Status values: `not started` · `in progress` · `ported` (compiles, wired) · `verified` (user checked on device) · `skipped`

## Features

JN file counts as of 2026-10-08. "Velvet counterpart" is a best guess — confirm during step 2 of the procedure.

| JN feature | Files | Velvet counterpart | Status | Notes |
|---|---|---|---|---|
| core (shared UI, utils, data) | 80 | `shared/`, `utils/` | partial | `PurchaseMode`, `PaginatedData`, `LabelFilter`, `fundfiltersystem/*` |
| splashscreen | 4 | `presentation/SplashScreen` | not started | |
| login | 23 | `features/login` (was `presentation/LoginScreen`) | ported | 2026-11-05: data/domain = JN (login + verify only), UI = Velvet's, unchanged; wired in `LoginNavigation`. ChooseLanguage dropped. Not checked on device |
| auth (PIN / biometric) | 11 | `presentation/settingscreens` (PIN mockups, never wired) | ported | 2026-11-05: MPIN (app lock, Enter/Change PIN) + Biometric Login; profile Security → Change PIN / Biometric Login. Not checked on device |
| onboarding (KYC onboarding) | 59 | becomes Velvet's real onboarding; old Velvet onboarding → `fireonboarding` | ported | data/domain/UI (by user, 2026-11) + JN `OnBoardingNavigation` graph; wired 2026-11-05 (after login, at launch, and from Check KYC). Not checked on device |
| bottomNavigation (home) | 8 | `presentation/homescreen`, `shared/Navigation/BottomNavigation.kt` | partial | 2026-11-05: Explore tab (`ExploreFundsViewModel` = JN, screen in Velvet style). Home: JN onboarding-prompt KYC cards only (Velvet home VM/layout otherwise) |
| mutualfund | 54 | `presentation/mutualfund`, `explorefunds` | ported (buy flow) | 2026-11-05: data/domain = JN; Category / SearchResult / Details / InvestmentMethod in Velvet look; details = JN cart FAB + "Add to Cart" sheet next to Velvet's "Purchase Fund"; top-up parts commented out (`TODO(top-up)`). Not checked on device |
| plans (SIP/lumpsum purchase) | 22 | none — Velvet buys via cart | ported | data/domain/UI by user (2026-11); 2026-11-05: wired in `shared/Navigation/MutualFundNavigation.kt`, DAILY removed, UI theme-translated (typography slots, `Spacing`, colour tokens). Not checked on device |
| cart | 56 | `presentation/cart` | ported | 2026-11-05: all of JN `features/cart`; screens = JN function on Velvet's old cart visuals (`velvetize.py`); Cart + CartCheckoutOtp wired in `shared/Navigation/CartNavigation.kt`. Not checked on device |
| bundles | 20 | `presentation/bundle` | ported | 2026-10-08; compiles + wired; Bundles is a bottom-nav tab; not yet checked on device |
| fd | 26 | `features/fd` (was `presentation/fixeddeposits`) | ported | 2026-11-05: data/domain/VMs = JN on `getFDUrl`, UI = Velvet's 3 screens re-pointed at JN state/events; nav `FdNavigation.kt`; Explore top picks via feature. Not checked on device |
| goals | 25 | `features/goals` (was `presentation/goals`, deleted) | ported | 2026-11-05: data/domain/VMs = JN (v2 `/user-goal` config/calculate/create/map/remove); 4 screens in Velvet look; nav `GoalsNavigation.kt`; home + `/user/` goals on JN `GoalsSummaryDomain`. Not checked on device |
| portfolio | 92 | `presentation/portfolio` | ported | 2026-11-05: data/domain/VMs = JN; PortfolioScreen = Velvet design (3 tabs, 3 reports) + JN header "My Orders", JN money formatting, full refresh; JN Active SIP tab left out. Row → order details → Redeem → OTP (JN). Other screens: Velvet visuals restored via `velvetize.py`. Top-up commented/parked. Not checked on device |
| orders | 11 | partial in `presentation/portfolio` (pending orders, cancel) | ported | 2026-11-05: My Orders + Order details, Velvet headers/empty state; entry from portfolio header + PurchaseSuccess "View holdings". Not checked on device |
| search | 6 | in `presentation/explorefunds` | ported | 2026-11-05: overlay restyled; Room 2.8 `VelvetDatabase` (recent searches). Daily/monthly quick actions dropped |
| insurance | 9 | `presentation/insurance` | not started | |
| profile | 21 | `presentation/profile`, `settingscreens` | not started | drop LanguageSelectionSettings |

## Bundles work plan (started 2026-10-08)

User decisions: port **slices** of cart/mutualfund/plans that bundles needs (marked partial below);
All Bundles becomes a **bottom-nav tab** like JN. JN's flow has no review screen — the details
page carries purchase mode, debit day and summary, and adds straight to the cart.

~~Known gap: bundles v2 cart vs Velvet's v1 cart screen~~ — gone 2026-11-05: the cart screen is JN's
now and reads the same `/user/cart` the bundles add to.

Files (JN path → same path under `org.sharad.velvetinvestment.`):
- [x] `features/bundles/**` — all 20 files
- [x] cart (partial): `domain/CartCountController.kt`; `data/remote/model/bundlecart/*` (3);
      `data/remote/model/usercart/{UserCartDto,Data,CartItem}.kt`; `data/remote/mapper/UserCartMapper.kt`;
      `domain/models/UserCartDomain.kt`; `domain/repository/CartRepository.kt` + `data/repository/CartRepo.kt`
      (only `getCart`, `addBundleToCartLumpsum`, `addBundleToCartSip`);
      `domain/usecases/{AddBundleToCartLumpsum,AddBundleToCartSip,GetUserCart}UseCase.kt`.
      `features/cart/CartInfo` → Velvet `utils.CartInfo` (identical; drives Velvet's cart badge)
- [x] mutualfund (partial): `domain/models/{MutualFundDomain,SelectedReturnRatePeriod}.kt`;
      `utils/MutualFundUtils.kt`; `data/remote/model/mffunds/MfFundsDto.kt`; `data/remote/mapper/MfFundsMapper.kt`;
      `domain/repository/MutualFundRepository.kt` + `data/remote/repository/MutualFundRepo.kt` (only `getFunds`);
      `domain/usecases/GetMutualFundSearchResultUseCase.kt`; `ui/viewmodel/MutualFundSearchResultViewModel.kt`
- [x] plans (partial): `domain/model/SchemePlan.kt`; `data/model/SchemePlanDtos.kt`;
      `domain/repository/PlansRepo.kt` + `data/repository/PlansRepoImpl.kt` (only `getSchemePlan`);
      `domain/usecases/PlanUseCases.kt` (only `GetSchemePlanUseCase`);
      ~~`PurchaseDayPickerSheet`, `DebitDayField`~~ — NOT ported: Velvet's `ListWheelPicker` does the same job,
      and `DebitDayField` was recreated inside `features/bundles/.../InvestmentSummaryCard.kt` in Velvet's SIP-day style
- [x] core (partial): `features/core/domain/models/{PurchaseMode,PaginatedData}.kt`;
      `features/core/utils/LabelFilter.kt` + `features/core/utils/fundfiltersystem/*` — ported mirrored, NOT merged:
      JN's `CreateFilterData`/`LabelFilter` drifted 137/74 lines from Velvet's, merging would change Velvet's explore screen.
- [x] shared composables: same-name → merge function into Velvet: `AppSearchBar`, `BackHeader`, `ErrorScreen`,
      `LoaderScreen`, `ShadowCard`, `PaginationEffect`, `PaginationFooter`, `NextButtonFooter`.
      JN-only → recreate: `FundIcon`. Modifiers: `clearFocusOnTap`, `genericDropShadow`, `dashedBorder`→`dottedBorder`.
- [x] drawables: copy `ic_bundle_card_fund`, `ic_pointer_right`, `rupee_icon`; keep Velvet's `lock_icon`, `plus_icon`, `tick_icon` (differ)
- [x] wiring: Koin modules, routes `BundleDetails`/`BundleSelectFund`/`BundleExploreFunds`, re-point `AllBundleScreen`,
      Bundles bottom-nav tab, Velvet entry points (AppNavigation.kt ~361, ~619)

## FIRE onboarding refactor (2026-10-08, Velvet-only, user-requested)

Velvet's old onboarding questionnaire + FIRE report now live in `features/fireonboarding/`:
- `presentation/onboarding/**` (8 steps, models, nav graph `FireOnboardingNavigation`, root `FireOnboardingScreenRoot`)
- `presentation/firereport/**` (report, update options, 3 edit screens)
- `navigation/FireOnboardingRoutes.kt` (feature-local `FireOnboardingRoute`), `navigation/FireReportNavigation.kt`
  (`NavGraphBuilder.fireReportDestinations(...)`, ready but NOT called), `di/fireOnboardingModule`.
- All 12 ViewModels converted to MVI (UiState data class + sealed Event + Effect channel + `handleEvent`), screens take
  `(state, onEvent)`. Derived values are UiState getters; shared math in `models` (`expensePercentages()`, `summary()`, `total()`).
- Generic widgets moved to `shared/compose`: `NextButtonFooter`, `OnBoardingTextField`, `MoneyTextField`, `IconMoneyTextField`.
- ~~UNWIRED~~ — wired 2026-11-05, see "FIRE onboarding wired".
- Data/domain (UserFinance repo, use cases) left in Velvet's layer-first `data/`/`domain/` — shared with home, goals, loans.

## FIRE onboarding wired (2026-11-05, Velvet-only, user-requested)

Home: JN's 4 onboarding-prompt cards (`OnboardingPromptCard` in `presentation/homescreen/compose/KYCCard.kt`; tap →
`navigateToKYC` = `Route.OnboardingGraph(true)` directly, like JN — was Check KYC; KYC-in-progress = refresh icon →
`HomeScreenViewModel.silentRefresh()`). Trading-account card removed. FIRE section: `is_fire_completed` → `FireReportCard`
(opens `FireOnboardingRoute.FireReport`), else `CompleteFireJourneyCard` ("N of 5 steps done") → `FireOnboardingRoute.FireOnboardingFlow`.

FIRE onboarding = finance → assets → loan → insurance → goals (personal details + summary are no longer part of it):
- API (`features/fireonboarding/data`): `POST /onboarding/{finance|assets|loans|insurance|goals}`, each with either
  `FireStepSkipBodyDto {skip:true}` or its data body (`FinanceStepBodyDto`, `AssetsStepBodyDto`, `LoansStepBodyDto`,
  `InsuranceStepBodyDto`, `GoalsStepBodyDto` — goals = `CreateGoalRequest.toCreateBody()` list). Response `FireStepResponseDto`
  (only `onboarding` modelled). `FireOnboardingRepository` (Koin single).
- Stage logic `domain/models/FireStage.kt`: `nextFireStage()` = first stage in that order that is not VERIFIED/SKIPPED
  (missing status counts as pending).
- Nested NavHost: `FireOnboardingScreenRoot` (header step x/5 + Skip, CAS dialog) → `FireOnboardingNavigation`
  (start `Resolving` loader/error, then step routes; `AddLoan` and `GoalAdd` pushed on top). Coordinator
  `FireOnboardingViewModel`: reads `GET /user/` → NavigateToStage (pops the inner stack); Skip → skip API → re-read;
  step VM `FireStepEffect.Submitted` → re-read; none left → `sendFireRefreshEvent()` (one event: home reloads; FIRE
  report VM reloads + forwards home refresh) → Close pops `FireOnboardingFlow`. Back on a step leaves the flow (saved steps stay).
- Step VMs got the repo + `OnSubmitClicked`/`isSubmitting`/`canSubmit`. Continue is disabled when a step is empty
  (income 0, assets 0, no loans, insurance 0, no goals) — the user skips instead. Loan delete now works.
- Goals step: `FireGoalsViewModel` reuses the goals feature (`/user-goal/config`, `/calculate`, `GoalFormState.toRequest()`,
  `AddGoalUiState`/`AddGoalEvent`) and the goals `FinancialGoalScreen` (new `title`/`saveText` params, defaults unchanged);
  Save adds a `FireGoalDraft` to the list; Continue sends all in one `POST /onboarding/goals`. `OnBoardingGoalScreen` rewritten
  for drafts (`FireGoalDraftCard`, Velvet onboarding card look).
- FIRE report wired: `fireReportDestinations` in `AppNavigation`; Update Details → Loans = `Route.LoansFlowScreen`
  (unchanged `/user-loan` CRUD), Goals = `Route.GoalsScreen`. Finance/Assets/Insurance edit VMs now save through the
  onboarding step endpoints (skip=false) instead of `/onboarding/complete-onboarding`.
- `ContinueBackButtonFooter` gained `loading` (default false). `refresh_icon` copied from JN (Velvet had none).

## JN onboarding import — status

- Ported: `features/onboarding/{data,domain}` (32 files), `features/core/domain/models/OnboardingStage.kt`,
  `AuthPrefs.{set,get}OnboardingStage` + `advanceOnboardingStage` (Velvet class, additive), `onboardingModule`.
- Wired 2026-11-05 (interim MainAppFlow routing removed). See "Login feature + base navigation".
- UI ported (2026-11, by user): 16 screens + 12 VMs under `features/onboarding/ui/**`, OTP UI (`features/core/ui/otp`).
- Remaining: JN `OnboardingNavigation` graph (514 lines) + `Route.OnboardingGraph(stage)`,
  OTP UI (`features/core/ui/otp`), shared composables (`TitledAppTextField`, `AppDatePicker`, `DropDownSelector`,
  `AgreementCheckBoxCard`, `InfoNoteCard`, `AppBackButton`), image picker sheet, external-app (UPI) launcher, location.

## Navigation plan: JN login + onboarding (agreed 2026-10-08)

App-level routes added to Velvet `Route` (JN names): `LoginGraph`, `OnboardingGraph(stage: String, shouldRefresh = false)`,
`MainAppGraph`(= Velvet `MainAppFlow`), plus the login/onboarding destinations below.

```
BaseNavigation start:
  !isLoggedIn            -> LoginGraph
  !isOnboardingCompleted -> OnboardingGraph(OnboardingStage.resumePoint(prefs.getOnboardingStage()).id)
  else                   -> MainAppFlow

LoginGraph (features/login, JN; ChooseLanguage DROPPED — no localization):
  SplashScreen (first launch: notification -> location permission ask)
    -> LoginWithPhone -> EnterOtp(phone)
         EnterOtpEffect.NavigateOnboardingFlow(stage) -> OnboardingGraph(stage)   (pop LoginGraph)
         EnterOtpEffect.NavigateToMainAppFlow          -> MainAppFlow              (pop LoginGraph)

OnboardingGraph(stage) (features/onboarding, JN), start = resumePoint(stage):
  KYCSplash -> PANVerification -> [KYCInitiation (DigiLocker) -> SignatureUpload] -> BankVerification
    -> (UpiPaymentWebView for reverse penny drop) -> Email -> EmailOtp(email) -> Profile (geotag) -> Nominee | NomineeOptOutTerms
  onCompleted: prefs.setLoggedIn(true); setOnboardingCompleted(true); if shouldRefresh send HomeRefresh; -> MainAppFlow (pop graph)

MainAppFlow: "complete KYC" entry points -> OnboardingGraph(stage, shouldRefresh = true)
  (replaces Velvet's old KYC / trading-account entry points — confirm each when wiring)
```

Port order: (1) ✅ location expect/actual + dep (2026-10-08: `utils/location` common/android/ios, `play-services-location` 21.4.0, Android FINE/COARSE permissions, iOS `NSLocationWhenInUseUsageDescription`, `LocationProvider` in both platformModules; Android + iOS compile), (2) shared core UI (`features/core/ui/otp`, `TitledAppTextField`,
`AppDatePicker`, `DropDownSelector`, `AgreementCheckBoxCard`, `InfoNoteCard`, `AppBackButton`, image-picker sheet,
external-app launcher), (3) JN login (replaces Velvet `presentation/LoginScreen` — superseded code listed, deleted only on approval),
(4) JN onboarding UI, (5) wire `BaseNavigation` per plan and remove the `TODO(jn-onboarding)` interim routing.

## User data (`GET /user/`) refactor — 2026-11-05

`shared/data/remote/model/userdata/*` (JN shape) replaced Velvet's old `data/remote/model/useedata/*`, which had
`user_home_data` (fire %, net worth) and `kyc_types` — **the server no longer sends either**.
- All `shared/{data,domain}` files moved off leftover JN packages to `org.sharad.velvetinvestment.shared.*`.
- DTO matches the live response (sample in session 2026-11-05): `OnboardingStatusDto` gained `finance/assets/loan/insurance/goals_status`,
  `is_fire_completed`, `fire_completed_at`, `has_navigated_home`, `stages`/`fire_stages` maps; `Data` gained top-level
  `is_completed`, `is_fire_completed`. Finance/assets/insurance/loan DTO fields are nullable strings.
  `UserGoal.holdings` was dropped here, then restored by the goals port (2026-11-05); `UserGoal` now matches JN.
- Domain: `shared/domain/models/FireDataDomain.kt` (`UserFinanceDomain`, `UserAssetsDomain`, `UserInsuranceDomain`, `UserLoanDomain`);
  `UserDataDomain` gained `finance`, `assets`, `insurance`, `loans`, `isFireCompleted`, `netWorth` (assets − loan outstanding),
  `totalLoanOutstanding`, `totalMonthlyEmi`; the raw `UserFinance` DTO field was removed. `OnboardingStatusDomain` gained the FIRE step statuses.
- Goals from `/user/` → `GoalsSummaryDomain` via `toGoalSummary()` in `UserDataToDomain.kt`. Since the goals port this is JN's
  `toDomain().toSummary()` and the domain type is JN's `features/bottomNavigation/domain/models/GoalsSummaryDomain` (the
  interim id-1/2 → child-goal mapping is gone).
- Koin: `single<UserDataRepo>`, `factory { GetUserDataUseCase }` (were never registered). Old `UserAuth.getUserData()` still exists, unused.
- Consumers moved to `GetUserDataUseCase` + domain: home, goals info, add goal, insurance, check-KYC, personal info, FIRE edit VMs (finance/assets/insurance).
- Home FIRE card: `/user/` lost the fire %, so `HomeScreenViewModel` reads this year's `includeEmi` point from `GetFireReportUseCase`
  (only when `isFireCompleted`). Net worth = `UserDataDomain.netWorth`. Trading-account prompt hidden (`tradingAccountCompletion = true`);
  Check-KYC `tradingAccountKYC = false`; profile "Trading Account Status" shows "Unknown".

## Login feature + base navigation (started 2026-11-05)

Scope (user): move Velvet `presentation/LoginScreen` + its data/domain into `features/login` (JN layout), APIs as JN,
**UI untouched**, verify-OTP DTO = live response (2026-11-05); `BaseNavigation` per JN `Navigation.kt`; onboarding graph =
JN `OnBoardingNavigation.kt` (already copied to `shared/Navigation/OnBoardingNavigation.kt`). FIRE onboarding does not gate login:
post-OTP decision is JN's (`onboarding.can_navigate_home`).
- [x] `features/login/data/models/auth/{sendotp,verifyotp,tokens}` (from Velvet `data/remote/model/auth`; verifyotp per live JSON,
      `onboarding` = shared `OnboardingStatusDto`; old `Metadata` dropped), `data/mapper/AuthMappers.kt`,
      `data/repository/UserAuthenticationRepo.kt` (req-otp, validate-otp only; notifications stay on Velvet `UserAuth`),
      `domain/{model/LoginDomain, repository/UserAuth, usecases/LoginWithNumberUseCase, usecases/VerifyOTPUseCase}`, `di/loginModule`
- [x] UI moved as-is to `features/login/ui/{screens,viewmodels}` (Velvet `LoginScreenViewModel` kept — UI calls it directly)
- [x] Velvet `UserAuth`: drop `loginWithNumber`/`verifyOTP`; HttpClient refresh imports → features/login tokens
- [x] Routes: `LoginFlow`→`LoginGraph`, `MainAppFlow`→`MainAppGraph` (JN names); `OnboardingGraph(shouldRefresh)`
- [x] `BaseNavigation`: JN start-destination logic, LoginGraph → OnboardingGraph / MainAppGraph, onboarding `onCompleted`,
      token-expiry LogOut listener moved up from `AppNavigation` (+ `SignOutUseCase` wipe), main-app KYC entry → `OnboardingGraph(true)`
- [x] Onboarding VMs registered in `onboardingModule` (were missing → graph would crash)

Done 2026-11-05 — notes for later sessions:
- Login UI is still Velvet's (`LoginScreen`/`OtpScreen` taking one shared `LoginScreenViewModel`, routes
  `Route.SplashScreen/LoginScreen/OtpScreen`), not JN's `LoginWithPhoneNumberViewModel`/`EnterOtpViewModel` +
  `Route.LoginWithPhone/EnterOtp(phone)`. Porting JN's VMs means touching the UI — user said not to, so ask first.
- Behaviour changes in the VM: verify-OTP is one attempt (was 3 retries — re-sent a wrong OTP); `OtpScreen` routes on
  `LoginDomain.canEnterMainApp` (`onboarding.can_navigate_home`) and `navigateToOnboarding` takes no stage.
- Removed (moved): `data/remote/model/auth/**` → `features/login/data/models/auth/**` (old `verifyotp/Metadata` dropped),
  `data/remote/mapper/AuthMappers.kt`, `domain/models/auth/LoginDomain.kt`, `domain/usecases/user/{LoginWithNumber,VerifyOtp}UseCase.kt`;
  `loginWithNumber`/`verifyOTP` off Velvet `UserAuth`. `HttpClient` refresh uses `features/login/.../tokens`.
- Velvet `UserAuthenticationRepo` still takes `DeviceInfoRetriever` (now unused). `AuthPrefs.{set,get}OnboardingStep` no longer written.
- Token expiry: `AppEvent.LogOut` listener moved from `AppNavigation` to `BaseNavigation`; it now runs Velvet `SignOutUseCase`
  (clears prefs + client tokens) before going to login. Before, expiry navigated to login but left the session on disk.
- Main-app KYC entry: Check KYC → "Mutual Fund KYC" → `navigateToKYC` → `Route.OnboardingGraph(shouldRefresh = true)`.
  Home/profile/cart KYC prompts still open Check KYC first (unchanged).

## MF purchase flow + bundles sync (started 2026-11-05)

User request: feature-based MF; flow is ExploreFunds → InvestmentMethod (picks the default mode:
monthly SIP or one-time) → CategoryMutualFund (bundles + JN search overlay) → search result →
MutualFundDetails (id **and isin** on the route) → "Purchase Fund" → plans `FundPurchase`
(opens on the chosen mode) → rest exactly as JN (SipSetup → SipPurchaseOtp → PurchaseSuccess,
lumpsum payment web view, AddMandate). Only MONTHLY + ONE_TIME anywhere (rule 8).
Top-up (existing fund) flow is parked: commented out on InvestmentMethod, its files moved to
`composeApp/later/` (not compiled, original paths kept) — never deleted.

Decisions:
- "later" = `composeApp/later/<original path>` (outside the source set, so it never breaks the
  build). Holds only files nothing references any more.
- Old Velvet MF data/domain (`data/remote/repository/MutualFundRepo`, `domain/usecases/fundusecases`,
  `domain/models/mutualfunds`, `presentation/mutualfund/UIModels.kt`, `MutualFundIcon`,
  `InvestmentFilterScreen`) stays: portfolio, cart, home, FD, goals still use it. The new feature
  carries JN's own data/domain side by side.
- `FundTypeSelector` = Velvet's `utils.FundTypeSelector` (identical to JN's; cart shares it).
- ExploreFunds screen moves to JN's path `features/bottomNavigation/ui/{compose,viewmodels}`.
- Search overlay storage: JN `core/database` (Room 3) adapted to Velvet's Room 2.8 at
  `shared/data/local/database/` (+ android/ios builders). KSP + room plugin already configured.
- FundPurchase gets an initial mode (route `purchaseMode`, from `FundTypeSelector`); it still
  falls back to a mode the scheme offers.

Work plan:
- [x] mutualfund data/domain = JN (details, graph, frontend-data categories, redeem, top picks, all use cases)
- [x] search feature + Room DB (RecentSearch)
- [x] mutualfund UI: Category, SearchResult, Details (Purchase Fund button), InvestmentMethod,
      sub-composables — Velvet visuals, JN behaviour; no Daily ₹10 / Monthly ₹100 chips
- [x] ExploreFunds: Velvet screen on JN `ExploreFundsViewModel` (MVI, `getFunds(tag=popular, limit=4)`)
- [x] bundles sync with JN: per-mode `types`, `BundleDetailsViewModel` mode snapshots, card amounts
- [x] plans: one `PurchaseMode` (drop the `shared/domain/models` duplicate's users), register VMs,
      routes FundPurchase/SipSetup/SipPurchaseOtp/PurchaseSuccess/AddMandate + web-view returns
- [x] routes/nav rewired; old screens no longer referenced moved to `composeApp/later/`
- [x] check-port + Android + iOS compile; record

Wiring notes (2026-11-05):
- All MF + plans destinations live in `shared/Navigation/MutualFundNavigation.kt`
  (`mutualFundDestinations(navController, navigateToKYC)`, called from `AppNavigation`). Plans web-view
  returns go through `completePlansWebView(key)` from the `WebViewScreen` destination.
- Every fund tap (explore top picks, category, search result) → `MutualFundDetails(id, isin)`;
  "Purchase Fund" → `FundPurchase(…, purchaseMode = FundTypeSelector → MONTHLY/ONE_TIME)`. Details VM
  falls back to the ISIN from `GET /mf/{id}` when the route has none (portfolio folio entry).
- PurchaseSuccess "View holdings" → back to `BottomNav` (JN opens My Orders; Velvet has none yet).
- Portfolio `FolioFundScreen` "Top Up" still opens `MutualFundDetails(id, folioId)`, which now shows
  "Purchase Fund" (a new purchase, not a folio top-up) — see Open questions.
- `NavLineChart` now takes the feature `MutualFundGraphPointsDomain`; pruning util ported to
  `features/core/utils/GraphChartPruning.kt`.
- `MutualFundIcon` extracted to `shared/compose/MutualFundIcon.kt`; `LumpSumAdd` to
  `presentation/portfolio/viewmodel/LumpSumAdd.kt` (still used by the invest-more use case).
- Parked in `composeApp/later/` (see its README): old MF Category/SearchResult/Details screens + VMs,
  `BundledFundCard`, old ExploreFund screen + VM, ExistingFund(LumpSum) screens + VM, old `utils/GraphChartPruning`.
- `shared/domain/models/PurchaseMode.kt` (has DAILY) has no users left; delete when convenient.
- Old Velvet `domain/TransactionStatus` kept; the JN copy in `features/mutualfund/domain/models` is unused.

## Orders + portfolio (2026-11-05)

User plan (2026-11-05): (1) orders + PurchaseSuccess → My Orders; (2) portfolio — Velvet's PortfolioScreen
design with its 3 tabs (Dashboard / Mutual Funds / Fixed Deposits) and all 3 reports (Holdings/Capital,
Tax, Portfolio), everything else JN incl. flows; top-up parts commented out for a future reimplementation;
(3) cart — next, also settles the "cart icon on CategoryMutualFund" question.

- Orders: `features/orders` (JN), Koin `ordersFeatureModule`, nav `shared/Navigation/OrdersNavigation.kt`.
  Retry-order passes the purchase mode (systematic → MONTHLY else ONE_TIME); "Need help" is a no-op (no
  Request Callback screen in Velvet); receipt download is JN's "coming soon" snackbar. JN footer is commented out in JN too.
- Portfolio: `features/portfolio` (JN), Koin `portfolioFeatureModule`, nav `shared/Navigation/PortfolioNavigation.kt`
  (order details, Redeem, RedeemOtp, FD details ↔ web view `fd_details`, folio funds).
  - PortfolioScreen rebuilt from Velvet's file (`velvetbase.py`) + JN function. VM = JN's + Velvet's capital/tax
    downloads + pending-orders read (JN declares `pendingOrders` but never loads it; Velvet's MF tab lists them
    with Cancel, so the read stays). JN's portfolio-report error branch didn't reset the spinner — fixed.
  - Active SIP: tab + `loadActiveSips()` commented out (3 tabs); `ActiveSipDomain`/use case kept.
  - SIP details = Velvet layout (details list + Withdraw/Cancel footer, "SOA" button) with JN behaviour:
    Withdraw → `Route.Redeem` (JN screens), Cancel → JN `CancelSipReasonSheet` + cancel-plan API. Start-date row
    dropped (not on payload). Velvet's RedemptionBottomSheet + withdraw web view are gone (parked).
  - Portfolio row → order details directly (JN); `FolioFundScreen` registered but unreachable (as in JN).
  - `Route.SIPPortfolioDetails` = JN shape (holdingId, returnAmount Double, currentValue, …); `Route.Redeem`, `Route.RedeemOtp` added.
  - Top-up: folio "Top Up" footer + `onTopUp` commented (`TODO(top-up)`); JN ExistingFund screens/VM parked;
    `LumpSumAdd` moved to `features/portfolio/domain/models/FolioFundDomain.kt`.
  - Kept old Velvet (still used): `presentation/portfolio/compose/{EmptyFundScreen, SIPCancellationReasonScreen,
    StopSipReasonSelection}`, `models/{StopSipReason, FDCardPortfolioData}`, `viewmodel/LumpSumAdd`, old portfolio
    domain/use cases (home, FD, shared cards). Cancel-SIP confirmation route now uses the feature screen.
- Tools (scratchpad, recreate if lost): `velvetize.py <jn> <velvet>` restores Velvet lines where an aligned changed
  line differs only in typography/style/colour/dp/shape; `velvetbase.py <velvet> <dest>` copies a Velvet screen into
  a feature re-pointing old portfolio imports. theme.py now maps `ProfileTitleColor`→Black, `grayColor`→titleColor,
  `redColor`→appRed, `LightGrayBorder`→lightGray, `White`/`GrayScreenBackGround`→Color.White.
- Utils merged: `formatMoneyAfterLWithPaise`, `Double.toTwoDecimals` → `utils/MoneyFuntions.kt`.

- 2026-11-05 (later): **Active SIP tab added** (user request) — `SelectedPortfolio.tabs` = JN's 4 (Dashboard / Mutual Funds /
  Active SIP / Fixed Deposits); `refresh()` calls `loadActiveSips()` again; `ActiveSipPortfolio`, `ActiveSipSummaryCard`,
  `ActiveSipCard`, `resolveHolding` (tap → SIP details of the matching holding) ported into Velvet `PortfolioScreen.kt`
  (Velvet shapes/`FundIcon`). Rule 8: Monthly/Daily sub-tabs only shown when the user already has daily SIPs. "See all"
  page jumps now use `tabs.indexOf(...)` instead of hardcoded 1/2.

## Cart (2026-11-05)

- `features/cart/**` = JN (56 files; `CartInfo` stays Velvet's `utils.CartInfo`). Koin `cartFeatureModule`
  (repo, 16 use cases, `CartViewModel`). Old `presentation/cart/**` (screens + `CartScreenViewModel`) →
  `composeApp/later/`; its `ViewModelModule` entry removed. `AllBundlesScreen` uses the feature `CartFab`.
- Screens: JN's, with Velvet's visuals kept where Velvet's old cart differed (velvetize + by hand):
  `Inter` for the ₹ glyph, "Clear Cart" header action (wired to JN's `OnClearCartClicked`), `imePadding`.
  `CartCutOffPopup` = Velvet's (consent toggle, `LaunchBrowserUseCase` AMFI link) + JN previews — JN never
  shows the popup (`showCutOffPopup` is never set) and its copy has the consent commented out with
  `enabled = isChecked` stuck false. Step-up stays commented out as in JN.
- Nav (`CartNavigation.kt`, after JN MainAppNavigation): `Route.CartScreen` + new `Route.CartCheckoutOtp`
  (shares the cart entry's VM). Effects: mandate → WebView "UPI Autopay" key "cart"; OTP → CartCheckoutOtp;
  KYC → `navigateToKYC`; orders → My Orders. Web-view key "cart" handled by `completeCartWebView`
  (`CART_WEBVIEW_RESULT` moved there from AppNavigation).
- Entry points: bottom-nav cart, bundles `NavigateToCart`, CategoryMutualFund header icon (kept — JN
  comments its icon out, but it now opens the working JN cart), MF details `CartFab`.
- Issue 3 settled: MF details has JN's `CartFab` + `ContinueBackButtonFooter("Add to Cart" | "Purchase Fund")`.
  "Add to Cart" opens JN's `CartPopup` (SIP date via `ListWheelPicker`); VM = JN's cart logic
  (`addToCart`, `MFBottomSheetType`); `toSipRequest` restored in `UIModels.kt`. KYC **and** trading-account
  errors open `KYCPopup` → `navigateToKYC` (JN's trading-account action is empty). No frequency picker
  (no daily SIP, rule 8). Folio top-up branch of `addToCart` left out (`TODO(top-up)`).

## FD (started 2026-11-05)

User scope: all FD data + domain + ViewModels = JN; UI = Velvet's screens only; add `FD_URL`/`getFDUrl` to
`utils/networking/BaseUrl.kt` matching JN (`https://vlv-jn-001-34918043640.asia-south1.run.app/api/v1`).

Inventory (JN `features/fd`, 26 files; `ui/unused/*` and JN screens NOT ported):
- [x] data: `models/dto/{FDDetailsDto,FixedDepositListDto,PurchaseFDDto}`, `mapper/FDMappers`, `repository/FixedDepositRepo` (getFDUrl)
- [x] domain: `repository/FixedDepositRepository`, `usecases/{GetFDDetails,GetFixedDepositsSearchResult,GetTopPickFD,PurchaseFD}UseCase`,
      `model/{FDDetailsDomain,FilterModels,FixedDepositDomain,PaginatedData,ReturnYears,RiskLevel}`, `utils/{FdFilterUtils,FdMathUtils}`
- [x] VMs: `ui/viewmodels/{ExploreFd,FdDetails,SetInvestmentDetails}ViewModel`
- [x] UI: Velvet `presentation/fixeddeposits/compose/*` → `features/fd/ui/compose/` (Velvet file names), re-pointed at JN state/events
      (FDSearchResultScreen ↔ ExploreFd, FDDetailsScreen ↔ FdDetails, PurchaseFDScreen ↔ SetInvestmentDetails)
- [x] wiring: `fdFeatureModule`, `shared/Navigation/FdNavigation.kt` (JN effects, web-view key `fd_purchase`), Explore top picks → feature use case
- [x] old Velvet FD VMs/uimodels → `composeApp/later/`; old FD data/domain listed as superseded
Deps: core networking/SnackBar/Log/parseHtmlToReadableText → Velvet `utils`. No new gradle deps, no expect/actual.

Done 2026-11-05 — notes for later sessions:
- Only VM change vs JN: `FdDetailsViewModel` `OnEditAmount/OnPayoutType/OnApplicantCategoryClicked` set `activeSheet`
  (commented out in JN, which uses inline dropdowns; Velvet's details screen uses bottom sheets). Screens send `OnCloseSheet` after a pick.
- Behaviour now JN's: payout/applicant changes re-fetch `GET /fd/{id}?payout_frequency&customer_type` (rates card fades while
  `isRefreshingRates`); payout list = `PayoutType.all` (adds On Maturity; Cumulative reads "Cumulative", was "Maturity");
  applicant names Regular/Senior Citizen/Women/Senior Citizen Women (were Standard/Female…); header rate = `headlineTenure`;
  list card rate/tenure = `bestTenure` + `label()` (was first slab + hardcoded "3 Y"); search runs in place (search bar moved
  above the loader so it stays during reloads) — `Route.FixedDepositSearchResult.search` is no longer read.
- Payment web view key `fd_purchase` → `completeFdWebView` pops `PurchaseFixedDeposit` inclusive (JN). Before, Velvet just popped the web view.
- Velvet filter overlay kept (its toggle row is still commented out, as before); `InvestmentFilter` mapped fd ↔ `utils.fundfiltersystem` in the screen.
- Projected-returns card computes with JN `calculateMaturity` in Velvet money format; the VM's `maturityAmount`/`totalInterest`/`maturityDate` strings are unused (Velvet UI has no date row).
- check-port: 5 `MaterialTheme.typography.title*` hits in `FDDetailsScreen.kt` are Velvet's original sheet/header code, kept as-is (user: keep Velvet UI).
- `FDCard.kt` composable `FDDetailCard` is unused (as before); `FDTenureRow` from it is used by the rates table.

## Goals (2026-11-05)

User scope: JN `features/goals` data/domain/APIs as-is, Velvet UI (new Velvet-styled UI only where Velvet had none),
proper feature, delete all old goal code and re-point its users.

- Ported 1:1 (package/imports only): `data/remote/model/{calculate,config,goalmapping,usergoal}`, `data/mapper/{GoalMapper,GoalRequestBodies}`,
  `data/repository/GoalsRepositoryImpl`, `domain/models/{GoalCalculationDomain,GoalConfigDomain,GoalDomain,GoalOption,GoalRequests}`,
  `domain/repository/GoalsRepository`, all 4 VMs (`ui/viewmodels`, JN file names incl. `MapSchemeViewModal.kt`), `features/bottomNavigation/domain/models/GoalsSummaryDomain`.
  Only change: `GoalOption.goalTypeColor` uses Velvet's old per-type goal colours (bgColor1/Secondary/bgColor4/homeGoalColor/Primary).
- NOT ported: JN `goals/domain/models/PortfolioDomain.kt` (dead duplicate of portfolio models) and `goals/utils/GoalCalculator.kt`
  (unused in JN; differs from Velvet's — Velvet's copy moved to `features/fireonboarding/utils`).
- `UserGoal` (shared DTO) regained `holdings: List<UserGoalHoldingDto>` → now identical to JN. `/user/` goals map with JN's
  `toDomain().toSummary()`; the old "map v2 ids onto Velvet's 4 types" mapper is gone.
- Screens (`features/goals/ui/compose`, JN names): `YourGoalsScreen` (Velvet list + header "+" + JN total-progress card, empty
  state via `EmptyFundScreen`, footer Invest Now / Add Goal), `FinancialGoalScreen` (Velvet Add Goal form: `SelectionDropDown`,
  `OnBoardingTextField`/`MoneyTextField`, `YearPicker`; live server projection), `ProjectedImpactScreen` (Velvet Goal Projection
  header/menu/card/Map Scheme pill + JN goal-type label, server progress %, "Mapped Funds" list, delete loader),
  `MapSchemesScreen` (Velvet screen/sheet + JN per-fund remove with loader, "N Mapped", Confirm loader). Shared:
  `GoalProjectionCard` (Velvet 2×2 card, used by add-goal preview + projection), `GoalCard` (= Velvet `GoalEntryCard`, moved
  here; JN icon mapping + server progress), `GoalTypeIcons` (`ic_home`, `ic_piggy_bank`, copied `ins_car_insurance`).
- Behaviour notes vs Velvet's old goals: no Retirement goal type (v2 has none; old id 3 = Buy a Home now); projection is the
  server's (old client math + "feasibility" removed); "Remove Mapping" (unmapped all via `/user-goal/map-remove`) replaced by
  per-fund remove; vehicle goals get a "Vehicle Type" picker when config lists >1 subtype (JN VM supports it, JN UI never shows it).
- Nav (`shared/Navigation/GoalsNavigation.kt`, `goalsDestinations`): `Route.GoalsScreen`, `Route.SingleGoalAdd` (was the typo
  `SingleGoadAdd`; MF search "Create Goal" + home re-pointed), `Route.GoalProjectionFlow(id)`, new `Route.MapSchemes(id)`.
  Old nested `GoalProjectionNavigation` + `Route.GoalProjectionImpact/GoalMapScreen` removed. Invest Now → `MutualFundSearchResult()` (JN).
  Only deviation from JN nav: the projection destination re-loads the goal when returned to (rememberSaveable flag), so
  mapping changes show without a restart.
- FIRE onboarding (unwired) still uses the OLD goal shape inside its own onboarding submit, so its models moved into the
  feature instead of being deleted: `domain/GoalTypes.kt`, `domain/models/goals/GoalRequest.kt`, `presentation/goals/uimodels/GoalOption.kt`
  → `features/fireonboarding/presentation/onboarding/models/`; `utils/GoalUtils/*` → `features/fireonboarding/utils/`.
  FIRE's `GoalSelectionDropDown` now delegates to the new generic `shared/compose/SelectionDropDown`.
- Deleted (user asked to remove unused goal code): `presentation/goals/**`, `domain/models/goals/GoalDomain.kt`,
  `domain/models/home/GoalsSummaryDomain.kt`, `data/mapper/GoalMapper.kt`, `data/remote/model/{goalbyid,goalmapping,addgoals}/**`,
  use cases `GetGoalById/MapGoal/UnMapGoal/UpdateGoals/GetGoalsSummary`, `UserFinance` add*/delete/getGoalById/map/unMap goal
  methods (+ repo impls), `UserAuth.updateGoals` + `GoalsUpdateDto` + `toGoalsDto`, `HomeRepository.getGoalsSummary` (mock),
  `shared/compose/GoalsCard.kt` (hardcoded dummy), old goal VM registrations.

## Auth / MPIN (2026-11-05)

- Ported 1:1 (package/imports only): `features/auth/{data/remote/model/MpinBodyDto, data/repository/MpinRepoImpl,
  domain/repository/MpinRepo, domain/usecase/{Update,Verify}MpinUseCase}`, `ChangePinViewModel`.
- `EnterPinViewModel`: JN minus biometrics (`biometricsAllowed`, prompt events dropped); added `showBack` +
  `OnBackClicked`/`NavigateBack` for the CHANGE_PIN gate. Subtitle "Enter your Velvet PIN".
- Screens restyled: `EnterPinScreen` = Velvet login look (`logo_app` 80dp, `OtpGrid(autoFocus)`), `ChangePinScreen` =
  `BackHeader` + `TitledAppTextField` (Velvet eye icons) + `ToggleSwitch` + `AppButton`.
- Velvet `utils.storage.AuthPrefs` (the bound one): `set/isMpinEnabled`, `set/isMpinSetup`; `clearAuth` removes them.
  The unbound JN-style `shared/domain/repository/AuthPrefs` + `AuthPrefsImpl` already had them — still unbound.
- Flags written by `HomeScreenViewModel` on every user load (JN). Wiring: `authModule`; `Route.EnterPin(purpose)`,
  `Route.ChangePin`; `AppNavigation` start = `EnterPin(APP_LOCK)` when setup && enabled && not
  `pinVerifiedThisSession`; flag reset in `BaseNavigation.navigateToLogin` (manual sign-out + token expiry).
- Profile: new "Security" `BarHeader` + `SecurityCard` (Change PIN, Velvet `lock_outlined_icon`) between Preferences and
  KYC → `navigateToChangePin` (EnterPin CHANGE_PIN gate when a PIN exists, else straight to ChangePin).
- Biometric (user-approved, same session): `features/core/biometric` expect + android/ios actuals 1:1;
  `androidx.biometric` 1.2.0-alpha05 (JN's version) in androidMain; `MainActivity` → `FragmentActivity`;
  `NSFaceIDUsageDescription` added to `iosApp/Info.plist` (JN lacks it — Face ID would crash without it).
  `EnterPinViewModel`/`EnterPinScreen` now = JN incl. auto prompt + "Use Biometrics" (APP_LOCK only).
  `AuthPrefs.set/isBiometricLoginEnabled` (default true, cleared on sign-out). `BiometricSettingsViewModel` 1:1,
  `BiometricSettingsScreen` restyled (InfoNoteCard look for the note, `dottedBorder`). `Route.BiometricLogin`.
  Drawables copied: `biometric`, `ic_face_id` (tinted). Velvet `tick_icon`/`info_icon` kept.
- Profile KYC row = JN `ProfileIntroScreen`: status from `onboardingPrompt` (Complete green / Under Review orange /
  Failed, Pending red); taps open the KYC onboarding graph only when `opensOnboarding`. `RowItem` got `subtitleColor`.
  Deleted (user-requested, trading account no longer used): `CheckKYCScreen.kt`, `CheckKYCViewModel.kt` (+ DI),
  `Route.CheckKYC`, unused `BottomNavigation.navigateToKYCScreen`. Trading-account graph (`TradingAccountNavigation`,
  `KYCCompleteScreen`, `CheckKYCAnimation`) is still declared but now unreachable from the profile.
- Deleted (user-approved): unwired mockups `presentation/settingscreens/compose/{CreateyourNewVelvetPINScreen,VerifyYourVelvetPINScreen}.kt`.
  `SettingViewModel.verifyOtpModel` may now be unused.

## Push notifications — FID-based (2026-11-05)

- KMPNotifier (`io.github.mirzemehdi:kmpnotifier`) **removed** (gradle api + iOS framework export, `VelvetApplication`,
  iOS `MainViewController`, `App.kt` listener). Replaced with JN's setup:
- Login sends the **Firebase Installation ID** as `fcm_token`: `utils/notification/PushNotificationManager` (common),
  `AndroidPushNotificationManager` (`FirebaseInstallations`, Koin in `PlatformModule.android`), `IosPushNotificationManager`
  + `FirebaseInstallationIdProvider` (Swift `FirebaseInstallationIdBridge`, bound in `MainViewController(installationIdProvider)`).
  `UserAuthenticationRepo` takes it as 4th ctor arg.
- Android: `utils/notification/VelvetFirebaseMessagingService` (JN's, Velvet channel/branding, `R.drawable.app_icon`) in the
  manifest + `firebase_messaging_installation_id_enabled`; `libs.firebase.messaging` (BoM) in androidMain.
- iOS: `iOSApp.swift` = JN's AppDelegate (APNs token → `Messaging.apnsToken`, foreground banners) — Velvet's debug
  prints dropped; Info.plist `FirebaseMessagingInstallationIdEnabled` = true, `FirebaseAppDelegateProxyEnabled` = false;
  `FirebaseInstallations` SPM product linked in `project.pbxproj`. `xcodebuild` simulator build succeeds.
- `utils/notification/PushNotificationHandler`: JN's `txn == "kyc"` → `AppEvent.KycSilentRefresh` (BottomNavigation →
  `homeViewModel.silentRefresh()`), plus Velvet's old behaviour: every push → `NotificationMarkEvent` (bell dot).
- Not checked on a device: needs the backend sending to installation IDs.

## Daily SIP (2026-11-07, rule 8 reversed by user)

User request: add JN's daily SIP across the MF flow, cart and bundles; bundle DTOs untouched.
- `PurchaseMode` = JN (DAILY/MONTHLY/ONE_TIME, `offered` removed). `FundTypeSelector` gained `DAILY_SIP`
  (`updateFundTypeToDailySIP()`, `purchaseMode` getter); `SelectedFundType` carries its `PurchaseMode`.
- Reverted to JN bodies (package/imports only differ): `LabelFilter` (DailyTen/MonthlyHundred),
  `CreateFilterData` (Min Investment group), `InvestmentModeFilter`, bundles `BundleMapper`/`BundleDomain`/
  `BundleTransactionRules`/`FundAvailability`/`BundleDetailsViewModel`, plans `SchemePlan`,
  `MutualFundSearchResultViewModel` (chips lead with Daily ₹10 / Monthly ₹100).
- `FundPurchaseViewModel` = JN + Velvet's `initialMode` (route `purchaseMode`, from `FundTypeSelector`;
  an amount-type chip overrides it as in JN). `FundPurchaseScreen`/`PurchaseSuccessScreen`: daily labels.
- InvestmentMethod: third card "Start a Daily SIP" (`ic_piggy_bank`) → `updateFundTypeToDailySIP()`.
- SearchOverlay: JN's Daily Micro-SIP / Monthly SIP quick actions back (Velvet look) → category/search
  `onFundsByAmountTypeClick` → `Route.MutualFundSearchResult(amountType)`.
- MF details cart sheet (Velvet-only): `CartBottomSheetState.purchaseMode` + `availableModes` +
  `minDailySipAmount` + `minAmount`; `MultiWaySwitch` (Monthly SIP / Daily SIP / Lump Sum) opens on
  `FundTypeSelector`, user can switch; modes from the fund (`sip_allowed`, `dailySipAllowed`, `purchase_allowed`).
  `addToCart` branches on the switch; SIP path → `toSipRequest` (now also daily when `purchaseMode == DAILY`)
  → `AddToCartSipUseCase` → `CartRepo.addToCartSip`. Daily hides the SIP-day field. Monthly button now needs a day.
  `MutualFundDetailsDomain.minDailySipAmount` (default 0) mapped from `sip_daily_amount_min` /
  `sip_frequency_data.daily|calendar_day_daily` — domain only, no DTO change.
- Cart opens on the SIP tab for daily too. Bundles: `PurchaseModeSwitch` = 3-way `MultiWaySwitch`
  (JN order Monthly/Daily/One-time); MIN. DAILY SIP, "/day". Portfolio Active SIP sub-tabs always shown (JN).
- New shared composable `shared/compose/MultiWaySwitch.kt`. check-port rule-8 check commented out.
- Not done: amount-type from a fund list isn't carried through MF details to FundPurchase (JN goes list →
  purchase directly); `FundTypeSelector` decides the mode there. Android + iOS compile; not run on device.

## Composable registry

One row per composable touched. Kind: `merged` (same-name, JN function → Velvet composable) or
`recreated` (JN-only, new Velvet-styled file).

| JN composable | Kind | Velvet location | What was ported / notes |
|---|---|---|---|
| `AppSearchBar` | merged | `shared/compose/AppSearchBar.kt` | `placeholder` + `focusRequester` params (defaults = old behaviour). Velvet's keyboard-hide kept. |
| `AppSearchBarButton` | recreated | `shared/compose/AppSearchBar.kt` | tappable fake search bar (opens the overlay); Velvet pill look |
| `SearchOverlay` | recreated | `features/search/ui/compose/SearchOverlay.kt` | Velvet cards/styles; Book FD + Create Goal quick actions only |
| `MutualFundListCard`, `YearRow`, `BundleCardSkeleton` | recreated | `features/mutualfund/ui/compose/MutualFundSearchResultRoot.kt` | theme-translated |
| `EmptyFundScreen` (JN title/subtitle/action) | merged (overload) | `shared/compose/EmptyStateScreen.kt` | Velvet illustration + Velvet text styles + AppButton; old image-only one in `presentation/portfolio/compose` untouched (Notifications) |
| `PortfolioHeader` "My Orders" | recreated | `features/portfolio/ui/screens/PortfolioScreen.kt` (`MyOrdersButton`) | Velvet outline pill in `BackHeader.rightContent` |
| `MyOrdersHeader` / `OrderDetailsHeader` | mapped | Velvet `BackHeader` | settlement info bubble kept as `rightContent` |
| `ExploreFundsScreen` | merged | `features/bottomNavigation/ui/compose/ExploreFundsScreen.kt` | Velvet explore look on JN state/events |
| `ErrorScreen` | merged | `shared/compose/ErrorScreen.kt` | `modifier` param |
| `LoaderScreen` | merged | `shared/compose/LoaderScreen.kt` | `modifier` param |
| `NextButtonFooter` | merged | `presentation/onboarding/compose/personaldetails/PersonalDetailScreen.kt` | `modifier` param (still lives in onboarding) |
| `ShadowCard`, `PaginationEffect`, `PaginationFooter` | merged | `shared/compose/` | identical function — nothing to port |
| `BackHeader` | merged | `shared/compose/BackHeader.kt` | only param names differ (`title/onBack` → `heading/onBackClick/showBack`); call sites adapted |
| `FundIcon` + `FundIconPlaceholder` | recreated | `shared/compose/FundIcon.kt` | JN API, Velvet look (2-letter navy tile, 2dp shadow). No `placeholder` slot yet |
| `PurchaseDayPickerSheet` | same role | `shared/compose/ListWheelPicker.kt` | reused Velvet's wheel picker, title "Select SIP Date" |
| `DebitDayField` | recreated | `features/bundles/presentation/compose/InvestmentSummaryCard.kt` | Velvet `SipDaySelector` look; move to `shared/compose` when plans is ported |
| `ConsentSection` | moved (Velvet) | `shared/compose/ConsentSection.kt` | from deleted old-KYC `KycContractScreen`; used by `CartCutOffPopup` |
| `ImagePickerBottomSheet` | moved (Velvet) | `shared/compose/ImagePickerBottomSheet.kt` | from deleted old-KYC `FileUploadScreen`; used by `UploadSignatureScreen`. check-port flags its M3 typography (original Velvet code) |
| `GoalCard` | merged | `features/goals/ui/compose/GoalCard.kt` (was Velvet `shared/compose/GoalEntryCard`) | JN icon-by-type + server `progressPercent`; Velvet look |
| `YourGoalsScreen` / `LocalBackHeader` | merged | `features/goals/ui/compose/YourGoalsScreen.kt` | Velvet `BackHeader` + `rightContent` "+"; JN progress card in Velvet card style |
| `FinancialGoalScreen`, `ProjectedImpactSection`, `ImpactCell` | merged | `features/goals/ui/compose/FinancialGoalScreen.kt` + `GoalProjectionCard.kt` | Velvet Add Goal form + Velvet projection card |
| `ProjectedImpactScreen` (+ `GoalAnalysisCard`, `ProgressSection`, `MapSipSection`) | merged | `features/goals/ui/compose/ProjectedImpactScreen.kt` | Velvet Goal Projection screen; JN's Map SIP card → Velvet's "Map Scheme" pill |
| `MapSchemesScreen`, `MappedHoldingCard`, `TotalCurrentValueCard` | merged | `features/goals/ui/compose/MapSchemesScreen.kt` | Velvet cards; delete = Velvet's (formerly commented) `ic_delete` |
| `DropDownSelector` (goal category) | same role | `shared/compose/SelectionDropDown.kt` | generic version of Velvet's goal dropdown look; FIRE's `GoalSelectionDropDown` delegates |
| `TitledDateField` (target year) | same role | Velvet `OnBoardingTextField(enabled = false)` + `YearPicker` | as Velvet's old Add Goal |
| `KycCard` (+ `KycRefreshButton`) | merged | `presentation/homescreen/compose/KYCCard.kt` | JN subtitle / accent / refresh-spinner / non-clickable notice on Velvet's `KYCCard` look; `OnboardingPromptCard` = JN's per-prompt copy |
| `OtpInputField` (focusRequester) | merged | `shared/compose/OtpGrid.kt` | `autoFocus` param (default false): focuses box 1 on entry and whenever the value is cleared |
| JN 3-way purchase-mode selector | recreated | `shared/compose/MultiWaySwitch.kt` | generic N-option `TwoWaySwitch` look; bundles + MF cart sheet |
| `clearFocusOnTap`, `genericDropShadow`, `dashedBorder` | mapped | `utils.clearFocusOnTap`, `shared.genericDropShadow`, `shared.dottedBorder` | |

## Utilities / infra merged

| JN symbol | Velvet location | Notes |
|---|---|---|
| `features.cart.CartInfo` | `utils.CartInfo` | identical; keeps Velvet's cart badge in sync |
| `core/database` (Room 3) | `shared/data/local/database/` (Room 2.8: `VelvetDatabase`, `RecentSearchDao`, android/ios builders, Koin in PlatformModule.*) | `velvet.db`, version 1 |
| `features.core.utils.LabelFilter`, `fundfiltersystem/*` | `features/core/utils/…` (mirrored, NOT merged) | drifted from Velvet's `utils/` copies; reconcile when mutualfund is fully ported |

## Superseded Velvet code (awaiting user decision to delete)

| Old Velvet files | Replaced by | Route re-pointed? |
|---|---|---|
| — (old bundle code deleted 2026-10-08, see session log) | | |
| old MF screens/VMs, old ExploreFund, ExistingFund top-up — **moved to `composeApp/later/`** 2026-11-05 | `features/mutualfund`, `features/bottomNavigation` | yes |
| old MF data/domain (`MutualFundRepo`, `fundusecases`, `domain/models/mutualfunds`), `presentation/mutualfund/compose/FundInvestementFilterScreen.kt` (FD uses it), `explorefunds/uimodel` (goals uses it) | feature copies | still used elsewhere — keep |
| old Velvet FD data/domain: `data/remote/repository/FixedDepositRepo`, `domain/repository/FixedDepositRepository`, `domain/usecases/fixeddepositusecases/*`, `domain/models/{fd,fixeddeposits}/*`, `data/remote/model/{getfds,fddetails,purchasefd}/*`, `data/remote/mapper/{FDDetailsMapper,FdListMapper}` + their `RepositoryModule`/`UseCaseModule` entries | `features/fd/{data,domain}` | yes — nothing outside these files uses them now; delete on approval (FD portfolio code is separate and still used) |
| old FIRE questionnaire: `features/fireonboarding/presentation/onboarding/{viewmodel/{PersonalDetailsScreen,GoalScreenOnboarding,OnBoardingConfirmation}ViewModel, compose/{personaldetails/**, ConfirmationScreen, goals/{AddGoalOnboardingScreen,GoalEntry(cards),GoalDropDown}}, models/{GoalRequest,GoalType,GoalOption,PersonalDetails}}`, `features/fireonboarding/utils/*`; use cases `OnBoardUserUseCase`, `UpdateFinance/Assets/InsuranceUseCase` (+ `UserAuth` methods, `/onboarding/complete-onboarding`) | FIRE step endpoints + `FireGoalsViewModel` | yes — unreferenced except DI; `GoalEntry.kt` `EntryPair`/`ClickableIcon` still used (made internal). Delete on approval |
| old Velvet KYC flow `presentation/kyc/**` — deleted by user 2026-11 | JN onboarding | destinations + VMs removed from `AppNavigation`/`ViewModelModule`; `Route.KYCScreen/KYCFormScreen/KYCImageUploadScreen/KYCContractScreen` still declared in `Route.kt`, unused. Check-KYC "Mutual Fund KYC" row is a no-op `TODO(jn-onboarding)` |

## Open questions

- DONE 2026-11-05: Biometric Login ported; old PIN mockups deleted.
- `EnterPinScreen` preview calls `rememberBiometricAuthenticator()`, which throws outside a FragmentActivity (as in JN) —
  the Android Studio preview may not render.

- DECIDED 2026-10-08: port JN **login** too (it stores the onboarding stage); add **play-services-location** + iOS CoreLocation.

- DONE 2026-11-05 (cart): (1) KYC gate = JN's (`GetUserDataUseCase` → `KYCPopup` → onboarding); (2) after purchase → JN My Orders; (3) CartPopup on MF details, beside "Purchase Fund".

- JN dropped the Insurance tab when it added Bundles; Velvet keeps it → 6 tabs. Confirm the nav bar isn't too crowded on small phones.
- Bundles port not yet verified on device; iOS not compiled (no expect/actual changes).
- Home FIRE % now comes from the FIRE report endpoint (extra call). Confirm with backend that this matches the old
  `user_home_data.fire_percentage.emi_include`, or whether `/user/` should send it again. Same for net worth (now assets − loans).
- DONE 2026-11-05 (goals port): goals UI uses only `shared.domain.GoalType` (v2 ids 1–6). The old 4-type `GoalType` lives on
  only inside unwired FIRE onboarding (`features/fireonboarding/.../models/GoalType.kt`) — decide when FIRE is wired back
  whether its goals step should create v2 goals instead.
- Goals not run on a device; iOS not compiled this session (no expect/actual changes).

- MF flow (2026-11-05): (1) DONE — "View holdings" → My Orders. (2) DONE — portfolio top-up commented out.
  (3) DONE (cart port) — category cart icon opens the JN cart; details has "Add to Cart" + cart FAB again.
- MF flow, orders and portfolio not yet run on a device. Orders has no profile entry yet (JN profile → My Orders; profile not ported).

## Session log

- 2026-11-07 — Daily SIP added across MF flow, cart sheet (3-way switch), plans, bundles, search chips/overlay; rule 8 retired. Android + iOS compile, check-port clean.

- 2026-11-05 — FIRE onboarding wired as a standalone feature (nested NavHost driven by `fire_stages`, 5 step APIs with
  skip/data bodies, goals collected via goals-feature config/calculate and sent together); home shows JN onboarding-prompt
  KYC cards + "Complete your FIRE journey"; FIRE report wired, its edit screens on the new APIs. Android compiles;
  check-port clean. Not run on device. Open: assets URL assumed `/onboarding/assets`; `loan_type` codes beyond
  HOME_LOAN/CAR_LOAN guessed (`LoanTypes.apiCode`); Back on a step exits the flow.

- 2026-11-05 — Goals ported: `features/goals` (JN data/domain/VMs, Velvet-look screens), `GoalsNavigation.kt`, home/`/user/`
  goals on JN `GoalsSummaryDomain`; old goal code deleted, FIRE-only goal models moved into `features/fireonboarding`.
  Android compiles; check-port clean.

- 2026-11-05 — FD ported: JN data/domain/VMs under `features/fd` on new `getFDUrl` (JN's `FD_URL`), Velvet screens moved there and re-pointed;
  `FdNavigation.kt` + `fd_purchase` web-view return; Explore FD top picks on the feature. Old FD VMs/uimodels → `composeApp/later/`. Android compiles; check-port only flags original Velvet typography.

- 2026-11-05 — Cart ported (JN `features/cart` full, Velvet cart visuals, Cart + CartCheckoutOtp nav); MF details gets JN add-to-cart
  next to "Purchase Fund". Old `presentation/cart` → `composeApp/later/`. Android + iOS compile, check-port clean.

- 2026-11-05 — Orders ported + wired; portfolio ported (Velvet PortfolioScreen design, JN flows incl. redeem/OTP and cancel-SIP sheet). Old portfolio screens/VMs → `composeApp/later/`. Android + iOS compile, check-port clean.

- 2026-11-05 — plans UI typography re-done against Velvet's analogous elements (the slot-for-slot table swap had left everything 14sp SemiBold `subHeading`, 44sp hero). Sheet titles → `headlineSmall`; list titles → `labelSmall`; secondary/helper → `titlesStyle` (12sp for hints/errors); hero amount → `largeTextStyle`; key-value rows → `titlesStyle`/SemiBold; keypad → `subHeadingMedium`. Hindi halves of `PurchaseOtpSheet` strings removed. Compiles.
- 2026-11-05 — MF buy flow: mutualfund data/domain/UI = JN (Velvet look), search overlay + Room DB, explore tab on JN VM,
  bundles per-mode sync, plans wired (`MutualFundNavigation.kt`), only MONTHLY/ONE_TIME. Unused old screens → `composeApp/later/`.
  Android + iOS compile, check-port clean on touched features.

- 2026-11-05 — Login → `features/login` (JN data/domain, Velvet UI unchanged), verify-OTP DTO = live response; `BaseNavigation`
  rewritten after JN `Navigation.kt` (LoginGraph / OnboardingGraph / MainAppGraph); onboarding graph wired + its 12 VMs in Koin;
  logout-on-expiry moved to base. Android + iOS compile; check-port clean. Not run on device.

- 2026-11-05 — User data refactor (see section above) to the live `GET /user/` shape with FIRE data; all compile errors from the
  onboarding/plans ports fixed: `submitInvestorProfile` → JN `InvestorProfileSubmission` (+ `InvestorProfileResponseDto` ported),
  `AppButton(shape=)` → `style = AppButtonDefaults.style(shape=)` (4 sites), duplicate `shared/location/*` removed (onboarding now uses
  `utils/location`, the one with actuals + Koin binding), leftover JN `package` lines in onboarding/plans/shared moved to Velvet.
  Android + iOS compile.

- 2026-10-08 — Decisions: port JN login; add location. Navigation plan written; location layer ported (step 1 of 5).
- 2026-10-08 — FIRE refactor (onboarding + fire report → `features/fireonboarding`, MVI, unwired) done; JN onboarding data/domain ported; cart TODOs recorded.
- 2026-10-08 — Old bundle code deleted (user-approved): `presentation/bundle/**`, `BundleNavigation.kt`, old bundle routes, old bundle DTOs/mappers/domain/use cases, bundle methods on Velvet `MutualFundRepository`. Explore funds "Curated Bundles" now loads via `features.bundles` `GetAllBundlesUseCase` + `BundleCard` (as JN); goals' dead bundle load removed.
- 2026-10-08 — Rule 8 added (no ₹10/day / ₹100/month): `PurchaseMode.offered`, `fromName` coerces DAILY→MONTHLY, bundle pickers → `TwoWaySwitch`, amount-type filter group/chips removed. Checker flags violations.
- 2026-10-08 — Cart port started: inventory done; open decisions (KYC gate, post-purchase destination, CartPopup timing) asked to user.
- 2026-10-08 — Bundles ported (+ partial cart/mutualfund/plans/core). Compiles; check-port clean. Pattern learned: JN bilingual labels `"Invest/" + stringResource(Res.string.invest)` → just `"Invest"`. JN "Janta …" titles → Velvet wording ("Curated Bundles", "VELVET CURATED BUNDLES").
- 2026-10-08 — Skill created. Decisions: JN feature-first layout; API ported as-is; English only, no localization; UI restyled to Velvet (gold accent, Poppins, Velvet components); same-name composables merge function/keep Velvet visuals; JN-only composables recreated in Velvet style.

- 2026-11-05: `ExternalAppLauncher` (commonMain `utils/deeplink`) had no platform impls bound → WebViewScreen crashed with
  Koin `NoDefinitionFoundException`. Ported `AndroidExternalAppLauncher` / `IosExternalAppLauncher` to
  `{android,ios}Main/.../utils/deeplink/` and bound them in each `PlatformModule` next to `BrowserLauncher`.
  Lesson: when porting a common interface, also port its platform impls + Koin binding.

- 2026-11-05: Home net-worth card — `GET /user/` now sends `user_home_data` (net_worth, fire_number,
  total_expenses, fire_percentage {emi_include, emi_exclude}); home reads net worth + FIRE % from it
  (no local assets−loans calc, no FIRE-report call). Growth/CAGR chip + Investing Rate link in
  `WorthCard.kt` commented out (TODO: not sent by server); replaced by JN `PortfolioCard`'s FD / MF
  amount boxes (`PortfolioAmountCard`, Velvet styling) fed from `dashboard`.
