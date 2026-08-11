package thetormented.cards.uncommon.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.powers.buff.RedemptionPathPower;
import thetormented.util.CardStats;

public class RedemptionPath extends BaseCard {
    public static final String ID = makeID(RedemptionPath.class.getSimpleName());

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );

    public RedemptionPath() {
        super(ID, info);
        setMagic(1, 1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new RedemptionPathPower(p, p, this.magicNumber)));
    }

    @Override
    public AbstractCard makecopy() {
        return new RedemptionPath();
    }
}
