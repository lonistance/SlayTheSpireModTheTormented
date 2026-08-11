package thetormented.cards.rare.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.powers.buff.HungeringBattleWillPower;
import thetormented.util.CardStats;

public class HungeringBattleWill extends BaseCard {
    public static final String ID = makeID(HungeringBattleWill.class.getSimpleName());

    private static final int COST = 1;
    private static final int ENERGY_GAIN = 1;

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.RARE,
            CardTarget.SELF,
            COST
    );

    public HungeringBattleWill() {
        super(ID, info);
        setMagic(ENERGY_GAIN, 0);
        setCostUpgrade(0); // 1 -> 0
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new HungeringBattleWillPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractCard makecopy() {
        return new HungeringBattleWill();
    }
}
