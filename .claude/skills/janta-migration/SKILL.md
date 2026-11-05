---
name: janta-migration
description: Port a feature, screen, composable or utility from the Janta Nivesh app (../Janta Nivesh) into Velvet. Use whenever the user asks to migrate, port, bring over, copy or sync anything from Janta Nivesh / "JN" / "janta" into Velvet, or to continue the migration. Holds the agreed architecture, UI-restyling and typography rules plus a cross-session progress log.
---

# Janta Nivesh → Velvet migration

Janta Nivesh (JN) and Velvet are sibling Kotlin Multiplatform + Compose apps from the same team.
Features built in JN are being migrated into Velvet across many sessions. This skill is the
single source of truth for **how** to port and **what has already been ported**.

| | Path |
|---|---|
| JN source root | `/Users/sharadsavy/StudioProjects/Janta Nivesh/shared/src/commonMain/kotlin/org/velvetinvesting/jantanivesh/app` (module `:shared`) |
| JN resources | `/Users/sharadsavy/StudioProjects/Janta Nivesh/shared/src/commonMain/composeResources` |
| Velvet source root | `composeApp/src/commonMain/kotlin/org/sharad/velvetinvestment` (module `:composeApp`) |
| Velvet resources | `composeApp/src/commonMain/composeResources` |

JN is **read-only** — never edit files in it.

## Agreed rules (decided by the user — do not re-litigate)

1. **Layout: JN's feature-first structure.** A ported feature lives at
   `org.sharad.velvetinvestment.features.<feature>/…`, mirroring JN's sub-paths exactly
   (`data/…`, `domain/…`, `ui/…` or `presentation/…` — whatever JN uses for that feature), so
   files can be diffed 1:1 against the source in later sessions. Keep JN's ViewModel style
   (Event/Effect channels, repositories injected directly) — that is the "working structure".
2. **API: port as-is.** Copy JN's DTOs, endpoints and `getUrl(...)`/`getFDUrl(...)` calls
   unchanged. Base URLs are configured separately by the user; don't reconcile v1 vs v2.
3. **Strings: permanent English only. Localization is not allowed.** Every
   `stringResource(Res.string.x)` becomes the English literal from JN's `values/strings.xml`.
   Never copy `values-*` folders, `LanguageRepository`, language-selection screens or locale code.
4. **UI is always restyled to Velvet.** JN contributes behaviour; Velvet owns every visual.
   Accent colour is Velvet's gold `Secondary`, never JN's cyan. Full procedure in
   [references/ui-porting.md](references/ui-porting.md) — read it before touching any composable.
5. **Composables with the same name in both apps:** port JN's *functional* changes into Velvet's
   existing composable, keep Velvet's visuals.
6. **JN-only composables:** create a new Velvet-styled version in `shared/compose/`.
7. **Features with no Velvet counterpart:** there is nothing to copy styling from, so build them on
   Velvet theme tokens and add any new named tokens to Velvet's theme files instead of hardcoding.
8. **No ₹10/day or ₹100/month options.** Velvet doesn't offer JN's daily SIP or micro-SIP
   starters. Wherever JN offers them — purchase-mode selectors, "From ₹10/day" copy, amount-type
   filter chips/tray ("Daily ₹10", "Monthly ₹100"), home micro-SIP cards, daily-SIP flows — offer
   only a normal SIP and a one-time (lumpsum) purchase instead. Use `PurchaseMode.offered`
   (`features/core/domain/models/PurchaseMode.kt`) as the single source of choosable modes and
   Velvet's `TwoWaySwitch` for the picker. Keep `PurchaseMode.DAILY` and daily branches that
   *read* server data (an existing daily SIP must still display), just never let a user pick it.
9. **Velvet owns infrastructure.** HttpClient, AuthPrefs, session/logout (`AppEventsController`),
   `NetworkResponse`/`safeRequest`, `UiState`, `SnackBarController` stay Velvet's. Ported code is
   re-pointed at them — see [references/structure-and-infra.md](references/structure-and-infra.md).

## Start of every session

1. Read [progress.md](progress.md) — what's ported, the composable registry, open questions.
2. If the user didn't name what to port, propose the next item from the progress table and confirm.
3. If they named a feature, confirm scope (whole feature vs specific screens) only when ambiguous.

## Porting procedure (per feature)

**1. Inventory.** List every file in `features/<x>/` in JN. Then collect its outside dependencies:
```bash
cd "/Users/sharadsavy/StudioProjects/Janta Nivesh/shared/src/commonMain/kotlin/org/velvetinvesting/jantanivesh/app"
grep -rhoE "import org\.velvetinvesting\.jantanivesh\.app\.[a-zA-Z.]+" features/<x> \
  | grep -v "features\.<x>\." | sort -u
grep -rhoE "Res\.(drawable|string|font)\.[a-z_0-9]+" features/<x> | sort -u
```
Classify each dependency as: Velvet already has it (map it) / another JN feature (port first or
ask) / JN core composable / JN util / platform `expect` / new gradle dependency. Write the list
into progress.md before writing code — it's the work plan and survives a context reset.

**2. Compare with Velvet's counterpart** (if any, see progress.md table): screens, routes, who
navigates in, use cases and repositories it relies on. Note what the ported version replaces.

**3. Data + domain.** Copy files into the mirrored path, rewrite `package` and imports using the
map in structure-and-infra.md. DTOs stay byte-for-byte apart from package/imports.

**4. Shared dependencies first.** Utils: merge missing functions into Velvet's same-named file.
Composables: follow ui-porting.md §3/§4 and record each in the progress.md registry.

**ViewModel shape (also for Velvet-only refactors):** MVI as JN writes it — one `XUiState` data
class (derived values as getters), a sealed `XEvent` handled by `fun handleEvent(event)`, a sealed
`XEffect` sent on a `Channel` for one-off actions (navigation, "saved"). Screens take
`(state, onEvent)`; the NavHost destination collects state + effects. No callback lambdas into the VM.

**5. UI.** Port every screen and ViewModel following ui-porting.md. Each screen keeps the
`Screen(viewModel)` → `ScreenContent(state, callbacks)` split with an `@Preview` of the content
wrapped in `VelvetTheme`.

**6. Wire up.**
- DI: a per-feature Koin module `features/<x>/di/<X>Module.kt` (repositories `single<Interface>`,
  view models `viewModel { }`), added to the list in `utils/di/PlatformModule.kt › initializeKoin`.
- Routes: add `@Serializable` entries to Velvet's `shared/Navigation/Route.kt` (`object Route { }`
  — not JN's sealed interface). Copy KDoc from JN routes.
- Nav graph: `composable<Route.X>` entries in `shared/Navigation/AppNavigation.kt` (or the
  matching sub-graph file). Translate JN's Effect handling into navController calls there.
- If a Velvet route already serves the same screen, re-point it at the ported screen and update
  every `navigate(...)` call site if arguments changed. **Do not delete** the superseded Velvet
  code — list it in progress.md under "Superseded Velvet code" and ask the user before removal.

**7. Verify.**
```bash
.claude/skills/janta-migration/scripts/check-port.sh composeApp/src/commonMain/kotlin/org/sharad/velvetinvestment/features/<x> [other touched files…]
./gradlew :composeApp:compileDebugKotlinAndroid -q
```
When you added or changed any `expect`/`actual`, also run
`./gradlew :composeApp:compileKotlinIosSimulatorArm64`. Fix everything; report any failure you
can't fix with the actual output. Offer to run the app (`/run`) for a visual check against Velvet
screens — previews are not proof the restyle is right.

**8. Record.** Update progress.md: feature status, composable registry, superseded code, open
questions, anything a future session must know. Commit only when the user asks.

## When to stop and ask

- A JN feature depends on another unported JN feature.
- A gradle dependency or version bump would be needed (JN is on newer Compose 1.11, lifecycle
  2.11-beta, kotlinx-datetime 0.8, Room **3**; Velvet is on Compose 1.10, lifecycle 2.9.6,
  kotlinx-datetime 0.7.1, Room 2.8). Prefer adapting code to Velvet's versions.
- Merging a JN functional change into a Velvet composable/util would change behaviour for existing
  Velvet callers (new params must default to the old behaviour; if that's impossible, ask).
- JN behaviour conflicts with Velvet infrastructure (auth/session, onboarding gates, PIN lock).
- Deleting or replacing working Velvet code.
