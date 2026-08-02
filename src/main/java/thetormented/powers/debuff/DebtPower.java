package thetormented.powers.debuff;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import thetormented.powers.BasePower;
import thetormented.powers.buff.MercyPower;

import static thetormented.BasicMod.makeID;


public class DebtPower extends BasePower {
    public static final String POWER_ID = makeID(DebtPower.class.getSimpleName());
    private static final PowerType POWER_TYPE = PowerType.DEBUFF;
    private static final boolean IS_TURN_BASED = true;

    // 每 1 点 Debt 增加 10% 伤害
    private static final float DAMAGE_INCREASE_PER_STACK = 0.10f;

    public DebtPower(AbstractCreature owner, int amount) {
        super(POWER_ID, POWER_TYPE, IS_TURN_BASED, owner, amount);
    }

    public DebtPower(AbstractCreature owner, AbstractCreature source, int amount) {
        super(POWER_ID, POWER_TYPE, IS_TURN_BASED, owner, source,amount);
    }

    /**
     * 受到伤害时增伤 10% * Debt 层数
     */
    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            if(owner.hasPower(MercyPower.POWER_ID)) {
                return damage;
            }
            float multiplier = 1.0f + (this.amount * DAMAGE_INCREASE_PER_STACK);
            return damage * multiplier;
        }
        return damage;
    }

    @Override
    public void updateDescription() {
        int totalPercent = this.amount * (int) (DAMAGE_INCREASE_PER_STACK * 100);
        this.description = DESCRIPTIONS[0] + totalPercent + DESCRIPTIONS[1];
    }
}