package thetormented.relics;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thetormented.character.Tormented;

import static thetormented.BasicMod.makeID;

public class Drumstick extends BaseRelic {
    public static final String ID = makeID(Drumstick.class.getSimpleName());
    private static final int EXHAUST_THRESHOLD = 6;

    private int exhaustedThisCombat = 0;

    public Drumstick() {
        super(ID, "drumstick", Tormented.Meta.CARD_COLOR, RelicTier.SHOP, LandingSound.CLINK);
    }

    @Override
    public void atBattleStartPreDraw() {
        exhaustedThisCombat = 0;
    }

    @Override
    public void onExhaust(AbstractCard card) {
        exhaustedThisCombat++;
        if (exhaustedThisCombat >= EXHAUST_THRESHOLD) {
            exhaustedThisCombat = 0;
            this.flash();
            AbstractDungeon.player.gainEnergy(1);
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + EXHAUST_THRESHOLD + DESCRIPTIONS[1];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Drumstick();
    }
}
