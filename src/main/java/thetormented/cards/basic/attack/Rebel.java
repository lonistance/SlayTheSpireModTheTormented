package thetormented.cards.basic.attack;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.powers.debuff.BleedPower; // 假设流血 Power 路径
import thetormented.util.CardStats;

public class Rebel extends BaseCard {
    public static final String ID = makeID(Rebel.class.getSimpleName());

    // 卡牌基础属性常量定义
    private static final CardStats STATS = new CardStats(
            Tormented.Meta.CARD_COLOR,           // 颜色：基础/角色卡
            CardType.ATTACK,          // 类型：攻击牌
            CardRarity.BASIC,         // 稀有度：基础牌 (Basic)
            CardTarget.ENEMY,         // 目标：单个敌人
            1                         // 费用：1
    );

    // 数值常量定义（严禁硬编码直接参与计算）
    private static final int BASE_DAMAGE = 4;
    private static final int UPGRADE_DAMAGE = 2; // 升级后伤害提升 2 (4 + 2 = 6)
    private static final int ZERO_AMOUNT = 0;

    public Rebel() {
        super(ID, STATS);
        // 设置伤害与升级增长
        setDamage(BASE_DAMAGE, UPGRADE_DAMAGE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            // 1. 重新计算并获取经 Vulnerable(易伤)、Strength(力量) 等结算后的预计纸面伤害
            this.calculateCardDamage(m);
            int calculatedDamage = this.damage;

            // 2. 执行伤害结算 Action（若敌人有格挡，将会优先扣除格挡）
            DamageInfo damageInfo = new DamageInfo(p, this.damage, this.damageTypeForTurn);
            addToBot(new DamageAction(m, damageInfo, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));

            // 3. 计算出在敌人受到物理伤害（并被格挡扣减）之前的纸面伤害，转化为流血施加给敌人
            if (calculatedDamage > ZERO_AMOUNT) {
                setMagic(calculatedDamage);
                addToBot(new ApplyPowerAction(m, p, new BleedPower(m, p, calculatedDamage), calculatedDamage));
            }
        }
    }

    @Override
    public AbstractCard makecopy() {
        return new Rebel();
    }
}