# UI porting rules

**Principle: JN contributes behaviour, Velvet owns every visual.** A ported screen must look like
it was built in Velvet: Velvet's gold accent, Poppins, Velvet's components and their styling.

## 1. Resolution order for every visual decision

For each visual element (button, text field, card, chip, header, colour, padding, corner, text
style) resolve in this order and stop at the first hit:

1. **Analogous Velvet element.** Find how Velvet already draws the same *kind* of element and copy
   its visual values. Look in `shared/compose/` first, then a Velvet screen of similar purpose in
   `presentation/`. You are matching *appearance* (colours, border, shape, padding, height,
   text style, shadow) — not its behaviour. Example: porting a JN text field → open Velvet's
   `LoginTextFields.kt`, `PhoneNumberTextField.kt`, `GenericDropDown.kt` and the text fields in
   `presentation/kyc/compose/KYCFormScreen.kt`, `presentation/fixeddeposits/compose/PurchaseFDScreen.kt`
   and reuse their border colour, focused colour, shape, text style and container.
2. **Velvet theme tokens** — the tables below.
3. **No Velvet equivalent exists** (typical for features Velvet never had): build it from Velvet
   tokens; when a genuinely new colour is needed, add a named token to Velvet's
   `shared/theme/AppColors.kt` under a `// <feature>` comment, harmonised to Velvet's palette
   (JN cyan/blue tints become gold/navy tints). Never inline new hex values in screens.

## 2. Composables with the same name in both apps — merge function, keep Velvet look

Same-named in both: `AppButton`, `InvertedAppButton`, `BackHeader`, `BarHeader`, `ErrorScreen`,
`LoaderScreen`, `FilterChip`, `AppSearchBar`, `ShadowCard`, `ToggleSwitch`, `TwoWaySwitch`,
`GenericTabSwitcher`, `ListWheelPicker`, `OnBoardingDateField`, `NavLineChart`,
`ContinueBackButtonFooter`, `PaginationEffect`, `PaginationFooter`, `UiStateContainer`,
`AppDialogList` (JN `AppDialogBox.kt` ↔ Velvet `shared/DropDown.kt`).
Different names, same role: JN `AppDatePicker` ↔ Velvet `DatePickerSelector`;
JN `OtpInputField` ↔ Velvet `OtpGrid`; JN `LoadingScreen`/`GenericLoader` ↔ Velvet `LoaderScreen`/`VelvetLoader`.

Procedure:
1. Diff JN's file against Velvet's. Classify every difference:
   - **Functional** → port: new parameters, callbacks, state handling, validation, enabled /
     loading / error logic, input filtering, keyboard options & IME actions, focus handling,
     accessibility semantics, bug fixes.
   - **Visual** → discard: colours, fonts, sizes, shapes, paddings, elevation/shadows, icons.
2. Apply the functional changes to **Velvet's** composable in place. New parameters get defaults
   that preserve current behaviour, so existing Velvet call sites compile and behave unchanged.
3. Call sites in ported screens use Velvet's composable. Adapt arguments; never call JN's.
4. Record it in the progress.md registry ("merged: <what was ported>"), so the next session
   doesn't re-diff it.

## 3. JN-only composables — recreate in Velvet styling

JN-only (as of migration start): `MoneyTextField`, `AppTextField`, `TitledAppTextField`,
`NextButtonFooter`, `AppBackButton`, `GenericPill`, `CheckBoxCard`, `AgreementCheckBoxCard`,
`YesNoRadioGroup`, `InfoNoteCard`, `DropDownSelector`, `WheelPicker`/`YearPicker`, `FundIcon`,
`TopAppBarWithBackButtonAndStepCount`, `InsuranceFeatureCard`, `InsurancePopularPlansCard`,
`OtpVerificationScreen` + `OtpController`, `Modifier.dashedBorder`.
(Velvet has a `NextButtonFooter` in `presentation/onboarding/compose/personaldetails/` — treat it
as same-name: merge, and consider moving it to `shared/compose/`.)

Procedure: create it in `shared/compose/<Name>.kt` (package `org.sharad.velvetinvestment.shared.compose`)
with JN's API and behaviour, styled via §1. Port it when the first screen needs it, not up front.
Add an `@Preview` wrapped in `VelvetTheme`. Record it in the registry.

## 4. Typography — map to the nearest Velvet style

Velvet's `Typography` (Poppins) only defines some Material slots. **`titleLarge`, `titleMedium`,
`bodyLarge`, `labelMedium` are NOT defined in Velvet** — they fall back to Material defaults with
the system font. Never map ported text to them.

Same slot names have **different sizes** in the two apps (JN `bodyMedium` = 14sp, Velvet
`bodyMedium` = 18sp). Never keep `MaterialTheme.typography.X` unchanged — always go through this table:

| JN style (Inter) | size / weight | → Velvet style (Poppins) | size / weight |
|---|---|---|---|
| `displayLarge` | 34 Bold | `displayMedium` | 28 Bold |
| `displayMedium` | 30 SemiBold | `displayMedium` | 28 Bold |
| `headlineLarge` | 36 Bold | `largeTextStyle` | 40 SemiBold |
| `headlineMedium` | 22 SemiBold | `headlineLarge` | 20 SemiBold |
| `headlineSmall` | 20 SemiBold | `headlineLarge` | 20 SemiBold |
| `titleLarge` | 16 SemiBold | `labelSmall` | 16 Medium |
| `titleMedium` | 14 SemiBold | `subHeading` | 14 SemiBold |
| `titleSmall` | 12 Normal | `titleSmall` | 12 Normal |
| `bodyLarge` | 16 Normal | `displayLarge` | 16 Normal |
| `bodyMedium` | 14 Normal | `bodySmall` | 14 Normal |
| `bodySmall` | 12 Normal | `displaySmall` | 12 Normal |
| `labelLarge` | 16 Bold | `buttonTextStyle` (in buttons) / `labelSmall` (elsewhere) | 16 Medium |
| `labelMedium` | 14 Medium | `subHeading` (labels, emphasis) / `bodySmall` (running text) | 14 SB / 14 N |
| `labelSmall` | 14 Normal | `bodySmall` | 14 Normal |
| `largeTextStyle`, `titlesStyle`, `subHeadingMedium`, `subHeading`, `tinyLabel`, `buttonTextStyle` | same | same name in `shared.theme` | identical sizes |

**The table is a fallback, not the first step** (§1 order still applies). A slot-for-slot swap makes
ported screens look off, because JN leans on `labelMedium`/`titleMedium` (→ 14sp SemiBold) for
almost everything. Match Velvet's own roles first:

| Role | Velvet style | Seen in |
|---|---|---|
| Screen / sheet / dialog title | `headlineSmall` (18 SB) | `RedemptionBottomSheet`, `CartPopup`, login `OtpScreen` |
| Section label above a field | `subHeadingMedium` (18 M) | `RedemptionBottomSheet`, `CartPopup` |
| List-row / card title (fund name) | `labelSmall` (16 M) | `SIPDetailsScreen` |
| Secondary / supporting text | `titlesStyle` (14 N) | everywhere |
| Hint, footnote, inline error | `titlesStyle.copy(fontSize = 12.sp)` | `CartPopup` min-amount error |
| Key–value row | `titlesStyle` / `titlesStyle` + SemiBold | `SIPDetailsScreen` |
| Chip / segmented-toggle label | `titlesStyle` (+ Medium) | `TwoWaySwitch`, `CartPopup` chips |
| Field value | `displayLarge` (16 N) | `CartPopup` fields |
| Hero amount | `largeTextStyle` (40 SB), ₹ in `headlineMedium` | — |

Rule behind the table, for anything new: nearest font size first; on a tie, nearest weight.
Velvet's top-level styles (`largeTextStyle`, `subHeading`, …) live in `shared/theme/Typography.kt`.

Inline text styling in JN (`fontFamily = InterFontFamily, fontSize = X.sp, …`): if the analogous
Velvet element (§1) has a style, use it; else switch the family to `Poppins` and keep the size.
Poppins runs wider than Inter — check long labels for overflow and wrap in previews.

**Every** `Text` in ported UI must get a Velvet style or `fontFamily = Poppins`; a bare `Text`
renders in the system font in Velvet.

## 5. Colours

Accent is always Velvet's gold `Secondary`. Velvet colours: `shared/theme/AppColors.kt` (package
`org.sharad.emify.core.ui.theme` — yes, the odd package is correct) and `MaterialTheme.colorScheme`.
Default mapping (still apply §1 first: copy the analogous Velvet element when one exists):

| JN token | JN value | → Velvet |
|---|---|---|
| `Primary`, `BorderFocused`, `SelectedTextColor` | #23336E, #00497E | `Primary` (#273E71) |
| `Secondary`, `SelectedBoxBorder`, `TextFieldBorder` (cyan accent) | #00AEEF | `Secondary` (#D2B077) |
| `SelectedBoxColor`, `InterestChip`, `PreviewBackground`, other cyan/blue tints | | gold tint of `Secondary` (match a Velvet selected chip/tab first) |
| `PrimaryContainer`, `SelectedTenureChipColor`, `FdIconBg`, `HighlightRowBg`, `BackgroundFill` | lavender tints | `Primary` tint, or Velvet's analogous card/chip fill |
| `appGreen`, `appRed` | | `appGreen`, `appRed` (same) |
| `LightGray`, `Border`, `BoxBorder` | #D9D9D9, #D0D1D5, #E1E3E4 | `lightGray` |
| `GreyText`, `GreyLock`, `Gray` | #767681 | `titleColor` (#5A5E60) |
| `GreyBox`, `GrayBackGround`, `GreyBoxDivider`, `SelectTenureCardColor` | | `backgroundGray` (#EDEDEE) |
| `ShadowColor` | #DEE2F6 @ 0.75 | `bgShadowColor` (identical) |
| `TextPrimary`, `TextBlack`, `TextOnSurface`, `PrimaryLanguageText` | #191C1D | `Color.Black` / `MaterialTheme.colorScheme.onSurface` (follow neighbours) |
| `Background`, `Surface`, `White` | | `Color.White` (Velvet screens use `containerColor = Color.White`) |
| feature tags (`TagPopularBg`, `TagBonus*`, `TagMaxReturn*`, …) | | Velvet analog; else new token per §1.3 |

## 6. Spacing and shapes

- `Spacing.dpX` → `X.dp` (Velvet uses literals; there's no Spacing object). If the analogous
  Velvet element uses different padding, use Velvet's.
- `LocalShapes.current.roundedDpX` → Velvet's `RoundedCornerShape(X.dp)` (Velvet's idiom; it uses
  8/10/12/15/20/24). JN `4` → `8`, JN `16` → `15`, `menuContainer` → `8`.

## 7. Screen structure

- Screen root: collects state + effects, owns navigation callbacks. Content composable: pure
  state + lambdas, previewable. `@Preview` wraps in `VelvetTheme { }`.
- `Scaffold(containerColor = Color.White)`, Velvet headers (`BackHeader`, `SidedBackHeader`,
  `BarHeader`), Velvet `UiStateContainer` for load/error.
- Keep JN's layout *structure* (sections, order, conditional content). Don't redesign layout
  unless Velvet's analogous screen clearly does it differently.
- Remove `PoweredByVelvet`, `JantaNiveshAndVelvetLogo` and similar JN branding.
