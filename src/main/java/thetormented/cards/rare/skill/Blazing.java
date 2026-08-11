package thetormented.cards.rare.skill;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.util.CardStats;

import java.util.ArrayList;

public class Blazing extends BaseCard {
    public static final String ID = makeID(Blazing.class.getSimpleName());

    private static final int COST = 1;
    private static final int BASE_DRAW = 2;
    private static final int UPG_DRAW = 1; // 2 -> 3

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            COST
    );

    public Blazing() {
        super(ID, info);
        setMagic(BASE_DRAW, UPG_DRAW);
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> handCards = new ArrayList<>(p.hand.group);

        int statusCount = 0;
        for (AbstractCard c : handCards) {
            if (c.type == CardType.STATUS) {
                statusCount++;
            }
            addToBot(new ExhaustSpecificCardAction(c, p.hand));
        }

        if (statusCount > 0) {
            addToBot(new DrawCardAction(p, this.magicNumber * statusCount));
        }
    }

    @Override
    public AbstractCard makecopy() {
        return new Blazing();
    }
}
