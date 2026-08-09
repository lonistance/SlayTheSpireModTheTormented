package thetormented.powers.buff;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.WeakPower;
import thetormented.powers.BasePower;

import static thetormented.BasicMod.makeID;

public class HungeringBattleWillPower extends BasePower {
    public static final String POWER_ID = makeID(HungeringBattleWillPower.class.getSimpleName());

    private static final PowerType POWER_TYPE = PowerType.BUFF;
    private static final boolean IS_TURN_BASED = false;

    public HungeringBattleWillPower(AbstractCreature owner, int amount) {
        super(POWER_ID, POWER_TYPE, IS_TURN_BASED, owner, amount);
    }

    @Override
    public void atStartOfTurn() {
        this.flash();
        addToBot(new GainEnergyAction(this.amount));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
