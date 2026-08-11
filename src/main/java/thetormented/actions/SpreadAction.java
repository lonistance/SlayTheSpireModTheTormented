package thetormented.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class SpreadAction extends AbstractGameAction {
    private final AbstractMonster sourceMonster;
    private final AbstractPlayer sourcePlayer;

    public SpreadAction(AbstractMonster sourceMonster, AbstractPlayer sourcePlayer) {
        this.sourceMonster = sourceMonster;
        this.sourcePlayer = sourcePlayer;
        this.actionType = ActionType.DEBUFF;
    }

    @Override
    public void update() {
        if (this.sourceMonster != null && !this.sourceMonster.isDeadOrEscaped()) {
            ArrayList<AbstractPower> debuffs = new ArrayList<>();
            for (AbstractPower p : this.sourceMonster.powers) {
                if (p.type == AbstractPower.PowerType.DEBUFF && p.amount > 0) {
                    debuffs.add(p);
                }
            }

            for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
                if (mo == null || mo == this.sourceMonster || mo.isDeadOrEscaped()) {
                    continue;
                }
                for (AbstractPower debuff : debuffs) {
                    AbstractPower copy = copyPower(debuff, mo, this.sourcePlayer);
                    if (copy != null) {
                        this.addToTop(new ApplyPowerAction(mo, this.sourcePlayer, copy, debuff.amount));
                    }
                }
            }
        }
        this.isDone = true;
    }

    private AbstractPower copyPower(AbstractPower original, AbstractMonster newOwner, AbstractCreature source) {
        Class<?> cls = original.getClass();
        try {
            Constructor<?> c = cls.getConstructor(AbstractCreature.class, AbstractCreature.class, int.class);
            return (AbstractPower) c.newInstance(newOwner, source, original.amount);
        } catch (Exception e) {
        }
        try {
            Constructor<?> c = cls.getConstructor(AbstractCreature.class, int.class);
            return (AbstractPower) c.newInstance(newOwner, original.amount);
        } catch (Exception e) {
        }
        try {
            Constructor<?> c = cls.getConstructor(AbstractCreature.class, int.class, boolean.class);
            return (AbstractPower) c.newInstance(newOwner, original.amount, false);
        } catch (Exception e) {
        }
        return null;
    }
}
