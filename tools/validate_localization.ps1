<#
.SYNOPSIS
    Validates a thetormented localization language pack against the eng baseline.

.DESCRIPTION
    Checks (all against eng baseline unless noted):
      - 9 files exist and parse as UTF-8 JSON
      - CardStrings: key set equality (78 cards), EXTENDED_DESCRIPTION length per card,
        UPGRADE_DESCRIPTION presence per card
      - PowerStrings/RelicStrings/PotionStrings: DESCRIPTIONS array length per key
        (RelicStrings also requires FLAVOR)
      - OrbStrings: DESCRIPTION (singular key) array length
      - UIStrings: TEXT / EXTRA_TEXT array lengths
      - CharacterStrings: NAMES (2) / TEXT (3) lengths
      - Keywords: exactly 4 entries, IDs = bleed/sin/debt/restriction, non-empty fields
      - Keyword token coverage: every ${modID}:<Word> used in string VALUES must be
        covered by a (lowercased) NAMES entry; dynamic variable tokens
        !${modID}:<KEY>! must be on the whitelist
      - *CardName prose tokens should match a card NAME in the target CardStrings
      - No U+FFFD replacement characters (mojibake)
      - Heuristic scan for leftover English words (warnings only)

.EXAMPLE
    powershell -ExecutionPolicy Bypass -File tools\validate_localization.ps1 -Lang fin

.PARAMETER Lang
    Target language code (directory name under localization/), default "fin".

.PARAMETER BaseDir
    Override the localization root directory. Default: resolved from script location.
#>
param(
    [string]$Lang = "fin",
    [string]$BaseDir = ""
)

$ErrorActionPreference = "Stop"

$repoRoot = (Resolve-Path (Join-Path $PSScriptRoot "..")).Path
if (-not $BaseDir) {
    $BaseDir = Join-Path $repoRoot "src\main\resources\thetormented\localization"
}
$engDir = Join-Path $BaseDir "eng"
$langDir = Join-Path $BaseDir $Lang

$files = @(
    "CardStrings.json", "PowerStrings.json", "RelicStrings.json", "PotionStrings.json",
    "UIStrings.json", "CharacterStrings.json", "OrbStrings.json", "Keywords.json",
    "EventStrings.json"
)
$script:errors = @()
$script:warnings = @()

function Add-Err([string]$m) { $script:errors += $m; Write-Host "  [ERROR] $m" -ForegroundColor Red }
function Add-Warn([string]$m) { $script:warnings += $m; Write-Host "  [WARN]  $m" -ForegroundColor Yellow }

function Get-PropNames($obj) {
    if ($obj -is [System.Management.Automation.PSCustomObject]) { return @($obj.PSObject.Properties.Name) }
    return @()
}
function ArrLen($a) { if ($null -eq $a) { return 0 } else { return @($a).Count } }
function Get-StringValues($obj) {
    $out = @()
    if ($obj -is [System.Management.Automation.PSCustomObject]) {
        foreach ($p in $obj.PSObject.Properties) { $out += Get-StringValues $p.Value }
    } elseif ($obj -is [System.Collections.IEnumerable] -and $obj -isnot [string]) {
        foreach ($i in $obj) { $out += Get-StringValues $i }
    } elseif ($obj -is [string]) { $out += $obj }
    return $out
}

Write-Host "== Validating localization: $Lang (base: $BaseDir) =="

if (-not (Test-Path $engDir)) { Add-Err "eng baseline dir missing: $engDir"; exit 1 }
if (-not (Test-Path $langDir)) { Add-Err "language dir missing: $langDir"; exit 1 }

$engData = @{}
$langData = @{}
foreach ($f in $files) {
    $ep = Join-Path $engDir $f
    $lp = Join-Path $langDir $f
    if (-not (Test-Path $ep)) { Add-Err "eng missing file: $f"; continue }
    if (-not (Test-Path $lp)) { Add-Err "$Lang missing file: $f"; continue }
    try { $engData[$f] = Get-Content $ep -Raw -Encoding UTF8 | ConvertFrom-Json }
    catch { Add-Err "eng JSON parse fail: $f : $($_.Exception.Message)"; continue }
    try { $langData[$f] = Get-Content $lp -Raw -Encoding UTF8 | ConvertFrom-Json }
    catch { Add-Err "$Lang JSON parse fail: $f : $($_.Exception.Message)"; continue }
}

# --- CardStrings ---
if ($engData.ContainsKey("CardStrings.json") -and $langData.ContainsKey("CardStrings.json")) {
    $e = $engData["CardStrings.json"]; $l = $langData["CardStrings.json"]
    $ek = @(Get-PropNames $e); $lk = @(Get-PropNames $l)
    if ($ek.Count -ne $lk.Count) { Add-Err "CardStrings: key count eng=$($ek.Count) $Lang=$($lk.Count)" }
    foreach ($k in $ek) { if ($k -notin $lk) { Add-Err "CardStrings: missing key '$k' in $Lang" } }
    foreach ($k in $lk) { if ($k -notin $ek) { Add-Err "CardStrings: extra key '$k' in $Lang" } }
    foreach ($k in $ek) {
        if ($k -notin $lk) { continue }
        if ((ArrLen $e.$k.NAME) -eq 0) { Add-Err "CardStrings ${k}: eng NAME missing (baseline broken)" }
        if ((ArrLen $l.$k.NAME) -eq 0) { Add-Err "CardStrings ${k}: NAME missing in $Lang" }
        $eExt = ArrLen $e.$k.EXTENDED_DESCRIPTION; $lExt = ArrLen $l.$k.EXTENDED_DESCRIPTION
        if ($eExt -ne $lExt) { Add-Err "CardStrings ${k}: EXTENDED_DESCRIPTION len eng=$eExt $Lang=$lExt" }
        $eUp = (ArrLen $e.$k.UPGRADE_DESCRIPTION) -gt 0; $lUp = (ArrLen $l.$k.UPGRADE_DESCRIPTION) -gt 0
        if ($eUp -ne $lUp) { Add-Err "CardStrings ${k}: UPGRADE_DESCRIPTION presence eng=$eUp $Lang=$lUp" }
    }
    Write-Host "  [OK] CardStrings: $($ek.Count) keys, EXT/UPGRADE aligned"
}

# --- PowerStrings / RelicStrings / PotionStrings ---
foreach ($f in @("PowerStrings.json", "RelicStrings.json", "PotionStrings.json")) {
    if (-not $engData.ContainsKey($f)) { continue }
    $e = $engData[$f]; $l = $langData[$f]
    $ek = @(Get-PropNames $e); $lk = @(Get-PropNames $l)
    if ($ek.Count -ne $lk.Count) { Add-Err "${f}: key count eng=$($ek.Count) $Lang=$($lk.Count)" }
    foreach ($k in $ek) { if ($k -notin $lk) { Add-Err "${f}: missing key '$k' in $Lang"; continue } }
    foreach ($k in $ek) {
        if ($k -notin $lk) { continue }
        $ed = ArrLen $e.$k.DESCRIPTIONS; $ld = ArrLen $l.$k.DESCRIPTIONS
        if ($ed -ne $ld) { Add-Err "${f} ${k}: DESCRIPTIONS len eng=$ed $Lang=$ld" }
        if ($e.$k.DESCRIPTIONS -is [string]) { Add-Err "${f} ${k}: eng DESCRIPTIONS is a STRING (must be array)" }
        if ($l.$k.DESCRIPTIONS -is [string]) { Add-Err "${f} ${k}: DESCRIPTIONS is a STRING in $Lang (must be array)" }
    }
    if ($f -eq "RelicStrings.json") {
        foreach ($k in $ek) {
            if (($k -in $lk) -and ((ArrLen $e.$k.FLAVOR) -eq 0 -or (ArrLen $l.$k.FLAVOR) -eq 0)) {
                Add-Err "RelicStrings ${k}: FLAVOR missing"
            }
        }
    }
    Write-Host "  [OK] $f : $($ek.Count) keys, DESCRIPTIONS aligned"
}

# --- OrbStrings ---
if ($engData.ContainsKey("OrbStrings.json") -and $langData.ContainsKey("OrbStrings.json")) {
    $e = $engData["OrbStrings.json"]; $l = $langData["OrbStrings.json"]
    $ek = @(Get-PropNames $e)
    foreach ($k in $ek) {
        if ($k -notin @(Get-PropNames $l)) { Add-Err "OrbStrings: missing key '$k' in $Lang"; continue }
        if ((ArrLen $e.$k.DESCRIPTION) -ne (ArrLen $l.$k.DESCRIPTION)) {
            Add-Err "OrbStrings ${k}: DESCRIPTION len mismatch"
        }
    }
    Write-Host "  [OK] OrbStrings: DESCRIPTION aligned"
}

# --- UIStrings ---
if ($engData.ContainsKey("UIStrings.json") -and $langData.ContainsKey("UIStrings.json")) {
    $e = $engData["UIStrings.json"]; $l = $langData["UIStrings.json"]
    $ek = @(Get-PropNames $e); $lk = @(Get-PropNames $l)
    if ($ek.Count -ne $lk.Count) { Add-Err "UIStrings: key count eng=$($ek.Count) $Lang=$($lk.Count)" }
    foreach ($k in $ek) {
        if ($k -notin $lk) { Add-Err "UIStrings: missing key '$k' in $Lang"; continue }
        if ((ArrLen $e.$k.TEXT) -ne (ArrLen $l.$k.TEXT)) { Add-Err "UIStrings ${k}: TEXT len mismatch" }
        if ((ArrLen $e.$k.EXTRA_TEXT) -ne (ArrLen $l.$k.EXTRA_TEXT)) { Add-Err "UIStrings ${k}: EXTRA_TEXT len mismatch" }
    }
    Write-Host "  [OK] UIStrings: TEXT/EXTRA_TEXT aligned"
}

# --- CharacterStrings ---
if ($engData.ContainsKey("CharacterStrings.json") -and $langData.ContainsKey("CharacterStrings.json")) {
    $e = $engData["CharacterStrings.json"]; $l = $langData["CharacterStrings.json"]
    $ek = @(Get-PropNames $e)
    foreach ($k in $ek) {
        if ($k -notin @(Get-PropNames $l)) { Add-Err "CharacterStrings: missing key '$k' in $Lang"; continue }
        if ((ArrLen $e.$k.NAMES) -ne (ArrLen $l.$k.NAMES)) { Add-Err "CharacterStrings ${k}: NAMES len mismatch" }
        if ((ArrLen $e.$k.TEXT) -ne (ArrLen $l.$k.TEXT)) { Add-Err "CharacterStrings ${k}: TEXT len mismatch" }
    }
    Write-Host "  [OK] CharacterStrings: NAMES/TEXT aligned"
}

# --- Keywords ---
$kwNames = @()
if ($engData.ContainsKey("Keywords.json") -and $langData.ContainsKey("Keywords.json")) {
    $e = $engData["Keywords.json"]; $l = @($langData["Keywords.json"])
    $expectedIds = @("bleed", "sin", "debt", "restriction")
    if ($l.Count -ne 4) { Add-Err "Keywords: expected 4 entries, got $($l.Count)" }
    $gotIds = @()
    foreach ($k in $l) {
        $gotIds += [string]$k.ID
        if (($k.ID -notin $expectedIds)) { Add-Err "Keywords: unexpected ID '$($k.ID)'" }
        if ((ArrLen $k.PROPER_NAME) -eq 0) { Add-Err "Keywords $($k.ID): PROPER_NAME empty" }
        if ((ArrLen $k.NAMES) -eq 0) { Add-Err "Keywords $($k.ID): NAMES empty" }
        if ((ArrLen $k.DESCRIPTION) -eq 0) { Add-Err "Keywords $($k.ID): DESCRIPTION empty" }
        foreach ($n in @($k.NAMES)) { $kwNames += ([string]$n).ToLower() }
    }
    foreach ($id in $expectedIds) { if ($id -notin $gotIds) { Add-Err "Keywords: missing ID '$id'" } }
    # --- debt keyword MUST carry the %%SIN_PER_DEBT%% marker (bound at runtime to SinPower.SIN_PER_DEBT) ---
    foreach ($k in $l) {
        if ($k.ID -eq "debt" -and ([string]$k.DESCRIPTION -notmatch '%%SIN_PER_DEBT%%')) {
            Add-Err "Keywords debt: DESCRIPTION missing %%SIN_PER_DEBT%% marker"
        }
    }
    Write-Host "  [OK] Keywords: 4 entries, NAMES collected ($($kwNames.Count) forms)"
}

# --- token coverage ---
$dynVars = @("SIN", "BLEED", "BLOCK_THRESHOLD", "TOTAL_BLOCK", "TOTAL_DAMAGE", "TOTAL_DRAW", "HITS", "TOTAL_ENERGY", "BLOCK", "PLAYS")
# Char class: Latin letters incl. Vietnamese precomposed (U+1E00-\u1EFF), combining marks, Greek, Cyrillic, Thai, Hangul, kana, CJK.
# Keyword/star tokens may be multi-word (space-separated); the check loops truncate at word boundaries if the full form is not covered.
$kwClass = 'A-Za-z\u00C0-\u024F\u0300-\u036F\u0370-\u03FF\u0400-\u04FF\u0E00-\u0E7F\u1100-\u11FF\u3040-\u30FF\u4E00-\u9FFF\uAC00-\uD7AF\u1E00-\u1EFF'
$kwTokenRe = [regex]("(?<![!$kwClass])\`$\{modID\}:([$kwClass]+(?:\u0020[$kwClass]+)*)")
$dynRe = [regex]'!\$\{modID\}:([A-Za-z_]+)!'
$starRe = [regex]("\*([$kwClass][$kwClass-]*(?:\u0020[$kwClass][$kwClass-]*)*)")
$cardNames = @()
if ($langData.ContainsKey("CardStrings.json")) {
    $l = $langData["CardStrings.json"]
    foreach ($k in @(Get-PropNames $l)) { $cardNames += [string]$l.$k.NAME }
}
$usedKw = @{}
foreach ($f in $files) {
    if (-not $langData.ContainsKey($f)) { continue }
    foreach ($v in (Get-StringValues $langData[$f])) {
        foreach ($m in $kwTokenRe.Matches($v)) { $usedKw[$m.Groups[1].Value.ToLower()] = $true }
        foreach ($m in $dynRe.Matches($v)) {
            $key = $m.Groups[1].Value
            if ($key -notin $dynVars) { Add-Err "Dynamic variable token !`${modID}:$key! not in whitelist" }
        }
        foreach ($m in $starRe.Matches($v)) {
            $w = $m.Groups[1].Value
            $cand = $w
            while ($cand -notin $cardNames) {
                $i = $cand.LastIndexOf(' ')
                if ($i -le 0) { break }
                $cand = $cand.Substring(0, $i)
            }
            if ($cand -notin $cardNames) { Add-Warn "*$w used in $f but no card NAME matches (case-sensitive)" }
        }
    }
}
foreach ($k in @($usedKw.Keys | Sort-Object)) {
    $cand = $k
    while ($cand -notin $kwNames) {
        $i = $cand.LastIndexOf(' ')
        if ($i -le 0) { break }
        $cand = $cand.Substring(0, $i)
    }
    if ($cand -notin $kwNames) { Add-Err "Keyword token '${modID}:$k' not covered by Keywords NAMES" }
}
Write-Host "  [OK] token coverage: $($usedKw.Count) keyword forms checked, dynamic vars whitelisted"

# --- mojibake ---
foreach ($f in $files) {
    $lp = Join-Path $langDir $f
    if (Test-Path $lp) {
        $c = Get-Content $lp -Raw -Encoding UTF8
        if ($c.Contains([string][char]0xFFFD)) { Add-Err "${f}: contains U+FFFD replacement char (mojibake)" }
    }
}

# --- leftover English (heuristic, warnings only) ---
# Per-language native words that would false-positive the scan
$nativeWords = @{
    "dut" = @("hand", "Max")
    "fra" = @("combat", "max")
    "ind" = @("Status")
    "spa" = @("Vulnerable")
}
$engWords = "Deal|Gain|Apply|Draw|Exhaust|Block|Bleed|Debt|Sins|damage|enemy|turn|hand|stack|Remove|Retain|Innate|Ethereal|Strength|Dexterity|Vulnerable|Weak|Frail|Attack|Status|Random|Card|Upgrade|Max|combat"
foreach ($f in @("CardStrings.json", "PowerStrings.json", "RelicStrings.json", "PotionStrings.json", "EventStrings.json")) {
    if (-not $langData.ContainsKey($f)) { continue }
    foreach ($v in (Get-StringValues $langData[$f])) {
        foreach ($m in [regex]::Matches($v, "\b($engWords)\b")) {
            $w = $m.Groups[1].Value
            if ($nativeWords.ContainsKey($Lang) -and $w -in $nativeWords[$Lang]) { continue }
            Add-Warn "${f}: leftover English word '$w'"
        }
    }
}

# --- summary ---
Write-Host ""
if ($errors.Count -eq 0 -and $warnings.Count -eq 0) {
    Write-Host "RESULT: PASS (no errors, no warnings)" -ForegroundColor Green
    exit 0
} elseif ($errors.Count -eq 0) {
    Write-Host "RESULT: PASS with $($warnings.Count) warning(s)" -ForegroundColor Yellow
    exit 0
} else {
    Write-Host "RESULT: FAIL - $($errors.Count) error(s), $($warnings.Count) warning(s)" -ForegroundColor Red
    exit 1
}
