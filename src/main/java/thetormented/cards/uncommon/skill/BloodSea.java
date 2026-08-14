package thetormented.cards.uncommon.skill;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.actions.ApplyBleedAction;
import thetormented.actions.MultiplyBleedAction;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.util.CardStats;

public class BloodSea extends BaseCard {
    public static final String ID = makeID(BloodSea.class.getSimpleName());

    private static final int COST = 1;
    private static final int BLEED_APPLY = 3;
    private static final int BASE_MULTI = 2;
    private static final int UPG_MULTI = 1; // 2 -> 3

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            COST
    );

    public BloodSea() {
        super(ID, info);
        setMagic(BASE_MULTI, UPG_MULTI);
        setCustomVar("BLEED", BLEED_APPLY);
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyBleedAction(m, p, BLEED_APPLY));
        addToBot(new MultiplyBleedAction(m, p, this.magicNumber));
    }

    @Override
    public AbstractCard makecopy() {
        return new BloodSea();
    }
}
