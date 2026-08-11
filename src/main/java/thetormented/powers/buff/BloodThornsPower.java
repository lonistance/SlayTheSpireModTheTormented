package thetormented.powers.buff;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import thetormented.actions.ApplyBleedAction;
import thetormented.powers.BasePower;

import static thetormented.BasicMod.makeID;

public class BloodThornsPower extends BasePower {
    public static final String POWER_ID = makeID(BloodThornsPower.class.getSimpleName());

    private static final PowerType POWER_TYPE = PowerType.BUFF;
    private static final boolean IS_TURN_BASED = false;

    public BloodThornsPower(AbstractCreature owner, AbstractCreature source, int amount) {
        super(POWER_ID, POWER_TYPE, IS_TURN_BASED, owner, source, amount);
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.owner != null && !info.owner.isPlayer) {
            this.flash();
            this.addToBot(new ApplyBleedAction(info.owner, this.owner, this.amount));
        }
        return damageAmount;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
