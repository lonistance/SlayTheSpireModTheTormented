# 仓库内代码/内容变更时务遵守

## 硬性指令（每次都必须执行）

- **每次修改调整（代码、文案、文档、工具脚本）完成后，必须同步更新 CHANGELOG.md 与 CHANGELOG.zh-CN.md**（描述实际改动与影响，编号续用下一条，新条目插在文件末尾的 `---` 分隔符之前）。这是不可省略的流程，不是可选步骤。

## 常用约定

- 编译：`javac` 全量编译（classpath 见 CHANGELOG 记录，输出 `target\javac-check-*`），必须 EXIT=0。
- 本地化：任何文案改动后运行 `tools\validate_localization.ps1` 直到通过；翻译准则见 `docs\LOCALIZATION_SPEC.md`（含"关键词 token 必须紧跟空格"规则）。
- 文件编码：JSON 一律 UTF-8 无 BOM；PowerShell 脚本若非 ASCII 需 UTF-8 带 BOM（PS 5.1 按 ANSI 读取无 BOM 脚本会乱码）。