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

### 13. Bug-Fix Archive — Symptom · Root Cause · Fix · Verification

Every entry below follows the same four-part structure, with the exact reproduction path and the
code path that was wrong. All fixes were verified in-game against the deployed jar
(`mods/thetormented.jar`), and the vanilla mechanisms quoted here were confirmed by
decompiling the game's own bytecode (`javap` on `AbstractCard` / `SingleCardViewPopup` /
`UnlockTracker`) — nothing was assumed.

**#1 Upgraded card text not shown (part 1) — `initializeDescription()` parsed stale text**
- **Symptom**: an upgraded card (e.g. Pardon) kept showing its *base* description in the
  collection, in the card-view popup's upgrade preview, and in the deck view — but showed the
  correct text in combat. Reproduce: open the collection → hover Pardon → toggle the upgrade
  preview.
- **Root cause**: `BaseCard.initializeDescription()` overrode the vanilla method but only
  re-derived `rawDescription` inside the `isCardInHand()` branch; everywhere else it fell through
  to `super.initializeDescription()`, which parses whatever `rawDescription` happened to hold
  (stale base text). Worse, the line `this.rawDescription = base;` ran *after* the parse, so the
  correct text was always written too late — the parse consumed the old value.
- **Fix**: `rawDescription` is now unconditionally re-derived from `baseDescription()` (upgraded
  → `UPGRADE_DESCRIPTION`, in-hand → plus the injected clause) **before** the parse; it is still
  reset to the clean base string afterwards. One change, every context fixed (collection, popup,
  deck, combat, campfire).

**#2 Upgraded card text not shown (part 2) — nothing re-parses after `upgrade()`**
- **Symptom**: even after fix #1, the popup's upgrade preview still rendered the base text —
  the copy made by the popup was never re-parsed.
- **Root cause** (two layers, both confirmed in bytecode): (a) vanilla `upgradeName()` does **not**
  call `initializeDescription()` — it only bumps `timesUpgraded`, sets `upgraded`, appends "+" to
  the name and calls `initializeTitle()`; (b) `SingleCardViewPopup.render()` builds the preview as
  `card = card.makeStatEquivalentCopy(); card.upgrade(); card.displayUpgrades();` and then renders
  the card's *parsed* `description` tokens — and the popup never calls `initializeDescription()`
  anywhere. `makeStatEquivalentCopy()` only calls `upgrade()` `timesUpgraded` times, so an
  un-upgraded card's copy got its description from `upgrade()` → `upgradeName()` → no re-parse.
  Cards whose `upgrade()` overrides never call `super` (Pardon, Relapse, Taboo, DreadMemory,
  Bloodstain…) skipped `BaseCard.upgrade()`'s re-parse entirely.
- **Fix**: `BaseCard.upgradeName()` now forces `initializeDescription()` after `super` — a single
  choke point that covers every card, because every custom `upgrade()` calls `upgradeName()`.
  Verified: preview toggle, deck view of upgraded cards, in-combat and campfire upgrades all show
  the upgraded text.

**#3 Pardon's upgraded description incomplete in all 24 languages**
- **Symptom**: Simplified Chinese showed "将所有手牌变化为随机牌…变化为随机能力牌" for the
  upgraded version — the second sentence was missing "升级过的" (and the collection preview
  claimed "打出 2 次").
- **Root cause**: the localization data file's `UPGRADE_DESCRIPTION` second sentence had never
  been updated for the new wording; an earlier automated batch also left spaces around `!M!`
  ("打出 !M! 次后") and a `-1` magic upgrade that conflicted with the fixed 3-play rule.
- **Fix**: all 24 language files re-synced in one scripted pass (upgraded sentence now ends
  "…变化为随机升级过的能力牌", with `TrimEnd` guarding against double punctuation), `!M!` spacing
  normalized in zhs/zht, and `Pardon.upgrade()` simplified to `upgradeName()` only (plays
  requirement stays 3). Verified by previewing all 24 languages in the generated files.

**#4 Beta-art toggle never appeared in the card view popup**
- **Symptom**: no "test art" checkbox in the popup for any Tormented card.
- **Root cause**: decompiled `SingleCardViewPopup.canToggleBetaArt()` =
  `UnlockTracker.isAchievementUnlocked("THE_ENDING") || switch (card.color)` — RED → RUBY_PLUS,
  GREEN → EMERALD_PLUS, BLUE → SAPPHIRE_PLUS, PURPLE → AMETHYST_PLUS, **default → false**. Mod
  colors never qualify, so the toggle was unreachable unless the player had beaten the heart.
- **Fix**: new `patches/BetaArtUnlockPatch.java` (prefix patch on `canToggleBetaArt`) short-circuits
  to `SpireReturn.Return(true)`. Note: this MTS build (3.30.3) has no `SpirePrefix` class — the
  existing `SpirePrefixPatch` spelling used by `RestrictionPower` was followed. Verified: toggle
  appears and per-card art switches at render time.

**#5 Crash (NPE) on cards without test art**
- **Symptom**: game crashed on startup/card creation with
  `NullPointerException at TextureLoader.loadTexture(124) ← getTextureNull(73) ←
  BaseCard.refreshJokePortrait(197) ← <init> ← Misery`.
- **Root cause**: `refreshJokePortrait()` unconditionally called `getTextureNull(testPath, false)`
  even when `getCardTestTextureString()` returned null (Misery is a Status — there is no
  `cards_test/status/Misery.png`), and `getTextureNull(null)` → `new Texture(null)` throws.
- **Fix**: null-guard — when no test art exists, fall back to the card's normal portrait.
  Verified: Misery and every other art-less card load without crashing; jar contents checked to
  confirm the missing resource.

**#6 Overrigid reduced damage twice**
- **Symptom**: attacking under Overrigid (过度僵硬) dealt less damage than expected — the penalty
  seemed applied twice.
- **Root cause**: `OverrigidPower.onAfterCardPlayed()` did both
  `card.baseDamage = max(0, baseDamage - amount)` **and**
  `card.damage = max(0, damage - amount)` — the *current turn's* damage (already derived from
  baseDamage) got reduced a second time.
- **Fix**: keep only the permanent `baseDamage` reduction; the current `damage` field follows
  automatically. Verified: in-combat damage matches `baseDamage − penalty`.

**#7 "Bleed not removed" cards still lost half their Bleed (Relapse / DeepWound)**
- **Symptom**: after playing Relapse (血瘾) / applying DeepWound (旧伤复发), the enemy still lost
  50% of its Bleed at turn start — contradicting the card text.
- **Root cause**: `BleedPower.atStartOfTurn()` still carried the old "keep 50%" rule in its
  DeepWound branch (`ReducePowerAction` of `ceil(amount*0.5)`), left over from an earlier
  design; the mechanic had since changed to "Bleed is not removed this turn". The no-DeepWound
  branch removed **all** Bleed, which was also out of date.
- **Fix**: one coherent rule — without DeepWound remove exactly `100 − BLEED_RETAIN_PERCENT`
  (50%) per turn; with DeepWound remove nothing. New constant `BLEED_RETAIN_PERCENT = 50`
  (tunable). Verified: Bleed stacks stay put under DeepWound, halve otherwise.

**#8 Boiling applied Bleed to itself**
- **Symptom**: playing Boiling (沸腾) gave the *player* Bleed.
- **Root cause**: in `use()`, the card is still in the hand; the exhaustion sweep
  (`c.type != CardType.ATTACK`) caught the card itself — a Skill — so Boiling exhausted itself and
  counted itself in its own multiplier.
- **Fix**: exclude self from the exhaustion target list (and, as a follow-up rework, Bleed now
  lands on a *random enemy* per exhausted card instead of all enemies). Verified: no self-Bleed,
  no self-exhaust.

**#9 ExecutionForm's upgrade did nothing (25 → 25)**
- **Symptom**: upgrading ExecutionForm (ex-JudgmentForm) changed nothing.
- **Root cause**: the constants literally read `BASE_THRESHOLD = 25; UPG_THRESHOLD = 25;`
  with the comment `// 25% -> 50%` — the design intent (50) was never written into the constant,
  and the "upgrade" applied 25 again.
- **Fix**: base 50, upgraded 75. Verified: upgraded card applies 75% of remaining HP as damage
  at the threshold.

**#10 RedemptionPath's 0-cost upgrade bounced back to 1**
- **Symptom**: after upgrading, RedemptionPath (救赎之道) showed 1 cost again.
- **Root cause**: the cost reduction was hand-rolled inside `upgrade()`, but
  `BaseCard.upgrade()`'s cost branch (when `isCostModified && cost < baseCost`) recomputes the
  cost as `cost + (costUpgrade − baseCost)`, overwriting the manual 0-cost back to 1.
- **Fix**: declare the intent instead — `setCostUpgrade(0)` — so the cost branch adjusts from
  `baseCost` and the 0-cost sticks, even when the cost was modified by external effects.
  Verified: stays 0 after upgrade, including with bottle/relic cost modifiers.

**#11 Relic descriptions empty in-game**
- **Symptom**: BlackstoneLantern (黑石提灯) and HeavyFetters (沉重脚镣) showed no description.
- **Root cause**: vanilla `AbstractRelic.getUpdatedDescription()` returns `""` by default and
  `updateDescription()` is empty — the description is assigned exactly once, in the constructor.
  Both relics simply never overrode `getUpdatedDescription()`.
- **Fix**: override it to return `DESCRIPTIONS[0]`. Verified: both relics display their text.

**#12 ChaosDirty — upgrade numbers never took effect, and the preview lied**
- **Symptom**: (a) the upgraded card kept the base damage and Debt bonus (text promised 7 / +4);
  (b) the hand preview showed a different total than the actual hit.
- **Root cause**: (a) the upgrade parameters were never wired into the preview path —
  `applyPowers()`/`calculateCardDamage()` temporarily add `getDebtDamageBonus()` and restore the
  base afterwards, so a missing `damageUpgrade`/`magicUpgrade` silently vanished; (b) the preview
  counted only the *current* Debt, ignoring that this play first converts Sin → Debt, and behaved
  differently under Artifact.
- **Fix**: real upgrade values (damage 6 → 7, Debt bonus 3 → 4) and a preview that mirrors actual
  resolution (with/without Artifact). Verified: preview equals the damage dealt.

**#13 UnceasingWar counted minion kills as permanent hits**
- **Symptom**: killing a minion (e.g. Gremlin Leader's adds) permanently increased UnceasingWar's
  (无休战火) bonus-hit counter.
- **Root cause**: the kill check counted every `isDeadOrEscaped()` target regardless of the
  `MinionPower` minions carry.
- **Fix**: `UnceasingWarKillAction` takes a `countKill` flag; the card passes
  `!m.isDeadOrEscaped() && !m.hasPower(MinionPower)` and only enemies alive when the card was
  played are eligible. Verified: minion kills no longer accumulate.

**#14 Startup crash: "Expected BEGIN_ARRAY but was STRING"**
- **Symptom**: the game refused to start after a localization round.
- **Root cause**: `HungeringBattleWillPower.DESCRIPTIONS` was written as a plain string; basemod
  requires the array form and the JSON parser aborted.
- **Fix**: reverted to the array form, and the validation script now **rejects** string-typed
  `DESCRIPTIONS` fields so the mistake can't recur. Verified: clean boot + validator catches the
  bad shape.

**#15 Small text/display fixes**
- "状态 牌" word-split in Simplified/Traditional Chinese (5 cards affected) — zhs/zht line-break
  parser split the keyword; fixed the wording so the term stays intact.
- Odyssey's Simplified Chinese description showed "1 Sin" instead of 3.
- JudgmentFormPower's display name desynced after the card rename → ExecutionFormPower.
- German FaceDanger was missing its upgraded description entirely.
- All touched card texts no longer end with a period (keyword-recognition robustness).

**#16 Triggered-Bleed settlement unified (BloodFeud)**
- **Symptom**: BloodFeud (血仇) dealt an opaque amount of damage ("trigger Bleed N times") that
  was hard to reason about and hard to tune.
- **Root cause**: the action looped a full-Bleed HP-loss hit `ticks` times — the "times" model
  conflicted with the new 50%-retention Bleed economy.
- **Fix**: `TriggerBleedAction` now takes a `percent` and deals `amount * percent / 100` once
  (BloodFeud: 75%, upgraded 150%), guarded by `> 0`. Verified: damage matches the stated percent.

---

### Help Translate Your Language

All translations other than Simplified Chinese were generated via an automated pipeline; speakers
of any language are welcome to submit corrections (issue / PR). See `docs/LOCALIZATION_SPEC.md`
for the translation spec and how to use the validation script.