package thetormented.cards.rare.skill;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.actions.VoidCallAction;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.util.CardStats;

public class VoidCall extends BaseCard {
    public static final String ID = makeID(VoidCall.class.getSimpleName());

    private static final int COST = 0;
    private static final int BASE_CARDS = 1;
    private static final int UPG_CARDS = 1; // 1 -> 2

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            COST
    );

    public VoidCall() {
        super(ID, info);
        setMagic(BASE_CARDS, UPG_CARDS);
        setInnate(true);
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new VoidCallAction(this.magicNumber));
    }

    @Override
    public AbstractCard makecopy() {
        return new VoidCall();
    }
}
