package thetormented.cards.special.curse;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.util.CardStats;

public class Entangled extends BaseCard {
    public static final String ID = makeID(Entangled.class.getSimpleName());
    private static final CardStats info = new CardStats(
            CardColor.CURSE,
            CardType.CURSE,
            CardRarity.CURSE,
            CardTarget.NONE,
            1
    );

    public Entangled() {
        super(ID, info);
        setExhaust(true);
    }

    @Override
    public void triggerWhenDrawn() {
        addToTop(new MakeTempCardInHandAction(makeStatEquivalentCopy(), 1));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public AbstractCard makecopy() {
        return new Entangled();
    }
}
