package thetormented.cards.uncommon.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.powers.buff.RelentlessBleedPower;
import thetormented.util.CardStats;

public class RelentlessBleed extends BaseCard {
    public static final String ID = makeID(RelentlessBleed.class.getSimpleName());

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );

    public RelentlessBleed() {
        super(ID, info);
        setMagic(3, 1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new RelentlessBleedPower(p, p, this.magicNumber)));
    }

    @Override
    public AbstractCard makecopy() {
        return new RelentlessBleed();
    }
}
