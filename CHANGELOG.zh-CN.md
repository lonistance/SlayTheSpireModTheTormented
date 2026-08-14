# 更新日志

## v1.0.0（2026-08-14）— 正式发布：从数值微调滚成一次全项目重做

### 前言——这一版是怎么来的

这一版本来只是一次例行平衡调整：改几个数值、修几个"小" Bug。
然而每修好一个 Bug，就会牵出另一个隐藏更深的 Bug；每调一个数值，又会冒出新的设计想法——
小修小补不知不觉滚成了几乎把整个项目从头到尾重写一遍的重做。版本号因此直接从 0.0.x 跳到
**1.0.0**，不再伪装成"初步可玩版"。

与 `main` 分支上一版（0.0.0）的完整对比：

- **60 个 Java 路径有改动**——59 个重写 + 1 个新增（`patches/BetaArtUnlockPatch.java`）
- **41 张卡牌**受影响：13 张纯数值、23 张机制/文本、4 张改名、4 张调整稀有度
- **全部 7 个能力**、**4 个动作类**、**3 个遗物**均有修改
- 本地化：**6 → 24 种官方语言**
- 美术：全部卡图重绘/放大增强并 pngquant 压缩（jar 48 MB → 16 MB），另新增
  **160 张测试画风（cards_test）资源**
- 框架新特性：升级文本引擎修复、测试画风热切换、Beta 画风开关解锁

### 1. 卡牌改名（4 张，类名 / ID / 本地化键全部同步）

| 旧 | 新 |
|---|---|
| Wandering（漂泊） | Odyssey |
| JudgmentForm | ExecutionForm（处刑形态） |
| HeavySmite（重砸） | HeavySmash |
| Accumulate | Unleash（蓄势待发） |

### 2. 稀有度调整（4 张）

| 卡牌 | 旧 → 新 |
|---|---|
| 悲恸 Grief | 罕见 → **稀有** |
| 血仇 BloodFeud | 稀有 → **罕见** |
| 无罪恳求 PleaOfInnocence | 普通 → **罕见** |
| 怜悯 Mercy | 罕见 → **普通** |

### 3. 纯数值调整（13 张）

| 卡牌 | 旧 → 新 |
|---|---|
| 苦痛 Agony | 伤害 7（+3）→ **7（+1）= 8**；升级 虚弱/易伤 1 → **2** |
| 残酷折磨 CruelTorture | 伤害 15（+5）；获得原罪 5 → **7**（升级数值与文案对齐） |
| 淬火刀刃 QuenchedBlade | 伤害 7（+3）→ **7（+1）= 8**；升级卡牌 2 → **4 张** |
| 纠缠不放 RelentlessEntanglement | 伤害 4（+1）→ **5（+2）= 7** |
| 鲜血援护 BloodGuard | 格挡 6 → **5**；施加流血 6 → **5**（升级均 +2） |
| 坚定 Firm | 格挡 9 → **10**（+3 = 13） |
| 不避艰险 FaceDanger | 伤害 15 → **16**；升级加成 +5 → **+4**（升级后仍 20） |
| 大出血 MassiveBleeding | 伤害 6 → **4** |
| 淬炼之剑 TemperedSword | 伤害 8 → **9**；每张状态牌加成 +2 → **+3**（升级 +4） |
| 血流不止 RelentlessBleed | 流血 4（+2）→ **3（+1）** |
| 血海 BloodSea | 施加流血 5 → **3** |
| 招架 Parry | 格挡 5 → **6**（+3 = 9） |
| 无罪恳求 PleaOfInnocence | 格挡 5 → **7**（+2 = 9）；稀有度 普通 → 罕见 |

### 4. 机制与文本调整（23 张）

- **赎罪之击 AtonementStrike**：抽牌之外，额外**降低 4 点原罪**。
- **净土 SacredLand**：格挡 6（+3→+2）=8、抽 2（+1）不变；新增——**若你身负血债，降低 7 点原罪**。
- **株连 Implication**：完整重做——旧版先打目标再对每个流血敌人补打；新版——
  **消耗手牌中 1 张牌**、对**所有敌人**造成 8（+3=11）伤害、**每命中一个流血敌人抽 1 张牌**；
  目标由单体改为全体。
- **沉重过往 HeavyPast**：重做——15（+5）单段 → **6（+1=7）× 2 段**；苦痛进抽牌堆+弃牌堆不变；
  新增——**抽 1 张牌（升级抽 2）**。
- **污浊之乱 ChaosDirty**：伤害 5 → 6（升级修正为 +1，血债加成 3 → 4）；**伤害预览与实际结算
  一致**（正确计入本回合原罪转血债，除非被人工制品阻挡）。
- **虚空呼唤 VoidCall**：**不再先手**；升级不再多抽——改为**移除消耗**（选择的牌变为虚无）。
- **血浴 Bloodbath**：旧升级（magic 1→2）根本不生效；升级改为**费用 2 → 1**。
- **血渍 Bloodstain**：施加 4 流血；基础版**带消耗**，升级**移除消耗**（原为 +1 流血）。
- **沸腾 Boiling**：流血 4 → **5**（+1=6）；改为每消耗一张牌对**随机一个敌人**施加（原为全体）；
  **不再消耗自身**（修复了给自己叠流血的真 Bug）。
- **破碎甲胄 BrokenArmor**：格挡 16（+0）→ **15（+5=20）**；自身负面**虚弱 2（升级1）→ 脆弱 1**。
- **缔结契约 FormPact**：生成的苦痛改为进入**弃牌堆**（原为手牌）。
- **祭奠 Memorial**：完整重做——旧"记忆格挡"版本（发布前已回滚）→ **0 费：抽 1 张牌；
  若抽到技能牌，降低 3 点原罪（升级 5）**。
- **赦免 Pardon**：完整重做——将你手牌中**其他所有牌**变为随机本模组卡牌（本牌升级则生成升级版
  本；永远不会变成自身；排除无休战火、处刑形态、状态牌与诅咒牌）；**打出 3 次后自身变为随机
  能力牌**；不再消耗自身；升级不改变 3 次触发条件。
- **虔诚 Devotion**：**现在同时移除脆弱**（原仅移除血债/易伤/虚弱）。
- **纠缠（诅咒）Entangled**：新增 `triggerOnExhaust`——**被消耗时向抽牌堆添加一张粘液**。
- **血仇 BloodFeud**：机制变更——旧版完整触发流血 2（升级 3）次；新版通过百分比动作类
  **一次性触发流血值的 75%（升级 150%）**。
- **悲恸 Grief**：稀有度 → 稀有；每次抽牌施加流血 2（+1=3）→ **4（+1=5）**；随机状态池不变。
- **怜悯 Mercy**：基础费用 0 → **1**，升级降至 **0**；**以太效果升级后保留**（原被移除）。
- **试炼 Trial**：自身虚弱 2（升级 1）→ **1**；力量/敏捷 1（+1）不变。
- **过度僵硬 Overrigid**：获得力量 4 → **5**；惩罚不变。
- **无休战火 UnceasingWar**：**击杀爪牙（MinionPower）不再累积永久段数**；仅当打出该牌时目标
  仍存活才计数。
- **恐惧记忆 DreadMemory**：原升级完全不生效（数值从未变化）；现改为**费用 2 → 1**。
- **救赎之道 RedemptionPath**：升级不再增加抽牌——改为**费用 1 → 0**，且 0 费升级后不再被
  Bug 顶回 1。
- **文本规范**：涉及文案的卡牌描述结尾不再带句号，避免关键词识别问题。

### 5. 能力（7 个）

- **饥饿战意 HungeringBattleWill**：删除条件自伤（仅无敌人死亡时）；改为每回合结束**受到 3 点
  NORMAL 可格挡伤害**（原为直接失去生命）；每回合开始获得 [E] 不变。
- **激愤 Indignation**：每回合**任意类型的第一张牌** 0 费（原仅限非攻击牌）。
- **血肉撕裂 MangledFlesh**：触发阈值 5 → **6 层流血**；触发伤害改为**对所有敌人**（原仅
  流血敌人本身）。
- **流血 BleedPower**：每回合**只移除 50% 流血**（原全部移除）；**深创（DeepWound）现在完全
  阻止移除**（修复与血瘾（Relapse）长期不兼容的问题）；关键词文本同步更新。
- **血债 DebtPower**：描述补充"每 1 点被人工制品阻挡的血债，降低 5 点原罪"。
- **原罪 SinPower**：新增公共常量 `SIN_PER_DEBT = 5`——血债/原罪转换动作、污浊之乱预览与
  关键词文本统一引用此单一来源。
- **过度僵硬 OverrigidPower**：修复双重削减——惩罚只减 **baseDamage**，不再同时减当前回合伤害。

### 6. 动作与战斗机制（4 个）

- **TriggerBleedAction**：`ticks` → `percent`（每个敌人一次性结算流血百分比，>0 才结算）。
- **UnceasingWarKillAction**：新增 `countKill` 标志——只有打出时仍存活、且非爪牙的敌人计入击杀。
- **UpdateDebtAction / UpdateSinAction**：仅重构——统一引用 `SinPower.SIN_PER_DEBT`（转换逻辑不变）。

### 7. 遗物（3 个）

- **黑石提灯 BlackstoneLantern、沉重脚镣 HeavyFetters**：补上 `getUpdatedDescription()` 覆写——
  此前游戏内描述一直为空。
- **石像鬼之臂 GargoyleArm**：纯格式修正。

### 8. 框架与渲染

- **升级文本引擎修复（本轮最关键）**：`BaseCard.initializeDescription()` 原先只在手牌分支重算
  `rawDescription`，导致图鉴、弹窗预览、牌组视图解析到陈旧文本；现在解析前无条件重算正确的
  基础/升级文本，且 `BaseCard.upgradeName()` 在**每次升级后强制重解析**——升级描述终于在图鉴
  预览、牌组、战斗、篝火各处正确显示。
- **测试画风管线**：新增 `getPortraitImage()` 覆写加载 `_p.png` 大图；`update()` 在游戏中途
  切换"测试玩家画风"时热切换卡图；`refreshJokePortrait()` 对接原版玩笑立绘（Beta 画风）渲染路径。
- **TextureLoader**：`getCardTestTextureString()` 解析 `cards_test/<类型>/<卡名>.png` 并做
  空安全回退（修复无测试图的卡牌——如苦痛状态牌——直接 NPE 崩溃）。
- **BasicMod**：关键词注册支持 `%%SIN_PER_DEBT%%` 动态替换。

### 9. 新功能——Beta 画风（测试玩家画风）完整支持

- `patches/BetaArtUnlockPatch.java` 强制解锁卡牌弹窗的 Beta 画风勾选框——原版此开关被成就与
  基础色系卡牌门控，本模组卡牌永远不满足条件。
- 随包附带 160 张测试画风资源；在弹窗中逐卡切换，渲染期即时生效，无需重载游戏。

### 10. 本地化——6 → 24 种官方语言

- 此前：deu、dut、eng、epo、fin、zhs。此后：**官方全部 24 种语言**（新增 fra、gre、ind、
  ita、jpn、kor、nor、pol、ptb、rus、spa、srb、srp、tha、tur、ukr、vie、zht）。
- `docs/LOCALIZATION_SPEC.md` 记录了翻译规范；`tools/validate_localization.ps1` 对每种语言
  校验键对齐、EXT/UPGRADE 一致性与关键词覆盖。
- 简体中文为人工校对；其余语言由自动化管线生成——欢迎通过 issue/PR 提交修正。
- 显示修复："状态 牌"分词问题（zhs/zht，5 张卡）、德语 FaceDanger 缺失升级描述、
  HungeringBattleWill DESCRIPTIONS 类型崩溃（必须为数组）、漂泊（Odyssey）原罪数值文本。

### 11. 美术与体积

- 全部 160 张卡图以 AI 放大/精修替换；角色立绘、卡背、能量球、篝火贴图一并打磨。
- pngquant 压缩：卡图体积降至 1/3–1/4，jar **48 MB → 16 MB**。
- 额外 160 张测试画风资源（约 12 MB 下载量）。

### 12. 文档与工具链

- README.md / README.zh-CN.md 重写；双语 CHANGELOG（本文件）；本地化规范；
  构建与校验脚本已入库。

### 13. Bug 修复档案——现象 · 根因 · 修复 · 验证

每条均按四段式撰写，附精确复现路径与出错的代码位置。所有修复都对照已部署的
`mods/thetormented.jar` 在游戏内验证；文中引用的原版机制均通过反编译原版字节码
（`javap` 反汇编 `AbstractCard` / `SingleCardViewPopup` / `UnlockTracker`）确认，无一猜测。

**#1 升级后的卡牌文本不显示（上）——`initializeDescription()` 解析的是陈旧文本**
- **现象**：升级后的卡牌（如赦免）在图鉴、弹窗升级预览、牌组视图中仍显示**基础版**描述，
  只有战斗中显示正确。复现：图鉴 → 悬停赦免 → 切换升级预览。
- **根因**：`BaseCard.initializeDescription()` 覆写只在 `isCardInHand()` 分支内重算
  `rawDescription`；其余场景直接落进 `super.initializeDescription()`，解析的是 `rawDescription`
  里残留的旧文本。更要命的是 `this.rawDescription = base;` 写在**解析之后**——正确文本永远晚到一步。
- **修复**：解析前无条件按 `baseDescription()` 重算 `rawDescription`（升级态取
  `UPGRADE_DESCRIPTION`，手牌中再叠加注入段），解析后再重置回纯净基础文本。一处修改，
  图鉴/弹窗/牌组/战斗/篝火全场景生效。

**#2 升级后的卡牌文本不显示（下）——升级后从来没人重新解析**
- **现象**：修完 #1 后，弹窗升级预览仍显示基础文本——弹窗复制的卡牌从未被重新解析。
- **根因**（两层，均经字节码确认）：(a) 原版 `upgradeName()` **不调用** `initializeDescription()`——
  它只做 `timesUpgraded++`、`upgraded = true`、名字加 "+"、`initializeTitle()`；(b)
  `SingleCardViewPopup.render()` 的预览流程是 `card = card.makeStatEquivalentCopy();
  card.upgrade(); card.displayUpgrades();`，随后直接渲染卡牌**已解析的** `description` token，
  而整个弹窗类里根本没有任何 `initializeDescription()` 调用；`makeStatEquivalentCopy()` 只在
  `timesUpgraded > 0` 时循环调 `upgrade()`，未升级卡的副本全靠 `upgrade() → upgradeName()`，
  于是从不重解析。此外，覆写了 `upgrade()` 且**不调 super** 的卡（赦免、血瘾、禁忌、恐惧记忆、
  血渍…）连 `BaseCard.upgrade()` 尾部的重解析也一并绕过了。
- **修复**：`BaseCard.upgradeName()` 覆写——`super` 之后强制 `initializeDescription()`。
  由于所有自定义 `upgrade()` 都调用 `upgradeName()`，这一处兜底覆盖全部卡牌。
  验证：预览开关、牌组里的升级卡、战斗内与篝火升级均显示升级文本。

**#3 赦免升级描述在 24 种语言中不完整**
- **现象**：简体中文升级版仍是"将所有手牌变化为随机牌…变化为随机能力牌"——第二句漏译
  "升级过的"；图鉴预览还显示过"打出 2 次"。
- **根因**：本地化数据里 `UPGRADE_DESCRIPTION` 第二句从未按新措辞更新；早前一次批量处理还在
  `!M!` 两侧留了空格（"打出 !M! 次后"），`-1` 的魔法值升级也与"固定 3 次"规则冲突。
- **修复**：一次脚本化批量修复 24 个语言文件（升级句改为"…变化为随机升级过的能力牌"，
  `TrimEnd` 防双标点）；zhs/zht 去掉 `!M!` 空格；`Pardon.upgrade()` 简化为只 `upgradeName()`
  （3 次触发条件不变）。验证：24 语言生成文件逐一预览核对。

**#4 卡牌弹窗的 Beta 画风开关永远不出现**
- **现象**：本模组任何卡牌都没有"测试画风"勾选框。
- **根因**：反编译 `SingleCardViewPopup.canToggleBetaArt()` =
  `UnlockTracker.isAchievementUnlocked("THE_ENDING") || switch (card.color)`——
  RED→RUBY_PLUS、GREEN→EMERALD_PLUS、BLUE→SAPPHIRE_PLUS、PURPLE→AMETHYST_PLUS、
  **default→false**。模组色卡永远不满足条件；除非通关真结局，否则原版四色都不解锁。
- **修复**：新增 `patches/BetaArtUnlockPatch.java`（对 `canToggleBetaArt` 前缀注入）
  直接 `SpireReturn.Return(true)`。注意：本 MTS 3.30.3 没有 `SpirePrefix` 类，采用
  `RestrictionPower` 已有的 `SpirePrefixPatch` 写法。验证：勾选框出现，逐卡切换即时生效。

**#5 无测试画风的卡牌直接 NPE 崩溃**
- **现象**：进游戏/生成卡牌即崩溃，
  `NullPointerException at TextureLoader.loadTexture(124) ← getTextureNull(73) ←
  BaseCard.refreshJokePortrait(197) ← <init> ← Misery`。
- **根因**：`refreshJokePortrait()` 无条件对 `getCardTestTextureString()` 的结果调
  `getTextureNull`；苦痛（状态牌）没有 `cards_test/status/Misery.png`，路径为 null →
  `getTextureNull(null)` → `new Texture(null)` 抛异常。
- **修复**：null 守卫——无测试图时回退正常卡图。验证：苦痛等无图卡正常加载；
  抽查 jar 内确实不存在该资源。

**#6 过度僵硬的惩罚被扣两次**
- **现象**：挂着过度僵硬（Overrigid）打攻击牌，伤害比预期低——惩罚好像算了两次。
- **根因**：`OverrigidPower.onAfterCardPlayed()` 同时执行
  `card.baseDamage = max(0, baseDamage - amount)` **和**
  `card.damage = max(0, damage - amount)`——当前回合的伤害（由 baseDamage 推导而来）
  被二次削减。
- **修复**：只保留永久性的 `baseDamage` 削减，`damage` 字段自动跟随。
  验证：战斗内伤害 = baseDamage − 惩罚。

**#7 血瘾/旧伤复发后流血仍被移除一半**
- **现象**：对敌人打出血瘾（Relapse）/施加旧伤复发（DeepWound）后，文案说"本回合流血不移除"，
  实际回合开始时仍被移除 50%。
- **根因**：`BleedPower.atStartOfTurn()` 的 DeepWound 分支还残留早期"保留 50%"规则
  （`ReducePowerAction` 减 `ceil(amount*0.5)`），机制早已改为"完全不移除"却从未更新；
  而无 DeepWound 的分支移除**全部**流血，同样过时。
- **修复**：统一规则——无旧伤复发：每回合恰好移除 `100 − BLEED_RETAIN_PERCENT`（50%）；
  有旧伤复发：完全不移除。新增常量 `BLEED_RETAIN_PERCENT = 50`（便于后续调参）。
  验证：有旧伤复发时层数纹丝不动，无时每回合减半。

**#8 沸腾给自己叠流血**
- **现象**：打出沸腾（Boiling）后**玩家自己**多了流血。
- **根因**：`use()` 执行时卡牌还在手牌；消耗扫描条件（`c.type != CardType.ATTACK`）把自身
  （技能牌）也扫了进去——沸腾既消耗了自己，又把自己计入了倍率。
- **修复**：从消耗目标中排除自身（后续顺带重做：改为每消耗一张牌对**随机一个敌人**施加流血）。
  验证：不再自伤、不再自耗。

**#9 处刑形态升级完全无收益（25 → 25）**
- **现象**：升级处刑形态（原审判形态）什么都没有变。
- **根因**：常量原样写着 `BASE_THRESHOLD = 25; UPG_THRESHOLD = 25;`，旁边注释
  `// 25% -> 50%`——设计意图（50）从未写进常量，升级又施加了 25。
- **修复**：基础 50、升级 75。验证：升级后按剩余生命 75% 触发斩杀。

**#10 救赎之道升级后的 0 费被顶回 1**
- **现象**：升级救赎之道（RedemptionPath）后费用又变回 1。
- **根因**：减费写在 `upgrade()` 里手工处理，而 `BaseCard.upgrade()` 的费用分支（当
  `isCostModified && cost < baseCost` 时）会按 `cost + (costUpgrade − baseCost)` 重算费用，
  把手动减出的 0 覆盖回 1。
- **修复**：改为声明式 `setCostUpgrade(0)`——费用分支从 `baseCost` 出发调整，0 费稳定保留，
  即使费用被外部效果（瓶装/遗物改费）修改过也不回弹。验证：升级后保持 0 费。

**#11 遗物描述在游戏内为空**
- **现象**：黑石提灯（BlackstoneLantern）、沉重脚镣（HeavyFetters）没有任何描述文字。
- **根因**：原版 `AbstractRelic.getUpdatedDescription()` 默认返回空串、`updateDescription()`
  是空方法——描述只在构造函数里赋一次值。这两个遗物忘了覆写。
- **修复**：覆写返回 `DESCRIPTIONS[0]`。验证：两个遗物描述正常显示。

**#12 污浊之乱——升级数值不生效，且预览与结算不符**
- **现象**：(a) 升级后的污浊之乱（ChaosDirty）伤害与血债加成仍是基础值（文案写 7 / +4）；
  (b) 手牌预览总伤害与实际打出的伤害不一致。
- **根因**：(a) 升级参数没有接入预览链路——`applyPowers()`/`calculateCardDamage()` 临时加
  `getDebtDamageBonus()` 再还原 base，缺了 `damageUpgrade`/`magicUpgrade` 就悄悄消失；
  (b) 预览只算**当前**血债，没有计入本回合原罪→血债的转换，且人工制品阻挡时行为不同。
- **修复**：补齐真实升级数值（伤害 6 → 7，血债加成 3 → 4），预览按实际结算口径重写
  （区分有无人工制品）。验证：预览 = 实际伤害。

**#13 无休战火把击杀爪牙算进永久段数**
- **现象**：击杀爪牙（如地精领袖的小怪）也会永久增加无休战火（UnceasingWar）的段数。
- **根因**：击杀检测对一切 `isDeadOrEscaped()` 目标计数，没排除带 `MinionPower` 的爪牙。
- **修复**：`UnceasingWarKillAction` 增加 `countKill` 标志；卡牌传入
  `!m.isDeadOrEscaped() && !m.hasPower(MinionPower)`，且仅当打出时目标仍存活才计数。
  验证：击杀爪牙不再累积段数。

**#14 启动崩溃："Expected BEGIN_ARRAY but was STRING"**
- **现象**：某轮本地化后游戏无法启动。
- **根因**：`HungeringBattleWillPower.DESCRIPTIONS` 被写成了纯字符串；basemod 要求数组形式，
  JSON 解析直接中止。
- **修复**：还原为数组形式；校验脚本新增规则**拒绝**字符串型 `DESCRIPTIONS`，杜绝复发。
  验证：正常启动；校验器能抓出错误形态。

**#15 小型文本/显示修复**
- 简繁中文"状态 牌"被分词拆开（波及 5 张卡）：换行分词器把关键词拆断，调整措辞使词组完整。
- 漂泊（Odyssey）简中描述显示"1 原罪"而非 3。
- 审判形态能力名在卡牌改名后不同步 → 同步为处刑形态（ExecutionFormPower）。
- 德语不避艰险（FaceDanger）整体缺失升级描述。
- 涉及文案的卡牌描述结尾不再带句号（关键词识别健壮性）。

**#16 血仇的触发流血结算口径统一**
- **现象**：血仇（BloodFeud）"触发流血 N 次"的伤害量不直观，且难以调参。
- **根因**：动作类循环 `ticks` 次完整流血值的 HP_LOSS 伤害——"次数"模型与新的
  "每回合保留 50%"流血经济不协调。
- **修复**：`TriggerBleedAction` 改为按 `percent` 一次性结算 `amount * percent / 100`
  （血仇 75%，升级 150%），并加 `> 0` 守卫。验证：伤害与描述百分比一致。

---

### 帮助翻译你的语言

除简体中文外的翻译均由自动化管线生成；欢迎任何语言的玩家提交修正（issue / PR）。
翻译规范与校验脚本用法见 `docs/LOCALIZATION_SPEC.md`。