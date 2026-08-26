package thetormented.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.cards.rare.attack.UnceasingWar;

public class UnceasingWarKillAction extends AbstractGameAction {
    private final boolean countKill;

    public UnceasingWarKillAction(AbstractMonster target, boolean countKill) {
        this.target = target;
        this.countKill = countKill;
        this.actionType = ActionType.DAMAGE;
    }

    @Override
    public void update() {
        if (this.countKill && this.target != null && this.target.isDeadOrEscaped()) {
            UnceasingWar.onKill();
        }
        this.isDone = true;
    }
}