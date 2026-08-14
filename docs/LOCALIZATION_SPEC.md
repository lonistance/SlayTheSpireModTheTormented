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
| PowerStrings | 25 key；`DESCRIPTIONS` 数组长度逐 key 一致（代码按 `DESCRIPTIONS[0] + amount + DESCRIPTIONS[1]` 拼接，分段边界必须保留；`DebtPower` 目前为 **3 段**：`[0] + percent + [1] + SinPower.SIN_PER_DEBT + [2]`，`[1]`/`[2]` 为"被人工制品抵消时……同步降低 N 点原罪"句的左右两半，数字必须留给代码插入） |
| RelicStrings | 15 key；`DESCRIPTIONS` 长度一致；`FLAVOR` 必须有 |
| PotionStrings | 3 key；`DESCRIPTIONS` 长度一致 |
| OrbStrings | `DESCRIPTION`（单数键！）数组长度一致 |
| UIStrings | `TEXT`/`EXTRA_TEXT` 长度一致；`TEXT_DICT` 的 key 与 value 均可翻译（dut 先例：`"Dit"`/`"Dit is een sleutel"`） |
| CharacterStrings | `NAMES` 长度 2、`TEXT` 长度 3 |
| Keywords | 恰 4 个条目，ID 固定为 `bleed`/`sin`/`debt`/`restriction`；每条须有 `PROPER_NAME`/`NAMES`/`DESCRIPTION`；**debt 的 DESCRIPTION 必须含 `%%SIN_PER_DEBT%%` 标记**（注册时替换为 `SinPower.SIN_PER_DEBT`=5，缺失即 ERROR） |
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

关键词同样支持绑定运行时数值：`%%SIN_PER_DEBT%%` 标记（`BasicMod.registerKeyword` 注册时替换为 `SinPower.SIN_PER_DEBT`）。与 PowerStrings 的"分段+代码插入"不同，Keywords 的 `DESCRIPTION` 是单串，数字必须写成该标记。调整原罪↔血债折算比时**只改 `SinPower.SIN_PER_DEBT` 一处**，24 种语言的 DebtPower 与关键词文本全部自动同步。

## 5. 关键词机制（为什么 NAMES 必须含格变形）

- `KeywordInfo.prep()` 会把 NAMES 全部 `toLowerCase()`，随后经 BaseMod `addKeyword` 逐条注册为 `thetormented:<词>` 键
- 游戏词典按**精确词形**匹配：正文 `${modID}:X` 的显示文本就是 `X`，tooltip 按 `X.toLowerCase()` 查键
- **结论：正文里出现的每一个表面形式（屈折语的格/数变形）都必须收录进 NAMES**；`PROPER_NAME` 用词典形/主格
- 官方 STSL 芬兰语先例：`BLOCK` NAMES = `["suoja","suojaa","suojat","suojasi"]`、`ARTIFACT` = `["artefakti","artefaktia","artefaktilla"]`

## 6. 官方术语基准（翻译前必须核读）

- 官方 STSL 各语言 localization 解包目录：`C:\Users\Administrator\AppData\Local\Temp\opencode\basemod_extract\<lang>\*.json`（fin/epo 仅 cards/keywords/ui 三件）；其余语言完整 17 件套在 `C:\Users\Administrator\AppData\Local\Temp\opencode\localization\<lang>\`（含 jpn/jpn2 与 ptb/zht/kor/nor/pol/rus/spa/srb/srp/tha/tur/ukr/vie）
- 若目标语言官方存在，先抽取官方句式与名词再套用
- **官方 Artifact（人工制品）术语**（"被人工制品抵消时，每被抵消 1 点血债，同步降低 N 点原罪"句使用；提取自官方 `powers.json`）：eng Artifact / zhs 人工制品 / zht 人工製品 / deu Artefakt / dut Artefact / fin Artefakti / fra Artefact / gre Μαγική Ασπίδα / ind Artefak / ita Artefatto / jpn 人の作りし物 / kor 인공물 / nor Magimotstand / pol Hart / ptb Artefato / rus Артефакт / spa Artefacto / srb Zaštita / srp Заштита / tha อาคม / tur Yapı / ukr Артефакт / vie Thánh tích
- **日文特例**：官方有两版——新版 `localization/jpn2`（PC 版 `LocalizedStrings` 实际加载，旧 `jpn` 仅供 console 构建）。翻译必须参照 jpn2：`C:\Users\Administrator\AppData\Local\Temp\opencode\localization\jpn2\*.json`；但 mod 目录名仍必须用 `jpn`（`getLangString()` 返回 `Settings.language.name().toLowerCase()` = `jpn`），jpn2 只是参考基准
- 官方 jpn2 句式样例：
  - "!D! ダメージを与える。"（Deal X damage.）
  - "!B! ブロック を得る。"（Gain X Block.）
  - "カードを !M! 枚引く。"（Draw X cards.）
  - "敵全体に !D! ダメージを与える。"（Deal X damage to ALL enemies.）
  - "弱体 !M! を与える。"（Apply X Weak.）
  - "#y自動効果"（Passive）、"#y解放"（Evoke）
  - "廃棄"（Exhaust）、"保留"（Retain）、"天賦"（Innate）、"エセリアル"（Ethereal）
  - "筋力"（Strength）、"敏捷性"（Dexterity）、"弱体"（Weak）、"脱力"（Vulnerable）、"状態異常"（Status）、"呪い"（Curse）
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
| fra | Saignement（saignement） | Péché（péché/péchés） | Dette（dette/dettes） | Restriction（restriction/restrictions） | Misère | Enchevêtré |
| gre | Αιμορραγία（αιμορραγία） | Αμαρτία（αμαρτία/αμαρτίες） | Χρέος（χρέος/χρέη） | Περιορισμός（περιορισμό/περιορισμός，正文用宾格） | Μιζέρια | Μπερδεμένος |
| ind | Perdarahan（perdarahan） | Dosa（dosa） | Utang（utang） | Pembatasan（pembatasan） | Sengsara | Terjerat |
| ita | Sanguinamento（sanguinamento/sanguinamenti） | Peccato（peccato/peccati） | Debito（debito/debiti） | Restrizione（restrizione/restrizioni） | Miseria | Intricato |
| jpn | 出血 | 罪 | 負債 | 制限 | 悲惨 | もつれ |
| zht | 流血 | 原罪 | 血債 | 禁制 | 苦痛 | 糾纏 |
| kor | 출혈（출혈/출혈을/출혈이/출혈은/출혈로/출혈의/출혈만큼/출혈상태이면/출혈상태인） | 죄（죄/죄를/죄가/죄는/죄의） | 빚（빚/빚을/빚이/빚은/빚의/빚하나당/빚수만큼） | 제약（제약/제약을/제약이/제약은/제약의） | 고통 | 얽힘 |
| nor | Blødning（blødning/blødningen/blødninger） | Synd（synd/synd en/synder） | Gjeld（gjeld/gjelden） | Restriksjon（restriksjon/restriksjonen） | Elendighet | Floket |
| pol | Krwawienie（krwawienie/krwawienia/krwawieniu） | Grzech（grzech/grzechu/grzechy/grzechów） | Dług（dług/długu/długi） | Ograniczenie（ograniczenie） | Nędza | Splątanie |
| ptb | Sangramento（sangramento/sangramentos） | Pecado（pecado/pecados） | Dívida（dívida/dívidas） | Restrição（restrição/restrições） | Miséria | Emaranhado |
| rus | Кровотечение（кровотечение/кровотечения） | Грех（грех/грехи/грехов） | Долг（долг/долга） | Ограничение（ограничение） | Мучение | Спутанность |
| spa | Sangrado（sangrado/sangrados） | Pecado（pecado/pecados） | Deuda（deuda/deudas） | Restricción（restricción/restricciones） | Misería | Enredado |
| srp | Крварење（крварење/крварења/крварењу） | Грех（грех/греха/грехе） | Дуг（дуг/дуга/дугу） | Ограничење（ограничење） | Беда | Запетљан |
| srb | Krvarenje（krvarenje/krvarenja/krvarenju） | Greh（greh/greha/grehe） | Dug（dug/duga/dugu） | Ograničenje（ograničenje） | Beda | Zapetljan |
| tha | เลือดไหล | บาป | หนี้ | ข้อจำกัด | ความทุกข์ทรมาน | พันยุ่ง |
| tur | Kanama（kanama/kanaması/kanamaya） | Günah（günah/günahlar/günahı） | Borç（borç/borcun/borcunu） | Kısıtlama（kısıtlama） | Sefalet | Dolaşık |
| ukr | Кровотеча（кровотеча/кровотечу/кровотечі/кровотечею） | Гріх（гріх/гріхи/гріхів） | Борг（борг/боргу/борги） | Обмеження（обмеження） | Страждання | Заплутаність |
| vie | Chảy Máu | Tội Lỗi | Nợ | Hạn Chế | Khốn Khổ | Rối Rắm |

- 注意：epo 的 `sangon`/`pekojn`（宾格）曾因未收录进 NAMES 导致 tooltip 失效，已补入 NAMES（`*` 卡名引用同理，须用 NAME 原形）

- fin 句式定稿："Tee !D! vauriota." / "Saa !B! Suojaa." / "Lisää !M! ${modID}:Verenvuotoa." / "Saa !M! ${modID}:Syntiä." / "jokainen ${modID}:Velka" / "Poista kaikki ${modID}:Synti."
- 动态变量 token（`!${modID}:SIN!` 等）与词汇 token（`${modID}:Sins`→"Syntiä"）在正文中并存，翻译时区分处理
- ita 句式定稿（沿用官方意大利语）："Infligge !D! danni." / "Ottieni !B! Blocco." / "Pesca !M! carte."；"Esaurita"/"Conservata"/"Innata"/"#yEterea"；HP 用 "PF"、"PF massimi"；Status 卡 = "carta di Stato"；官方词：#yDebolezza（Weak）/ #yVulnerabile（Vulnerable）/ #yForza（Strength）/ #yDestrezza（Dexterity）/ #yFragilità（Frail）；复数 Peccati 已收入 NAMES
- jpn 句式定稿（沿用官方 jpn2）："!D! ダメージを与える。" / "!B! ブロック を得る。" / "カードを !M! 枚引く。" / "敵全体に !D! ダメージを与える。"；"廃棄"/"保留"/"天賦"/"エセリアル"/"#y自動効果"/"#y解放"；官方词：弱体（Weak）/ 脱力（Vulnerable）/ 筋力（Strength）/ 敏捷性（Dexterity）/ 状態異常（Status）/ 呪い（Curse）；主角名定稿"苛まれし者"
- jpn token 规则：${modID}: 关键词 token 后必须跟空格、不与助词连写（"あなたの ${modID}:負債 に等しい"），与 jpn2 官方空格风格一致；日文无屈折变形，NAMES 单形即可；`*悲惨`（Misery）/`*もつれ`（Entangled）星引用与 NAME 完全一致
- 角色名定稿：zht 受折磨者 / kor 수형자 / nor Den Plagede / pol Dręczony / ptb O Atormentado / rus Истерзанный / spa El Atormentado / srp Мучени / srb Mučeni / tha ผู้ถูกทรมาน / tur Azap Çeken / ukr Змучений / vie Kẻ Khổ Đau
- kor 规则：正文中 ${modID}: 关键词 token 与助词**空格分隔**（"출혈 을"），NAMES 另收粘连形（출혈을/빚하나당 等）保证任何写法命中
- srp/srb 提醒：官方约定 **srp=西里尔、srb=拉丁**（同一塞尔维亚语两种文字，术语必须一致：Крварење/Krvarenje、Грех/Greh、Дуг/Dug、Ограничење/Ograničenje、Беда/Beda、Запетљан/Zapetljan），勿互换
- vie 提醒：关键词可多词（Chảy Máu），校验器已支持空格分词 token 与 NAMES 截断回退；文本用 NFC 预组合变音符（官方同款）
- spa 提醒：官方 VULNERABLE 词形就是 "Vulnerable"（撞英文启发式词表），已入 `$nativeWords`；nor/pol/tur/tha/rus/ukr/ptb 等无撞词，无需白名单

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

- `tools/validate_localization.ps1`：结构/长度/key/token 覆盖/`*`卡名匹配/乱码/残留英文 全套校验；残留英文为启发式警告，可对误报语言在脚本内 `$nativeWords` 表追加本土词（如 dut 的 hand/Max、fra 的 combat/max）；`-Lang` 指定语言，`-BaseDir` 可覆盖本地化根目录（默认自动定位）
- 工具字符集说明：脚本源文件为 UTF-8 无 BOM，PowerShell 5.1 按 ANSI 读取；正则中所有非 ASCII 字符一律用 `\u00XX` 转义（如 `[A-Za-z\u00C0-\u00FF]` 覆盖法文重音字母、`\u0370-\u03FF` 覆盖希腊字母），禁止直接写入 literal 重音字符，否则会报 "range in reverse order"
- ita 结论：字符集已被 `\u00C0-\u00FF` 覆盖，无需改工具；无英文撞词，`$nativeWords` 无需新增 ita 条目
- jpn 结论：`kwTokenRe`/`starRe` 字符类已加入 `\u3040-\u30FF`（假名）与 `\u4E00-\u9FFF`（汉字）（坚持 `\uXXXX` 转义，遵守 PowerShell 5.1 ANSI 读取）；`dynRe` 不变（动态 key 恒为拉丁）；日文正文无英文撞词，"HP" 未误报，`$nativeWords` 无需新增 jpn 条目；正则改动对非日文语言无副作用（已回归 fra/gre/ind/fin/dut/epo/ita 全 PASS）
- 全语言结论（23 语言批次）：字符类现覆盖 Latin `A-Za-z\u00C0-\u024F\u0300-\u036F`（含法德意西葡波挪土越全变音符与组合音标）、希腊 `\u0370-\u03FF`、西里尔 `\u0400-\u04FF`（rus/ukr/srp/srb）、泰 `\u0E00-\u0E7F`、谚文 `\u1100-\u11FF\uAC00-\uD7AF`、假名 `\u3040-\u30FF`、汉字 `\u4E00-\u9FFF`、越南预组合 `\u1E00-\u1EFF`；keyword/star token 支持空格分词（多词关键词如 vie "Chảy Máu"），覆盖检查带 NAMES 最长前缀截断回退（仅多词场景生效，单词行为不变）；`$nativeWords` 现有 dut(hand/Max)/fra(combat/max)/ind(Status)/spa(Vulnerable)；zhs（7 错）与 deu（1 错+Bleed 描述历史问题）为既有失败，与本批无关
- 人工制品抵消句批次：`DebtPower` 全部 24 语言扩为 3 段、debt 关键词全部追加 `%%SIN_PER_DEBT%%` 句；`SIN_PER_DEBT` 常量从 `UpdateSinAction`/`UpdateDebtAction` 的两个私有副本收敛为 `SinPower.SIN_PER_DEBT`（单点）；校验器新增 debt 关键词标记强制检查；24 语言校验回归：22 语言 PASS，zhs/deu 仍为既有失败（与本批无关）
- 旧伤复发（`Relapse`/`DeepWoundPower`）批次：`DeepWoundPower` 语义由"流血只减少 50%"改为"回合开始时**保留全部流血**"（`BleedPower.atStartOfTurn` 不再 ReducePower，有 DeepWound 即不移除、也不减少）；24 语言 `DeepWoundPower` 描述已同步改为"…不会被移除"（eng：`Bleed is not removed at the start of its turn.`；zhs：`回合开始时，流血 不会被移除。`）；24 语言校验回归：22 语言 PASS，zhs/deu 仍为既有失败（与本批无关）
- 旧伤复发（50% 保留）批次（本批）：`DeepWoundPower` 语义回调为"回合开始时只移除一半流血"——新增可调常量 `DeepWoundPower.BLEED_RETAIN_PERCENT = 50`（单点调整，后续改数值只动这一处），`BleedPower.atStartOfTurn` 有 DeepWound 时按 `amount - amount*BLEED_RETAIN_PERCENT/100` ReducePower；24 语言 `DeepWoundPower` 描述扩为 2 段（`DESCRIPTIONS[0] + BLEED_RETAIN_PERCENT + DESCRIPTIONS[1]`，运行时拼接，仿 `DebtPower` 的 `SIN_PER_DEBT` 模式），eng：`Bleed is reduced by 50% at the start of its turn.`；zhs：`回合开始时，流血 只移除 50%。`；24 语言 `Relapse` 卡牌描述同步（eng：`An enemy's Bleed is reduced by 50%...`/zhs：`…只会被移除 50%`，数值 50% 已写入卡面文本，若改常量需同步卡面文案）；24 语言校验回归：22 语言 PASS，zhs/deu 仍为既有失败（与本批无关）
- 本批 13 新语言（ptb/zht/kor/nor/pol/rus/spa/srb/srp/tha/tur/ukr/vie）全部 PASS 0/0，官方句式为基准；角色名与 6 关键词译词见 §7
- 官方基准解包：`C:\Users\Administrator\AppData\Local\Temp\opencode\basemod_extract\`
