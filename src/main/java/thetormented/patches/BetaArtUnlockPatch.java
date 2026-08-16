package thetormented.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import thetormented.character.Tormented;
import thetormented.util.TextureLoader;

public class BetaArtUnlockPatch {
    @SpirePatch(clz = SingleCardViewPopup.class, method = "canToggleBetaArt")
    public static class CanToggleBetaArt {
        @SpirePrefixPatch
        public static SpireReturn<Boolean> Prefix() {
            return SpireReturn.Return(true);
        }
    }

    // 上一版的错误做法：把 250x190 的小卡图塞进 portraitImg（大图槽位）。
    // 而 renderPortrait 用固定源矩形 (0,0,500,380) 绘制 portraitImg，
    // 小图会被左上角裁切 + 拉伸成残影，并且非 null 的 portraitImg 会抑制 Basemod 的官方大图兜底。
    // 补救：portraitImg 只允许放 500x380 的 _p 大图；
    //   beta 开 -> cards_test/.../Name_p.png（不存在则交给原版/Basemod 的官方大图流程）；
    //   beta 关 -> 不干预（1024Portraits 失败 -> portraitImg 置 null -> Basemod OpenFix 填官方 _p 大图）。
    private static Texture lastBetaPortrait;

    private static AbstractCard getCard(SingleCardViewPopup popup) {
        return ReflectionHacks.getPrivate(popup, SingleCardViewPopup.class, "card");
    }

    private static boolean wantBetaArt(AbstractCard card) {
        return Settings.PLAYTESTER_ART_MODE
                || UnlockTracker.betaCardPref.getBoolean(card.cardID, false);
    }

    private static Texture newPortrait(String path) {
        Texture t = new Texture(path);
        t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        return t;
    }

    private static void setPortrait(SingleCardViewPopup popup, Texture t) {
        ReflectionHacks.setPrivate(popup, SingleCardViewPopup.class, "portraitImg", t);
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "loadPortraitImg")
    public static class LoadPortraitImg {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(SingleCardViewPopup __instance) {
            AbstractCard card = getCard(__instance);
            if (card == null || card.color != Tormented.Meta.CARD_COLOR) {
                lastBetaPortrait = null;
                return SpireReturn.Continue();
            }
            boolean beta = wantBetaArt(card);
            if (!beta) {
                lastBetaPortrait = null;
                return SpireReturn.Continue();
            }
            String path = TextureLoader.getCardTestPortraitString(card.cardID, card.type);
            if (path == null) {
                lastBetaPortrait = null;
                return SpireReturn.Continue();
            }
            Texture old = ReflectionHacks.getPrivate(__instance, SingleCardViewPopup.class, "portraitImg");
            if (old != null) {
                old.dispose();
            }
            lastBetaPortrait = newPortrait(path);
            setPortrait(__instance, lastBetaPortrait);
            return SpireReturn.Return(null);
        }
    }

    // Basemod 的 UpgradeChangesPortraitPatch（插入到 updateUpgradePreview 的 isViewingUpgrade 访问点，
    // 每帧都会执行）会把 portraitImg 换成官方大图，无视 beta 状态 -> 测试画风 + 升级预览会被覆盖回正式插画。
    // 这里在 update() 的每帧末尾兜底：只要当前 portraitImg 不是我们放的 beta 大图，就换回来。
    // 用 lastBetaPortrait 做身份比对，纹理没被换走时空转返回，零开销。
    @SpirePatch(clz = SingleCardViewPopup.class, method = "update")
    public static class EnforceBetaPortrait {
        @SpirePostfixPatch
        public static void Postfix(SingleCardViewPopup __instance) {
            AbstractCard card = getCard(__instance);
            if (card == null || card.color != Tormented.Meta.CARD_COLOR
                    || !wantBetaArt(card)) {
                lastBetaPortrait = null;
                return;
            }
            String path = TextureLoader.getCardTestPortraitString(card.cardID, card.type);
            if (path == null) {
                lastBetaPortrait = null;
                return;
            }
            Texture cur = ReflectionHacks.getPrivate(__instance, SingleCardViewPopup.class, "portraitImg");
            if (cur == lastBetaPortrait) {
                return;
            }
            if (cur != null) {
                cur.dispose();
            }
            lastBetaPortrait = newPortrait(path);
            setPortrait(__instance, lastBetaPortrait);
        }
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "close")
    public static class Close {
        @SpirePostfixPatch
        public static void Postfix() {
            lastBetaPortrait = null;
        }
    }
}