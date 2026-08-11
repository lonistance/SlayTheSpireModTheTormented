package thetormented.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class DesperateMeasureAction extends AbstractGameAction {
    private final int damage;
    private final int hpLoss;

    public DesperateMeasureAction(AbstractMonster target, AbstractCreature source, int damage, int hpLoss) {
        this.target = target;
        this.source = source;
        this.damage = damage;
        this.hpLoss = hpLoss;
        this.actionType = ActionType.DAMAGE;
    }

    @Override
    public void update() {
        if (this.target != null && !this.target.isDeadOrEscaped()) {
            AbstractMonster mon = (AbstractMonster) this.target;
            AbstractCreature src = this.source;
            int loss = this.hpLoss;
            this.addToTop(new AbstractGameAction() {
                @Override
                public void update() {
                    if (!mon.isDeadOrEscaped() && mon.currentHealth > 0) {
                        addToTop(new LoseHPAction(src, src, loss));
                    }
                    this.isDone = true;
                }
            });
            this.addToTop(new DamageAction(mon, new DamageInfo(src, this.damage, DamageInfo.DamageType.NORMAL), AttackEffect.BLUNT_HEAVY));
        }
        this.isDone = true;
    }
}
