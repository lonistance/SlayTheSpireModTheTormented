package thetormented.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import thetormented.powers.buff.SinPower;
import thetormented.powers.debuff.DebtPower;


public class UpdateDebtAction extends AbstractGameAction {
    private static final int SIN_PER_DEBT = 5;

    public UpdateDebtAction(AbstractCreature target, AbstractCreature source, int debtChange) {
        this.target = target;
        this.source = source;
        this.amount = debtChange; // Debt 的增减量
        this.actionType = ActionType.DEBUFF;
    }

    @Override
    public void update() {
        if (this.target != null && this.amount != 0) {
            AbstractCreature p = this.target;

            // 1. 增加 Debt 的逻辑
            if (this.amount > 0) {
                // 检测是否有人工制品 (Artifact)
                if (p.hasPower(ArtifactPower.POWER_ID)) {
                    // Debt 施加失败！被 Artifact 抵消。
                    // 按照规则：没施加成功不扣除 Debt，但要扣除刚才多加的 Sin（保证 Sin 依然满足 <= Debt*5 + 4）
                    int currentSin = p.hasPower(SinPower.POWER_ID) ? p.getPower(SinPower.POWER_ID).amount : 0;
                    int currentDebt = p.hasPower(DebtPower.POWER_ID) ? p.getPower(DebtPower.POWER_ID).amount : 0;

                    // 抵消后允许保留的最大 Sin 为: currentDebt * 5 + 4
                    int maxAllowedSin = (currentDebt * SIN_PER_DEBT) + (SIN_PER_DEBT - 1);
                    if (currentSin > maxAllowedSin) {
                        int refundSin = currentSin - maxAllowedSin;
                        this.addToTop(new ReducePowerAction(p, p, SinPower.POWER_ID, refundSin));
                    }
                }

                // 正常提交 ApplyPowerAction（原版 ApplyPowerAction 会自行处理人工制品的消耗动画与逻辑）
                this.addToTop(new ApplyPowerAction(p, this.source, new DebtPower(p, this.amount), this.amount));
            }
            // 2. 减少 Debt 的逻辑
            else {
                int reduceDebtAmount = -this.amount;
                this.addToTop(new ReducePowerAction(p, this.source, DebtPower.POWER_ID, reduceDebtAmount));

                // 触发“Debt 减少”时的钩子，通知所有能力牌/遗物
                for (AbstractPower power : p.powers) {
                    if (power instanceof OnDebtUpdateSubscriber) {
                        ((OnDebtUpdateSubscriber) power).onDebtReduce(reduceDebtAmount);
                    }
                }
            }
        }
        this.isDone = true;
    }

    /**
     * 接口：供检测“Debt 减少”的能力牌/遗物实现
     */
    public interface OnDebtUpdateSubscriber {
        void onDebtReduce(int reducedAmount);
    }
}