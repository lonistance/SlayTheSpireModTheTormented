package thetormented.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thetormented.powers.debuff.BleedPower;

public class TriggerBleedAction extends AbstractGameAction {
    private final int percent;

    public TriggerBleedAction(int percent) {
        this.percent = percent;
        this.actionType = ActionType.DAMAGE;
    }

    @Override
    public void update() {
        for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
            if (mo == null || mo.isDeadOrEscaped()) {
                continue;
            }
            AbstractPower bleed = mo.getPower(BleedPower.POWER_ID);
            if (bleed == null || bleed.amount <= 0) {
                continue;
            }
            int dmg = bleed.amount * this.percent / 100;
            if (dmg > 0) {
                DamageInfo info = new DamageInfo(mo, dmg, DamageInfo.DamageType.HP_LOSS);
                this.addToTop(new DamageAction(mo, info, AttackEffect.POISON));
            }
        }
        this.isDone = true;
    }
}
