package thetormented.cards.uncommon.skill;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import thetormented.actions.UpdateDebtAction;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.util.CardStats;

public class Pardon extends BaseCard {
    public static final String ID = makeID(Pardon.class.getSimpleName());

    private static final int COST = 1;

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            COST
    );

    public Pardon() {
        super(ID, info);
        setExhaust(true, false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new UpdateDebtAction(p, p, -1));
        addToBot(new ReducePowerAction(p, p, VulnerablePower.POWER_ID, 1));
        addToBot(new ReducePowerAction(p, p, WeakPower.POWER_ID, 1));
        addToBot(new ReducePowerAction(p, p, FrailPower.POWER_ID, 1));
    }

    @Override
    public AbstractCard makecopy() {
        return new Pardon();
    }
}
