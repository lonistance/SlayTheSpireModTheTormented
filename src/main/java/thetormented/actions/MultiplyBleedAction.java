package thetormented.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thetormented.powers.debuff.BleedPower;

public class MultiplyBleedAction extends AbstractGameAction {
    private final int multiplier;

    public MultiplyBleedAction(AbstractCreature target, AbstractCreature source, int multiplier) {
        this.target = target;
        this.source = source;
        this.multiplier = multiplier;
        this.actionType = ActionType.DEBUFF;
    }

    @Override
    public void update() {
        if (this.target != null && this.multiplier > 1) {
            AbstractPower bleed = this.target.getPower(BleedPower.POWER_ID);
            if (bleed != null && bleed.amount > 0) {
                int extra = (this.multiplier - 1) * bleed.amount;
                this.addToTop(new ApplyBleedAction(this.target, this.source, extra));
            }
        }
        this.isDone = true;
    }
}
