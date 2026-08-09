package thetormented.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import thetormented.powers.debuff.RestrictionPower;

public class RestrictionAction extends AbstractGameAction {

    public RestrictionAction(AbstractCreature target, AbstractCreature source) {
        this.target = target;
        this.source = source;
        this.actionType = ActionType.DEBUFF;
    }

    @Override
    public void update() {
        // 判断目标身上是否已经有该 Debuff，没有才挂载（保证不可叠加）
        if (this.target != null && !this.target.hasPower(RestrictionPower.POWER_ID)) {
            // 使用 addToTop 确保它立刻插队生效
            this.addToTop(new ApplyPowerAction(this.target, this.source, new RestrictionPower(this.target)));
        }
        this.isDone = true;
    }
}