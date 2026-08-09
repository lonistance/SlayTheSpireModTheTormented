package thetormented.cards.rare.skill;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.actions.ApplyBleedAction;
import thetormented.actions.SpreadAction;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.util.CardStats;

public class Spread extends BaseCard {
    public static final String ID = makeID(Spread.class.getSimpleName());

    private static final int COST = 2;
    private static final int BASE_BLEED = 6;
    private static final int UPG_BLEED = 4; // 6 -> 10

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.ENEMY,
            COST
    );

    public Spread() {
        super(ID, info);
        setMagic(BASE_BLEED, UPG_BLEED);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyBleedAction(m, p, this.magicNumber));
        addToBot(new SpreadAction(m, p));
    }

    @Override
    public AbstractCard makecopy() {
        return new Spread();
    }
}
