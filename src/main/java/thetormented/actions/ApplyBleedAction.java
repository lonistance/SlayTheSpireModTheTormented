package thetormented.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thetormented.powers.debuff.BleedPower;
import thetormented.relics.BarbedHook;

public class ApplyBleedAction extends AbstractGameAction {
    // 默认动画/音效配置常量（便于后期统一修改）
    private static final AttackEffect DEFAULT_EFFECT = AttackEffect.POISON;

    public ApplyBleedAction(AbstractCreature target, AbstractCreature source, int amount) {
        this.target = target;
        this.source = source;
        this.amount = amount;
        this.actionType = ActionType.DEBUFF;
        this.attackEffect = DEFAULT_EFFECT;
    }

    public ApplyBleedAction(AbstractCreature target, AbstractCreature source, int amount, AttackEffect effect) {
        this(target, source, amount);
        this.attackEffect = effect;
    }

    @Override
    public void update() {
        if (this.target != null && this.amount > 0) {
            AbstractCreature actionTarget = this.target;
            AbstractCreature actionSource = this.source;
            int bleedAmount = this.amount;

            // 倒刺钩：玩家给予流血时额外 +1
            if (actionSource != null && actionSource.isPlayer && ((AbstractPlayer) actionSource).hasRelic(BarbedHook.ID)) {
                bleedAmount += 1;
            }

            // 构造 BleedPower 实体并提交 ApplyPowerAction 队列
            AbstractPower powerToApply = new BleedPower(actionTarget, bleedAmount);
            ApplyPowerAction applyPowerAction = new ApplyPowerAction(
                    actionTarget,
                    actionSource,
                    powerToApply,
                    bleedAmount,
                    this.attackEffect
            );

        this.addToTop(applyPowerAction);

        this.notifyBleedApplied(bleedAmount);
    }

    this.isDone = true;
}

    private void notifyBleedApplied(int appliedAmount) {
        if (this.source != null && this.source.isPlayer) {
            for (AbstractPower power : this.source.powers) {
                if (power instanceof OnBleedApplySubscriber) {
                    ((OnBleedApplySubscriber) power).onBleedApplied(this.target, appliedAmount);
                }
            }
        }
    }

    /**
     * 接口：供检测“玩家向敌人施加流血”的能力牌实现
     */
    public interface OnBleedApplySubscriber {
        void onBleedApplied(AbstractCreature target, int amount);
    }
}