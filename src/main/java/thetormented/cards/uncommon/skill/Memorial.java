package thetormented.cards.uncommon.skill;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.actions.UpdateSinAction;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.util.CardStats;

public class Memorial extends BaseCard {
    public static final String ID = makeID(Memorial.class.getSimpleName());

    private static final int COST = 0;
    private static final int DRAW_AMOUNT = 1;
    private static final int SIN_FALL_BASE = 3;
    private static final int SIN_FALL_UPGRADE = 2; // 3 -> 5

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            COST
    );

    public Memorial() {
        super(ID, info);
        setMagic(SIN_FALL_BASE, SIN_FALL_UPGRADE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DrawCardAction(DRAW_AMOUNT, new AbstractGameAction() {
            @Override
            public void update() {
                if (!DrawCardAction.drawnCards.isEmpty()) {
                    AbstractCard drawn = DrawCardAction.drawnCards.get(0);
                    if (drawn.type == AbstractCard.CardType.SKILL) {
                        addToTop(new UpdateSinAction(p, p, -Memorial.this.magicNumber));
                    }
                }
                this.isDone = true;
            }
        }));
    }

    @Override
    public AbstractCard makecopy() {
        return new Memorial();
    }
}