package thetormented.cards.rare.attack;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.actions.DesperateMeasureAction;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.util.CardStats;

public class DesperateMeasure extends BaseCard {
    public static final String ID = makeID(DesperateMeasure.class.getSimpleName());

    private static final int COST = 1;
    private static final int BASE_DAMAGE = 24;
    private static final int UPG_DAMAGE = 6; // 24 -> 30
    private static final int HP_LOSS = 7;

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.RARE,
            CardTarget.ENEMY,
            COST
    );

    public DesperateMeasure() {
        super(ID, info);
        setDamage(BASE_DAMAGE, UPG_DAMAGE);
        setMagic(HP_LOSS);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DesperateMeasureAction(m, p, this.damage, this.magicNumber));
    }

    @Override
    public AbstractCard makecopy() {
        return new DesperateMeasure();
    }
}
