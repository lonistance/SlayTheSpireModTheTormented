package thetormented.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;

import static thetormented.BasicMod.makeID;

public class RegretlessCoin extends BaseRelic {
    public static final String ID = makeID(RegretlessCoin.class.getSimpleName());
    private static final int GOLD_GAIN = 30;

    public RegretlessCoin() {
        super(ID, "regretlessCoin", RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        if (room instanceof ShopRoom) {
            this.flash();
            AbstractDungeon.player.gainGold(GOLD_GAIN);
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + GOLD_GAIN + DESCRIPTIONS[1];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new RegretlessCoin();
    }
}
