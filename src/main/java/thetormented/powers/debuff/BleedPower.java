package thetormented.powers.debuff;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thetormented.powers.BasePower;

import static thetormented.BasicMod.makeID;

public class BleedPower extends BasePower {
    // 基础配置常量
    public static final String POWER_ID = makeID(BleedPower.class.getSimpleName());

    // 数值与行为配置变量（便于后续调整数值）
    private static final PowerType POWER_TYPE = PowerType.DEBUFF;
    private static final boolean IS_TURN_BASED = true;
    private static final int BLEED_INCREMENT_ON_DAMAGE = 1;

    public BleedPower(AbstractCreature owner, int amount) {
        super(POWER_ID, POWER_TYPE, IS_TURN_BASED, owner, null, amount);
    }
    public BleedPower(AbstractCreature owner, AbstractCreature source,int amount) {
        super(POWER_ID, POWER_TYPE, IS_TURN_BASED, owner, source, amount);
    }

    /**
     * 回合开始时：受到等同于流血层数的伤害，然后移除该能力
     */
    @Override
    public void atStartOfTurn() {
        if (this.amount > 0) {
            // 闪烁特效与音效
            this.flash();

            // 造成未忽略格挡/忽略护甲的直接伤害（可根据需求改为 HP_LOSS）
            DamageInfo bleedDamageInfo = new DamageInfo(this.owner, this.amount, DamageInfo.DamageType.HP_LOSS);
            AbstractGameAction.AttackEffect attackEffect = AbstractGameAction.AttackEffect.BLUNT_LIGHT;
            this.addToBot(new DamageAction(this.owner, bleedDamageInfo, attackEffect));

            // 结算伤害后移除流血状态
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }

    /**
     * 敌人/目标受到未被格挡的伤害时：流血层数 +1
     *
     * @return
     */
    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        // 条件判断：如果是敌人/目标受击、伤害类型为普通攻击/卡牌伤害、且扣除了真实生命值（damageAmount > 0）
        boolean isOwnerEnemy = (this.owner != null && !this.owner.isPlayer);
        boolean isNormalDamageType = (info.type == DamageInfo.DamageType.NORMAL);
        boolean tookDamage = (damageAmount > 0);

        if (isOwnerEnemy && isNormalDamageType && tookDamage) {
            this.flash();

            // 触发增加层数
            AbstractCreature attacker = info.owner;
            AbstractCreature target = this.owner;
            AbstractPower bleedToApply = new BleedPower(target, BLEED_INCREMENT_ON_DAMAGE);

            this.addToBot(new ApplyPowerAction(target, attacker, bleedToApply, BLEED_INCREMENT_ON_DAMAGE));
        }
        return damageAmount;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}