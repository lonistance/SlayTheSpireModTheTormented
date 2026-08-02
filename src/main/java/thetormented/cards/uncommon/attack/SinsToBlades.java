package thetormented.cards.uncommon.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.powers.debuff.DebtPower; // 替换为您项目中的血债 Power 类
import thetormented.util.CardStats;

public class SinsToBlades extends BaseCard {
    public static final String ID = makeID(SinsToBlades.class.getSimpleName());

    // 常量定义，避免硬编码参与计算或函数调用
    private static final int CARD_COST = 1;
    private static final int BASE_DAMAGE = 8;
    private static final int UPGRADE_PLUS_DAMAGE = 2;
    private static final int BASE_EXTRA_DRAW = 0;
    private static final int UPGRADE_PLUS_DRAW = 1;

    private static final CardStats STATS = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            CARD_COST
    );

    public SinsToBlades() {
        super(ID, STATS);
        this.baseDamage = BASE_DAMAGE;
        // magicNumber 用于表示“额外抽牌量”（未升级为0，升级后为1）
        this.baseMagicNumber = BASE_EXTRA_DRAW;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 1. 造成伤害
        DamageInfo.DamageType damageType = DamageInfo.DamageType.NORMAL;
        AbstractGameAction.AttackEffect attackEffect = AbstractGameAction.AttackEffect.SLASH_HEAVY;
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, damageType), attackEffect));

        // 2. 计算并执行抽牌
        int currentDebt = getPowerAmount(p);
        int extraDraw = this.magicNumber;
        int totalDrawAmount = currentDebt + extraDraw;

        if (totalDrawAmount > 0) {
            addToBot(new DrawCardAction(p, totalDrawAmount));
        }
    }

    @Override
    public AbstractCard makecopy() {
        return new SinsToBlades();
    }

    /**
     * 辅助获取玩家身上的特定 Power 层数
     */
    private int getPowerAmount(AbstractPlayer player) {
        if (player == null || player.powers == null) {
            return 0;
        }
        for (AbstractPower power : player.powers) {
            if (power != null && DebtPower.POWER_ID.equals(power.ID)) {
                return power.amount;
            }
        }
        return 0;
    }
}