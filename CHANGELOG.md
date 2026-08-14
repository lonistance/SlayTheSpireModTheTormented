# Changelog

## v1.0.0 (2026-08-14) — Official Release: From Balance Tweaks to a Project-Wide Rework

### Preface — How This Release Came to Be

This version began as a routine balance pass: a handful of number tweaks and a few "quick" bug fixes.
But every fix uncovered another latent bug, and every retune surfaced new design ideas — until the
small patch had quietly grown into a rewrite of nearly the entire project. The version therefore
jumps straight from 0.0.x to **1.0.0** instead of masquerading as the playable prototype.

Against the previous state of `main` (0.0.0), a full comparison shows:

- **60 Java paths changed** — 59 rewritten + 1 new (`patches/BetaArtUnlockPatch.java`)
- **41 cards** touched: 13 numbers-only, 23 mechanic/text changes, 4 renamed, 4 rarity moves
- **All 7 powers** changed, **4 actions** reworked or refactored, **3 relics** fixed
- Localization: **6 → 24 official languages**
- Art: every card image redone/upscaled and pngquant-compressed (jar 48 MB → 16 MB), plus
  **160 new playtester-art (cards_test) files**
- New framework features: upgrade-text engine fix, playtester-art hot-swap, beta-art toggle unlock

### 1. Card Renames (4, class + ID + localization keys synced)

| Old | New |
|---|---|
| Wandering | Odyssey |
| JudgmentForm | ExecutionForm |
| HeavySmite | HeavySmash |
| Accumulate | Unleash |

### 2. Rarity Moves (4)

| Card | Old → New |
|---|---|
| Grief | UNCOMMON → **RARE** |
| BloodFeud | RARE → **UNCOMMON** |
| PleaOfInnocence | COMMON → **UNCOMMON** |
| Mercy | UNCOMMON → **COMMON** |

### 3. Card Rebalance — Numbers Only (13)

| Card | Old → New |
|---|---|
| Agony | Damage 7 (+3) → **7 (+1) = 8**; Weak/Vulnerable upgrade 1 → **2** |
| CruelTorture | Damage 15 (+5); Sin gained 5 → **7** (upgrade fixed to match text) |
| QuenchedBlade | Damage 7 (+3) → **7 (+1) = 8**; upgrades 2 → **4 cards** |
| RelentlessEntanglement | Damage 4 (+1) → **5 (+2) = 7** |
| BloodGuard | Block 6 → **5**; Bleed applied 6 → **5** (both +2 upgraded) |
| Firm | Block 9 → **10** (+3 = 13) |
| FaceDanger | Damage 15 → **16**; upgrade bonus +5 → **+4** (still 20) |
| MassiveBleeding | Damage 6 → **4** |
| TemperedSword | Damage 8 → **9**; per-Status bonus +2 → **+3** (+4 upgraded) |
| RelentlessBleed | Bleed 4 (+2) → **3 (+1)** |
| BloodSea | Bleed applied 5 → **3** |
| Parry | Block 5 → **6** (+3 = 9) |
| PleaOfInnocence | Block 5 → **7** (+2 = 9); rarity COMMON → UNCOMMON |

### 4. Card Changes — Mechanics & Text (23)

- **AtonementStrike**: after drawing, also **lose 4 Sin**.
- **SacredLand**: Block 6 (+3→+2)=8 and Draw 2 (+1) unchanged; new — **if you have any Debt, lose 7 Sin**.
- **Implication**: full rework — old hit the target, then re-hit every Bleeding enemy; new —
  **Exhaust 1 card from the hand**, **deal 8 (+3=11) damage to ALL enemies**, and **draw 1 card
  for each Bleeding enemy hit**; target ENEMY → ALL_ENEMY.
- **HeavyPast**: rework — 15 (+5) single hit → **6 (+1=7) × 2 hits**; Misery into draw + discard
  unchanged; **new: Draw 1 (2 upgraded)**.
- **ChaosDirty**: damage 5 → 6 (upgrade now correctly +1 and Debt bonus 3 → 4); the **damage
  preview now matches real resolution** (includes the Debt→Sin conversion of this play unless
  Artifact blocks it).
- **VoidCall**: **no longer Innate**; upgrade no longer draws 2 — it now **removes Exhaust**
  (chosen card becomes Ethereal).
- **Bloodbath**: the old magic upgrade (1→2) never applied; upgrade now **reduces cost 2 → 1**.
- **Bloodstain**: 4 Bleed; base card **Exhausts**, upgrade **removes Exhaust** (was +1 Bleed).
- **Boiling**: Bleed 4 → **5** (+1=6); applies to **a random enemy** per card exhausted (was ALL
  enemies); **no longer exhausts itself** (fixed a real self-Bleed bug).
- **BrokenArmor**: Block 16 (+0) → **15 (+5=20)**; self-application **Weak 2 (upg 1) → Frail 1**.
- **FormPact**: generated Misery now goes to the **discard pile** (was the hand).
- **Memorial**: full rework — old block-memory version (reverted before shipping) → **0 cost:
  draw 1 card; if it is a Skill, lose 3 Sin (5 upgraded)**.
- **Pardon**: full rework — transforms **all other hand cards** into random Tormented cards
  (upgraded copies if Pardon is upgraded; never itself; excludes UnceasingWar, ExecutionForm,
  statuses and curses); **after 3 plays it transforms into a random Power card**; no longer
  self-Exhausts; the 3-play requirement is unchanged by the upgrade.
- **Devotion**: **now also removes Frail** (was Debt / Vulnerable / Weak only).
- **Entangled** (curse): new `triggerOnExhaust` — **when exhausted, adds a Slimed to the draw pile**.
- **BloodFeud**: mechanic change — old triggered FULL Bleed 2 (upg 3) times; new triggers
  **75% of the Bleed (150% upgraded) once**, via a new percent-based action.
- **Grief**: rarity → RARE; Bleed per draw 2 (+1=3) → **4 (+1=5)**; random status pool unchanged.
- **Mercy**: base cost 0 → **1**, upgrade reduces to **0**; **Ethereal now persists on upgrade**
  (was removed).
- **Trial**: self-Weak 2 (upg 1) → **1**; Str/Dex 1 (+1) unchanged.
- **Overrigid**: Strength 4 → **5**; penalty unchanged.
- **UnceasingWar**: **killing Minions (MinionPower) no longer adds a permanent hit**; only enemies
  alive when the card was played count.
- **DreadMemory**: the upgrade previously did nothing (magic never changed); it now **reduces
  cost 2 → 1**.
- **RedemptionPath**: upgrade no longer increases draw — it now **reduces cost 1 → 0** and the
  0-cost sticks (was forced back to 1 by a bug).
- **GargoyleArm / text-only polish**: wording pass on multiple cards; all descriptions no longer
  end with a period to avoid keyword-recognition issues.

### 5. Powers (7)

- **HungeringBattleWillPower**: the conditional self-damage (only when no enemy died) is gone;
  you now **take 3 NORMAL, blockable damage at the end of every turn** (was raw HP loss);
  [E] at turn start unchanged.
- **IndignationPower**: the **first card of any type** each turn costs 0 (was: first non-Attack).
- **MangledFleshPower**: threshold 5 → **6 Bleed stacks**; the trigger damage now hits
  **ALL enemies** (was only the Bleeding enemy itself).
- **BleedPower**: **only 50% of Bleed is removed each turn** (was 100%); **DeepWound now prevents
  removal entirely** (fixes a long-standing Relapse incompatibility); keyword text updated.
- **DebtPower**: description now states "when 1 Debt is blocked by Artifact, lose 5 Sin".
- **SinPower**: new public constant `SIN_PER_DEBT = 5` — single source of truth for the
  UpdateDebt/UpdateSin actions, ChaosDirty's preview and the Debt keyword text.
- **OverrigidPower**: fixed double-reduction — the penalty now reduces **baseDamage only**, not
  the current turn's damage twice.

### 6. Actions & Combat Mechanics (4)

- **TriggerBleedAction**: `ticks` → `percent` (deals % of Bleed once per enemy, guarded > 0).
- **UnceasingWarKillAction**: new `countKill` flag — kills are only counted for non-Minion enemies
  that were alive when the card was played.
- **UpdateDebtAction / UpdateSinAction**: refactor only — use `SinPower.SIN_PER_DEBT`
  (Sin↔Debt conversion unchanged).

### 7. Relics (3)

- **BlackstoneLantern, HeavyFetters**: now override `getUpdatedDescription()` — their descriptions
  were silently empty in-game.
- **GargoyleArm**: cosmetic fix.

### 8. Framework & Rendering

- **Upgrade-text engine fixed (the big one)**: `BaseCard.initializeDescription()` only re-derived
  `rawDescription` inside the in-hand branch, so collection, card-view preview and deck view parsed
  stale text; it now always re-derives the correct base/upgrade text before parsing, and
  `BaseCard.upgradeName()` forces a **re-parse after every upgrade** — upgraded descriptions now
  show everywhere (preview toggle, deck view, combat, campfire).
- **Playtester-art pipeline**: `getPortraitImage()` override loads `_p.png` variants;
  `update()` hot-swaps images when Playtester Art Mode is toggled mid-run;
  `refreshJokePortrait()` feeds the vanilla joke-portrait (beta art) rendering path.
- **TextureLoader**: `getCardTestTextureString()` resolves `cards_test/<type>/<name>.png` with a
  null-safe fallback (fixes a crash on cards without test art, e.g. the Misery status).
- **BasicMod**: keyword registration supports `%%SIN_PER_DEBT%%` substitution.

### 9. New Feature — Beta Art (Playtester Art Mode), Complete

- `patches/BetaArtUnlockPatch.java` forces the beta-art toggle in the card view popup — vanilla
  gates it behind achievements/base-game colors, so mod cards never qualified before.
- 160 test-art files shipped; per-card toggling takes effect immediately (render-time).

### 10. Localization — 6 → 24 Official Languages

- Before: deu, dut, eng, epo, fin, zhs. After: **all 24 official languages** (new: fra, gre,
  ind, ita, jpn, kor, nor, pol, ptb, rus, spa, srb, srp, tha, tur, ukr, vie, zht).
- `docs/LOCALIZATION_SPEC.md` documents the translation pipeline; `tools/validate_localization.ps1`
  validates key alignment, EXT/UPGRADE consistency and keyword coverage for every language.
- Simplified Chinese is hand-proofread; all other languages were generated by an automated
  pipeline — corrections are welcome via issue/PR.
- Display fixes: "状态 牌" word-split in zhs/zht (5 cards), German FaceDanger missing upgrade
  description, HungeringBattleWill DESCRIPTIONS type crash (array form), Odyssey's Sin text.

### 11. Art & Size

- All 160 card images replaced/upscaled with AI refinement; character portrait, cardbacks,
  energy orb and campfire textures polished.
- pngquant compression: card art shrunk to 1/3–1/4, jar **48 MB → 16 MB**.
- 160 playtester-art files shipped separately (~12 MB of extra download).

### 12. Documentation & Tooling

- README.md / README.zh-CN.md rewritten, bilingual CHANGELOG (this file), Localization spec,
  build & validation scripts committed to the repo.

### 13. Notable Bug-Fix Highlights

- Upgraded card text not shown in collection preview / deck view / combat (stale rawDescription).
- Pardon's upgraded description missing the "upgraded" wording in all 24 languages.
- Overrigid dealt double-reduced damage (baseDamage + current damage both reduced).
- Relapse kept 50% of Bleed (old leftover code) while claiming "not removed this turn".
- Boiling applied Bleed to itself.
- ExecutionForm (ex-JudgmentForm) upgrade was 25 → 25 — the upgrade did nothing.
- RedemptionPath upgrade forced the cost back to 1.
- Relic descriptions empty in-game (missing getUpdatedDescription override).
- ChaosDirty preview mismatched the real damage (Artifact interaction).
- UnceasingWar counted minion kills as permanent hits.
- JudgmentFormPower display name desynced from the renamed card.
- Crash (NPE) when a card had no test art while the playtester mode was active.

---

### Help Translate Your Language

All translations other than Simplified Chinese were generated via an automated pipeline; speakers
of any language are welcome to submit corrections (issue / PR). See `docs/LOCALIZATION_SPEC.md`
for the translation spec and how to use the validation script.