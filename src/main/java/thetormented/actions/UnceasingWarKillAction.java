package thetormented.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.cards.rare.attack.UnceasingWar;

public class UnceasingWarKillAction extends AbstractGameAction {
    public UnceasingWarKillAction(AbstractMonster target) {
        this.target = target;
        this.actionType = ActionType.DAMAGE;
    }

    @Override
    public void update() {
        if (this.target != null && this.target.isDeadOrEscaped()) {
            UnceasingWar.onKill();
        }
        this.isDone = true;
    }
}
