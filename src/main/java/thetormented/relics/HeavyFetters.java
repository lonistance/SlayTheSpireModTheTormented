package thetormented.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thetormented.cards.special.curse.Entangled;
import thetormented.character.Tormented;

import static thetormented.BasicMod.makeID;

public class HeavyFetters extends BaseRelic {
    public static final String ID = makeID(HeavyFetters.class.getSimpleName());

    public HeavyFetters() {
        super(ID, "heavyFetters", Tormented.Meta.CARD_COLOR, RelicTier.BOSS, LandingSound.HEAVY);
    }

    @Override
    public void atBattleStartPreDraw() {
        this.flash();
        AbstractDungeon.player.drawPile.addToRandomSpot(new Entangled());
    }

    @Override
    public void atTurnStart() {
        this.flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.player.gainEnergy(1);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new HeavyFetters();
    }
}
