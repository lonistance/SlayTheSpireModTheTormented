package thetormented.cards.rare.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.powers.buff.BloodbathPower;
import thetormented.util.CardStats;

public class Bloodbath extends BaseCard {
    public static final String ID = makeID(Bloodbath.class.getSimpleName());

    private static final int COST = 2;
    private static final int BASE_THRESHOLD = 50;
    private static final int UPG_THRESHOLD = 25;

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.RARE,
            CardTarget.SELF,
            COST
    );

    public Bloodbath() {
        super(ID, info);
        setMagic(BASE_THRESHOLD, UPG_THRESHOLD);
        this.baseEthereal = true;
        this.isEthereal = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new BloodbathPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractCard makecopy() {
        return new Bloodbath();
    }
}
