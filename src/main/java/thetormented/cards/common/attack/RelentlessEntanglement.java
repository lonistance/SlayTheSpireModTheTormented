package thetormented.cards.common.attack;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.actions.UpdateDebtAction;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.util.CardStats;

public class RelentlessEntanglement extends BaseCard implements UpdateDebtAction.OnDebtChangeSubscriber {
    public static final String ID = makeID(RelentlessEntanglement.class.getSimpleName());

    // 卡牌基础属性常量定义
    private static final CardStats STATS = new CardStats(
            Tormented.Meta.CARD_COLOR,           // 颜色（假设角色使用蓝卡/自定义色板）
            CardType.ATTACK,          // 类型：攻击牌
            CardRarity.COMMON,        // 稀有度：普通（白牌）
            CardTarget.ENEMY,         // 目标：单个敌人
            0                         // 费用：0
    );

    // 数值常量定义（遵循常量不直接参与逻辑运算的规范）
    private static final int BASE_DAMAGE = 5;
    private static final int UPGRADE_DAMAGE = 2; // 升级后伤害加 2 (5 + 2 = 7)

    public RelentlessEntanglement() {
        super(ID, STATS);
        // 设置伤害与升级增加值
        setDamage(BASE_DAMAGE, UPGRADE_DAMAGE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 造成单体伤害
        DamageInfo damageInfo = new DamageInfo(p, this.damage, this.damageTypeForTurn);
        addToBot(new DamageAction(
                m,
                damageInfo,
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL
        ));
    }



    @Override
    public AbstractCard makecopy() {
        return new RelentlessEntanglement();
    }

    @Override
    public void onDebtIncrease(int amount) {
        // 当血债增加 (amount > 0) 且当前卡牌位于弃牌堆中时，将其移回手牌
        if (amount > 0 && AbstractDungeon.player.discardPile.contains(this)) {
            addToBot(new DiscardToHandAction(this));
        }
    }

    @Override
    public void onDebtReduce(int reducedAmount) {
    }
}