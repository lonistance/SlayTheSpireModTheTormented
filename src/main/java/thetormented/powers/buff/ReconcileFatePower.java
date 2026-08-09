package thetormented.powers.buff;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thetormented.cards.special.status.Misery;
import thetormented.powers.BasePower;

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

    public static void onMiseryCardCreated(AbstractCard card) {
        if (card == null || !Misery.ID.equals(card.cardID)) return;
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasPower(POWER_ID)) {
            ((ReconcileFatePower) AbstractDungeon.player.getPower(POWER_ID)).trigger();
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @SpirePatch(clz = MakeTempCardInHandAction.class, method = "makeNewCard")
    public static class InHandPatch {
        @SpirePostfixPatch
        public static AbstractCard Postfix(AbstractCard __result) {
            onMiseryCardCreated(__result);
            return __result;
        }
    }

    @SpirePatch(clz = MakeTempCardInDiscardAction.class, method = "makeNewCard")
    public static class InDiscardPatch {
        @SpirePostfixPatch
        public static AbstractCard Postfix(AbstractCard __result) {
            onMiseryCardCreated(__result);
            return __result;
        }
    }

    @SpirePatch(clz = MakeTempCardInDrawPileAction.class, method = "update")
    public static class InDrawPilePatchFew {
        @SpireInsertPatch(loc = 65, localvars = {"c"})
        public static void Insert(MakeTempCardInDrawPileAction __instance, AbstractCard c) {
            onMiseryCardCreated(c);
        }
    }

    @SpirePatch(clz = MakeTempCardInDrawPileAction.class, method = "update")
    public static class InDrawPilePatchMany {
        @SpireInsertPatch(loc = 75, localvars = {"c"})
        public static void Insert(MakeTempCardInDrawPileAction __instance, AbstractCard c) {
            onMiseryCardCreated(c);
        }
    }
}
