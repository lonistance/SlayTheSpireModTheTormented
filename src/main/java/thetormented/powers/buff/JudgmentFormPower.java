package thetormented.powers.buff;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import thetormented.powers.BasePower;

import static thetormented.BasicMod.makeID;

public class JudgmentFormPower extends BasePower {
    public static final String POWER_ID = makeID(JudgmentFormPower.class.getSimpleName());

    private static final PowerType POWER_TYPE = PowerType.BUFF;
    private static final boolean IS_TURN_BASED = false;
    private static final float BONUS_MULTIPLIER = 0.5f;

    public JudgmentFormPower(AbstractCreature owner, int amount) {
        super(POWER_ID, POWER_TYPE, IS_TURN_BASED, owner, amount);
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.type != DamageInfo.DamageType.NORMAL) {
            return;
        }
        if (target == null || target == this.owner || target.isDeadOrEscaped()) {
            return;
        }
        if (damageAmount <= 0) {
            return;
        }
        if (target.currentHealth >= target.maxHealth * this.amount / 100.0f) {
            return;
        }
        int bonus = Math.round(damageAmount * BONUS_MULTIPLIER);
        if (bonus > 0) {
            this.addToBot(new DamageAction(target, new DamageInfo(null, bonus, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.NONE));
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
