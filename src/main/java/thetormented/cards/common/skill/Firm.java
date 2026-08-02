package thetormented.cards.common.skill;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.util.CardStats;

public class Firm extends BaseCard {
    public static final String ID = makeID(Firm.class.getSimpleName());

    // 基础数值常量
    private static final int CARD_COST = 1;
    private static final int BASE_BLOCK = 9;
    private static final int UPGRADE_BLOCK_ADD = 3; // 升级增加3点格挡 (9 + 3 = 12)

    // 卡牌属性配置
    private static final CardType CARD_TYPE = CardType.SKILL;
    private static final CardRarity CARD_RARITY = CardRarity.COMMON;
    private static final CardTarget CARD_TARGET = CardTarget.SELF;

    public Firm() {
        super(ID, new CardStats(
                Tormented.Meta.CARD_COLOR, // 替换为你的角色卡牌颜色
                CARD_TYPE,
                CARD_RARITY,
                CARD_TARGET,
                CARD_COST
        ));
        setBlock(BASE_BLOCK, UPGRADE_BLOCK_ADD);
        setSelfRetain(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 获得格挡 Action
        int blockAmount = this.block;
        addToBot(new GainBlockAction(p, p, blockAmount));
    }

    @Override
    public AbstractCard makecopy() {
        return new Firm();
    }

}