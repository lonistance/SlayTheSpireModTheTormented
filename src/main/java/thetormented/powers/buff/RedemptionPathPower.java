package thetormented.powers.buff;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import thetormented.actions.UpdateDebtAction;
import thetormented.powers.BasePower;

import static thetormented.BasicMod.makeID;

public class RedemptionPathPower extends BasePower implements UpdateDebtAction.OnDebtChangeSubscriber {
    public static final String POWER_ID = makeID(RedemptionPathPower.class.getSimpleName());

    private static final PowerType POWER_TYPE = PowerType.BUFF;
    private static final boolean IS_TURN_BASED = false;

    public RedemptionPathPower(AbstractCreature owner, AbstractCreature source, int amount) {
        super(POWER_ID, POWER_TYPE, IS_TURN_BASED, owner, source, amount);
    }

    @Override
    public void onDebtIncrease(int increasedAmount) {
        // 增加血债不触发抽牌
    }

    @Override
    public void onDebtReduce(int reducedAmount) {
        if (reducedAmount > 0) {
            this.flash();
            this.addToBot(new DrawCardAction(this.owner, reducedAmount * this.amount));
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
