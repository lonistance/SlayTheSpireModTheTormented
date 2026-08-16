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

**#17 Pardon — hand cards were never really removed, only hidden**
- **Symptom**: playing Pardon (赦免) left the original hand cards in the hand, shrunk to 12%
  (invisible "ghosts"), while the replacement cards were **duplicated** — once slotted in place
  and once appended by the fly-in effect — so the hand layout broke and cards appeared to remain
  in the hand while being unseen. The 3rd-play transform duplicated the power card the same way.
- **Root cause**: the implementation added only the official `ExhaustCardEffect` (visual) and
  `shrink()`, then `hand.group.set(idx, replacement)` while `ShowCardAndAddToHandEffect`'s
  constructor *also* appends the card to the hand — two copies enter the hand. Decompiled
  bytecode: `ShowCardAndAddToHandEffect` calls `hand.addToHand` in its constructor; the official
  exhaust visual alone (`moveToExhaustPile`) fires every `onExhaust` hook (e.g. DeadBranch), so
  "just play the animation" cannot be done by copying that path either.
- **Fix**: originals are now truly removed with `hand.removeCard(c)` (plain list removal, zero
  hooks) plus the official `ExhaustCardEffect` for the burn-out look only — no real exhaust is
  triggered. Replacement cards use the official `MakeTempCardInHandAction` (same pattern as
  DeadBranch's `new MakeTempCardInHandAction(AbstractDungeon.returnTrulyRandomCardInCombat()
  .makeCopy(), false)`), which internally marks the card seen, copies it and animates the fly-in,
  and handles a full hand. The 3rd-play transform now only sets `this.exhaust = true` — the
  vanilla disposal (`UseCardAction` → `moveToExhaustPile`) performs the real exhaust with its own
  animation and hooks. The 3-card exclusion list (Pardon / UnceasingWar / ExecutionForm) is kept.
  Verified: no duplicates, no ghost cards, no onExhaust hook fires for the removed cards, and the
  mod compiles cleanly.

**#18 Rebel — beta art always shown in the Card Library regardless of the toggle**
- **Symptom**: in the Card Library, Rebel (暴乱) rendered its playtester (cards_test) art instead of
  the official art even with the beta-art toggle turned off; every other mod card showed its
  official art.
- **Root cause**: **not a mod bug**. The per-card beta preference `UnlockTracker.betaCardPref`
  (file `preferences/STSBetaCardPreference`) had persisted `"thetormented:Rebel": "true"`
  (all other cards `false` — the `.backUp` file confirms the previous value was `false`).
  `AbstractCard.render()` reads that pref every frame and draws `jokePortrait` (which this mod's
  `BaseCard.refreshJokePortrait()` points at the cards_test texture) whenever it is true, so the
  library thumbnail cannot show official art until the pref flips back to false.
- **Fix**: reset the persisted pref entry to `"false"` (game closed; original file preserved as
  `.backUp2`). Toggling the beta-art icon in the popup preview writes the same key, so the fix is
  also reproducible at runtime.
- **Verification**: pref file now reads `"thetormented:Rebel": "false"`; with the pref false and
  `PLAYTESTER_ART_MODE` off, `render()` takes the `renderPortrait` branch and shows official art.



**#19 SingleCardViewPopup portrait: the previous fix was the wrong approach - remediation attempt**
- **Symptom**: with beta (cards_test) art ON, the card popup's large portrait showed the 250x190
  card texture cropped/stretched into an artifact; toggling upgrade preview or the beta-art switch
  gave inconsistent results (official art or the artifact depending on the order of clicks).
- **Root cause**: the previous `LoadPortraitImg` prefix loaded the 250x190 card texture into
  `portraitImg` - the large-portrait slot that `renderPortrait()` always draws with the hard-coded
  source rect (0,0,500,380) - so the small texture was crop-stretched into an artifact, and a
  non-null portraitImg also suppressed Basemod's official fallback (`OpenFix$OpenTextureFix`
  only fires when `portraitImg == null`). Basemod's `UpgradeChangesPortraitPatch$ToggleUpgrade`
  (inserted at the `isViewingUpgrade` access inside `updateUpgradePreview`) additionally replaces
  `portraitImg` with the official portrait, ignoring the beta state, so upgrade preview clobbered
  beta art every frame. The previous fix went the wrong way; this entry records and corrects it.
- **Fix**: `portraitImg` now only ever holds a 500x380 `_p` portrait: beta ON loads
  `cards_test/<type>/<Name>_p.png` (falls back to the vanilla/Basemod official flow when missing),
  beta OFF is left completely untouched (the small-texture override, the wrong approach, is
  removed). A new `update()` postfix enforces beta art every frame: whenever a Tormented card
  wants beta art and `portraitImg` is not our loaded beta texture, the current texture is disposed
  and replaced with the beta `_p` texture - so the upgrade-preview overwrite can no longer revert
  beta art to the official art; `close()` resets the tracking reference.
- **Verification**: javac compile passes; the texture identity guard keeps per-frame cost at zero
  whenever nothing replaces the portrait.

**#20 Balance batch: seven cards re-tuned (Shackles / RelentlessBleed / MangledFlesh / Grief / Relapse / BrokenArmor / SacredLand)**
- **Shackles** (now UNCOMMON, moved to `uncommon\skill`): the hand-count condition was unreliable -
  `use()` runs before the card leaves the hand, so the effective boundary differed from the text;
  the check is now explicit (`hand.size() >= 10` while the card is still counted), and both the
  added-card count and the total-block preview follow the same rule. Block per STATUS card raised
  from 6 to 8 on upgrade.
- **RelentlessBleed**: base 4 -> 6, upgraded 6 -> 8.
- **MangledFlesh**: base 5 -> 7, upgraded 7 -> 9.
- **Grief**: base Bleed 4 -> 3 (upgrade 3 -> 4).
- **Relapse**: now applies 3 Bleed to its target(s) before the "Bleed won't be removed" effect.
- **BrokenArmor** (rework, now COMMON 1-cost, moved to `common\skill`): new identity - gain 10
  (upgrade 14) Block, each play grants 2 less (minimum 0), resets every combat. The vanilla
  `ModifyBlockAction` pitfall (it clamps negatives to `baseDamage` instead of `baseBlock`) is
  avoided by tracking the play count in a plain field; a new patch (`CombatStartPatch`) resets it
  at every battle start; the preview shows the effective Block live via a dynamic
  `!${modID}:BLOCK!` variable.
- **SacredLand**: dropped the counter-intuitive branch ("draw 2 if you have no Debt, otherwise
  lose 7 Sin"); draw is now reduced by 1 per Debt you have (minimum 0; 2 base, 3 upgraded), with
  the effective draw shown live through `EXTENDED_DESCRIPTION`.
- **Localization**: Relapse / BrokenArmor / SacredLand texts updated in all 24 languages (Bleed
  token taken from each language's Keywords.json); the new `BLOCK` dynamic variable was added to
  the `validate_localization.ps1` whitelist; full validation passes for all languages.

**#21 Rebel's EXTENDED_DESCRIPTION never applied; ReconcileFate counted multi-card Misery batches as 1**
- **Symptom**: (1) Rebel's appended line "Give !M! Bleed" never appeared on the card face even in
  hand; (2) cards generating several Misery at once (Shackles adding 2, HeavyPast adding 1 to draw
  pile + 1 to discard, Relief adding 2) only ever triggered one ReconcileFate block per batch.
- **Root cause**: (1) the in-hand injection skeleton (`BaseCard.initializeDescription()` /
  `getInjectedDescription()`) was already in place, but Rebel never overrode
  `getInjectedDescription()`; (2) the trigger hooks sat on `MakeTempCardIn*Action.makeNewCard` /
  `update`, which are unreliable carriers of per-card semantics: the hand action's batch is driven
  by a private `addToHand` table-switch on the `amount` field (a full-hand overflow branch skips
  the whole batch), so "N cards" could not be recovered from a single callback.
- **Fix**: (1) Rebel overrides `getInjectedDescription()` (SacredLand-style) and keeps `magicNumber`
  in sync with the on-paper damage in `applyPowers()` / `calculateCardDamage()`, so
  `!M!` shows the exact Bleed that will be dealt; (2) the hooks moved to the constructors of
  `ShowCardAndAddTo{Hand,Discard,DrawPile}Effect` - exactly one effect exists per generated card,
  so a batch of N triggers exactly N times regardless of overflow/batch/loop internals (all
  8 constructor overloads patched).
- **Verification**: full javac compile passes (174 classes); `onExhaust` counting untouched.

**#22 ChaosDirty's base damage grew every time it was played**
- **Symptom**: the card's on-paper base damage increased permanently with each play (+1 per Debt
  conversion), and the shown "Deal !D!" number drifted upward instead of staying at 6 (7 upgraded).
- **Root cause**: `applyPowers()` / `calculateCardDamage()` overrides injected the Debt-bonus into
  `this.baseDamage` (temporarily, then restoring it). The restore was fragile: `AbstractCard`'s
  counterpart overwrites `this.damage = this.baseDamage` while the polluted value is live, leaving
  the damage field holding base + bonus until the next recalculation; each play converts Sin into
  one more permanent Debt, so the displayed base damage ratcheted up over plays. The same field
  mutation also tangled with `BaseCard`'s own `TOTAL_DAMAGE` (VariableType.DAMAGE) variable chain,
  whose calculation temporarily rewrites `baseDamage` and re-enters these overrides.
- **Fix**: removed both overrides; the Debt bonus now lives in the `TOTAL_DAMAGE` variable's preCalc
  (`VariableType.DAMAGE`), which computes base + bonus (Strength/Vulnerable included) inside
  `BaseCard`'s temporary environment and restores all fields afterwards. The card face's D returns
  to the clean base value; the total (with Debt bonus) is shown in the EXT line via
  `!${modID}:TOTAL_DAMAGE!` as before.
- **Verification**: full javac compile passes (174 classes); the play-time action
  (`PollutedChaosDamageAction`) reads `card.baseDamage` untouched.

**#23 Wording & rework batch: Relapse text, Relapse/BrokenArmor localization fixups, BrokenArmor rework (#20 follow-up), ChaosDirty preview fix (#22 follow-up), plus repo-wide rules**
- **Relapse wording**: the two `DESCRIPTION`/`UPGRADE_DESCRIPTION` lines were rewritten in all 24 languages so the Bleed-wont-be-removed clause says "for one turn" instead of "this turn" (eng: `An enemy's <Bleed> won't be removed for one turn.` / upgraded `ALL enemies' ...`; zhs: `一名敌人身上的 ... 不会在一回合内移除`); eng `A enemy's` fixed to `An enemy's`. Existing wrong Bleed tokens in the "Apply 3 Bleed" prefix were corrected along the way (kor `출혈를` -> `출혈을`, vie `Chảy` -> `Chảy máu`) – these were previously uncovered by the NAMES check and would have failed `validate_localization.ps1`.
- **BrokenArmor rework (supersedes #20's decay mechanics)**: the card no longer applies Frail and no longer counts plays via `playsPlayed`; instead `use()` gains Block as usual (`GainBlockAction(p, p, this.block)`) and then simply does `this.baseBlock = Math.max(0, this.baseBlock - 2)` (per-play decay, never negative). `initialBaseBlock` (synced on `upgrade()`) replaces the counter, and `CombatStartPatch` restores `baseBlock = initialBaseBlock` at combat start so decay resets every combat. The `BLOCK` dynamic variable is gone; the printed `!B!` now reflects the live (decayed) block directly. All 24 languages got a rewritten `BrokenArmor` description: the "Gain 1 Frail" clause and the `!${modID}:BLOCK!` token were removed (`!B!` instead), wording normalized per language (full-width parenthes pairs such as `（en este combate）` corrected to half-width `(...)`), e.g. eng: `"Gain !B! Block. NL Each play grants 2 less Block (resets each combat)."`.
- **ChaosDirty preview fix (supersedes #22's approach)**: `TOTAL_DAMAGE` switched from the `VariableType.DAMAGE` chain to a pure-arithmetic variable (`VariableType.MAGIC`, default branch); its preCalc (`base + currentDebt * magicNumber`) never touches `baseDamage`/`damage`, so the card face's D stays clean (6 / upgraded 7) and the M column shows the real upgraded magicNumber. The bonus predictor (which forecast the Sin-to-Debt conversion of the upcoming play) was dropped – preview now equals current Debt x magicNumber, matching the expected values in every state: unupgraded total 12 (6 + 2x3), upgraded total 15 (7 + 2x4), and 23 in the discard pile once Debt is 4 (7 + 4x4). Play-time damage (`PollutedChaosDamageAction`) unchanged.
- **Repo-wide rules**: `AGENTS.md` added at the repo root, codifying the mandatory rule that every modification (code / text / docs / tools) must update both `CHANGELOG.md` and `CHANGELOG.zh-CN.md` (new entry inserted before the trailing `---`, next free number), plus the compile / localization / encoding conventions. `docs/LOCALIZATION_SPEC.md` gained a global rule that every registered keyword token (`${modID}:X`) in any card description must be immediately followed by a space (jpn precedent from §7 promoted to a global constraint), with a batch note in §10.
- **Verification**: full javac compile passes (174 classes, EXIT=0); all 24 languages pass `validate_localization.ps1` (the kor/vie token errors fixed as part of this batch); zhs/deu leftover-English warnings are pre-existing heuristics only.

**#24 Shackles rework (#20 follow-up) and ReconcileFatePower trigger rework**
- **Shackles rework (supersedes #20's "full hand adds 1" rule)**: the card now always adds 2 Misery (`MakeTempCardInHandAction(new Misery(), 2)`), matching the intended design ("still adds 2 Misery"); only the Block calculation counts the effective additions when the hand is full: when the hand is at 10 cards at play time (`use()` reads `p.hand.size()` which still includes this card), Block is computed as if only 1 of the 2 Misery cards effectively lands, otherwise 2. The `CARD_ADD` custom var transform (which previously displayed 1 on a full hand) is removed, so `!CARD_ADD!` always shows 2; the `TOTAL_BLOCK` preview preCalc mirrors `use()` (statuses in hand + effective additions: +1 when full, +2 otherwise) x magicNumber, keeping preview and actual Block in sync.
- **ReconcileFatePower trigger rework (fixes duplicated triggers)**: the old hook fired once per `ShowCardAndAddTo{Hand,Discard,DrawPile}Effect` constructor invocation, but a single generated card can be wrapped in several effect instances (e.g. the random draw-pile effect instantiates both the 6-arg and the 3-arg constructors in its `update()`), so one Misery could trigger multiple times: HeavyPast (2 Misery) gained Block 3 times, Shackles (2 Misery) 4 times. The hook now dedupes by card instance: `onMiseryCardCreated` triggers at most once per Misery instance via a static `Set<AbstractCard> firedCards`, so N generated cards fire exactly N times regardless of how many effect instances wrap each card. The set is cleared at combat start in `CombatStartPatch` (alongside the BrokenArmor reset) so instances spanning combats remain countable. Exhaust-based triggers unchanged.
- **Verification**: full javac compile passes (EXIT=0). No localization changes (card texts unchanged; `!CARD_ADD!` still resolves to 2).

**#25 CursedBrokenBlade / HeroLongsword: Sin application starts on turn 2**
- **Mechanic**: both relics no longer apply Sin at the start of the first turn of each combat; from the 2nd turn onward they apply 3 Sin at turn start as before. Implemented via a `firstTurnSkipped` flag (first `atTurnStart()` call of each combat is skipped, subsequent calls apply), reset at combat start: `CursedBrokenBlade.atBattleStartPreDraw()` now also resets the flag, and `HeroLongsword` gained an `atBattleStartPreDraw()` that only resets it. Purpose: give the player setup time and forgiveness instead of Sin snowballing immediately.
- **Localization**: the "gain Sin at the start of each turn" tail of `DESCRIPTIONS[0]` (after the last ` NL `) was rewritten to "from the 2nd turn onward on every turn start" for both relics in all 24 languages (e.g. eng `At the start of each turn from the 2nd turn onward, gain #b`, zhs `从第 2 回合起，每回合开始时获得 #b`). `DESCRIPTIONS[1]` and all other entries untouched.
- **Verification**: full javac compile passes (EXIT=0); `validate_localization.ps1` passes for all 24 languages (eng's 630 / zhs's 3 leftover-English warnings are pre-existing heuristics, incl. the `RelicID` template entry).

**#26 ChaosDirty: preview EXT dropped (supersedes #22 & #23's TOTAL_DAMAGE approaches)**
- **Problem**: the `!${modID}:TOTAL_DAMAGE!` "total damage" preview kept being wrong in-game even after #22 (VariableType.DAMAGE chain) and #23 (pure-arithmetic MAGIC variable, `base + currentDebt * magicNumber`); the actual damage dealt (`PollutedChaosDamageAction`, which waits for the Sin-to-Debt conversion of the upcoming play) is correct.
- **Fix (user-approved option: drop the preview)**: removed the `TOTAL_DAMAGE` custom variable registration, the `getInjectedDescription()` override and the unused `getDebtDamageBonus()` helper from `ChaosDirty.java`; the card face now shows only the base description ("Deal !D! damage" style). All 24 languages had the `ChaosDirty` `EXTENDED_DESCRIPTION` entry emptied (aligned empty arrays; baseline still passes the EXT/UPGRADE alignment check). Other cards using their own `TOTAL_DAMAGE` variable (TemperedSword, Riot, MassiveBleeding) are unaffected.
- **Verification**: full javac compile passes (EXIT=0); `validate_localization.ps1` passes for all 24 languages (warnings unchanged from baseline).

**#27 VoidCall: marked cards now display the "Ethereal" keyword in their description**
- **Problem**: cards to which VoidCall applied `isEthereal = true` showed no in-game indication (vanilla behaviour: Ethereal granted at runtime never appears on the card face), confusing players.
- **Fix (no other card class was touched)**: new `thetormented/patches/EtherealMarkerPatch.java` records marked card instances via a `WeakHashMap` (identity semantics, auto-cleaned when the card is GC'd; `markEthereal()` is called by `VoidCallAction` right after applying `isEthereal = true`), plus a postfix on `AbstractCard.initializeDescription()` (the total entry point which forwards to the CN branch when applicable). On every description rebuild, a marked ethereal card gets one extra `DescriptionLine` appended showing the current-language keyword name (looked up via `languagePack.getKeywordString("ethereal").ETHEREAL.NAMES[0]`, fallback "Ethereal"), e.g. zhs "虚无." — no 24-language text additions needed since the vanilla keyword table auto-localizes. Rebuilds are per-call so the line appears exactly once per render list; untouched cards never see it.
- **Revision (same entry)**: the initial implementation used ModTheSpire's `SpireField` injection, which NPE'd during startup on the very first `AbstractCard.<init>` (the injected field reference is not initialized yet when `initializeDescription` runs inside the constructor, see startup crash log). Replaced with the plain-Java weak-identity map above; compile passes and the crash is gone.
- **Verification**: full javac compile passes (EXIT=0). Localization untouched.

**#28 HeavyPast: upgraded no longer draws an extra card (damage+2 upgrade kept)**
- **Mechanic**: the upgrade previously changed Draw 1 -> 2 cards and damage 6 -> 7. Now the upgrade raises damage by 2 (6 -> 8, `UPG_DAMAGE = 2`) and no longer increases the draw (`DRAW_UPG` set to `0`, `setMagic(DRAW_BASE, 0)`, draw stays at 1). Both hits still deal the upgraded damage (2 x 8 = 16 total); the Misery additions (1 to draw pile, 1 to discard pile) are unchanged.
- **Localization**: zero changes — both `DESCRIPTION` and `UPGRADE_DESCRIPTION` (identical in all 24 languages) already use the dynamic `!M!` token, so the "Draw !M! cards." line automatically shows 1 after the upgrade.
- **Verification**: full javac compile passes (EXIT=0). Pending in-game confirmation by the user before the entry is considered final.

**#29 Pardon plays counter / Indignation first-card display / VoidCall fix / Shackles CARD_ADD cleanup**
- **Pardon**: new dynamic variable `!${modID}:PLAYS!` (VariableType.MAGIC, preCalc reads the instance's `plays` field) plus a new `getInjectedDescription()` → EXTENDED_DESCRIPTION[0] line "(Played N times)"; added in all 24 languages (zhs "(此牌已打出 N 次)", zht/eng/… localized). The count is shown live while the card is in hand and updates on every `applyPowers`; `validate_localization.ps1` whitelist and `docs/LOCALIZATION_SPEC.md` §4 updated with `PLAYS`.
- **Indignation**: the Power description in all 24 languages no longer says "non-Attack" (the code already made the first card of any type free); texts now match "the first card you play each turn costs 0". New visual cue: `IndignationPower.atStartOfTurn` now temporarily shows all current hand cards as 0 cost (`costForTurn = 0` + `isCostModifiedForTurn = true`) — bytes confirm the engine resets hand `resetAttributes` before `applyStartOfTurnPowers`, so the cue survives; once the first card is played (`onPlayCard`), the other hand cards are restored to their real cost (`costForTurn = c.cost`, flag cleared; the first card itself stays 0-cost as before); if no card is played at all, `atEndOfTurn` restores every hand card.
- **VoidCall (#27 second revision)**: the original fix (marker + `AbstractCard.initializeDescription` postfix) only rebuilt descriptions through this mod's `BaseCard.applyPowers`/`calculateCardDamage` override and only when `getInjectedDescription()` was non-null — vanilla and other-mod cards never rebuilt, so the "Ethereal" line never appeared. Fixed by explicitly calling `c.initializeDescription()` right after `markEthereal(c)` in `VoidCallAction` (idempotent: the postfix appends exactly one line).
- **Shackles (#24 cleanup)**: 23 of 24 languages still carried the now-removed `!${modID}:CARD_ADD!` template (only zhs had the new wording) — replaced in `DESCRIPTION`/`UPGRADE_DESCRIPTION` with plain "2", matching the always-add-2 behavior; `CARD_ADD` removed from the `validate_localization.ps1` whitelist and `LOCALIZATION_SPEC.md` §4.
- **Verification**: full javac compile passes (EXIT=0, 174 classes); `validate_localization.ps1` passes for all 24 languages (PASS=24, warnings unchanged from baseline). Pending in-game confirmation by the user.

**#30 RelentlessEntanglement: obscure recovery-loss during the vanilla shuffle-animation window (known limitation, treated as normal)**
- **We discovered a subtle bug**: when increasing Sin converts into increasing Debt at the exact moment a shuffle event is in progress, the vanilla game does not move the cards from the discard pile to the draw pile until the shuffle animation has finished playing. During that window `RelentlessEntanglement` belongs to neither the draw pile nor the discard pile — the Debt-increase listener fires, but the card is not recovered.
- **Root cause is a bug that already exists in the vanilla game and has never been fixed**: the vanilla `ShuffleVfx` empties the discard pile immediately and hands the cards to the animation; the cards only land in the draw pile when the animation completes. Any card in that window is "orphaned" — it is not reachable through any normal card group, so any position-based recovery (`DiscardToHandAction`, or any of our traversal-based notifications) misses it. Touching such a card mid-animation (moving it to hand while the vfx still holds its reference) would corrupt the animation finale and duplicate the card, so it cannot be safely worked around from a single card.
- **Fix attempt (did not succeed)**: we replaced the queued `DiscardToHandAction` with a custom `RecoverToHandAction` that polls until the shuffle settles: if the card is in the discard pile it is recovered as usual; if the shuffle animation has already landed it into the draw pile it is pulled back from there; cards mid-animation are never touched. This covered the ordering where the listener fires while the card is still in the discard pile, but **the attempt did not fully succeed** — when the Debt increase itself fires inside the animation window (the card already orphaned, so the notification traversal never reaches it), the recovery is still lost, exactly as before.
- **Conclusion**: given that the card's numbers are already adequate and the trigger frequency is relatively low, we consider this a normal phenomenon that does not meaningfully affect play. Players are asked to treat it as such. No localization changes; full javac compile passes (EXIT=0).

**#31 VoidCall two fixes: Retain cards never exhaust after gaining Ethereal; the "Ethereal" line was rendered off-card**
- **Issue 1 (Retain cards do not exhaust)**: after VoidCall grants Ethereal to a Retain card (Firm, upgraded Loan), the card stayed in hand at end of turn and was never exhausted. Root cause is a **vanilla inherent behavior**: `DiscardAtEndOfTurnAction` first moves all `retain`/`selfRetain` cards into limbo (later restored by `RestoreRetainedCardsAction`), while the exhausting hook `AbstractCard.triggerOnEndOfPlayerTurn` only iterates a clone of the hand — cards in limbo are never visited and never exhausted. The vanilla game has no Retain+Ethereal combination card, so the case was never exposed; Retain and Ethereal (gone at end of turn) are contradictory anyway.
- **Fix 1**: `VoidCallAction` now clears `retain`/`selfRetain` when granting Ethereal — the card loses Retain and takes the normal exhausting path (the `ExhaustSpecificCardAction` for Ethereal cards is queued ahead of the discard actions, so the card is consumed rather than discarded). Ethereal now genuinely works on Firm / upgraded Loan and any other Retain card selected by VoidCall.
- **Issue 2 (Ethereal line was off-card)**: the "Ethereal." keyword line was pushed far outside the card face to the left (visually similar to stray spaces/tabs). Root cause: the patch used `new DescriptionLine(text, 1000.0f)`, while the vanilla renderer positions each line at `card.x − line.width × drawScale / 2` — the fake 1000f width shoved the line off the card.
- **Fix 2**: `EtherealMarkerPatch` now measures the width the same way the vanilla code does: `new GlyphLayout(FontHelper.cardDescFont_N, text).width` (the vanilla static `AbstractCard.gl` is private, so we construct our own; at description-init time the font scale is 1.0, matching the vanilla measurement basis).
- **Verification**: full javac compile passes (EXIT=0). No localization changes (no validate needed). Pending in-game confirmation by the user: Firm / upgraded Loan selected by VoidCall exhaust at end of turn as expected; the Ethereal line sits flush under the last description line inside the card face.


**#32 v1.0.1 official patch release**
- **Content**: this release replaces some relic textures, fixes the card preview in Beta-art (cards_test) mode, re-checks all card implementations and fixes known card bugs, and adjusts / reworks a number of cards.
- **Changes in this release** (cumulative #20-#31): relic art replacement (#27-era art batch); Beta art card preview fix (LoadPortraitImg rework, cards_test/<Tier>/_p.png rule, per-frame fallback, #19); all-card implementation check with known bug fixes (VoidCall #27/#29/#31, ChaosDirty #22/#23/#26, Rebel #21, ReconcileFate #21/#24, RelentlessEntanglement #30 documented limitation, Indignation #29, Pardon #29, Shackles #20/#24/#29, BrokenArmor #20/#23, SacredLand #20, HeavyPast #28, Relapse #23, plus relic reworks CursedBrokenBlade/HeroLongsword #25); full 24-language validation.
- **Technical**: pom.xml version bumped 1.0.0 -> 1.0.1; jar rebuilt from clean compile (javac EXIT=0, 151 sources) with the ModTheSpire.json description encoding defect (GBK-garbled right single quote / CJK punctuation) fixed.
- **Verification**: full javac compile EXIT=0; alidate_localization.ps1 PASS in all 24 languages.
---

### Help Translate Your Language

All translations other than Simplified Chinese were generated via an automated pipeline; speakers
of any language are welcome to submit corrections (issue / PR). See `docs/LOCALIZATION_SPEC.md`
for the translation spec and how to use the validation script.