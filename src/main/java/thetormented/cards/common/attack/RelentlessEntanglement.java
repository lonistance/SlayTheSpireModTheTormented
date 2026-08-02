package thetormented.cards.common.attack;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnDrawPileShufflePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.util.CardStats;

public class RelentlessEntanglement extends BaseCard implements OnDrawPileShufflePower {
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
    private static final int BASE_DAMAGE = 6;
    private static final int UPGRADE_DAMAGE = 2; // 升级后伤害提升 2 (6 + 2 = 8)

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

    /**
     * 来自 StSLib 的 OnShuffleSubscriber 接口触发函数
     * 当玩家触发洗牌动作（弃牌堆洗入抽牌堆）时自动调用
     */
    @Override
    public void onShuffle() {
        AbstractPlayer player = AbstractDungeon.player;
        if (player == null) {
            return;
        }
        addToBot(new GainBlockAction(player, 1));
        // 检查卡牌当前是否在抽牌堆中，若在则将其移入手牌
        if (player.drawPile.contains(this)) {
            addToBot(new MoveCardsAction(player.hand, player.drawPile, card -> card == this));
        } else if (player.discardPile.contains(this)) {
            addToBot(new MoveCardsAction(player.hand, player.drawPile, card -> card == this));
        }
    }

    @Override
    public AbstractCard makecopy() {
        return new RelentlessEntanglement();
    }
}