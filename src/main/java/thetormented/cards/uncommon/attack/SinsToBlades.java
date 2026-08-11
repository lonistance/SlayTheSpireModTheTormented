package thetormented.cards.uncommon.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
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
        setDamage(BASE_DAMAGE, UPGRADE_PLUS_DAMAGE);
        setMagic(BASE_EXTRA_DRAW, UPGRADE_PLUS_DRAW);

        // 实时预览：抽牌数 = 当前血债 + 额外抽牌
        setCustomVar("TOTAL_DRAW", VariableType.MAGIC, 0, 0,
                (c, m, base) -> getPowerAmount() + c.magicNumber);
    }

    @Override
    protected String getInjectedDescription() {
        return extDescription(0);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 1. 造成伤害
        DamageInfo.DamageType damageType = DamageInfo.DamageType.NORMAL;
        AbstractGameAction.AttackEffect attackEffect = AbstractGameAction.AttackEffect.SLASH_HEAVY;
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, damageType), attackEffect));

        // 2. 计算并执行抽牌
        int currentDebt = getPowerAmount();
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
    private int getPowerAmount() {
        return getPlayerPowerAmount(DebtPower.POWER_ID);
    }
}