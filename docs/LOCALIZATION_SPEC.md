# thetormented 多语言本地化规范

本规范固化多语言翻译的全部规则与结论，目的是让后续每种新语言的翻译**不再重新探索**（不重读源码、不重复研究机制、不重写校验逻辑），直接把成本压到「翻译本身 + 一条校验命令」。

---

## 1. 目录与加载机制

- 本地化根目录：`src/main/resources/thetormented/localization/<lang>/`
- 每种语言固定 9 个文件：`CardStrings` / `PowerStrings` / `RelicStrings` / `PotionStrings` / `UIStrings` / `CharacterStrings` / `OrbStrings` / `Keywords` / `EventStrings`（均 `.json`）
- 加载逻辑（`BasicMod.java`）：
  - `getLangString()` = `Settings.language.name().toLowerCase()` → 语言目录名必须是小写语言代码（`fin`/`dut`/`epo`/`zhs`/`deu`…）
  - 先加载 `eng` 兜底，再覆盖当前语言 → 缺字段会回退英文，不会崩
  - `Keywords.json` 单独以 UTF-8 读取并走自定义 `KeywordInfo` 注册（非 BaseMod 默认路径）
- 结构基准永远是 **eng**（新语言必须与 eng 逐 key 对齐）

## 2. 硬约束（由 `tools/validate_localization.ps1` 自动检查）

| 文件 | 约束 |
|---|---|
| CardStrings | key 集合与 eng 完全一致（78 卡含 ExampleCard）；`EXTENDED_DESCRIPTION` 数组长度逐卡一致；`UPGRADE_DESCRIPTION` **存在性**逐卡一致（如 `Implication`/`Entangled` 没有 UPGRADE，不要补） |
| PowerStrings | 25 key；`DESCRIPTIONS` 数组长度逐 key 一致（代码按 `DESCRIPTIONS[0] + amount + DESCRIPTIONS[1]` 拼接，分段边界必须保留） |
| RelicStrings | 15 key；`DESCRIPTIONS` 长度一致；`FLAVOR` 必须有 |
| PotionStrings | 3 key；`DESCRIPTIONS` 长度一致 |
| OrbStrings | `DESCRIPTION`（单数键！）数组长度一致 |
| UIStrings | `TEXT`/`EXTRA_TEXT` 长度一致；`TEXT_DICT` 的 key 与 value 均可翻译（dut 先例：`"Dit"`/`"Dit is een sleutel"`） |
| CharacterStrings | `NAMES` 长度 2、`TEXT` 长度 3 |
| Keywords | 恰 4 个条目，ID 固定为 `bleed`/`sin`/`debt`/`restriction`；每条须有 `PROPER_NAME`/`NAMES`/`DESCRIPTION` |
| 编码 | 全部 UTF-8；禁止 U+FFFD（�）乱码字符 |

## 3. 文本 token 规则（翻译时逐字对照）

| Token | 含义 | 处理 |
|---|---|---|
| `!D!` `!B!` `!M!` | 动态数值（伤害/格挡/魔法） | **原样保留** |
| `[E]` `[B]` | 能量图标 | **原样保留** |
| `NL` | 换行 | **原样保留** |
| `#y #g #b #r #p` | 颜色前缀 | 保留前缀，其后内容翻译（`#yVulnerable` → `#yHaavoittuva`） |
| `[#ffffff]…[]` | hex 色标记 | 保留标记，翻译内容 |
| `~…~` `@…@` | 波浪/震动样式 | 保留标记，翻译内容 |
| `${modID}:词` | 关键词 token（tooltip） | 译为 `${modID}:<译词>`；**译词小写后必须存在于 Keywords NAMES**（含格变形，见 §5） |
| `!${modID}:大写词!` | 动态变量 token（运行时数值） | **一律原样保留，不翻译**（白名单见 §4） |
| `*卡名` | 引用某张卡（如 `*Misery`） | 替换为 `*<该卡在 CardStrings 中的译名>`，必须与该卡 NAME 完全一致；**始终用 NAME 原形**，即使正文语法上更自然的是变形（dut/fin/epo 均如此，保证 tooltip 命中；epo 曾误用宾格 `*Mizerojn`，已修正） |

## 4. 动态变量 token 白名单（禁止翻译）

```
SIN  BLEED  BLOCK_THRESHOLD  TOTAL_BLOCK  CARD_ADD  TOTAL_DAMAGE  TOTAL_DRAW  HITS  TOTAL_ENERGY
```

来源：`BaseCard.java` 的 `QuickDynamicVariable` / `setCustomVar`（key 经 `makeID` 注册为 `thetormented:<KEY>`）。
出现形态示例：`!${modID}:SIN!`、`!${modID}:BLEED!`、`!${modID}:TOTAL_BLOCK!`、`!${modID}:BLOCK_THRESHOLD!`、`!${modID}:CARD_ADD!`、`!${modID}:TOTAL_DAMAGE!`、`!${modID}:TOTAL_DRAW!`、`!${modID}:HITS!`、`!${modID}:TOTAL_ENERGY!`。
注意：`!${modID}:BLEED!` / `!${modID}:SIN!` 是动态变量（数值），与关键词 token `${modID}:Bleed` / `${modID}:Sin` 不是一回事，不能互换。

## 5. 关键词机制（为什么 NAMES 必须含格变形）

- `KeywordInfo.prep()` 会把 NAMES 全部 `toLowerCase()`，随后经 BaseMod `addKeyword` 逐条注册为 `thetormented:<词>` 键
- 游戏词典按**精确词形**匹配：正文 `${modID}:X` 的显示文本就是 `X`，tooltip 按 `X.toLowerCase()` 查键
- **结论：正文里出现的每一个表面形式（屈折语的格/数变形）都必须收录进 NAMES**；`PROPER_NAME` 用词典形/主格
- 官方 STSL 芬兰语先例：`BLOCK` NAMES = `["suoja","suojaa","suojat","suojasi"]`、`ARTIFACT` = `["artefakti","artefaktia","artefaktilla"]`

## 6. 官方术语基准（翻译前必须核读）

- 官方 STSL 各语言 localization 解包目录：`C:\Users\Administrator\AppData\Local\Temp\opencode\basemod_extract\<lang>\*.json`（含 cards/powers/relics/keywords/orbs/ui 等）
- 若目标语言官方存在（如 fin），先抽取官方句式与名词再套用
- 官方芬兰语句式样例：
  - "Tee !D! vauriota."（Deal X damage.）
  - "Saa !B! Suojaa."（Gain X Block.）
  - "Nosta !M! korttia."（Draw X cards.）
  - "#yPassiivinen"（Passive）、"#yManaa"（Evoke）
  - "Kuluta"（Exhaust）、"Pidä"（Retain）、"Synnynnäinen"（Innate）、"Aineeton"（Ethereal）
  - "Vahvuus"（Strength）、"Näppäryys"（Dexterity）、"Heikko"（Weak）、"Haavoittuva"（Vulnerable）、"Hauras"（Frail）

## 7. 已定稿译词记录

| 语言 | Bleed | Sin | Debt | Restriction | Misery（状态卡） | Entangled（状态卡） |
|---|---|---|---|---|---|---|
| dut | Bloeden（bloeden/bloeding） | Zonde（zonde/zonden） | Schuld（schuld/schulden） | Beperking（beperking/beperkingen） | Ellende | Verstrikt |
| epo | Sango（sango/sangado/sangon） | Peko（peko/pekoj/pekojn） | Ŝuldo（?uldo/?uldoj，文件内为 mojibake） | Limigo（limigo/limigoj） | Mizero | Implikita |
| fin | Verenvuoto（verenvuoto/verenvuotoa/verenvuodon） | Synti（synti/syntiä） | Velka（velka/velkaa/velkaasi/velkasi） | Rajoitus（rajoitus） | Kurjuus | Kietoutunut |

- 注意：epo 的 `sangon`/`pekojn`（宾格）曾因未收录进 NAMES 导致 tooltip 失效，已补入 NAMES（`*` 卡名引用同理，须用 NAME 原形）

- fin 句式定稿："Tee !D! vauriota." / "Saa !B! Suojaa." / "Lisää !M! ${modID}:Verenvuotoa." / "Saa !M! ${modID}:Syntiä." / "jokainen ${modID}:Velka" / "Poista kaikki ${modID}:Synti."
- 动态变量 token（`!${modID}:SIN!` 等）与词汇 token（`${modID}:Sins`→"Syntiä"）在正文中并存，翻译时区分处理

## 8. 新增语言工作流

1. 新建 `<lang>/` 目录，从 eng 复制 9 文件为骨架
2. 核读官方 `<lang>` localization（§6），抽取句式与名词
3. 按 §2/§3/§4 翻译 9 个文件（卡名注意与 `*卡名` 引用一致）
4. 屈折语（如 fin）自检：正文所有 `${modID}:词` 表面形式已收录进 NAMES
5. 运行校验直到通过：
   ```powershell
   powershell -ExecutionPolicy Bypass -File tools\validate_localization.ps1 -Lang <lang>
   ```
6. 若出现警告（如 `*卡名` 未匹配），人工确认后处理

## 9. 已知问题

- **epo/Keywords.json**：非 ASCII 字符（ĉ/ĝ/ŝ/ĵ）被 `?` 替代（mojibake），如 `"?iu Sango estas forigita"`、`"esta?o"`、`"?uldo"`（应为 Ŝuldo）→ 待修复，需确认 epo 文件的编码约定后统一恢复
- **epo/QuenchedBlade** NAME 笔误：`"Eestingita Klingo"` → 应为 `"Estingita Klingo"`
- 已修复（校验脚本回归时发现）：
  - dut/epo FaceDanger 缺失 `UPGRADE_DESCRIPTION` → 已补
  - epo 关键词 NAMES 缺少宾格 `sangon`/`pekojn` → 已补
  - epo `*` 卡名引用用宾格（`*Mizerojn`/`*Mizeron`/`*Implikitan`）→ 已统一为 NAME 原形
- eng/PowerStrings `MangledFlesh` 描述已修正为 "6 Bleed"（历史记录，已处理）

## 10. 相关工具

- `tools/validate_localization.ps1`：结构/长度/key/token 覆盖/`*`卡名匹配/乱码/残留英文 全套校验；残留英文为启发式警告，可对误报语言在脚本内 `$nativeWords` 表追加本土词（如 dut 的 hand/Max）；`-Lang` 指定语言，`-BaseDir` 可覆盖本地化根目录（默认自动定位）
- 官方基准解包：`C:\Users\Administrator\AppData\Local\Temp\opencode\basemod_extract\`
