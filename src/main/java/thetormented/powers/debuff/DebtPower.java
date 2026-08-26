package thetormented.powers.debuff;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import thetormented.powers.BasePower;
import thetormented.powers.buff.MercyPower;
import thetormented.powers.buff.SinPower;

import static thetormented.BasicMod.makeID;


public class DebtPower extends BasePower {
    public static final String POWER_ID = makeID(DebtPower.class.getSimpleName());
    private static final PowerType POWER_TYPE = PowerType.DEBUFF;
    private static final boolean IS_TURN_BASED = false;

    // 每 1 点 Debt 增加 10% 伤害
    private static final float DAMAGE_INCREASE_PER_STACK = 0.10f;

    public DebtPower(AbstractCreature owner, int amount) {
        super(POWER_ID, POWER_TYPE, IS_TURN_BASED, owner, amount);
    }

    public DebtPower(AbstractCreature owner, AbstractCreature source, int amount) {
        super(POWER_ID, POWER_TYPE, IS_TURN_BASED, owner, source,amount);
    }

    /**
     * 在所有常规 atDamageReceive (如易伤) 执行完毕后，统一调整最终伤害
     */
    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            // 1. 如果拥有 MercyPower (慈悲)，直接返回原伤害
            if (owner.hasPower(MercyPower.POWER_ID)) {
                return damage;
            }

            // 2. 获取 Debt 增加的百分比（如 2 层 Debt = 0.20f）
            float debtPercent = this.amount * DAMAGE_INCREASE_PER_STACK;

            // 3. 判断当前目标是否有易伤，并拿到易伤的增加比例 (vulnPercent)
            if (owner.hasPower(VulnerablePower.POWER_ID)) {
                float vulnMultiplier = 1.5f;
                if (owner.isPlayer && AbstractDungeon.player.hasRelic("Odd Mushroom")) {
                    vulnMultiplier = 1.25f;
                }
                // 核心逻辑：
                // 目前传入的 damage 已经被 VulnerablePower 乘以了 vulnMultiplier。
                // 即：damage = baseDamage * vulnMultiplier
                // 我们期望的目标伤害是：targetDamage = baseDamage * (vulnMultiplier + debtPercent)
                // 转换公式：targetDamage = damage * ((vulnMultiplier + debtPercent) / vulnMultiplier)
                return damage * ((vulnMultiplier + debtPercent) / vulnMultiplier);
            } else {
                // 如果没有易伤，直接按 (1 + debtPercent) 增加伤害
                return damage * (1.0f + debtPercent);
            }
        }
        return damage;
    }

    @Override
    public void updateDescription() {
        int totalPercent = this.amount * (int) (DAMAGE_INCREASE_PER_STACK * 100);
        this.description = DESCRIPTIONS[0] + totalPercent + DESCRIPTIONS[1] + SinPower.SIN_PER_DEBT + DESCRIPTIONS[2];
    }
}