package thetormented.relics;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thetormented.character.Tormented;

import static thetormented.BasicMod.makeID;

public class  GargoyleArm extends BaseRelic {
    public static final String ID = makeID(GargoyleArm.class.getSimpleName());
    private int lastDebuffTotal = 0;

    public GargoyleArm() {
        super(ID, "gargoyleArm", Tormented.Meta.CARD_COLOR, RelicTier.UNCOMMON, LandingSound.SOLID);
    }

    @Override
    public void atBattleStartPreDraw() {
        lastDebuffTotal = totalDebuffStacks();
    }

    @Override
    public void atTurnStart() {
        compareAndGrant();
    }

    @Override
    public void onPlayerEndTurn() {
        compareAndGrant();
    }

    private void compareAndGrant() {
        int current = totalDebuffStacks();
        int removed = lastDebuffTotal - current;
        if (removed > 0) {
            flash();
            addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, removed));
        }
        lastDebuffTotal = current;
    }

    private int totalDebuffStacks() {
        int total = 0;
        for (AbstractPower p : AbstractDungeon.player.powers) {
            if (p.type == AbstractPower.PowerType.DEBUFF) {
                total += p.amount;
            }
        }
        return total;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new GargoyleArm();
    }
}
