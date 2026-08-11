package thetormented.powers.buff;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.powers.BasePower;

import java.util.HashMap;

import static thetormented.BasicMod.makeID;

public class HungeringBattleWillPower extends BasePower {
    public static final String POWER_ID = makeID(HungeringBattleWillPower.class.getSimpleName());

    private static final PowerType POWER_TYPE = PowerType.BUFF;
    private static final boolean IS_TURN_BASED = false;

    private static final int SELF_DAMAGE = 3;

    private final HashMap<AbstractMonster, Boolean> aliveAtTurnStart = new HashMap<>();

    public HungeringBattleWillPower(AbstractCreature owner, int amount) {
        super(POWER_ID, POWER_TYPE, IS_TURN_BASED, owner, amount);
    }

    @Override
    public void atStartOfTurn() {
        aliveAtTurnStart.clear();
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            aliveAtTurnStart.put(m, !(m.isDead || m.isDying || m.isEscaping));
        }
        this.flash();
        addToBot(new GainEnergyAction(this.amount));
    }

    @Override
    public void atEndOfTurn(boolean isPlayerTurn) {
        if (isPlayerTurn) {
            boolean killedThisTurn = false;
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                Boolean wasAlive = aliveAtTurnStart.get(m);
                if (Boolean.TRUE.equals(wasAlive) && (m.isDead || m.isDying)) {
                    killedThisTurn = true;
                    break;
                }
            }
            if (!killedThisTurn) {
                addToBot(new DamageAction(this.owner, new DamageInfo(this.owner, SELF_DAMAGE, DamageInfo.DamageType.THORNS)));
            }
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
