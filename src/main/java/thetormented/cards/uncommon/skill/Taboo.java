package thetormented.cards.uncommon.skill;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thetormented.actions.RestrictionAction;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.powers.debuff.DebtPower;
import thetormented.util.CardStats;

public class Taboo extends BaseCard {
    public static final String ID = makeID(Taboo.class.getSimpleName());

    private static final int COST = 0;
    private static final int BASE_MAGIC = 0;
    private static final int UPG_MAGIC = 1;

    // 基础配置：0费、罕见、技能牌、目标为自己
    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            COST
    );

    public Taboo() {
        super(ID, info);
        setMagic(BASE_MAGIC, UPG_MAGIC);

        // 实时预览：可获得能量 = 当前血债 + 额外能量（升级 +1）
        setCustomVar("TOTAL_ENERGY", VariableType.MAGIC, 0, 0,
                (c, m, base) -> getPlayerPowerAmount(DebtPower.POWER_ID) + c.magicNumber);
    }

    @Override
    protected String getInjectedDescription() {
        int energy = getPlayerPowerAmount(DebtPower.POWER_ID) + this.magicNumber;
        int index = energy <= 0 ? 0 : (energy <= 3 ? 1 : 2);
        return extDescription(index);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 1. 获取玩家当前的血债层数
        int debtAmount = 0;
        AbstractPower debtPower = p.getPower(DebtPower.POWER_ID);
        if (debtPower != null) {
            debtAmount = debtPower.amount;
        }

        // 2. 计算应获得的能量 (基础血债层数 + 升级后的附加值)
        int energyToGain = debtAmount + this.magicNumber;

        if (energyToGain > 0) {
            // 先压入获得能量的 Action
            this.addToBot(new GainEnergyAction(energyToGain));
        }

        // 3. 后压入施加束缚(Restriction)的 Action
        // 这样可以确保先获得能量，再被套上无法获得能量的 Debuff
        this.addToBot(new RestrictionAction(p, p));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_MAGIC);
            // 升级后改变卡牌描述以体现 "+1" 的效果
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makecopy() {
        return new Taboo();
    }
}