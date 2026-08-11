package thetormented.powers.buff;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thetormented.powers.BasePower;
import thetormented.powers.debuff.DebtPower;

import static thetormented.BasicMod.makeID;

public class SinAvatarPower extends BasePower {
    public static final String POWER_ID = makeID(SinAvatarPower.class.getSimpleName());

    private static final PowerType POWER_TYPE = PowerType.BUFF;
    private static final boolean IS_TURN_BASED = false;
    private static final int BASE_BLOCK = 3;

    public SinAvatarPower(AbstractCreature owner, int amount) {
        super(POWER_ID, POWER_TYPE, IS_TURN_BASED, owner, amount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer) {
            return;
        }
        int debt = 0;
        AbstractPower debtPower = this.owner.getPower(DebtPower.POWER_ID);
        if (debtPower != null) {
            debt = debtPower.amount;
        }
        this.flash();
        addToBot(new GainBlockAction(this.owner, BASE_BLOCK + debt * this.amount));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + BASE_BLOCK + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
    }
}
