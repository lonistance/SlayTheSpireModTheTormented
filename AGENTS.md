# 仓库内代码/内容变更时务遵守

## 小说项目常读文件（每次对话开始必须执行）

在 `E:\SlayTheSpireMod\The-Tormented-Story` 项目工作时，**每次对话开始必须先读取**：

1. 正文各卷（`第一卷 血痕之子.md` ～ `第七卷 最后的契约.md`）
2. `小说大纲.md`
3. `STORY_BIBLE.md`
4. `EDITOR_PROTOCOL.md`（作者意见审议协议——用户意见须先分级（LEVEL 0～5）、分析，再执行；禁止机械讨好）

## 硬性指令（每次都必须执行）

- **每次修改调整（代码、文案、文档、工具脚本）完成后，必须同步更新 CHANGELOG.md 与 CHANGELOG.zh-CN.md**（描述实际改动与影响，编号续用下一条，新条目插在文件末尾的 `---` 分隔符之前）。这是不可省略的流程，不是可选步骤。

## 常用约定

- 编译：`javac` 全量编译（classpath 见 CHANGELOG 记录，输出 `target\javac-check-*`），必须 EXIT=0。
- 本地化：任何文案改动后运行 `tools\validate_localization.ps1` 直到通过；翻译准则见 `docs\LOCALIZATION_SPEC.md`（含"关键词 token 必须紧跟空格"规则）。
- 文件编码：JSON 一律 UTF-8 无 BOM；PowerShell 脚本若非 ASCII 需 UTF-8 带 BOM（PS 5.1 按 ANSI 读取无 BOM 脚本会乱码）。