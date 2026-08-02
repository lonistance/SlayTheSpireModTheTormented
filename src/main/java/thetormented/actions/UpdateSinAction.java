package thetormented.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thetormented.powers.buff.SinPower;


public class UpdateSinAction extends AbstractGameAction {
    private static final int SIN_PER_DEBT = 5; // 5 点 Sin 对应 1 点 Debt
    public UpdateSinAction(AbstractCreature target, AbstractCreature source, int sinAmount) {
        this.target = target;
        this.source = source;
        this.amount = sinAmount; // 可正可负
        this.actionType = ActionType.SPECIAL;
    }

    public UpdateSinAction(AbstractCreature target, AbstractCreature source, int sinAmount, AttackEffect effect) {
        this(target, source, sinAmount);
        this.attackEffect = effect;
    }

    @Override
    public void update() {
        if (this.target != null && this.amount != 0) {
            AbstractCreature p = this.target;
            AbstractPower sinInstance = p.getPower(SinPower.POWER_ID);
            int currentSin = (sinInstance != null) ? sinInstance.amount : 0;
            int currentDebt = currentSin / SIN_PER_DEBT;

            int targetSin = currentSin + this.amount;
            if (targetSin < 0) {
                targetSin = 0;
            }
            int targetDebt = targetSin / SIN_PER_DEBT;

            int debtChange = targetDebt - currentDebt;

            // 如果 Sin 增加了
            if (this.amount > 0) {
                this.addToTop(new UpdateDebtAction(p, this.source, debtChange));
                this.addToTop(new ApplyPowerAction(p, this.source, new SinPower(p, this.amount), this.amount));
            }
            // 如果 Sin 减少了
            else {
                int reduceAmount = -this.amount;
                this.addToTop(new UpdateDebtAction(p, this.source, debtChange));
                this.addToTop(new ReducePowerAction(p, this.source, SinPower.POWER_ID, reduceAmount));
            }
        }
        this.isDone = true;
    }
}