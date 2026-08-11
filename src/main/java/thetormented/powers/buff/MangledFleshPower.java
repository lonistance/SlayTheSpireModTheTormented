package thetormented.powers.buff;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.actions.ApplyBleedAction;
import thetormented.powers.BasePower;

import java.util.HashMap;
import java.util.Map;

import static thetormented.BasicMod.makeID;

public class MangledFleshPower extends BasePower implements ApplyBleedAction.OnBleedApplySubscriber {
    public static final String POWER_ID = makeID(MangledFleshPower.class.getSimpleName());

    private static final int BLEED_PER_TICK = 6;

    private static final PowerType POWER_TYPE = PowerType.BUFF;
    private static final boolean IS_TURN_BASED = false;

    private final Map<AbstractCreature, Integer> trackedBleed = new HashMap<>();

    public MangledFleshPower(AbstractCreature owner, AbstractCreature source, int amount) {
        super(POWER_ID, POWER_TYPE, IS_TURN_BASED, owner, source, amount);
    }

    @Override
    public void onBleedApplied(AbstractCreature target, int amount) {
        if (!(target instanceof AbstractMonster)) return;
        AbstractMonster m = (AbstractMonster) target;
        if (m.isDead || m.isDying || m.halfDead) return;

        int total = this.trackedBleed.getOrDefault(m, 0) + amount;
        int ticks = total / BLEED_PER_TICK;
        this.trackedBleed.put(m, total % BLEED_PER_TICK);

        if (ticks > 0) {
            this.flash();
            // THORNS 类型伤害，避免触发流血的“受到未格挡伤害时叠层”机制
            this.addToBot(new DamageAction(
                    m,
                    new DamageInfo(this.owner, ticks * this.amount, DamageInfo.DamageType.THORNS),
                    AbstractGameAction.AttackEffect.POISON
            ));
        }
    }

    @Override
    public void onVictory() {
        this.trackedBleed.clear();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
