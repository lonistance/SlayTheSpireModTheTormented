package thetormented.patches;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DescriptionLine;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.KeywordStrings;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

// VoidCall 给卡牌附加“虚无”（isEthereal）时，被附加的卡面描述不会自动显示该状态。
// 本补丁不修改任何卡牌类：VoidCallAction 施加 ethereal 时通过 markEthereal() 记录该卡实例，
// 此后每次 initializeDescription（手牌/图鉴/悬停渲染都会触发，总入口会转发中文分支）重建描述时，
// 在被附加卡的描述末尾追加一行当前语言的“Ethereal”（关键词名），提醒玩家这张卡被附加了“虚无”。
// 说明：不用 ModTheSpire 的 SpireField 注入（其对目标类首次构造的初始化时序在启动期会 NPE），
// 直接以弱引用身份映射记录卡实例，卡被 GC 后自动清理，无内存残留。
public class EtherealMarkerPatch {

    private static final Map<AbstractCard, Boolean> markedCards =
            Collections.synchronizedMap(new WeakHashMap<AbstractCard, Boolean>());

    public static void markEthereal(AbstractCard c) {
        markedCards.put(c, Boolean.TRUE);
    }

    @SpirePatch(clz = AbstractCard.class, method = "initializeDescription")
    public static class VoidCallEthereal {
        @SpirePostfixPatch
        public static void Postfix(AbstractCard __instance) {
            if (!Boolean.TRUE.equals(markedCards.get(__instance)) || !__instance.isEthereal) {
                return;
            }
            String name = "Ethereal";
            KeywordStrings ks = CardCrawlGame.languagePack.getKeywordString("ethereal");
            if (ks != null && ks.ETHEREAL != null
                    && ks.ETHEREAL.NAMES != null && ks.ETHEREAL.NAMES.length > 0) {
                name = ks.ETHEREAL.NAMES[0];
            }
            String text = name + ".";
            // DescriptionLine 的 width 必须是被渲染行的实际像素宽（原版用 GlyphLayout 测量，
            // 渲染时以 card.x - width*drawScale/2 定位行的左端）；若塞一个过大的宽度值，
            // 该行会被推到卡面左侧屏幕外（表现为文本严重超出卡面）。
            GlyphLayout layout = new GlyphLayout(FontHelper.cardDescFont_N, text);
            __instance.description.add(new DescriptionLine(text, layout.width));
        }
    }
}