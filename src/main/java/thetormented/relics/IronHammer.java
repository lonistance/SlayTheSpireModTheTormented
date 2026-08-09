package thetormented.relics;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;

import java.util.ArrayList;
import java.util.List;

import static thetormented.BasicMod.makeID;

public class IronHammer extends BaseRelic {
    public static final String ID = makeID(IronHammer.class.getSimpleName());
    private static final int UPGRADE_COUNT = 2;

    public IronHammer() {
        super(ID, "ironHammer", RelicTier.RARE, LandingSound.HEAVY);
    }

    @Override
    public void onVictory() {
        if (!(AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite)) {
            return;
        }
        this.flash();
        List<AbstractCard> candidates = new ArrayList<>();
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.canUpgrade()) {
                candidates.add(c);
            }
        }
        for (int i = 0; i < UPGRADE_COUNT && !candidates.isEmpty(); i++) {
            int idx = AbstractDungeon.cardRandomRng.random(0, candidates.size() - 1);
            AbstractCard card = candidates.remove(idx);
            card.upgrade();
            CardCrawlGame.sound.play("CARD_UPGRADE");
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + UPGRADE_COUNT + DESCRIPTIONS[1];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new IronHammer();
    }
}
