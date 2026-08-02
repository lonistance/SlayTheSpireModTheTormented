package thetormented.powers.buff;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import thetormented.powers.BasePower;

import static thetormented.BasicMod.makeID;


public class GainEnergyNextTurnPower extends BasePower {
    public static final String POWER_ID = makeID(GainEnergyNextTurnPower.class.getSimpleName());
    private static final PowerType TYPE = PowerType.DEBUFF;
    private static final boolean TURN_BASED = false;

    public GainEnergyNextTurnPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public void atStartOfTurn() {
        // 闪烁特效与音效
        this.flash();
        if(amount <= 0)
            return;
        addToBot(new GainEnergyAction(amount));
        this.amount = 0;
        addToBot(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
    }
}