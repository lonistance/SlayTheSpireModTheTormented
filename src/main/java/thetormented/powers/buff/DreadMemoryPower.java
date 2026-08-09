package thetormented.powers.buff;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import thetormented.actions.UpdateDebtAction;
import thetormented.powers.BasePower;

import static thetormented.BasicMod.makeID;

public class DreadMemoryPower extends BasePower implements UpdateDebtAction.OnDebtChangeSubscriber {
    public static final String POWER_ID = makeID(DreadMemoryPower.class.getSimpleName());

    private static final PowerType POWER_TYPE = PowerType.BUFF;
    private static final boolean IS_TURN_BASED = true;

    private boolean debtGainedThisTurn = false;

    public DreadMemoryPower(AbstractCreature owner, AbstractCreature source, int amount) {
        super(POWER_ID, POWER_TYPE, IS_TURN_BASED, owner, source, amount);
    }

    @Override
    public void atStartOfTurn() {
        this.debtGainedThisTurn = false;
    }

    @Override
    public void onDebtIncrease(int increasedAmount) {
        this.debtGainedThisTurn = true;
    }

    @Override
    public void onDebtReduce(int reducedAmount) {
        // 减少血债不影响本回合减伤状态
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (this.debtGainedThisTurn && info.owner != null && !info.owner.isPlayer) {
            this.flash();
            return Math.round(damageAmount * (1.0f - this.amount / 100.0f));
        }
        return damageAmount;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
