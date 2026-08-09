package thetormented.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thetormented.actions.CursedBrokenBladeAction;
import thetormented.actions.UpdateSinAction;
import thetormented.character.Tormented;

import static thetormented.BasicMod.makeID;

public class CursedBrokenBlade extends BaseRelic {
    public static final String ID = makeID(CursedBrokenBlade.class.getSimpleName());
    private static final int SIN_PER_TURN = 3;

    public CursedBrokenBlade() {
        super(ID, "cursedBrokenBlade", Tormented.Meta.CARD_COLOR, RelicTier.COMMON, LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStartPreDraw() {
        this.flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new CursedBrokenBladeAction());
    }

    @Override
    public void atTurnStart() {
        this.flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new UpdateSinAction(AbstractDungeon.player, AbstractDungeon.player, SIN_PER_TURN));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + SIN_PER_TURN + DESCRIPTIONS[1];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new CursedBrokenBlade();
    }
}
