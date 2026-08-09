package thetormented.powers.debuff;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import thetormented.powers.BasePower;

import static thetormented.BasicMod.makeID;

public class DeepWoundPower extends BasePower {
    public static final String POWER_ID = makeID(DeepWoundPower.class.getSimpleName());

    private static final PowerType POWER_TYPE = PowerType.DEBUFF;
    private static final boolean IS_TURN_BASED = true;

    public DeepWoundPower(AbstractCreature owner, int amount) {
        super(POWER_ID, POWER_TYPE, IS_TURN_BASED, owner, null, amount);
    }

    public DeepWoundPower(AbstractCreature owner, AbstractCreature source, int amount) {
        super(POWER_ID, POWER_TYPE, IS_TURN_BASED, owner, source, amount);
    }

    /**
     * 敌人回合结束时：自身层数 -1
     * 独立于 BleedPower 处理，确保无论是否有流血，此 Debuff 都会按回合自然衰减
     */
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        // 如果此 Debuff 挂在敌人身上（非玩家），在敌人回合结束时减少 1 层
        if (!isPlayer && this.amount > 0) {
            this.addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}