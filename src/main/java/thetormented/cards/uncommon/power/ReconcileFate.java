package thetormented.cards.uncommon.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.cards.BaseCard;
import thetormented.cards.special.status.Misery;
import thetormented.character.Tormented;
import thetormented.powers.buff.ReconcileFatePower;
import thetormented.util.CardStats;

public class ReconcileFate extends BaseCard {
    public static final String ID = makeID(ReconcileFate.class.getSimpleName());

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );

    public ReconcileFate() {
        super(ID, info);
        setMagic(2, 1);
        this.cardsToPreview = new Misery();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new ReconcileFatePower(p, p, this.magicNumber)));
    }

    @Override
    public AbstractCard makecopy() {
        return new ReconcileFate();
    }
}
