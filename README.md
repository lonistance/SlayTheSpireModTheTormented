# The Tormented

> A Slay the Spire character mod — blood, guilt, and a covenant with the dark.

**The Tormented** adds a new playable character of the same name, built on the [BasicMod](https://github.com/Alchyr/BasicMod) template.

[中文说明](README.zh-CN.md)

## Disclaimer

This mod is a **work in progress** (testing phase). Please keep the following in mind:

- Some cards, relics, and potions have **not been fully tested** and may contain bugs.
- Card numbers and mechanics are **subject to change** based on actual gameplay experience and feedback.
- All translations except Simplified Chinese **lack human proofreading** and may contain errors or awkward wording.
- Art assets have **inconsistent styles** across different sources.
- This is a **personal hobby project** developed by one person. The code structure may not follow strict conventions, and there is **no guarantee of timely bug fixes**.

Your understanding and patience are greatly appreciated. If you encounter any issues, feel free to report them via issues or feedback.

## Story

Once the tribe's mightiest blade, he hewed the right arm from an ancient in single combat. For that insolence, the god laid a curse upon him: wherever his feet should fall, war would kindle, and only by draining his enemies' blood to the dregs could he buy a brief respite from his guilt. To shatter that doom, he struck a covenant with Neow, the goddess of renewal, and set his course toward the Spire, there to tear out the very root of the blight.

## Features

- New playable character **The Tormented** (3 Energy, 80 Max HP)
- **77 cards** — 4 basic / 19 common / 36 uncommon / 16 rare / 2 special
- **15 relics**, including the starter *Cursed Broken Blade* (replaceable with *Hero's Longsword*)
- **3 potions**, **25 powers**
- **4 custom keywords**: Bleed, Sin, Debt, Restriction
- **2 status cards**: Misery, Entangled

### Starting setup

- Deck: 4× Strike, 1× Rebel, 4× Defend, 1× Forgive
- Relic: Cursed Broken Blade

## Core mechanics

| Keyword | Effect |
|---|---|
| **Bleed** | At the start of the creature's turn, it loses HP equal to its Bleed stacks, then all Bleed is removed. Unblocked attack damage adds 1 Bleed. |
| **Sin** | Every 5 Sin you have grants 1 Debt. |
| **Debt** | Each stack of Debt makes you take 10% more damage. |
| **Restriction** | You cannot gain Energy unless you fully remove all of your Debt. Removed at the end of your turn. |

Cards weave these together: gain Sin, convert it into Debt, spend or purge Debt for powerful effects, stack and detonate Bleed, and generate the Misery status card.

## Card breakdown

| Rarity | Attack | Skill | Power | Curse/Status | Total |
|---|---|---|---|---|---|
| Basic | 2 | 2 | – | – | 4 |
| Common | 10 | 9 | – | – | 19 |
| Uncommon | 13 | 15 | 8 | – | 36 |
| Rare | 4 | 7 | 5 | – | 16 |
| Special | – | – | – | 2 | 2 |

## Localization

Six languages are bundled and chosen automatically by the game language setting:

| Language | Code | Status |
|---|---|---|
| English | eng | Base (reference) |
| 简体中文 | zhs | Complete; minor consistency issues pending |
| Deutsch | deu | Complete; minor consistency issues pending |
| Nederlands | dut | Validated, passes all checks |
| Esperanto | epo | Validated, passes all checks |
| Suomi | fin | Validated, passes all checks |

Run the validation tool for any language:

```powershell
powershell -ExecutionPolicy Bypass -File tools\validate_localization.ps1 -Lang <code>
```

See `docs/LOCALIZATION_SPEC.md` for the full translation rules, token conventions, and known issues.

## Build & Run

Requirements: JDK 8, Maven, a Steam copy of Slay the Spire, and [ModTheSpire](https://github.com/kiooeht/ModTheSpire), [BaseMod](https://github.com/daviscook477/BaseMod) and [StSLib](https://github.com/kiooeht/StSLib) subscribed on the Steam Workshop.

1. Set `steam.windows` in `pom.xml` to your Steam install path.
2. Build and auto-install into the Steam mods folder:

   ```
   mvn package
   ```

3. Launch the game via **ModTheSpire** and enable **The Tormented**.

## Project structure

```
src/main/java/thetormented/
├── actions/      # Card and power actions
├── cards/        # 77 cards (basic / common / uncommon / rare / special)
├── character/    # The Tormented character class
├── potions/      # 3 potions
├── powers/       # 25 powers
├── relics/       # 15 relics
└── util/         # KeywordInfo and helpers
src/main/resources/
├── ModTheSpire.json
└── thetormented/
    ├── audio/        # Sound assets
    ├── images/       # Card, relic, power and character art
    └── localization/ # eng / zhs / deu / dut / epo / fin
docs/                 # Localization spec
tools/                # Localization validator
```

## Credits

- Based on the [BasicMod](https://github.com/Alchyr/BasicMod) modding template
- Thanks to the Slay the Spire modding community
