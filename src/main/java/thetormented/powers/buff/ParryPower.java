package thetormented.powers.buff;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import thetormented.powers.BasePower;

import static thetormented.BasicMod.makeID;

public class ParryPower extends BasePower {
    public static final String POWER_ID = makeID(ParryPower.class.getSimpleName());

    private static final PowerType POWER_TYPE = PowerType.BUFF;
    private static final boolean IS_TURN_BASED = true;

    public ParryPower(AbstractCreature owner, int amount) {
        super(POWER_ID, POWER_TYPE, IS_TURN_BASED, owner, amount);
    }

    public ParryPower(AbstractCreature owner, AbstractCreature source, int amount) {
        super(POWER_ID, POWER_TYPE, IS_TURN_BASED, owner, source, amount);
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        // Only reflect normal attacks from other creatures.
        // info.output is the damage before block, damageAmount is the damage after block,
        // so the difference is exactly the amount of damage that was blocked.
        if (info.type == DamageInfo.DamageType.NORMAL && info.owner != null && info.owner != this.owner) {
            int blocked = Math.max(0, info.output - damageAmount);
            if (blocked > 0) {
                this.flash();
                this.addToBot(new DamageAction(info.owner, new DamageInfo(this.owner, blocked, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            }
        }
        return damageAmount;
    }

    @Override
    public void atStartOfTurn() {
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
