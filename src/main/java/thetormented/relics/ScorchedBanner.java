package thetormented.relics;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thetormented.character.Tormented;

import static thetormented.BasicMod.makeID;

public class ScorchedBanner extends BaseRelic {
    public static final String ID = makeID(ScorchedBanner.class.getSimpleName());
    private static final int EXTRA_DRAW = 2;
    private boolean extraDrawPending = false;

    public ScorchedBanner() {
        super(ID, "scorchedBanner", Tormented.Meta.CARD_COLOR, RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public void onPlayerEndTurn() {
        if (AbstractDungeon.player.hand.isEmpty()) {
            extraDrawPending = true;
        }
    }

    @Override
    public void atTurnStartPostDraw() {
        if (extraDrawPending) {
            extraDrawPending = false;
            flash();
            addToBot(new DrawCardAction(AbstractDungeon.player, EXTRA_DRAW));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ScorchedBanner();
    }
}
