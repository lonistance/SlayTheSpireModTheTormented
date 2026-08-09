package thetormented.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class CursedBrokenBladeAction extends AbstractGameAction {
    @Override
    public void update() {
        upgradeStarterCard(AbstractCard.CardTags.STARTER_STRIKE);
        upgradeStarterCard(AbstractCard.CardTags.STARTER_DEFEND);
        this.isDone = true;
    }

    private void upgradeStarterCard(AbstractCard.CardTags tag) {
        AbstractPlayer p = AbstractDungeon.player;
        ArrayList<AbstractCard> candidates = new ArrayList<>();
        for (CardGroup group : new CardGroup[]{p.hand, p.drawPile, p.discardPile}) {
            for (AbstractCard c : group.group) {
                if (c.hasTag(tag)) {
                    candidates.add(c);
                }
            }
        }
        if (candidates.isEmpty()) {
            return;
        }
        AbstractCard chosen = candidates.get(AbstractDungeon.cardRng.random(candidates.size() - 1));
        AbstractCard upgraded = chosen.makeCopy();
        upgraded.upgrade();
        replaceCard(chosen, upgraded);
        CardCrawlGame.sound.play("CARD_UPGRADE");
    }

    private void replaceCard(AbstractCard original, AbstractCard replacement) {
        AbstractPlayer p = AbstractDungeon.player;
        for (CardGroup group : new CardGroup[]{p.hand, p.drawPile, p.discardPile}) {
            int index = group.group.indexOf(original);
            if (index != -1) {
                group.group.set(index, replacement);
                return;
            }
        }
    }
}
