package thetormented.cards.rare.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.powers.buff.SinAvatarPower;
import thetormented.powers.debuff.DebtPower;
import thetormented.util.CardStats;

public class SinAvatar extends BaseCard {
    public static final String ID = makeID(SinAvatar.class.getSimpleName());

    private static final int COST = 1;
    private static final int BASE_BLOCK = 4;
    private static final int UPG_BLOCK = 1;
    private static final int DEBT_BONUS = 1;
    private static final int UPG_DEBT_BONUS = 1; // 1 -> 2

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

        // 实时预览：回合结束时总格挡 = 基础格挡 + 血债层数 * 加成
        setCustomVar("TOTAL_BLOCK", VariableType.MAGIC, 0, 0,
                (c, m, base) -> c.baseBlock + getPlayerPowerAmount(DebtPower.POWER_ID) * c.magicNumber);
    }

    @Override
    protected String getInjectedDescription() {
        return extDescription(0);
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
