# Changelog

## Fix Round 3 (2026-08-14): HungeringBattleWill, Pardon, FormPact & More Corrections

Corrections to the previous rounds, plus gameplay fixes; all 24 languages resynced.

### Card & Power Adjustments

- HungeringBattleWill (饥渴战意): the end-of-turn self-damage is no longer HP loss — you now **take 3 damage — normal, blockable damage** at the end of every turn. Energy gain at the start of each turn is unchanged.
- Pardon (宽恕): the transformation pool is now limited to this mod's own cards (COMMON / UNCOMMON / RARE), excluding UnceasingWar (无休战火), ExecutionForm (处刑形态) and Pardon itself. The description is now static — "…After being played !M! times, transform into a random Power card" (the dynamic in-hand counter and EXTENDED_DESCRIPTION were removed); the exhaust animation of the original cards and the add-to-hand animation of the new cards were added.
- FormPact (缔结契约): the generated Misery now goes to your **discard pile** instead of the draw pile.
- Devotion (虔诚): the "Frail removal was previously omitted" note was removed from all 24 card texts (it was meant for the changelog only).
- UnceasingWar (无休战火): kill detection now checks the **MinionPower** — killing a minion does NOT add a hit, killing any other enemy does. The kill-check only triggers for enemies that were alive when the card was played. The bonus-hit counter intentionally persists across combats within a run (card mechanic).
- Boiling (沸腾): base Bleed 4 → 5 (upgraded 6).
- Text convention: all touched texts no longer end with a period, to avoid keyword-recognition issues.
- Playtester art mode: fixed — both the small (image.png) and large (image_p.png) images are now loaded from the cards_test folder when the mode is on, including toggling it mid-run.

### Localization Sync (all 24 official languages)

- All 24 languages resynced for HungeringBattleWill (card + power), Pardon (static !M! description, EXTENDED_DESCRIPTION removed), FormPact and Devotion.
- Localization validation passes for all 24 languages (0 errors).

---

## Fix Round 2 (2026-08-14): Devotion, FormPact & Memorial Adjustments

A follow-up adjustment round for 3 skills and 1 power, with all 24 languages synced.

### Card Adjustments

- Overrigid (过度僵硬): Strength gained 4 → 5.

- Devotion (虔诚): now also removes **Frail** from yourself (previously only Debt, Vulnerable and Weak were removed). The omission was an oversight; the card text in all 24 languages notes this.
- FormPact (缔结契约): the generated **Misery** is now added to your **draw pile** instead of your hand.
- Memorial (祭奠): fully reworked again — cost 0. Draw 1 card; if the card drawn is a Skill, lose 3 Sin (5 upgraded). Note: the previous round announced a Block-memory version of Memorial; that version was reverted before shipping and has been replaced by this one.

### Localization Sync (all 24 official languages)

- All 24 languages resynced for the 3 adjusted cards (Devotion, FormPact, Memorial): localized Misery card-name references, the new Frail clause, and the draw-pile wording.
- Localization validation passes for all 24 languages (0 errors).

---

## Fix Round (2026-08-14): Bug Fixes and Reworks (7 Cards)

A bug-fix round covering 7 cards/powers, followed by a 24-language localization sync.

### Card & Power Adjustments

- RedemptionPath (救赎之道): fixed the upgrade bug that forced the cost back to 1 — the empty cost now sticks after upgrading.
- HungeringBattleWill (饥饿战意): the end-of-turn self-damage is now **unconditional** — lose 3 HP at the end of every turn (previously only when no enemy was killed); still gains [E] at the start of each turn.
- BloodFeud (血仇): retained Bleed raised to 75% (150% upgraded) to fit its UNCOMMON rarity.
- Pardon (赦免): reworked — no longer exhausts; on play, transforms ALL other cards in your hand into random Common or Uncommon cards (upgraded versions if Pardon is upgraded; it can never transform into itself); after being played 3 times, transforms itself into a random Power card (upgraded if Pardon is upgraded).
- RelentlessEntanglement (纠缠不放): when this card is exhausted, add a Slimed to your draw pile.
- BrokenArmor (破碎甲胄): Block 15 (20 upgraded) and applies 1 Frail to yourself.
- Memorial (祭奠): reworked — now 1 cost: you cannot gain Block this turn, and next turn you gain 50% (75% upgraded) of the Block you attempted to gain this turn. *(Reverted before shipping — see Fix Round 2 below.)*

### Localization Sync (all 24 official languages)

- All 24 languages resynced for the 5 reworked cards (BloodFeud, Pardon, RelentlessEntanglement, BrokenArmor, Memorial) and 2 powers (HungeringBattleWill, MemorialPower).
- Localization validation script passes for all 24 languages (0 errors).
- Hotfix: the HungeringBattleWillPower DESCRIPTIONS field was reverted to the array form required by basemod (fixes a startup crash: "Expected BEGIN_ARRAY but was STRING"); the German FaceDanger additionally gained its missing upgrade description. The validation script now also rejects string-typed DESCRIPTIONS fields.

---

## Final Tuning Round (2026-08-13): 18-Card Balance Pass and Full Localization Sync

A focused tuning round touching 18 cards/powers, followed by a full 24-language localization sync so that every language matches the new numbers and the card texts.

### Card & Power Adjustments

- Boiling (沸腾): Bleed now applies to a **random enemy** instead of ALL enemies (per card exhausted); the card no longer exhausts itself (fixes self-Bleed); upgraded Bleed 4 → 5.
- PleaOfInnocence (无罪恳求): Block 5 → 7 (9 upgraded); rarity COMMON → UNCOMMON.
- Mercy (怜悯): base cost 1 (upgraded cost 0); Ethereal is no longer removed on upgrade.
- Bloodbath (血浴): base cost 2 (upgraded cost 1); applies Bleed with the **first** attack card played each turn (was 2); texts singularised in all 24 languages.
- SacredLand (净土): Block 6(+2) → 8; draws 2(+1) cards; playing it now reduces your Sin by 7.
- QuenchedBlade (淬火刀刃): damage 7 (+1 upgraded, 8 total).
- AtonementStrike (赎罪之击): damage 8 (+1 upgraded, 9 total); draws 2(+1) cards; playing it now reduces your Sin by 4.
- BloodFeud (血仇): rarity RARE → UNCOMMON; Bleed triggers 3 times (2 upgraded); base keeps 50% of Bleed, upgrade keeps 100%.
- BloodThorns (血荆棘): Bleed 3 (+1 upgraded, 4 total) — reverted to the original value.
- MangledFleshPower (血肉撕裂): threshold 5 → 6 stacks of Bleed; the trigger now hits **ALL enemies**.
- HeavyPast (沉重过往): deals damage twice (×2); damage 6 (+1 upgraded, 7 total).
- Bloodstain (血渍): applies 4 Bleed; Exhaust on the base card, removed on upgrade.
- RedemptionPath (救赎之道): upgraded cost 1 → 0.
- HungeringBattleWillPower (饥饿战意): removed the self-damage clause ("take 3 damage if no enemy was killed this turn"); now simply grants [E] at the start of each turn.
- Trial (试炼): Weak applied 2 → 1; Strength/Dexterity 1 (+1 upgraded).
- VoidCall (虚空呼唤): no longer Innate; Exhaust on the base card, removed on upgrade; draw 1 (upgrade no longer increases it).
- Agony (苦痛): damage 7 (+1 upgraded, 8 total).
- Relief (解忧): now adds 2 Misery (苦痛牌) to your discard pile, with a preview.

### Localization Sync (all 24 official languages)

- All 24 languages re-synced to the new numbers: the 3 powers above (MangledFleshPower, HungeringBattleWillPower, BloodbathPower) and the 13 affected cards (AtonementStrike, Bloodbath, BloodFeud, Boiling, HeavyPast, HungeringBattleWill, MangledFlesh, Mercy, RedemptionPath, Relief, SacredLand, Trial, VoidCall).
- Fixed collateral damage from the earlier automated batch: RelentlessBleed in Simplified/Traditional Chinese correctly says "all enemies" again, and Boiling says "a random enemy".
- Simplified Chinese field alignment: Agony/ArmSevering/UnrighteousGuilt/FaceDanger upgraded descriptions restored; RelentlessEntanglement's redundant upgraded description removed; Mercy's upgrade description now shows Ethereal.
- Keyword inflection forms added for Finnish (`verenvuodosta`), Greek (`αιμορραγίας`), Russian (`греха`) and Ukrainian (`гріха`).
- The localization validator now passes cleanly for all 24 languages (0 errors, 0 warnings).


## Major Update (2026-08-13): Localization, Card Art Overhaul, Size Optimization

This version brings sweeping changes to the project, covering localization, card illustrations, file size, balance, and bug fixes.

### 1. Full Localization: All 24 Official Game Languages

- Translations for every language officially supported by the game are now complete.
- Only **Simplified Chinese** has been manually proofread by the author so far; players using other languages are warmly encouraged to contribute fixes (e.g., via issue or PR).
- How it was done: the agent learned the translation specification, and both the spec document and the validation script were committed into the project (`docs/LOCALIZATION_SPEC.md` and `tools/validate_localization.ps1`, each with usage instructions so others can pick them up) — averaging roughly 8 minutes per language.

### 2. Card Art Overhaul (~70% of Cards)

- Why redo it: previous illustrations were generated using the free quotas of multiple image-generation AIs — each run produced one large sheet containing several sub-images, from which compositions were manually selected, scaled, and cropped to the game's standard size. The cost: blurry quality and inconsistent styles.
- After finalizing the illustration for each card, the author refined the images with AI upscaling/enhancement and redid some illustrations that were previously unsatisfying.

### 3. Size Optimization: pngquant Compression (jar 48MB → 16MB)

- Using the open-source tool **pngquant**, all card illustrations were compressed to 1/3–1/4 of their original size with no obvious loss of visual quality.
- Why compression was necessary: the `images` folder accounts for 95% of the jar's size, and within it, `cards` accounts for 85% of `images`. A single 500px×380px image weighs 400–500KB, and a 250px×190px image 95–120KB — individually modest, yet the total of 77×2 images was unacceptable.
- Result: the jar was reduced from 48MB to 16MB.

### 4. Texture Polish

- Also beautified the character defeat texture and the campfire texture.

### 5. New Feature: Playtester Art Mode

- Playtester Art Mode is now available and can be viewed in-game (Settings → Playtester Art Mode).
- These scrapped/alternate illustrations are also pngquant-compressed. Thanks to the tool's developer, the author could use the saved space to ship the discarded concepts.
- The cost for players: roughly 12MB of additional download.

### 6. Bug Fixes

- **Boiling applied Bleed to itself** (Boiling.java:38-42): when `use()` runs, the card is still in hand; being a SKILL (non-ATTACK), it was swept into `cardsToExhaust` by `c.type != CardType.ATTACK` — exhausting itself and counting toward the multiplier. Fixed by excluding itself.
- **Empty relic descriptions**: the official `AbstractRelic.getUpdatedDescription()` returns an empty string by default, and the base `updateDescription()` is an empty method — the description is assigned only once, in the constructor, via `getUpdatedDescription()`. Not overriding it means an empty relic description. The relics Blackstone Lantern (黑石提灯) and Heavy Fetters (沉重脚镣) previously forgot to override this method; now fixed.
- **Relapse kept the wrong number of stacks** (long-standing): the card was originally designed as "keep 50% of the Bleed on one enemy this turn," with the upgrade changing it to "Bleed is not removed this turn." It was later buffed to its current form — "Bleed on one enemy is not removed this turn," upgraded to "Bleed on ALL enemies is not removed this turn" — but the old "keep 50%" code was forgotten and left in. Fixed.
- **ChaosDirty (污浊之乱) showed wrong total damage**: the card now correctly computes its actual damage depending on whether the player has Artifact.

### 7. Balance Adjustments

- Boiling (沸腾): 4(4) → 5(6).
- Grief (悲恸): rarity changed to UNCOMMON; Bleed applied per turn 2(3) → 3(4).
- BloodFeud (血仇): rarity changed to RARE; Bleed tick count 2(1) → 3(2).
- Bleed removal logic changed: Bleed is no longer removed entirely — only 50% is removed each turn (values are subject to further tuning).

### 8. Text & Localization Fixes

- The Debt description now states: "when offset by Artifact, every 1 point of Debt offset also reduces Sin by 5." This mechanic was designed long ago but had been forgotten in the description text.
- The previous version registered Keywords.json, letting players view featured mechanics; this version re-checked the description texts of **all** cards and relics in Simplified Chinese and English, fixing a few display issues so they can be viewed correctly.
- Re-checked all cards whose descriptions reference Agony (苦痛) to ensure correct previews.

## Balance Pass (2026-08-13): Tuning, Card Reworks, and Renames

### Number Tuning

- Agony (伤痛): upgrade damage +2 (7 → 9); upgrade Weak/Vulnerable 1 → 2.
- CruelTorture (残酷折磨): Sin gained 5 → 7.
- Firm (坚定): Block 9 → 10.
- QuenchedBlade (淬火刀刃): upgrade damage bonus 3 → 2 (9 total upgraded); upgrade grants +1 extra damage card (4 total upgraded).
- RelentlessEntanglement (纠缠不放): damage 4(+1) → 5(+2) (7 upgraded).
- SacredLand (净土): upgrade block bonus 3 → 2 (8 upgraded).
- FaceDanger (不避艰险): damage 15 → 16, upgrade bonus 5 → 4 (still 20 upgraded).
- BloodGuard (鲜血援护): Block and Bleed both 6 → 5.
- DreadMemory (恐惧记忆): upgrade now reduces cost 2 → 1 (25% damage reduction unchanged).
- Parry (招架): Block 5 → 6.
- TemperedSword (淬炼之剑): damage 8 → 9, bonus per Status card 2 → 3 (3 → 4 on upgrade).
- ChaosDirty (污浊之乱): fixed upgrade not applying the Debt bonus increase (upgraded: damage 6 → 7, Debt bonus 3 → 4, matching the card text).
- RelentlessBleed (血流不止): Bleed 4(+2) → 3(+1).
- BloodSea (血海): Bleed applied 5 → 3.
- BloodThorns (血荆棘): Bleed 3(+1) → 2(+1).
- HeavySmite (重砸): damage 12(+4) → 15(+5) (20 upgraded).
- Indignation (激愤): no longer restricted to non-Attack cards — the first card you play each turn now costs 0.
- JudgmentForm (处刑形态): threshold corrected 25% → 50% (upgrade to 75%), matching the original design.
- MassiveBleeding (大出血): damage 6 → 4.
- Bloodstain (血渍): applies 4 Bleed; upgrade no longer adds Bleed, instead removing "Exhaust".
- RedemptionPath (救赎之道): upgrade no longer increases card draw (stays 1), now gains "Innate".
- Unleash (蓄势待发, formerly Accumulate): upgrade damage bonus 2 → 1 (8 upgraded).

### Card Rework

- Implication (株连) reworked: 1 energy. Exhaust 1 card from your hand; deal !D! damage to ALL enemies (11 upgraded); for each enemy with Bleed dealt damage to, draw 1 card.

### Card Renames (Java class, card ID, and localization keys synced)

- Accumulate → Unleash (流浪 in Chinese: 蓄势待发)
- HeavySmite → Heavy Smash (Chinese: 重砸)
- JudgmentForm → Execution Form (Chinese: 处刑形态)
- Wandering → Odyssey (Chinese: 漂泊)

### Text & Display Fixes

- The "Bleed" keyword description now reflects the current rule: at the start of its turn, lose HP equal to its stacks, then only 50% of the stacks are removed (DeepWound prevents removal this turn).
- The vanilla "Status" keyword now displays correctly in Simplified/Traditional Chinese: fixed the word-splitting issue "状态 牌" → "状态牌" (5 cards affected).
- Odyssey (漂泊): the Simplified Chinese description now correctly shows 3 Sin reduced instead of 1.
- The JudgmentFormPower display name was synced to the card's new name (English/Simplified Chinese).

---

### Help Translate Your Language

All translations other than Simplified Chinese were generated via an automated pipeline; speakers of any language are welcome to submit corrections (issue / PR). See `docs/LOCALIZATION_SPEC.md` for the translation spec and how to use the validation script.