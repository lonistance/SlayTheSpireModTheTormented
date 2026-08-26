package thetormented.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.cutscenes.Cutscene;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import thetormented.character.Tormented;

import java.util.ArrayList;

// 碎心结局过场：原版 Cutscene 构造器按 PlayerClass 分发 4 个原版角色的结局插画，
// mod 角色落入 default 分支，会显示 Ironclad 的画面。
// 本补丁在 Cutscene 构造完成后，若当前角色是 TheTormented，则用自定义
// 背景/插画替换其私有字段 bgImg 与 panels；素材缺失时保持原版（loadImage 返回
// null 即放弃替换），避免因缺图崩溃。素材文件约定（放资源目录）：
//   thetormented/images/character/endingBg.jpg  （背景，全屏 1920x1080）
//   thetormented/images/character/ending1.png / ending2.png / ending3.png（三张插画）
public class VictoryCutscenePatch {

    private static final String BG_PATH = "thetormented/images/character/endingBg.jpg";
    private static final String[] PANEL_PATHS = {
            "thetormented/images/character/ending1.png",
            "thetormented/images/character/ending2.png",
            "thetormented/images/character/ending3.png"
    };

    @SpirePatch(clz = Cutscene.class, method = SpirePatch.CONSTRUCTOR,
            paramtypez = {AbstractPlayer.PlayerClass.class})
    public static class TormentedCutscene {
        @SpirePostfixPatch
        public static void Postfix(Cutscene __instance) {
            if (AbstractDungeon.player == null
                    || AbstractDungeon.player.chosenClass != Tormented.Meta.TORMENTED) {
                return;
            }
            Texture bg = ImageMaster.loadImage(BG_PATH);
            if (bg == null) {
                return;
            }
            for (String path : PANEL_PATHS) {
                if (ImageMaster.loadImage(path) == null) {
                    return;
                }
            }
            ArrayList<CutscenePanel> panels = new ArrayList<>();
            for (String path : PANEL_PATHS) {
                panels.add(new CutscenePanel(path));
            }
            ReflectionHacks.setPrivate(__instance, Cutscene.class, "bgImg", bg);
            ReflectionHacks.setPrivate(__instance, Cutscene.class, "panels", panels);
        }
    }
}