package thetormented.powers.buff;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.actions.ApplyBleedAction;
import thetormented.powers.BasePower;

import static thetormented.BasicMod.makeID;

public class RelentlessBleedPower extends BasePower {
    public static final String POWER_ID = makeID(RelentlessBleedPower.class.getSimpleName());

    private static final PowerType POWER_TYPE = PowerType.BUFF;
    private static final boolean IS_TURN_BASED = false;

    public RelentlessBleedPower(AbstractCreature owner, AbstractCreature source, int amount) {
        super(POWER_ID, POWER_TYPE, IS_TURN_BASED, owner, source, amount);
    }

    @Override
    public void atStartOfTurn() {
        if (!this.owner.isPlayer) return;
        this.flash();
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.isDead && !m.isDying) {
                this.addToBot(new ApplyBleedAction(m, this.owner, this.amount));
            }
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
