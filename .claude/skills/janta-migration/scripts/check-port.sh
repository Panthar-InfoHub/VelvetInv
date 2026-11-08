#!/usr/bin/env bash
# Flags Janta Nivesh leftovers in ported Velvet code.
# Usage: check-port.sh <file-or-dir> [more…]   (exit 1 if anything is found)
set -u
if [ $# -eq 0 ]; then
  echo "usage: $0 <file-or-dir> [more…]" >&2
  exit 2
fi

found=0
check() {
  local label=$1 pattern=$2
  local hits
  # Comment lines (KDoc, //) are skipped: explaining a removed JN option is fine.
  hits=$(grep -rnE --include='*.kt' "$pattern" "${@:3}" 2>/dev/null | grep -vE '^[^:]+:[0-9]+:[[:space:]]*(\*|/\*|//)')
  if [ -n "$hits" ]; then
    found=1
    echo "✗ $label"
    echo "$hits" | sed 's/^/    /'
  fi
}

check "JN package/import or generated Res"   'jantanivesh' "$@"
check "Localized string (English literals only)" 'stringResource\(|pluralStringResource\(|Res\.string\.' "$@"
check "JN theme/typography"                  'JantaNiveshTheme|rememberLessPayTypography|InterFontFamily|LocalShapes\.current' "$@"
check "JN Spacing object (use X.dp)"         '\bSpacing\.dp' "$@"
check "Velvet-undefined typography slot"     'typography\.(titleLarge|titleMedium|bodyLarge|labelMedium)\b' "$@"
check "JN cyan accent"                       '00AEEF|E5F7FD|00497E' "$@"
check "JN branding"                          'PoweredByVelvet|JantaNiveshAndVelvetLogo' "$@"
# Rule 8 (no daily / micro SIP) was reversed on 2026-11-07 — daily SIP is ported as JN has it.
# check "₹10/day or ₹100/month option (rule 8)" '10/day|₹10/day|u20B910|Daily ₹10|Monthly ₹100|u20B9100|AMOUNT_DAILY_10|AMOUNT_MONTHLY_100|DailyTen|MonthlyHundred|listOf\(PurchaseMode\.DAILY|PurchaseMode\.entries' "$@"
check "Localization code"                    'LanguageRepository|AppLanguage|LocalAppLanguageLocale' "$@"

if [ $found -eq 0 ]; then
  echo "✓ no Janta Nivesh leftovers"
fi
echo "Manual checks still needed: every Text has a Velvet style or Poppins; same-name composables use Velvet's; visuals match an analogous Velvet element."
exit $found
