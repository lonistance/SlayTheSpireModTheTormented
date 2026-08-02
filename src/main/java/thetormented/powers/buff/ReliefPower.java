package thetormented.powers.buff;

import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import thetormented.cards.special.statue.Misery;
import thetormented.powers.BasePower;

import java.util.ArrayList;

import static thetormented.BasicMod.makeID;

public class ReliefPower extends BasePower {
    public static final String POWER_ID = makeID(ReliefPower.class.getSimpleName());
    private static final PowerType TYPE = PowerType.BUFF;

    public ReliefPower(AbstractCreature owner) {
        super(POWER_ID, TYPE, false, owner, owner, -1);
    }

    @Override
    public void updateDescription(){
        this.description = DESCRIPTIONS[0] + Misery.class.getSimpleName() + DESCRIPTIONS[1];
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if(!isPlayer)
            return;
        if(!(owner instanceof AbstractPlayer))
            return;
        AbstractPlayer player = (AbstractPlayer) owner;
        ArrayList<AbstractCard> cards = new ArrayList<>(player.hand.group);

        for(AbstractCard card : cards) {
            if(card.cardID.equals(Misery.ID)) {
                addToBot(new ExhaustSpecificCardAction(card, player.hand));
            }
        }
    }
}
