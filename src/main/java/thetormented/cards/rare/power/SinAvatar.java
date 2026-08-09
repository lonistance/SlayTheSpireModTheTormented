package thetormented.cards.rare.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.powers.buff.SinAvatarPower;
import thetormented.util.CardStats;

public class SinAvatar extends BaseCard {
    public static final String ID = makeID(SinAvatar.class.getSimpleName());

    private static final int COST = 1;
    private static final int BASE_BLOCK = 3;
    private static final int UPG_BLOCK = 0;
    private static final int DEBT_BONUS = 2;
    private static final int UPG_DEBT_BONUS = 1; // 2 -> 3

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.RARE,
            CardTarget.SELF,
            COST
    );

    public SinAvatar() {
        super(ID, info);
        setBlock(BASE_BLOCK, UPG_BLOCK);
        setMagic(DEBT_BONUS, UPG_DEBT_BONUS);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new SinAvatarPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractCard makecopy() {
        return new SinAvatar();
    }
}
