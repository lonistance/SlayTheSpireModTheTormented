package thetormented.cards.rare.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.powers.debuff.OverrigidPower;
import thetormented.util.CardStats;

public class Overrigid extends BaseCard {
    public static final String ID = makeID(Overrigid.class.getSimpleName());

    private static final int COST = 1;
    private static final int STRENGTH = 4;
    private static final int PENALTY = 2;
    private static final int UPG_PENALTY = -1; // 2 -> 1

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.RARE,
            CardTarget.SELF,
            COST
    );

    public Overrigid() {
        super(ID, info);
        setMagic(PENALTY, UPG_PENALTY);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, STRENGTH), STRENGTH));
        addToBot(new ApplyPowerAction(p, p, new OverrigidPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractCard makecopy() {
        return new Overrigid();
    }
}
