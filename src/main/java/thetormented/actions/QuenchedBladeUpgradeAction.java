package thetormented.actions;


import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class QuenchedBladeUpgradeAction extends AbstractGameAction {
    private final AbstractPlayer p;
    private final ArrayList<AbstractCard> cannotUpgrade = new ArrayList<>();
    private final int amount;

    public QuenchedBladeUpgradeAction(AbstractPlayer p, int amount) {
        this.p = p;
        this.amount = amount;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if(duration == Settings.ACTION_DUR_FAST) {
            for(AbstractCard c : p.hand.group) {
                if(!c.canUpgrade()) {
                    cannotUpgrade.add(c);
                }
            }
            p.hand.group.removeAll(cannotUpgrade);
            if(p.hand.group.size() <= amount) {
                for(AbstractCard c : p.hand.group) {
                    c.upgrade();
                    c.superFlash();
                    c.applyPowers();
                }
                returnCards();
                isDone=true;
                return;
            }
            AbstractDungeon.handCardSelectScreen.open(
                    "Upgrade " + amount + " cards",
                    amount,
                    false,
                    false,
                    false,
                    false
            );
            tickDuration();
            return;
        }
        if(!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for(AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                c.upgrade();
                c.superFlash();
                c.applyPowers();
                p.hand.addToTop(c);
            }
            returnCards();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved=true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            isDone=true;
        }
        tickDuration();
    }
    private void returnCards() {
        for(AbstractCard c : cannotUpgrade) {
            p.hand.addToTop(c);
        }
        p.hand.refreshHandLayout();
    }
}
