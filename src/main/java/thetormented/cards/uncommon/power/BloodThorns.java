package thetormented.cards.uncommon.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.powers.buff.BloodThornsPower;
import thetormented.util.CardStats;

public class BloodThorns extends BaseCard {
    public static final String ID = makeID(BloodThorns.class.getSimpleName());

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );

    public BloodThorns() {
        super(ID, info);
        setMagic(3, 1); // 回调：基础数值 +1（3 -> 4）
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new BloodThornsPower(p, p, this.magicNumber)));
    }

    @Override
    public AbstractCard makecopy() {
        return new BloodThorns();
    }
}
