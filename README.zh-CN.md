# The Tormented（受折磨者）

> 一个杀戮尖塔角色模组——血、罪孽，以及与黑暗的契约。

**The Tormented** 新增一名同名可玩角色，基于 [BasicMod](https://github.com/Alchyr/BasicMod) 模组模板构建。

[English](README.md)

## 免责声明

本项目**尚处于测试阶段**，请知悉以下几点：

- 部分卡牌、遗物、药水**未经充分测试**，可能存在 Bug。
- 卡牌数值与机制将**根据实际游戏体验和反馈进行调整**，并非最终版本。
- 除简体中文外，其他语言的翻译**缺少人工校对**，可能存在错误或不通顺之处。
- 美术资源来自不同来源，**画风不稳定**。
- 本作由**个人开发，属于练手作品**，代码结构缺少规范，且**无法保证及时修复 Bug**。

感谢你的宽容与理解。如遇到问题，欢迎通过 Issue 或反馈渠道告知。

## 背景故事

他曾是部族最强的剑，在一对一的对决中斩下了远古神明的右臂。为惩罚他的僭越，神明降下诅咒：他的脚步所至，战火必起；唯有将敌人的鲜血饮尽，才能换得片刻赎罪之宁。为打破这宿命，他与新生女神涅奥立下契约，向尖塔进发，誓要斩断灾祸的根源。

## 特色

- 新可玩角色 **受折磨者**（3 点能量，80 点生命上限）
- **77 张卡牌** —— 基础 4 / 普通 19 / 罕见 36 / 稀有 16 / 特殊 2
- **15 件遗物**，含初始遗物 *被诅咒的断剑*（可被 *英雄长剑* 替换）
- **3 瓶药水**、**25 种能力**
- **4 个自定义关键词**：流血、罪孽、负债、束缚
- **2 张状态卡**：苦难、纠缠

### 初始配置

- 初始牌组：4× 打击、1× 反叛、4× 防御、1× 宽恕
- 初始遗物：被诅咒的断剑

## 核心机制

| 关键词 | 效果 |
|---|---|
| **流血** | 回合开始时失去等同于层数的生命，随后清除所有流血。未被格挡的攻击伤害施加 1 层流血。 |
| **罪孽** | 每持有 5 层罪孽，获得 1 层负债。 |
| **负债** | 每层负债使你受到的伤害提高 10%。 |
| **束缚** | 在完全清除全部负债前，你无法获得能量。回合结束时移除。 |

卡牌围绕这些机制展开：积攒罪孽、将其转化为负债、花费或清除负债换取强力效果、叠加并引爆流血，以及制造苦难状态卡。

## 卡牌统计

| 稀有度 | 攻击 | 技能 | 能力 | 诅咒/状态 | 合计 |
|---|---|---|---|---|---|
| 基础 | 2 | 2 | – | – | 4 |
| 普通 | 10 | 9 | – | – | 19 |
| 罕见 | 13 | 15 | 8 | – | 36 |
| 稀有 | 4 | 7 | 5 | – | 16 |
| 特殊 | – | – | – | 2 | 2 |

## 本地化

内置六种语言，随游戏语言设置自动切换：

| 语言 | 代码 | 状态 |
|---|---|---|
| English | eng | 基准（参考） |
| 简体中文 | zhs | 已完整；存在少量一致性问题待修 |
| Deutsch | deu | 已完整；存在少量一致性问题待修 |
| Nederlands | dut | 已校验，全部检查通过 |
| Esperanto | epo | 已校验，全部检查通过 |
| Suomi | fin | 已校验，全部检查通过 |

对任意语言运行校验工具：

```powershell
powershell -ExecutionPolicy Bypass -File tools\validate_localization.ps1 -Lang <代码>
```

完整的翻译规范、token 约定与已知问题见 `docs/LOCALIZATION_SPEC.md`。

## 构建与运行

依赖：JDK 8、Maven、Steam 版杀戮尖塔，并在 Steam 创意工坊订阅 [ModTheSpire](https://github.com/kiooeht/ModTheSpire)、[BaseMod](https://github.com/daviscook477/BaseMod) 与 [StSLib](https://github.com/kiooeht/StSLib)。

1. 将 `pom.xml` 中的 `steam.windows` 改为你的 Steam 安装路径。
2. 构建并自动安装到 Steam mods 目录：

   ```
   mvn package
   ```

3. 通过 **ModTheSpire** 启动游戏，勾选启用 **The Tormented**。

## 项目结构

```
src/main/java/thetormented/
├── actions/      # 卡牌与能力的行为代码
├── cards/        # 77 张卡牌（基础/普通/罕见/稀有/特殊）
├── character/    # 受折磨者角色类
├── potions/      # 3 瓶药水
├── powers/       # 25 种能力
├── relics/       # 15 件遗物
└── util/         # KeywordInfo 与工具类
src/main/resources/
├── ModTheSpire.json
└── thetormented/
    ├── audio/        # 音频资源
    ├── images/       # 卡牌/遗物/能力/角色美术
    └── localization/ # eng / zhs / deu / dut / epo / fin
docs/                 # 本地化规范
tools/                # 本地化校验工具
```

## 致谢

- 基于 [BasicMod](https://github.com/Alchyr/BasicMod) 模组模板
- 感谢杀戮尖塔模组社区
