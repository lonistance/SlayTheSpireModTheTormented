package thetormented.cards.rare.skill;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.actions.TriggerBleedAction;
import thetormented.cards.BaseCard;
import thetormented.cards.special.status.Misery;
import thetormented.character.Tormented;
import thetormented.util.CardStats;

public class BloodFeud extends BaseCard {
    public static final String ID = makeID(BloodFeud.class.getSimpleName());

    private static final int COST = 1;
    private static final int BASE_TICKS = 1;
    private static final int UPG_TICKS = 1; // 1 -> 2

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            COST
    );

    public BloodFeud() {
        super(ID, info);
        setMagic(BASE_TICKS, UPG_TICKS);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new MakeTempCardInHandAction(new Misery()));
        addToBot(new TriggerBleedAction(this.magicNumber));
    }

    @Override
    public AbstractCard makecopy() {
        return new BloodFeud();
    }
}
