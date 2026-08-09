package thetormented.relics;

import com.megacrit.cardcrawl.relics.AbstractRelic;
import thetormented.character.Tormented;

import static thetormented.BasicMod.makeID;

public class BarbedHook extends BaseRelic {
    public static final String ID = makeID(BarbedHook.class.getSimpleName());

    public BarbedHook() {
        super(ID, "barbedHook", Tormented.Meta.CARD_COLOR, RelicTier.COMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BarbedHook();
    }
}
