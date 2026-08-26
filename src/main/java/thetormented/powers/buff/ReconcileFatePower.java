package thetormented.powers.buff;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import thetormented.cards.special.status.Misery;
import thetormented.powers.BasePower;

import java.util.HashSet;
import java.util.Set;

import static thetormented.BasicMod.makeID;

public class ReconcileFatePower extends BasePower {
    public static final String POWER_ID = makeID(ReconcileFatePower.class.getSimpleName());

    private static final PowerType POWER_TYPE = PowerType.BUFF;
    private static final boolean IS_TURN_BASED = false;

    public ReconcileFatePower(AbstractCreature owner, AbstractCreature source, int amount) {
        super(POWER_ID, POWER_TYPE, IS_TURN_BASED, owner, source, amount);
    }

    public void trigger() {
        this.flash();
        this.addToBot(new GainBlockAction(this.owner, this.owner, this.amount));
    }

    @Override
    public void onExhaust(AbstractCard card) {
        if (card != null && Misery.ID.equals(card.cardID)) {
            this.trigger();
        }
    }

    // 同一张生成卡可能被包装进多个 effect 实例（例如 DrawPile 的随机落位 effect 在
    // update() 里 new 了 6 参与 3 参两个构造器），按构造器触发会导致一次生成触发多次：
    // HeavyPast 生成 2 张实际 3 次（DrawPile 链 2 次 + Discard 1 次）、Shackles 2 张实际 4 次。
    // 因此以「卡实例」为计数单元：同一张 Misery 实例无论被多少个 effect 包装，只触发一次；
    // 批量生成 N 张就恰好触发 N 次。集合每场战斗开始前清空。
    private static final Set<AbstractCard> firedCards = new HashSet<>();

    public static void clearFiredCards() {
        firedCards.clear();
    }

    public static void onMiseryCardCreated(AbstractCard card) {
        if (card == null || !Misery.ID.equals(card.cardID)) return;
        if (!firedCards.add(card)) return;
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasPower(POWER_ID)) {
            ((ReconcileFatePower) AbstractDungeon.player.getPower(POWER_ID)).trigger();
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    // 在 effect 构造器上挂钩（能拿到被包装的卡引用）。一张卡可能被多个 effect 实例包装，
    // 重复触发由 onMiseryCardCreated 内的卡实例去重吸收，保证一次生成恰好触发一次。

    @SpirePatch(clz = ShowCardAndAddToHandEffect.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class})
    public static class HandSingle {
        @SpirePostfixPatch
        public static void Postfix(ShowCardAndAddToHandEffect __instance, AbstractCard __arg0) {
            onMiseryCardCreated(__arg0);
        }
    }

    @SpirePatch(clz = ShowCardAndAddToHandEffect.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class, float.class, float.class})
    public static class HandAtPos {
        @SpirePostfixPatch
        public static void Postfix(ShowCardAndAddToHandEffect __instance, AbstractCard __arg0, float __arg1, float __arg2) {
            onMiseryCardCreated(__arg0);
        }
    }

    @SpirePatch(clz = ShowCardAndAddToDiscardEffect.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class})
    public static class DiscardSingle {
        @SpirePostfixPatch
        public static void Postfix(ShowCardAndAddToDiscardEffect __instance, AbstractCard __arg0) {
            onMiseryCardCreated(__arg0);
        }
    }

    @SpirePatch(clz = ShowCardAndAddToDiscardEffect.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class, float.class, float.class})
    public static class DiscardAtPos {
        @SpirePostfixPatch
        public static void Postfix(ShowCardAndAddToDiscardEffect __instance, AbstractCard __arg0, float __arg1, float __arg2) {
            onMiseryCardCreated(__arg0);
        }
    }

    @SpirePatch(clz = ShowCardAndAddToDrawPileEffect.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class, boolean.class, boolean.class})
    public static class DrawPileXY {
        @SpirePostfixPatch
        public static void Postfix(ShowCardAndAddToDrawPileEffect __instance, AbstractCard __arg0, boolean __arg1, boolean __arg2) {
            onMiseryCardCreated(__arg0);
        }
    }

    @SpirePatch(clz = ShowCardAndAddToDrawPileEffect.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class, float.class, float.class, boolean.class})
    public static class DrawPilePosB {
        @SpirePostfixPatch
        public static void Postfix(ShowCardAndAddToDrawPileEffect __instance, AbstractCard __arg0, float __arg1, float __arg2, boolean __arg3) {
            onMiseryCardCreated(__arg0);
        }
    }

    @SpirePatch(clz = ShowCardAndAddToDrawPileEffect.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class, float.class, float.class, boolean.class, boolean.class})
    public static class DrawPilePosBB {
        @SpirePostfixPatch
        public static void Postfix(ShowCardAndAddToDrawPileEffect __instance, AbstractCard __arg0, float __arg1, float __arg2, boolean __arg3, boolean __arg4) {
            onMiseryCardCreated(__arg0);
        }
    }

    @SpirePatch(clz = ShowCardAndAddToDrawPileEffect.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class, float.class, float.class, boolean.class, boolean.class, boolean.class})
    public static class DrawPilePosBBB {
        @SpirePostfixPatch
        public static void Postfix(ShowCardAndAddToDrawPileEffect __instance, AbstractCard __arg0, float __arg1, float __arg2, boolean __arg3, boolean __arg4, boolean __arg5) {
            onMiseryCardCreated(__arg0);
        }
    }
}