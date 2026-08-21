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

**#17 赦免变化卡牌——原手牌从未真正移除，只是被藏起来**
- **现象**：打出赦免（Pardon）后，原手牌其实还留在手牌里，被缩到 12%（不可见的"幽灵牌"），
  而替换牌则被**重复加入**——先原位替换一次，飞入特效的构造函数又追加一次——手牌布局
  错乱，出现"本应临时移除的牌仍在手牌中、只是看不见"的现象；第 3 次打出的能力牌变换
  同样重复。
- **根因**：实现只加了官方 `ExhaustCardEffect`（动画）与 `shrink()`，再用
  `hand.group.set(idx, replacement)` 占位，而反编译字节码显示 `ShowCardAndAddToHandEffect`
  构造函数内部会调用 `hand.addToHand`——两张替换牌同时进手牌。另外照抄官方消耗路径
  （`moveToExhaustPile`）不行：它会触发全部 `onExhaust` 钩子（如枯树枝），与"只播动画
  不真消耗"的需求矛盾。
- **修复**：原牌改为 `hand.removeCard(c)` 真正移出手牌（纯列表移除、零钩子），只叠加官方
  `ExhaustCardEffect` 做燃烧质感，**不触发任何真实消耗**；替换牌改用官方
  `MakeTempCardInHandAction`（与枯树枝 `new MakeTempCardInHandAction(
  AbstractDungeon.returnTrulyRandomCardInCombat().makeCopy(), false)` 同一写法）——内部会
  标记已见、复制一张并播放飞入手牌动画，还处理手牌已满的边界；第 3 次变换分支只保留
  `this.exhaust = true`，由官方收尾流程（`UseCardAction` → `moveToExhaustPile`）完成真正的
  消耗（自带动画与钩子），不再手动占位。3 张排除牌（赦免 / 无休战火 / 处刑形态）保持不变。
  验证：无重复牌、无幽灵牌；被移除的牌不触发任何消耗钩子；工程编译通过。

**#18 暴乱 Rebel——图鉴里无论开关如何都显示 Beta 画风**
- **现象**：在卡牌图鉴中，暴乱（Rebel）始终渲染测试画风（cards_test）插画而不是正式插画，
  即使 Beta 画风开关已关闭；其余模组卡都正常显示正式插画。
- **根因**：**并非模组代码 Bug**。按卡持久化的 Beta 偏好 `UnlockTracker.betaCardPref`
  （文件 `preferences/STSBetaCardPreference`）里遗留了 `"thetormented:Rebel": "true"`
  （其余卡全是 `false`；`.backUp` 文件证实上一个值就是 `false`）。
  `AbstractCard.render()` 每帧读取该偏好，为 true 时绘制 `jokePortrait`（本模组
  `BaseCard.refreshJokePortrait()` 将其指向 cards_test 贴图），因此在偏好翻回 false 之前，
  图鉴缩略图不可能显示正式图。
- **修复**：将持久化偏好条目重置为 `"false"`（已确认游戏已关闭；原文件另存为 `.backUp2`）。
  弹出预览里点 Beta 图标按钮写入的就是同一个键，游戏内也可复现并复原该修复。
- **验证**：偏好文件现为 `"thetormented:Rebel": "false"`；在偏好为 false 且
  `PLAYTESTER_ART_MODE` 关闭时，`render()` 走 `renderPortrait` 分支，显示正式插画。



**#19 卡牌弹窗大图：之前使用了错误的做法，这是补救错误的一次尝试**
- **症状**：开启测试（cards_test）画风时，卡牌弹窗的大图把 250×190 的小卡图拉伸成左上角裁切的残影；点升级预览或测试画风开关时结果不稳定（显示正式大图还是残影取决于操作顺序）。
- **根因**：上一版 `LoadPortraitImg` 前缀把 250×190 的小卡图塞进了大图槽位 `portraitImg`，而 `renderPortrait()` 始终用硬编码源矩形 (0,0,500,380) 绘制它，小图被裁切拉伸成残影；且非 null 的 portraitImg 会让 Basemod 的官方兜底失效（`OpenFix$OpenTextureFix` 仅在 `portraitImg == null` 时生效）。Basemod 的 `UpgradeChangesPortraitPatch$ToggleUpgrade`（插入在 `updateUpgradePreview` 的 `isViewingUpgrade` 访问点、每帧执行）还会把 portraitImg 换成官方大图而无视 beta 状态，导致升级预览每帧都把测试画风覆盖回正式插画。上一轮修复走了错误的方向，这里记录并予以补救。
- **修复**：portraitImg 现在只放 500×380 的 `_p` 大图：beta 开启时加载 `cards_test/<类型>/<卡名>_p.png`（缺失则交给原版/Basemod 官方流程兜底）；beta 关闭时完全不干预（删掉了错误的小图覆盖做法）。新增 `update()` 每帧兜底：只要 Tormented 卡需要 beta 大图、且当前 portraitImg 不是我们放置的 beta 纹理，就销毁并换回 beta `_p` 纹理，升级预览的覆盖不再能把测试画风改回正式画风；`close()` 重置跟踪引用。纹理身份比对保证无人替换时每帧零开销。
- **验证**：javac 编译通过；无任何纹理被双重销毁（谁替换字段谁负责销毁旧值，与原版/Basemod 语义一致）。

**#20 平衡调整批次：七张卡重新调校（Shackles / RelentlessBleed / MangledFlesh / Grief / Relapse / BrokenArmor / SacredLand）**
- **Shackles**（现为罕见 UNCOMMON，移入 `uncommon\skill`）：手牌数判定并不可靠——`use()` 在卡牌离开手牌之前执行，实际边界与文本描述不一致；判定已改为显式（卡牌仍在手牌时 `hand.size() >= 10`），补牌数与总格挡预览遵循同一条规则。升级后每张状态牌提供的格挡由 6 提高到 8。
- **RelentlessBleed**：基础 4 → 6，升级 6 → 8。
- **MangledFlesh**：基础 5 → 7，升级 7 → 9。
- **Grief**：基础流血 4 → 3（升级 3 → 4）。
- **Relapse**：现在先对其目标施加 3 层流血，再施加「流血不会被移除」效果。
- **BrokenArmor**（重做，现为 1 费普通 COMMON，移入 `common\skill`）：新定位——获得 10 点（升级 14 点）格挡，每打出一次少 2 点（最低 0），每场战斗重置。规避了原版 `ModifyBlockAction` 的坑（其负值钳制写到 `baseDamage` 而非 `baseBlock`），改用普通字段记录打出次数；新增补丁（`CombatStartPatch`）在每场战斗开始时清零；预览通过动态变量 `!${modID}:BLOCK!` 实时显示有效格挡。
- **SacredLand**：去掉了反直觉的条件分支（「无血债抽 2 张，否则失去 7 点原罪」）；现在每有 1 点血债就少抽 1 张（最低 0 张；基础 2、升级 3），并通过 `EXTENDED_DESCRIPTION` 实时显示有效抽牌数。
- **本地化**：Relapse / BrokenArmor / SacredLand 的文本在全部 24 种语言中更新（流血令牌取自各语言 Keywords.json）；新增的 `BLOCK` 动态变量已加入 `validate_localization.ps1` 白名单；全语言校验通过。

**#21 Rebel 的 EXTENDED_DESCRIPTION 从未生效；ReconcileFate 把一次批量生成的 Misery 只算成 1 张**
- **现象**：(1) Rebel 的附文「施加 !M! 流血」即使在手牌中也不显示；(2) 一次生成多张 Misery 的卡（Shackles 加 2 张、HeavyPast 抽牌堆+弃牌堆各 1 张、Relief 加 2 张），每批只触发 1 次 ReconcileFate。
- **根因**：(1) 手牌实时注入的骨架（`BaseCard.initializeDescription()` / `getInjectedDescription()`）早已就绪，但 Rebel 从未重写 `getInjectedDescription()`；(2) 触发钩子挂在 `MakeTempCardIn*Action.makeNewCard` / `update` 上，无法承载「逐张」语义：手牌动作的批量由私有 `addToHand` 对 `amount` 字段的 table-switch 驱动（手牌满的溢出分支会整批跳过），单个回调里拿不到真实张数。
- **修复**：(1) Rebel 按 SacredLand 同款方式重写 `getInjectedDescription()`，并在 `applyPowers()` / `calculateCardDamage()` 中把 `magicNumber` 同步为纸面伤害，`!M!` 显示精确的流血量；(2) 钩子移到 `ShowCardAndAddTo{Hand,Discard,DrawPile}Effect` 的构造器上——每张生成的卡恰好对应一个 effect，一次生成 N 张就恰好触发 N 次，溢出/批量/循环的内部细节一概不影响正确性（8 个构造器重载全部覆盖）。
- **验证**：javac 全量编译通过（174 个类）；onExhaust 计数路径未改动。

**#22 混沌污化（ChaosDirty）每打出一次基础伤害就变高**
- **现象**：卡面基础伤害随每次打出永久增长（每完成一次原罪→血债转化 +1），「造成 !D! 伤害」的数字一路走高，而不是稳定在 6（升级 7）。
- **根因**：`applyPowers()` / `calculateCardDamage()` 覆写把血债加成临时写入 `this.baseDamage`（事后还原）。这条还原并不安全：原版 `AbstractCard` 对应逻辑会在污染值存活期间执行 `this.damage = this.baseDamage`，伤害字段带着「基础+加成」残留到下一次重算；而混沌污化每打出一次都会把原罪永久转化为更多血债，于是面板基础伤害随打出次数一路抬升。该字段污染还与 `BaseCard` 自身的 `TOTAL_DAMAGE`（VariableType.DAMAGE）变量链互相纠缠——该变量的计算也会临时改写 `baseDamage` 并递归进入这些覆写。
- **修复**：删除两个覆写；血债加成改由 `TOTAL_DAMAGE` 变量的 preCalc 承载（VariableType.DAMAGE 链在 `BaseCard` 的临时环境中完成「基础+加成+力量/易伤」结算并还原全部字段）。卡面 D 恢复纯净基础值；含血债加成的总伤害仍通过 EXT 附文的 `!${modID}:TOTAL_DAMAGE!` 实时显示。
- **验证**：javac 全量编译通过（174 个类）；结算时 `PollutedChaosDamageAction` 读取的 `card.baseDamage` 不再被污染。

**#23 措辞与重做批次：Relapse 文案、Relapse/BrokenArmor 本地化修正、BrokenArmor 重做（#20 后续）、ChaosDirty 预览修复（#22 后续）、仓库级规则**
- **Relapse 措辞**：24 语言 `DESCRIPTION`/`UPGRADE_DESCRIPTION` 两句全部重写——「流血不会被移除」从句由「本回合」改为「一回合内」（eng：`An enemy's <Bleed> won't be removed for one turn.`／升级 `ALL enemies' ...`；zhs：`一名敌人身上的 ... 不会在一回合内移除`）；eng 顺手修正 `A enemy's` → `An enemy's`。"施加 3 层流血"前缀中既有的错误流血令牌一并修正（kor `출혈를` → `출혈을`、vie `Chảy` → `Chảy máu`）——这两个令牌原先不被 NAMES 覆盖，`validate_localization.ps1` 早已会报错，随本批修复。
- **BrokenArmor 重做（取代 #20 的衰减机制）**：不再施加脆弱，也不再经 `playsPlayed` 计次数；`use()` 先正常获得格挡（`GainBlockAction(p, p, this.block)`），随后直接 `this.baseBlock = Math.max(0, this.baseBlock - 2)`（每次打出减 2，不为负）。新增 `initialBaseBlock`（`upgrade()` 时同步）替代计数，`CombatStartPatch` 改为战斗开始时把 `baseBlock` 复位为 `initialBaseBlock`，衰减依旧每场战斗重置。`BLOCK` 动态变量已删除，卡面 `!B!` 直接显示当前（已衰减的）格挡。24 语言 `BrokenArmor` 文案全部重写：删除「施加 1 层脆弱」句与 `!${modID}:BLOCK!` 令牌（改用原生 `!B!`），并按语言归一化措辞（如 `（en este combate）` 等全角括号修正为半角 `(...)`），示例 eng：`"Gain !B! Block. NL Each play grants 2 less Block (resets each combat)."`。
- **ChaosDirty 预览修复（取代 #22 方案）**：`TOTAL_DAMAGE` 由 `VariableType.DAMAGE` 链改走纯算术变量（`VariableType.MAGIC` 默认分支）；其 preCalc（`基础 + 当前血债 × 魔法数值`）全程不碰 `baseDamage`/`damage`，卡面 D 保持纯净（6／升级 7），!M! 显示真实升级后的魔法数值。删除了"预测本次打出会转化多少血债"的加成逻辑——预览口径统一为"当前血债 × 魔法"，各状态均符合预期：未升级总计 12（6+2×3）、升级总计 15（7+2×4）、弃牌堆中血债为 4 时 23（7+4×4）。结算伤害（`PollutedChaosDamageAction`）未改动。
- **仓库级规则**：仓库根新增 `AGENTS.md`，固化为强制流程——每次修改（代码/文案/文档/工具脚本）完成后必须同步更新 `CHANGELOG.md` 与 `CHANGELOG.zh-CN.md`（新条目插在末尾 `---` 之前，编号续用），并记录编译/本地化/编码约定。`docs/LOCALIZATION_SPEC.md` 新增全局规则：任何卡牌描述中已注册的关键词 token（`${modID}:X`）必须紧跟一个空格（原 §7 的 jpn 先例提升为全局约束），§10 附本批记录。
- **验证**：javac 全量编译通过（174 个类，EXIT=0）；24 语言全部通过 `validate_localization.ps1`（kor/vie 的令牌错误已随本批修复）；zhs/deu 的残留英文为既有启发式警告，与本批无关。

**#24 Shackles 重做（#20 后续）与 ReconcileFatePower 触发重做**
- **Shackles 重做（取代 #20 的「满手只补 1 张」规则）**：无论手牌是否满都**始终加入 2 张苦痛**（`MakeTempCardInHandAction(new Misery(), 2)`），与「依然是添加 2 张 Misery」的设计一致；只有「格挡计算」在满手时按有效落位计数：打出时手牌满 10 张（`use()` 内 `p.hand.size()` 仍含本卡）按 2 张中有效加入的 1 张计算格挡，否则按 2 张。删除了此前「满手时 `CARD_ADD` 显示 1」的变量变换，`!CARD_ADD!` 恒显示 2；`TOTAL_BLOCK` 预览 preCalc 与 `use()` 对齐（手牌状态数 + 有效加张数：满手 +1／否则 +2）× 魔法值，预览与实际格挡保持同步。
- **ReconcileFatePower 触发重做（修复重复触发）**：旧实现按 effect 构造器调用次数触发，而同一张生成的卡可能被多个 effect 实例包装（如随机落位抽牌 effect 在 `update()` 里同时 new 了 6 参与 3 参两个构造器），导致一次生成触发多次：HeavyPast 生成 2 张实际触发 3 次、Shackles 生成 2 张实际触发 4 次。现改为**按卡实例去重**：`onMiseryCardCreated` 对每张 Misery 实例仅触发一次（静态 `Set<AbstractCard> firedCards` 判重），无论一张卡被几个 effect 实例包装，生成 N 张恰好触发 N 次。集合在 `CombatStartPatch`（与 BrokenArmor 复位同点）每场战斗开始清空，跨战斗的同实例仍可重新计数。耗尽触发不受影响。
- **验证**：javac 全量编译通过（EXIT=0）。文案零改动（`!CARD_ADD!` 仍显示 2），本地化无需变动。

**#25 CursedBrokenBlade / HeroLongsword：原罪从第 2 回合起每回合施加**
- **机制**：两件遗物不再于每场战斗的第 1 回合开始时施加原罪；自第 2 回合起每回合开始时照常施加 3 点原罪。实现：新增 `firstTurnSkipped` 标志（每场战斗第一次 `atTurnStart()` 调用跳过，后续调用正常施加），战斗开始时复位——`CursedBrokenBlade.atBattleStartPreDraw()` 现顺带复位该标志，`HeroLongsword` 新增仅复位标志的 `atBattleStartPreDraw()`。目的：避免原罪开局即雪球式积累导致暴毙，给玩家启动时间与容错机会。
- **本地化**：两件遗物 `DESCRIPTIONS[0]` 中最后的 ` NL ` 之后的“每回合开始获得 X 原罪”句在全部 24 语言改写为“从第 2 回合起每回合开始获得”（示例 eng：`At the start of each turn from the 2nd turn onward, gain #b`；zhs：`从第 2 回合起，每回合开始时获得 #b`）。`DESCRIPTIONS[1]` 及其它条目不动。
- **验证**：javac 全量编译通过（EXIT=0）；`validate_localization.ps1` 24 语言全部 PASS（eng 的 630 条与 zhs 的 3 条残留英文为既有启发式警告，含 `RelicID` 模板条目）。

**#26 ChaosDirty：删除总伤预览 EXT（取代 #22/#23 的 TOTAL_DAMAGE 方案）**
- **问题**：`!${modID}:TOTAL_DAMAGE!` 总伤预览在游戏内依旧不准——#22（VariableType.DAMAGE 链）与 #23（纯算术 MAGIC 变量：`基础 + 当前血债 × 魔法`）之后仍不正确；实际伤害结算（`PollutedChaosDamageAction`，等待本次打出引发的原罪→血债转化后再计算）是对的。
- **修复（采纳用户选项：删除预览）**：`ChaosDirty.java` 删除 `TOTAL_DAMAGE` 自定义变量注册、`getInjectedDescription()` 覆写与不再使用的 `getDebtDamageBonus()` 辅助方法；卡面仅保留基础描述。24 语言的 `ChaosDirty` `EXTENDED_DESCRIPTION` 全部置空（对齐空数组，EXT/UPGRADE 对齐校验仍通过）。其它自注册同名 `TOTAL_DAMAGE` 变量的卡（TemperedSword、Riot、MassiveBleeding）不受影响。
- **验证**：javac 全量编译通过（EXIT=0）；`validate_localization.ps1` 24 语言全部 PASS（警告与基线一致）。

**#27 VoidCall：被附加“虚无”的卡牌描述中显示“虚无”关键词**
- **问题**：VoidCall 给卡牌设置 `isEthereal = true` 后，卡面没有任何提示（原版行为：运行期附加的虚无不会出现在卡面描述上），玩家难以辨认。
- **修复（未改动任何卡牌类）**：新增 `thetormented/patches/EtherealMarkerPatch.java`——以 `WeakHashMap` 记录被 VoidCall 附加的卡实例（身份语义，卡被 GC 后自动清理；`VoidCallAction` 设置 `isEthereal = true` 后立即调用 `markEthereal()` 记录），并以 `AbstractCard.initializeDescription()`（总入口，内部会转发中文分支）postfix 钩子，在被标记且 ethereal 的卡每次重建描述时追加一行当前语言的关键词名（经 `languagePack.getKeywordString("ethereal").ETHEREAL.NAMES[0]` 获取，兜底 "Ethereal"），如 zhs 显示「虚无。」。由于直接取原版关键词表本地化名，无需新增任何 24 语文案；每次重建只追加一行、天然无累积，未打标的卡完全不受影响。
- **修订（同条目）**：初版使用 ModTheSpire 的 `SpireField` 注入，在启动期首个 `AbstractCard` 构造时即 NPE（构造器内 `initializeDescription` 执行时注入字段引用尚未初始化，见启动崩溃日志）；改为上述纯 Java 弱引用身份映射，重新编译通过、启动崩溃消除。
- **验证**：javac 全量编译通过（EXIT=0）。本地化零改动。

**#28 HeavyPast：升级不再增加抽牌数（升级改为伤害 +2）**
- **机制**：升级前为「抽牌 1 -> 2、伤害 6 -> 7」；现在升级伤害 +2（6 -> 8，`UPG_DAMAGE = 2`），抽牌保持 1（`DRAW_UPG` 改为 `0`，`setMagic(DRAW_BASE, 0)`）。两次伤害均按升级后数值结算（2 x 8 = 16 总伤）；Misery 进场（抽牌堆 1 + 弃牌堆 1）不变。
- **本地化**：零改动——24 语言两种描述本就用动态 `!M!` token，「Draw !M! cards.」升级后自动显示 1。
- **验证**：javac 全量编译通过（EXIT=0）。等待用户进游戏确认实际效果后本条才算定稿。

**#29 Pardon 打出次数附文 / Indignation 首张牌 0 费显示 / VoidCall 修复补刀 / Shackles 残留 CARD_ADD 清理**
- **Pardon**：新增动态变量 `!${modID}:PLAYS!`（VariableType.MAGIC，preCalc 读实例 `plays` 字段）与 `getInjectedDescription()` → `EXTENDED_DESCRIPTION[0]` 附文「(此牌已打出 N 次)」，24 语言均已新增该条 EXT 文案。手牌中悬停实时显示累计打出次数，每次 `applyPowers` 刷新；`validate_localization.ps1` 白名单与 `docs/LOCALIZATION_SPEC.md` §4 同步加入 `PLAYS`。
- **Indignation（激愤）**：24 语言 Power 描述去掉「非攻击」限定（代码本就对任意类型的首张牌免费），与卡牌描述「第 1 张牌」口径一致；新增视觉预告——`IndignationPower.atStartOfTurn` 把当前所有手牌临时显示为 0 费（`costForTurn = 0` + `isCostModifiedForTurn = true`；字节码确认引擎在 `applyStartOfTurnPowers` 之前已对手牌执行 `resetAttributes`，故不会被清掉）；打出第 1 张牌后（`onPlayCard`）恢复其余手牌实际费用显示（`costForTurn = c.cost` 并清标志，第一张本身仍免费）；若整回合未打出任何牌，`atEndOfTurn` 统一恢复所有手牌。
- **VoidCall（#27 第二次修订，补充首次修复的盲区）**：初版补丁（标记 + `initializeDescription` postfix）只在**本 mod `BaseCard`** 的 `applyPowers`/`calculateCardDamage` 覆写路径（且 `getInjectedDescription()` 非空）触发描述重建——原版及其他 mod 的卡永远不重建，「虚无」行始终不出现。修复：`VoidCallAction` 在 `markEthereal(c)` 后显式调用 `c.initializeDescription()`（幂等，postfix 恰好追加一行），关键词行立即常驻。
- **Shackles（#24 补漏）**：23 种语言仍残留已删除的 `!${modID}:CARD_ADD!` 模板（仅 zhs 是 #24 新文案），本次在 `DESCRIPTION`/`UPGRADE_DESCRIPTION` 中统一替换为「2」（与始终添加 2 张的语义一致）；`CARD_ADD` 从 `validate_localization.ps1` 白名单及 `LOCALIZATION_SPEC.md` §4 移除。
- **验证**：javac 全量编译通过（EXIT=0，174 类）；`validate_localization.ps1` 24 语言全部 PASS（警告与基线一致）。等待用户重新打包进游戏确认。

**#30 RelentlessEntanglement：原版洗牌动画窗口期恢复丢失——隐蔽 bug，已知局限，视为正常现象**
- **我们发现了这个隐蔽的 bug**：当增加 Sin 导致增加 Debt、而此刻恰好发生了洗牌事件时，由于原版游戏播放完动画才会将牌从弃牌堆移动到抽牌堆，此时 RelentlessEntanglement 既不在弃牌堆也不在抽牌堆——尽管触发了增加 Debt 的监听，但卡牌没有被恢复。
- **成因涉及原版游戏就存在、且至今没有修复的 bug**：原版 `ShuffleVfx` 启动时立即清空弃牌堆并把卡牌交给动画，动画播完才将卡牌落位到抽牌堆；窗口期内卡牌“悬空”，不属于任何正常牌组——一切基于位置的恢复手段（`DiscardToHandAction`、以及我们基于牌组遍历的通知）都无法命中它。而在动画进行中移动卡牌（vfx 仍持有其引用时拖回手牌）会破坏动画收尾、造成卡牌重复，因此从单张卡侧无法安全绕过。
- **修复尝试（没有成功）**：把排队执行的 `DiscardToHandAction` 替换为自定义 `RecoverToHandAction`——轮询等待洗牌落位：卡在弃牌堆则照常拉回；洗牌动画已将其送入抽牌堆则从抽牌堆拉回；动画窗口期内绝不触碰卡牌。该尝试覆盖了“监听触发时卡仍在弃牌堆”的时序，但**没有彻底成功**——当增加 Debt 的触发恰落在动画窗口内（卡已悬空、通知遍历根本不会命中它）时，恢复依旧丢失，与修复前一致。
- **结论**：考虑到这张牌本身的数值已经合格，且触发频率相对较低，不会产生显著影响，请玩家将其视为正常现象。本地化零改动；javac 全量编译通过（EXIT=0）。

**#31 VoidCall 两处修复：retain 卡附加虚无后不消耗；虚无关键词行超出卡面**
- **问题 1（retain 卡不消耗）**：VoidCall 对拥有保留词条的卡（Firm、升级后的 Loan）附加虚无后，回合结束时该卡仍留在手牌、没有被消耗。根因是**原版固有行为**：回复节 `DiscardAtEndOfTurnAction` 先把 retain/selfRetain 卡移入 limbo 暂存（稍后 `RestoreRetainedCardsAction` 放回手牌），而耗虚钩子 `AbstractCard.triggerOnEndOfPlayerTurn` 只遍历手牌克隆——limbo 中的卡永远不会被遍历到、永不消耗。原版不存在 adopt「保留+虚无」组合卡，故从未暴露；且「保留」与虚无（回合末消失）语义本就相悖。
- **修复 1**：`VoidCallAction` 附加虚无时同步清除 `retain`/`selfRetain` 字段——卡失去保留词条后正常走耗虚流程（`DiscardAtEndOfTurnAction` 中虚无卡的 `ExhaustSpecificCardAction` 入队先于丢弃动作执行，不会误入弃牌堆）。Firm/升级 Loan 等任意保留牌被 VoidCall 选中时，虚无现在真实生效：回合结束时消耗。
- **问题 2（虚无行超出卡面）**：「虚无。」关键词行被推到卡面左侧屏幕外（视觉上像混入了多余空格/tab）。根因：补丁用 `new DescriptionLine(text, 1000.0f)`，而原版渲染以 `card.x − line.width × drawScale / 2` 定位每行左端——1000f 的假宽度把该行推到卡面外。
- **修复 2**：`EtherealMarkerPatch` 改用原版同款测量：`new GlyphLayout(FontHelper.cardDescFont_N, text).width`（AbstractCard 的静态 `gl` 是 private 不能复用，故自行构造，字体在初始化时 scale=1.0，与原版 initializeDescription 的测量口径一致）。
- **验证**：javac 全量编译通过（EXIT=0）。文案零改动（无需 validate）。等待用户重新打包进游戏确认：Firm/升级 Loan 被 VoidCall 附加后回合结束正确消耗；虚无行紧贴描述末行显示在卡面内。

**#32 v1.0.1 正式补丁发布**
- **内容**：本版替换了部分遗物贴图，修复了 Beta art 的卡牌预览（cards_test）问题，复查了所有卡牌的实现并修复已知卡牌 bug，并对部分卡牌进行了调整与重做。
- **本版累积改动（#20-#31）**：遗物贴图替换；Beta art 卡牌大图预览修复（LoadPortraitImg 重做、cards_test/<类型>/_p.png 规则、逐帧兜底，#19）；全卡牌复查与既有 bug 修复（VoidCall #27/#29/#31、ChaosDirty #22/#23/#26、Rebel #21、ReconcileFate #21/#24、RelentlessEntanglement #30 已知局限如实记录、Indignation #29、Pardon #29、Shackles #20/#24/#29、BrokenArmor #20/#23、SacredLand #20、HeavyPast #28、Relapse #23，及遗物重做 CursedBrokenBlade/HeroLongsword #25）；24 语言全量校验。
- **技术**：pom.xml 版本号 1.0.0 -> 1.0.1；jar 基于全新编译重建（javac EXIT=0，151 个源文件），并修复了 ModTheSpire.json 描述字段的编码损坏（GBK 误写导致的右单引号与中文标点乱码）。
- **验证**：javac 全量编译 EXIT=0；validate_localization.ps1 24 语言全部 PASS。
**#33 HeroLongsword 崩溃修复：拾取遗物后 ConcurrentModificationException**
- **现象**：拾取 HeroLongsword（落位动画结束后）游戏崩溃，`java.util.ConcurrentModificationException` 抛自 `OverlayMenu.update(OverlayMenu.java:69)`（`ArrayList$Itr.next`）。
- **根因（字节码验证）**：`OverlayMenu.update()` 每帧用 for-each 迭代 `AbstractDungeon.player.relics` 并调用 `r.update()`（源码第 69 行；字节码 PC 82-119）。新获得遗物的"落位"恰好发生在这个循环内部：原版 `AbstractRelic.update()` 检测到 `isAnimating` 飞行到达 `targetX/targetY` 后置 `isDone = true` 并调用 `onEquip()`（字节码 PC 376-437）。我们的 `HeroLongsword.onEquip()` 调用了 `AbstractDungeon.player.loseRelic(CursedBrokenBlade.ID)`，把该遗物从正在迭代的同一列表中结构性移除——下一次 `Iterator.next()` 即抛 CME。（原版 `loseRelic` 自身是安全的迭代模式：先扫描、循环结束后才 remove；崩溃由外层 OverlayMenu 的迭代器引发。）
- **修复**：`HeroLongsword.onEquip()` 不再同步移除遗物——开局打击/防御的升级仍在 `onEquip` 内立即执行；`loseRelic(CursedBrokenBlade.ID)` 改为并入 `AbstractDungeon.effectList` 的一次性特效（0.05 秒）递延执行：特效在下一帧更新，彼时对 `player.relics` 已无存活迭代器，碎刃照常移除。
- **验证**：javac 全量编译 EXIT=0（151 个源文件）。文案零改动（无需 validate）。等待玩家进游戏确认：Boss 遗物屏拾取 HeroLongsword 落位后碎刃被移除、游戏不再崩溃。
**#34 CharacterStrings 重写：角色简介、Spire Heart 攻击台词、Vampires 首句逐字对齐（24 语言）**
- **内容**：24 种语言的 `CharacterStrings.json` 三条 TEXT 全部重写。
  - **TEXT[0]（选人界面简介）**：由原来的单句口号扩写为 4 句背景故事（素材取自 Steam 描述）：被预言"灾厄之子"的战士、行处战争与死亡相随的不灭诅咒、为终结降世灾祸立约攀塔的旅程、"原罪/血债/流血"机制（机制名逐语言对齐各 PowerStrings.json 的现译，如 罪/血债/流血、Sin/Debt/Bleed、Péché/Dette/Saignement 等），收尾"这一次不为荣耀，而是为了赎罪"。
  - **TEXT[1]（Spire Heart 攻击台词，`getSpireHeartText`）**：全新赎罪台词——杀死心脏即可完成契约、诅咒随之解除，"这是唯一的赎罪之路"，末句"你举起了手中的剑"（各语言用其第二人称，如 vie "Ngươi giơ thanh kiếm trong tay lên."）。
  - **TEXT[2]（Vampires 问候，`getVampireText`）**：逐字复制原版 `Vampires` 事件 `DESCRIPTIONS[0]`（自 desktop-1.0.jar 按语言提取）——事件将正确定向称呼男性角色（如 zhs "加入我们，兄弟"）；此前 zhs 文件里残留的是英文原文。
- **技术**：译稿先落为 TSV 对照表，再由 ASCII-only 的 PowerShell 脚本统一写回；JSON 保持仓库既有格式（CRLF、2 空格缩进、UTF-8 无 BOM、键与 NAMES 原样保留）；TEXT[2] 从 jar 提取内容程序化复制，保证逐字节一致（24/24 比对通过）。
- **后续修复（启动崩溃）**：初版应用脚本把 24 个文件的 JSON 顶层键全部写坏为 `"$"`（PowerShell 字符串插值吃掉了 `${modID}` 后缀）且清空了 NAMES，导致游戏启动即崩溃——`Tormented.getNames(Tormented.java:45)` 处 NPE（`getCharacterString("thetormented:TheTormented")` 查无此键返回 null）。已将 24 个文件全部重建：键与 NAMES 逐字恢复自 HEAD，新 TEXT 从损坏文件中抢救保留。重新验证：无 BOM、键/NAMES/TEXT 完整，`validate_localization.ps1` 24 语言再次全部 PASS。
- **验证**：`validate_localization.ps1` 24 语言全部 PASS、total fails=0（eng 627 条 / zhs 3 条英文残留词警告为既有基线，非本次引入）。

**#35 自定义碎心结局过场框架（图片待补）：The Tormented 不再借用 Ironclad 的结局画面**
- **问题**：击败心脏后原版进入 `TrueVictoryRoom`，其构造器执行 `new Cutscene(player.chosenClass)`；`Cutscene` 构造器按 `PlayerClass` 分发（1-4 为四名原版角色），mod 角色落入 `default:` 分支——直接回退到 Ironclad 的结局插画（`images/scenes/redBg.jpg` + `ironclad1-3.png`）。字节码验证：`TrueVictoryRoom.<init>`（PC 12-25）创建过场；`Cutscene` 构造器 tableswitch 1/2/3/4 -> ironclad/silent/defect/watcher 面板，`default` -> ironclad；`CutscenePanel` 通过 `ImageMaster.loadImage` 加载贴图，文件缺失时返回 null（仅记日志）。
- **修复**：新增 `thetormented/patches/VictoryCutscenePatch.java` —— 对 `Cutscene.<init>`（paramtypez `{AbstractPlayer.PlayerClass.class}`）的 Postfix 补丁。当 `AbstractDungeon.player.chosenClass == Tormented.Meta.TORMENTED` 时，加载本 mod 自己的背景与 3 张面板图，经 `ReflectionHacks.setPrivate` 替换私有字段 `bgImg`/`panels`；任一张图加载失败则放弃替换、维持原版（Ironclad）过场，缺图绝不导致崩溃。素材文件约定（放入 `src/main/resources/thetormented/images/character/`）：`endingBg.jpg`（全屏背景，1920x1080）与 `ending1.png`/`ending2.png`/`ending3.png`（插画面板；非 16:10 时绘制区为 WIDTH x (HEIGHT + 110 x scale)，见 `Cutscene.renderImg`）。
- **素材已就位**：`endingBg.jpg` + `ending1/2/3.png` 均为 1920x1200（16:10，正好落入原版全屏绘制分支 `isSixteenByTen`，不裁切）。
- **预览验证（独立工具，不入库）**：针对 `desktop-1.0.jar` 的 libGDX/LWJGL2 桌面程序（资源解析与游戏内一致：LWJGL2 internal = classpath，mod 路径从资源目录解析），逐字节复刻 `Cutscene.update`/`updateSceneChange`/`updateFadeIn`/`updateFadeOut`/`updateIfDone` 与 `CutscenePanel.update`/`activate`/`fadeOut`/`render`（含原版 isDone 后每帧重复 `fadeOut()` 的原生行为）。结果：4 次 `ImageMaster.loadImage` 全部 OK（1920x1200）；完整时间线（黑场约 2s -> P1 5s -> P2 -> P3 -> 全体淡出 -> 背景淡出 -> 结算屏）跑完且零异常；预览裸环境特有的 `ImageMaster.WHITE_SQUARE_IMG` 为 null（游戏内绝不为 null）用自建 1x1 白纹兜底。
- **验证**：javac 全量编译 EXIT=0（180 类）。文案零改动。jar 已重建（`target/thetormented-cutscene.jar`，909 条目 / 180 类）并部署到 `mods/thetormented.jar`（旧版备份 `thetormented.jar.bak-1.0.2-pre-cutscene`），`copy/thetormented.jar` 同步。待玩家用 The Tormented 击败心脏后游戏内实机确认。

**#36 Beta Art 预览：关闭 Beta Art 后，已打开的放大预览立即恢复原版大图（#19 后续）**
- **症状**：游戏启动时全局 Beta Art 开启，之后在设置或预览内的单卡开关中关闭 Beta Art，已打开的 `SingleCardViewPopup` 仍显示旧的测试画风大图；卡牌总览网格能正常切回原图。反方向（关 -> 开）一直正常。
- **根因**：#19 的 `EnforceBetaPortrait`（`update()` Postfix）只处理了"开启"方向——`wantBetaArt(card)` 为 false 时仅把 `lastBetaPortrait` 标记置 null 就返回，`portraitImg` 槽位仍挂着旧 beta 纹理，也没有任何代码为仍在打开状态的弹窗重跑原版/Basemod 大图加载。
- **修复**：beta 关闭的那一帧现在会主动反向恢复：dispose 自己创建的 beta 纹理 → 清空 `portraitImg` → 经 `ReflectionHacks.privateMethod` 重跑原版 `loadPortraitImg()`；若原版仍留 null（mod 卡没有 `1024Portraits` 文件），再按 Basemod `OpenFix$OpenTextureFix` 在 open() 时的同一逻辑、用公开 API `CustomCard.getPortraitImage(card)` 填官方 `_p` 大图（不自行猜测路径；该 Basemod 修复只挂在 `open()` 上，运行中不会自动补跑）。恢复仅在"我们自己的纹理还挂在槽位上"时触发（`cur == lastBetaPortrait` 身份比对），其余情况每帧空转、零开销。纹理所有权收紧：替换/恢复路径只 dispose 自己创建的纹理；原版 `close()` 本身就会 dispose 并置空 `portraitImg`（字节码已验证），因此 close 钩子刻意只丢弃标记引用、不再重复 dispose（避免双重释放）。
- **验证**：javac 全量编译通过（EXIT=0）。文案零改动。

**#37 Beta Art 预览重构（#36 后续）：状态机驱动 + 严格纹理所有权**
- **#36 的缺陷**：恢复动作以 `cur == lastBetaPortrait` 身份比对作为闸门——但这个身份不是"当前大图是否归我们管"的可靠判据。`lastBetaPortrait` 是静态字段（每次启动 JVM 归零），而槽位里的纹理可能来自原版、Basemod 的 `OpenFix$OpenTextureFix`，甚至 Basemod 自己的 beta 大图选择逻辑（`CustomCard.getPortraitImage` 会按 `PLAYTESTER_ART_MODE`/`betaCardPref` 选 `_b_p`），身份比对可能跳过本应执行的恢复；反过来 `applyBetaPortrait` 会无条件 dispose 槽位里现有的纹理——那可能是**共享缓存纹理**（全局测试模式开启时 `BaseCard.getPortraitImage()` 返回 `TextureLoader` 缓存），dispose 掉会污染缓存，波及卡牌总览等其他使用者。
- **修复**：把两件事彻底拆开。(1) *状态机*：新增 `trackedCard`/`trackedBetaState`/`hasTrackedState`，检测换卡与 Beta 开关翻转（含游戏重启后首次出现）；状态变化只触发一次 `applyBetaPortrait`（开）或 `restoreNormalPortrait`（关）。Beta 开且状态未变时，仅当槽位不再是我们的大图才放回（对抗升级预览覆盖）；Beta 关且状态未变时每帧零操作。(2) *所有权*：全补丁范围内只有与 `lastBetaPortrait` 相同的纹理才会被 dispose（apply/drop/restore 三处统一）；别人的纹理只覆盖引用、绝不 dispose——与原版自身 reload 不 dispose 旧图的泄漏语义一致。`restoreNormalPortrait` 不再靠判断归属决定是否行动：有自家纹理则 dispose，然后无条件清空槽位、重跑原版 `loadPortraitImg()`；若原版仍留 null，再以公开 API `CustomCard.getPortraitImage(card)` 镜像 Basemod `OpenFix$OpenTextureFix` 兜底官方 `_p` 大图。`close()` 只重置状态机、不 dispose（原版 close 已 dispose `portraitImg`，字节码已验证）。
- **验证**：javac 全量编译通过（EXIT=0）。文案零改动。

**#38 ExecutionForm 与 Bloodbath 效果互换（卡牌、能力、图标、24 语言全量同步）**
- **改动**：两张稀有能力牌机制对调。ExecutionForm（3 费）改为流血引擎：每回合打出的前 1（升级 2）张攻击牌，按其未被格挡的伤害施加流血。Bloodbath（2 费）改为虚无并获得阈值增益：对生命值低于 50%（升级 75%）最大生命的敌人造成伤害+25%；旧的"升级减费 2→1"随旧机制一并移除。
- **能力类名跟随卡牌**：阈值逻辑（原 JudgmentFormPower）现居 BloodbathPower；流血逻辑（原 BloodbathPower）现居 ExecutionFormPower。POWER_ID 随类名生成：thetormented:ExecutionFormPower / thetormented:BloodbathPower。
- **图标相应互换**：JudgmentFormPower.png 改为 BloodbathPower.png，BloodbathPower.png 改为 ExecutionFormPower.png（普通+large/ 两套）；JudgmentFormPower 文件不再存在。
- **本地化（24 语言）**：PowerStrings——键 ${modID}:JudgmentFormPower 更名为 ${modID}:ExecutionFormPower，两条目 DESCRIPTIONS 互换，NAME 各语言保留。CardStrings——两卡的 DESCRIPTION/UPGRADE_DESCRIPTION 互换；ExecutionForm 的 UPGRADE_DESCRIPTION 按语言新译（"前2张攻击牌…"）。另修复 zhs/zht 既有缺陷：流血能力描述把数字拼在句中（"…第一张攻击牌1对敌人…"）；两段现已为数字留位（"前 1/2 张攻击牌"）。
- **验证**：validate_localization.ps1 24 语言全部 PASS；javac 全量编译 EXIT=0（179 类）；jar 已重建（target/thetormented-swap2.jar，908 条目）并部署到 mods/thetormented.jar（旧版备份 thetormented.jar.bak-1.0.2-pre-swap）。

**#40 BaseCard Beta Art 模式同步：实例注册表广播修复卡牌总览/战斗牌库残留画风**
- **症状**：启动时全局 Beta Art 开启后关闭，卡牌总览网格与战斗抽牌堆/弃牌堆中的卡牌仍保留测试画风；弹窗（SingleCardViewPopup）恢复正常。根因：`BaseCard.update()` 使用单一 `static lastPlaytesterMode` 标志——一旦任意实例（通常是战斗手牌副本）消费了该标志，其余实例（库单例、抽牌堆/弃牌堆卡）永远不会检测到模式翻转。
- **修复**：用版本号广播架构替代一次性静态标志。`WeakHashMap` 的 `ALL_INSTANCES` 注册表追踪所有存活的 `BaseCard` 实例（GC 时自动清理）；`static globalModeVersion` 计数器在模式翻转时递增；每个实例持有自己的 `syncedModeVersion`。任意实例的 `update()` 检测到不匹配时调用 `notifyGlobalModeChanged()`，遍历注册表强制所有实例执行 `syncToCurrentMode()`（loadCardImage + refreshJokePortrait）——包括库单例与不常调用 `update()` 的抽牌堆/弃牌堆卡。
- **验证**：javac 全量编译 EXIT=0（180 类）。文案零改动（无需 validate）。待玩家实机确认：开启 Beta Art → 关闭 → 卡牌总览网格 + 战斗抽牌堆/弃牌堆全部恢复正常画风；Rebel 的 jokePortrait 区域已更新；SingleCardViewPopup 正常。

**#39 v1.0.3 正式版发布**
- **本版变更**（累计 #33-#38）：HeroLongsword 拾取后 ConcurrentModificationException 崩溃修复（#33）；CharacterStrings 全面重写——真实角色简介、Spire Heart 攻击台词、Vampires 问候逐字对齐 24 语言（#34）；自定义碎心结局过场框架与专属插画（#35）；Beta Art 预览修复——单卡开关反向恢复（#36）与状态机重构+严格纹理所有权（#37）；ExecutionForm 与 Bloodbath 效果互换，能力类改名、图标互换、24 语言全量同步（#38）。
- **技术**：pom.xml / ModTheSpire.json 版本号 1.0.2 → 1.0.3。
- **验证**：javac 全量编译 EXIT=0（179 类）；validate_localization.ps1 24 语言全部 PASS；jar 已部署到 mods/thetormented.jar 及 Steam 创意工坊 content 目录。

---

### 帮助翻译你的语言

除简体中文外的翻译均由自动化管线生成；欢迎任何语言的玩家提交修正（issue / PR）。
翻译规范与校验脚本用法见 `docs/LOCALIZATION_SPEC.md`。