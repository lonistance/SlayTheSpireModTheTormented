package thetormented.relics;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thetormented.character.Tormented;

import static thetormented.BasicMod.makeID;

public class RecoveryCharm extends BaseRelic {
    public static final String ID = makeID(RecoveryCharm.class.getSimpleName());
    private static final int MAX_HP_LOSS = 18;

    public RecoveryCharm() {
        super(ID, "recoveryCharm", Tormented.Meta.CARD_COLOR, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.NORMAL && damageAmount > MAX_HP_LOSS) {
            flash();
            return MAX_HP_LOSS;
        }
        return damageAmount;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new RecoveryCharm();
    }
}
