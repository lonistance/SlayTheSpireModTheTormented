package thetormented.powers.debuff;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import thetormented.powers.BasePower;

import static thetormented.BasicMod.makeID;


public class LoseEnergyNextTurnPower extends BasePower {
    public static final String POWER_ID = makeID(LoseEnergyNextTurnPower.class.getSimpleName());
    private static final PowerType TYPE = PowerType.DEBUFF;
    private static final boolean TURN_BASED = true;

    public LoseEnergyNextTurnPower(AbstractCreature owner, AbstractCreature source, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, source, amount);
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
        //偿还能量
        addToBot(new LoseEnergyAction(amount));
        //一次性债务
        this.amount = 0;
        addToBot(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
    }
}