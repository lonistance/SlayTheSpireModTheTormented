package thetormented.powers.debuff;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import thetormented.powers.BasePower;

import static thetormented.BasicMod.makeID;

public class OverrigidPower extends BasePower {
    public static final String POWER_ID = makeID(OverrigidPower.class.getSimpleName());

    private static final PowerType POWER_TYPE = PowerType.DEBUFF;
    private static final boolean IS_TURN_BASED = false;

    public OverrigidPower(AbstractCreature owner, int amount) {
        super(POWER_ID, POWER_TYPE, IS_TURN_BASED, owner, amount);
    }

    @Override
    public void onAfterCardPlayed(AbstractCard card) {
        if (card.type == AbstractCard.CardType.ATTACK && this.amount > 0) {
            card.baseDamage = Math.max(0, card.baseDamage - this.amount);
            card.damage = Math.max(0, card.damage - this.amount);
            this.flash();
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
