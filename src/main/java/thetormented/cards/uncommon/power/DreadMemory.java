package thetormented.cards.uncommon.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.powers.buff.DreadMemoryPower;
import thetormented.util.CardStats;

public class DreadMemory extends BaseCard {
    public static final String ID = makeID(DreadMemory.class.getSimpleName());

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            2
    );

    public DreadMemory() {
        super(ID, info);
        setMagic(25, 25);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            // 升级后不再增加减伤，改为费用减 1
            upgradeBaseCost(1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new DreadMemoryPower(p, p, this.magicNumber)));
    }

    @Override
    public AbstractCard makecopy() {
        return new DreadMemory();
    }
}
