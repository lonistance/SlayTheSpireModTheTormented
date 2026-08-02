package thetormented.cards.common.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.util.CardStats;

public class Hostility extends BaseCard {
    public static final String ID = makeID(Hostility.class.getSimpleName());

    // 声明常量变量，避免任何数字硬编码参与计算或函数调用
    private static final int CARD_COST = 1;
    private static final int BASE_BLOCK = 6;
    private static final int UPGRADE_BLOCK = 2;
    private static final int BASE_WEAK_AMOUNT = 1;
    private static final int UPGRADE_PLUS_WEAK = 1;
    private static final int BLOCK_THRESHOLD = 5;

    private static final CardStats STATS = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            CARD_COST
    );

    public Hostility() {
        super(ID, STATS);
        setBlock(BASE_BLOCK, UPGRADE_BLOCK);
        setMagic(BASE_WEAK_AMOUNT, UPGRADE_PLUS_WEAK);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 条件判断：若玩家当前格挡小于或等于判定阈值（不大于5点）
        int currentBlock = p.currentBlock;
        int weakStacks = this.magicNumber;

        if (currentBlock <= BLOCK_THRESHOLD) {
            // 遍历所有活着的敌人并施加虚弱
            for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (!mo.isDeadOrEscaped()) {
                    addToBot(new ApplyPowerAction(mo, p, new WeakPower(mo, weakStacks, false), weakStacks));
                }
            }
        }

        // 无论是否触发条件，均获得格挡
        addToBot(new GainBlockAction(p, p, this.block));
    }

    @Override
    public AbstractCard makecopy() {
        return new Hostility();
    }
}