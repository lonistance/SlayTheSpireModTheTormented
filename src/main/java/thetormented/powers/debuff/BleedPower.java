package thetormented.powers.debuff;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thetormented.powers.BasePower;
import thetormented.relics.BarbedHook;

import static thetormented.BasicMod.makeID;

public class BleedPower extends BasePower {
    public static final String POWER_ID = makeID(BleedPower.class.getSimpleName());

    private static final PowerType POWER_TYPE = PowerType.DEBUFF;
    private static final boolean IS_TURN_BASED = true;
    private static final int BLEED_INCREMENT_ON_DAMAGE = 1;

    public BleedPower(AbstractCreature owner, int amount) {
        super(POWER_ID, POWER_TYPE, IS_TURN_BASED, owner, null, amount);
    }

    public BleedPower(AbstractCreature owner, AbstractCreature source, int amount) {
        super(POWER_ID, POWER_TYPE, IS_TURN_BASED, owner, source, amount);
    }

    /**
     * 回合开始时：造成等同于流血层数的伤害
     * 检测是否有 DeepWoundPower，有则保留 50% 流血，无则直接移除
     */
    @Override
    public void atStartOfTurn() {
        if (this.amount > 0) {
            this.flash();

            // 1. 造成生命值流失伤害
            DamageInfo bleedDamageInfo = new DamageInfo(this.owner, this.amount, DamageInfo.DamageType.HP_LOSS);
            AbstractGameAction.AttackEffect attackEffect = AbstractGameAction.AttackEffect.BLUNT_LIGHT;
            this.addToBot(new DamageAction(this.owner, bleedDamageInfo, attackEffect));

            // 2. 检测敌人身上是否有 DeepWoundPower
            AbstractPower deepWound = this.owner.getPower(DeepWoundPower.POWER_ID);

            if (deepWound != null) {
                // 有 DeepWoundPower 时，流血层数减少 50%（向上取整，确保例如 1 层时减少 1 层）
                int reduceAmount = (int) Math.ceil(this.amount * 0.5);
                this.addToBot(new ReducePowerAction(this.owner, this.owner, this, reduceAmount));
            } else {
                // 没有 DeepWoundPower 时，全部移除
                this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
            }
        }
    }

    /**
     * 目标受到未被格挡的伤害时：流血层数 +1
     */
    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        boolean isOwnerEnemy = (this.owner != null && !this.owner.isPlayer);
        boolean isNormalDamageType = (info.type == DamageInfo.DamageType.NORMAL);
        boolean tookDamage = (damageAmount > 0);

        if (isOwnerEnemy && isNormalDamageType && tookDamage) {
            this.flash();

            AbstractCreature attacker = info.owner;
            AbstractCreature target = this.owner;
            int increment = BLEED_INCREMENT_ON_DAMAGE;
            // 倒刺钩：玩家施加流血时额外 +1
            if (attacker != null && attacker.isPlayer && ((AbstractPlayer) attacker).hasRelic(BarbedHook.ID)) {
                increment += 1;
            }
            AbstractPower bleedToApply = new BleedPower(target, attacker, increment);

            this.addToBot(new ApplyPowerAction(target, attacker, bleedToApply, increment));
        }
        return damageAmount;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}