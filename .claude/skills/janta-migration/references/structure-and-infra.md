# Structure, imports and infrastructure mapping

## Package rewrite

| JN | Velvet |
|---|---|
| `org.velvetinvesting.jantanivesh.app.features.<x>.…` | `org.sharad.velvetinvestment.features.<x>.…` (same sub-path) |
| `jantanivesh.shared.generated.resources.Res` / `.<name>` | `velvet.composeapp.generated.resources.Res` / `.<name>` |

## JN core → Velvet equivalents

Velvet's version wins. Rows marked **identical** were diffed (bodies match); only the import changes.

| JN (`…jantanivesh.app.core.` / `features.core.`) | Velvet (`org.sharad.velvetinvestment.`) | Notes |
|---|---|---|
| `core.networking.NetworkResponse`, `ErrorDomain`, `ErrorType` | `utils.networking.*` | identical; Velvet also has `NetworkError` + `toMessage()` |
| `core.networking.safeRequest` / `safeUnitRequest` / `parseServerError` | `utils.networking.*` | identical |
| `core.networking.onSuccess` / `onError` | `utils.networking.onSuccess` / `onError` | identical |
| `core.networking.ServerError` / `Error` | `data.remote.model.ServerError.*` | |
| `core.networking.getUrl` | `utils.networking.getUrl` | base URL is the user's concern — don't change |
| `core.networking.getFDUrl` | *missing* | add `getFDUrl` to Velvet's `utils/networking/BaseUrl.kt` beside `getUrl` when first needed |
| `core.networking.getHttpClient` | `utils.networking.getHttpClient` | never port JN's client |
| `core.utils.UiState`, `LoadingState` | `utils.UiState`, `utils.LoadingState` | identical |
| `core.utils.SnackBarController` | `utils.SnackBarController` | identical |
| `core.utils.TrimDoubleFuntion` | `utils.TrimDoubleFuntion` | identical |
| `core.utils.MoneyFuntions`, `RuppeesHelper` | `utils.MoneyFuntions`, `utils.RuppeesHelper` | small drift — diff; add missing fns to Velvet's file |
| `core.utils.DateTime` | `utils.DateTime` | large drift. JN-only: `Int.withSuffix`, `String.isoUtcToIsoDate`, `isoUtcToMonthDayYear`, `toReadableDate`, `formatDateParts`, `formatMillisDateParts`, `formatMillisToDisplayDate`, `formatMillisToIsoDate` → append to Velvet's file when needed |
| `core.utils.AppBackHandler` (expect) | `utils.AppBackHandler` (in `BackPressHandler.kt`) | same signature |
| `core.utils.IsAndroid` | `utils.isAndroid` | |
| `core.utils.BrowserLauncher`, `HtmlToText` | `utils.BrowserLauncher`, `utils.HtmlToText` | diff before use |
| `core.platform.PermissionManager`, `PdfDownloadManager` | `utils.PermissionManager`, `utils.PdfDownloadManager` | diff before use |
| `core.platform.SharedPreferences` | `utils.storage.SharedPreferences` | |
| `features.core.domain.repository.AuthPrefs` (interface) + `AuthPrefsImpl` | `utils.storage.AuthPrefs` (class) | Velvet's class is the one in Koin; add missing methods to it |
| `features.core.domain.GoalType` | `shared.domain.GoalType` | identical (v2 goal ids 1–6) |
| `features.core.data.remote.model.userdata.*` (`UserGoal`, …) | `shared.data.remote.model.userdata.*` | `UserGoal` identical since the goals port |
| `features.core.ui.modifierextensions.clearFocusOnTap` | `utils.clearFocusOnTap` | |
| `features.core.ui.modifierextensions.genericDropShadow` | `shared.genericDropShadow` | |
| `features.core.ui.modifierextensions.dashedBorder` | `shared.dottedBorder` | check the look matches the use |
| `features.core.ui.composables.UiStateContainer` | `shared.UiStateContainer` | |
| `core.theme.*` | `shared.theme.*` + `org.sharad.emify.core.ui.theme.*` (AppColors.kt) | see ui-porting.md |
| `core.navigation.Route.*` | `shared.Navigation.Route.*` | Velvet's is `object Route { }`, not a sealed interface; drop `: Route` |
| `core.di.*` modules | per-feature module, registered in `utils/di/PlatformModule.kt` | |

Anything not in this table: search Velvet by name and by behaviour before porting it. Port it
into the closest Velvet location (`utils/` for helpers, `shared/compose/` for UI) and add a row
here so the next session finds it.

## Do NOT port

- `core.localization.*`, `ChooseLanguage`, `LanguageSelectionSettings`, `values-*` resources
- `PoweredByVelvet`, `JantaNiveshAndVelvetLogo`, any JN branding asset
- JN's `JantaNiveshTheme`, typography, `Spacing`, `LocalShapes` (translate per ui-porting.md)
- JN's `HttpClient`, Koin bootstrap, `PlatformModule` actuals

## JN-only infrastructure (port only if a feature needs it — ask first)

`core.database` (**Room 3**; Velvet uses Room 2.8 — code is not drop-in), `core.location`,
`core.deeplink`, `core.FirebaseNotification` (PORTED 2026-11-05 → `utils/notification/`), `core.analytics.ExceptionReporter`,
`core.webview` (Velvet has its own `presentation/webview` — prefer that),
`features.core.biometric` (PORTED 2026-11-05 → `features/core/biometric`, same path).

## Resources

- Drawables: copy only those the ported files reference. 63 names exist in both apps — when a
  name collides, `cmp` the files; identical → reuse; different → keep Velvet's (visuals are
  Velvet's) unless it depicts a different thing, then copy JN's as `<name>_jn` and use that.
- Fonts: never copy; Velvet already has Poppins + Inter.
- Strings: inline English literals (look up `<string name="x">` in JN `values/strings.xml`).
  JN often writes bilingual labels as `"Invest/" + stringResource(Res.string.invest)` — the English
  half before the slash is the whole label: `"Invest"`. Replace "Janta …" branding in copy with Velvet wording.
  `stringResource(Res.string.x, a, b)` → Kotlin template with the `%1$s`/`%1$d` placeholders
  replaced. `pluralStringResource` → an explicit `if (n == 1) … else …`.

## Platform code

If a ported file uses an `expect`, check Velvet for an equivalent first. If you must port one,
port the `expect` plus both `actual`s (`composeApp/src/androidMain`, `composeApp/src/iosMain`)
and compile iOS too.
