package thetormented.powers.buff;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import thetormented.powers.BasePower;

import static thetormented.BasicMod.makeID;

public class MercyPower extends BasePower {
    public static final String POWER_ID = makeID(MercyPower.class.getSimpleName());

    private static final PowerType POWER_TYPE = PowerType.BUFF;
    private static final boolean IS_TURN_BASED = false;

    public MercyPower(AbstractCreature owner, int amount) {
        super(POWER_ID, POWER_TYPE, IS_TURN_BASED, owner, amount);
    }
    public MercyPower(AbstractCreature owner, AbstractCreature source, int amount) {
        super(POWER_ID, POWER_TYPE, IS_TURN_BASED, owner, source, amount);
    }

    @Override
    public void atStartOfTurn() {
        if (this.amount == 0) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, MercyPower.POWER_ID));
        } else {
            this.addToBot(new ReducePowerAction(this.owner, this.owner, MercyPower.POWER_ID, 1));
        }

    }
    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
