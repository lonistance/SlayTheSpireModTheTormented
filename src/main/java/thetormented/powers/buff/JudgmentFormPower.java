package thetormented.powers.buff;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thetormented.powers.BasePower;

import static thetormented.BasicMod.makeID;

public class JudgmentFormPower extends BasePower {
    public static final String POWER_ID = makeID(JudgmentFormPower.class.getSimpleName());

    private static final PowerType POWER_TYPE = PowerType.BUFF;
    private static final boolean IS_TURN_BASED = false;

    public JudgmentFormPower(AbstractCreature owner, int amount) {
        super(POWER_ID, POWER_TYPE, IS_TURN_BASED, owner, amount);
    }

    private boolean isBelowThreshold(AbstractMonster m) {
        return m.currentHealth < m.maxHealth * this.amount / 100.0f;
    }

    private void syncConcede(AbstractMonster m) {
        if (m.isDead || m.isDying || m.isEscaping) {
            return;
        }
        AbstractPower concede = m.getPower(ConcedePower.POWER_ID);
        if (isBelowThreshold(m)) {
            if (concede == null) {
                this.addToBot(new ApplyPowerAction(m, this.owner, new ConcedePower(m, this.owner, 1), 1));
            }
        } else if (concede != null) {
            this.addToBot(new RemoveSpecificPowerAction(m, this.owner, ConcedePower.POWER_ID));
        }
    }

    private void syncAllMonsters() {
        if (AbstractDungeon.getMonsters() == null || AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            return;
        }
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (m != null) {
                syncConcede(m);
            }
        }
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.type != DamageInfo.DamageType.NORMAL) {
            return;
        }
        if (target == null || target == this.owner || target.isDeadOrEscaped()) {
            return;
        }
        if (damageAmount <= 0) {
            return;
        }
        if (target instanceof AbstractMonster) {
            syncConcede((AbstractMonster) target);
        }
    }

    @Override
    public void atStartOfTurn() {
        syncAllMonsters();
    }

    @Override
    public void atEndOfTurn(boolean isPlayerTurn) {
        if (isPlayerTurn) {
            syncAllMonsters();
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
