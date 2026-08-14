package thetormented.powers.buff;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import thetormented.powers.BasePower;

import static thetormented.BasicMod.makeID;

public class HungeringBattleWillPower extends BasePower {
    public static final String POWER_ID = makeID(HungeringBattleWillPower.class.getSimpleName());

    private static final PowerType POWER_TYPE = PowerType.BUFF;
    private static final boolean IS_TURN_BASED = false;

    private static final int DAMAGE_AMOUNT = 3;

    public HungeringBattleWillPower(AbstractCreature owner, int amount) {
        super(POWER_ID, POWER_TYPE, IS_TURN_BASED, owner, amount);
    }

    @Override
    public void atStartOfTurn() {
        this.flash();
        addToBot(new GainEnergyAction(this.amount));
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            this.flash();
            addToBot(new DamageAction(this.owner, new DamageInfo(this.owner, DAMAGE_AMOUNT, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}