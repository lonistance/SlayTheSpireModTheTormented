package thetormented.powers.buff;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.actions.ApplyBleedAction;
import thetormented.powers.BasePower;

import static thetormented.BasicMod.makeID;

public class BloodbathPower extends BasePower {
    public static final String POWER_ID = makeID(BloodbathPower.class.getSimpleName());

    private static final PowerType POWER_TYPE = PowerType.BUFF;
    private static final boolean IS_TURN_BASED = false;

    private int chargesLeft;
    private boolean pendingAttack;

    public BloodbathPower(AbstractCreature owner, int amount) {
        super(POWER_ID, POWER_TYPE, IS_TURN_BASED, owner, amount);
        this.chargesLeft = amount;
        this.pendingAttack = false;
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            this.pendingAttack = this.chargesLeft > 0;
            if (this.pendingAttack) {
                this.chargesLeft--;
            }
        }
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (!this.pendingAttack) {
            return;
        }
        if (info.type != DamageInfo.DamageType.NORMAL) {
            return;
        }
        if (target == null || target == this.owner || target.isDeadOrEscaped()) {
            return;
        }
        if (damageAmount <= 0) {
            return;
        }
        addToBot(new ApplyBleedAction(target, this.owner, damageAmount));
    }

    @Override
    public void atStartOfTurn() {
        this.chargesLeft = this.amount;
        this.pendingAttack = false;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
