package thetormented.patches;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Prefs;
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

    // 设计原则：状态机驱动，而不是靠“portraitImg 是不是我们放的图”做判断。
    //   - lastBetaPortrait 是跨帧/JVM 重启后不可靠的身份信息：静态字段随游戏重启归零，
    //     而槽位里的纹理可能来自原版、Basemod OpenFix$OpenTextureFix，甚至 Basemod 的
    //     beta 大图选择逻辑（CustomCard.getPortraitImage 会按 PLAYTESTER_ART_MODE /
    //     betaCardPref 选 _b_p）。所以恢复动作只由 “卡片/Beta 状态发生变化” 触发。
    //   - 纹理所有权：本 Patch 只 dispose 明确由自己创建的纹理（与 lastBetaPortrait
    //     身份相同）；别人的纹理一律只覆盖引用、绝不 dispose——其中可能混有
    //     TextureLoader 的共享缓存纹理（BaseCard.getPortraitImage 返回值），dispose 掉
    //     会污染缓存。原版自身 reload 时也从不 dispose 旧图，泄漏语义与其一致。
    //   - 原版 close() 本身就会 dispose 并置空 portraitImg（字节码已验证），因此 Close
    //     钩子只重置状态机标记，绝不重复 dispose。
    // portraitImg 只允许放 500x380 的 _p 大图（renderPortrait 用固定源矩形 (0,0,500,380)
    // 绘制，塞小图会被裁切拉伸成残影）：
    //   beta 开 -> cards_test/.../Name_p.png（不存在则交给原版/Basemod 流程）；
    //   beta 关 -> 清空槽位并重跑原版 loadPortraitImg()；仍为 null 时按 Basemod
    //     OpenFix$OpenTextureFix 在 open() 时的同一逻辑用公开 API
    //     CustomCard.getPortraitImage() 兜底官方 _p 大图（该 Fix 只挂 open()，
    //     运行中切换开关不会自动补跑）。
    private static Texture lastBetaPortrait;
    private static AbstractCard trackedCard;
    private static boolean trackedBetaState;
    private static boolean hasTrackedState;

    private static AbstractCard getCard(SingleCardViewPopup popup) {
        return ReflectionHacks.getPrivate(popup, SingleCardViewPopup.class, "card");
    }

    private static Texture getPortrait(SingleCardViewPopup popup) {
        return ReflectionHacks.getPrivate(popup, SingleCardViewPopup.class, "portraitImg");
    }

    private static boolean wantBetaArt(AbstractCard card) {
        Prefs pref = UnlockTracker.betaCardPref;
        if (pref == null) {
            return Settings.PLAYTESTER_ART_MODE;
        }
        return pref.getBoolean(card.cardID, Settings.PLAYTESTER_ART_MODE);
    }

    private static Texture newPortrait(String path) {
        Texture t = new Texture(path);
        t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        return t;
    }

    private static void setPortrait(SingleCardViewPopup popup, Texture t) {
        ReflectionHacks.setPrivate(popup, SingleCardViewPopup.class, "portraitImg", t);
    }

    // 若当前挂的是我们自己的 beta 纹理则销毁并清空槽位；别人的纹理不动。
    private static void dropOwnPortrait(SingleCardViewPopup popup) {
        Texture cur = getPortrait(popup);
        if (cur != null && cur == lastBetaPortrait) {
            cur.dispose();
            setPortrait(popup, null);
        }
        lastBetaPortrait = null;
    }

    private static void applyBetaPortrait(SingleCardViewPopup popup, AbstractCard card) {
        String path = TextureLoader.getCardTestPortraitString(card.cardID, card.type);
        if (path == null) {
            lastBetaPortrait = null;
            return;
        }
        Texture old = getPortrait(popup);
        if (old != null && old == lastBetaPortrait) {
            old.dispose();
        }
        lastBetaPortrait = newPortrait(path);
        setPortrait(popup, lastBetaPortrait);
    }

    // Beta 关闭时的恢复：不猜测槽位里是谁的纹理——只 dispose 属于自己的那一份，
    // 然后无条件清空槽位并重走原版加载流程，让原版/Basemod 自己放回正常大图。
    private static void restoreNormalPortrait(SingleCardViewPopup popup, AbstractCard card) {
        Texture cur = getPortrait(popup);
        if (cur != null && cur == lastBetaPortrait) {
            cur.dispose();
        }
        lastBetaPortrait = null;
        setPortrait(popup, null);
        ReflectionHacks.privateMethod(SingleCardViewPopup.class, "loadPortraitImg").invoke(popup);
        Texture normal = getPortrait(popup);
        if (normal == null && card instanceof CustomCard) {
            setPortrait(popup, CustomCard.getPortraitImage((CustomCard) card));
        }
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "loadPortraitImg")
    public static class LoadPortraitImg {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(SingleCardViewPopup __instance) {
            AbstractCard card = getCard(__instance);
            if (card == null || card.color != Tormented.Meta.CARD_COLOR
                    || !wantBetaArt(card)) {
                dropOwnPortrait(__instance);
                return SpireReturn.Continue();
            }
            String path = TextureLoader.getCardTestPortraitString(card.cardID, card.type);
            if (path == null) {
                dropOwnPortrait(__instance);
                return SpireReturn.Continue();
            }
            applyBetaPortrait(__instance, card);
            return SpireReturn.Return(null);
        }
    }

    // 每帧兜底，全部由状态变化驱动：
    //   卡片切换 / Beta 开关翻转（含游戏重启后的首次出现）-> 立即同步一次；
    //   Beta 开且状态未变 -> 仅当我们的 beta 图被其他 Patch（如升级预览）换掉时才放回；
    //   Beta 关且状态未变 -> 不做任何事。
    @SpirePatch(clz = SingleCardViewPopup.class, method = "update")
    public static class EnforceBetaPortrait {
        @SpirePostfixPatch
        public static void Postfix(SingleCardViewPopup __instance) {
            AbstractCard card = getCard(__instance);
            if (card == null || card.color != Tormented.Meta.CARD_COLOR) {
                return;
            }
            boolean beta = wantBetaArt(card);
            if (!hasTrackedState || trackedCard != card || trackedBetaState != beta) {
                trackedCard = card;
                trackedBetaState = beta;
                hasTrackedState = true;
                if (beta) {
                    if (getPortrait(__instance) != lastBetaPortrait) {
                        applyBetaPortrait(__instance, card);
                    }
                } else {
                    restoreNormalPortrait(__instance, card);
                }
                return;
            }
            if (beta && getPortrait(__instance) != lastBetaPortrait) {
                applyBetaPortrait(__instance, card);
            }
        }
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "close")
    public static class Close {
        @SpirePostfixPatch
        public static void Postfix() {
            // 原版 close() 已 dispose 并置空 portraitImg（含我们的 beta 纹理），这里只重置状态。
            lastBetaPortrait = null;
            trackedCard = null;
            hasTrackedState = false;
        }
    }
}
