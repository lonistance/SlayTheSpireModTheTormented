package thetormented.cards.common.skill;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.powers.debuff.DebtPower;  // 替换为你实际的“血债”Power类路径
import thetormented.util.CardStats;

public class PleaOfInnocence extends BaseCard {
    public static final String ID = makeID(PleaOfInnocence.class.getSimpleName());

    private static final int COST = 1;
    private static final int BASE_BLOCK = 5;
    private static final int UPGRADE_BLOCK = 2; // 升级后增加2点基础格挡 (5 -> 7)
    private static final int BASE_MAGIC = 3;
    private static final int UPGRADE_MAGIC = 1; // 升级后增加1点血债加成 (3 -> 4)

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public PleaOfInnocence() {
        super(ID, new CardStats(
                Tormented.Meta.CARD_COLOR, // 替换为你的角色卡牌颜色Enum
                TYPE,
                RARITY,
                TARGET,
                COST
        ));

        // 设置基础格挡与基础 MagicNumber（血债系数）
        setBlock(BASE_BLOCK, UPGRADE_BLOCK);
        setMagic(BASE_MAGIC, UPGRADE_MAGIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 计算初始格挡数值
        int totalBlock = this.block;

        // 获取玩家当前的“血债”层数
        int debtAmount = 0;
        AbstractPower debtPower = p.getPower(DebtPower.POWER_ID);
        if (debtPower != null) {
            debtAmount = debtPower.amount;
        }

        // 加上血债带来的额外格挡：血债层数 * magicNumber
        if (debtAmount > 0) {
            int extraBlock = debtAmount * this.magicNumber;
            totalBlock += extraBlock;
        }

        // 获得最终格挡
        addToBot(new GainBlockAction(p, p, totalBlock));
    }

    @Override
    public AbstractCard makecopy() {
        return new PleaOfInnocence();
    }
}