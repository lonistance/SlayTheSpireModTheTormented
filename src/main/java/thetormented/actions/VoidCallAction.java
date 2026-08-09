package thetormented.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static thetormented.BasicMod.makeID;

public class VoidCallAction extends AbstractGameAction {
    private static final String TEXT = CardCrawlGame.languagePack.getUIString(makeID("VoidCallAction")).TEXT[0];
    private final int numberOfCards;

    public VoidCallAction(int numberOfCards) {
        this.numberOfCards = numberOfCards;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.startDuration = Settings.ACTION_DUR_FAST;
        this.duration = this.startDuration;
    }

    @Override
    public void update() {
        AbstractPlayer p = AbstractDungeon.player;

        if (this.duration == this.startDuration) {
            if (p.drawPile.isEmpty()) {
                this.isDone = true;
                return;
            }
            int count = Math.min(this.numberOfCards, p.drawPile.size());
            AbstractDungeon.gridSelectScreen.open(p.drawPile, count, TEXT, false, false, false, false);
            tickDuration();
            return;
        }

        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                c.isEthereal = true;
                p.drawPile.removeCard(c);
                p.hand.addToHand(c);
                c.applyPowers();
                c.superFlash();
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            p.hand.refreshHandLayout();
        }

        this.isDone = true;
    }
}
