package thetormented.powers.buff;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.powers.BasePower;

import static thetormented.BasicMod.makeID;

public class IndignationPower extends BasePower {
    public static final String POWER_ID = makeID(IndignationPower.class.getSimpleName());

    private static final PowerType POWER_TYPE = PowerType.BUFF;
    private static final boolean IS_TURN_BASED = true;

    private boolean usedThisTurn = true;
    private boolean firstTurnSkipped = false;

    public IndignationPower(AbstractCreature owner, int amount) {
        super(POWER_ID, POWER_TYPE, IS_TURN_BASED, owner, amount);
    }

    @Override
    public void atStartOfTurn() {
        this.usedThisTurn = false;
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (!this.usedThisTurn) {
            this.usedThisTurn = true;
            card.costForTurn = 0;
            card.isCostModifiedForTurn = true;
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayerTurn) {
        if (isPlayerTurn) {
            if (!this.firstTurnSkipped) {
                this.firstTurnSkipped = true;
                return;
            }
            this.amount--;
            if (this.amount <= 0) {
                addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
            } else {
                updateDescription();
            }
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
