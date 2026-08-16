package thetormented.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thetormented.patches.EtherealMarkerPatch;

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
                // 原版回合末的耗虚只扫手牌（triggerOnEndOfPlayerTurn），而 retain 卡
                // 会被 DiscardAtEndOfTurnAction 先移入 limbo 暂存、随后放回手牌——
                // 耗虚遍历发生在其放回之前，retain 卡永远不会被消耗（原版固有行为，
                // 原版不存在 retain+ethereal 组合卡故从未暴露）。给虚无卡保留 retain
                // 也自相矛盾（虚无=回合末消失），故附加虚无时一并清除 retain。
                c.retain = false;
                c.selfRetain = false;
                EtherealMarkerPatch.markEthereal(c);
                p.drawPile.removeCard(c);
                p.hand.addToHand(c);
                c.applyPowers();
                // 描述重建过去只在本 mod 的 BaseCard 覆写路径（且需 getInjectedDescription 非空）时发生，
                // 原版/其他卡永远不会触发，导致“虚无”提示行不出现；这里显式重建一次，
                // EtherealMarkerPatch 的 postfix 即会追加关键词行并常驻
                c.initializeDescription();
                c.superFlash();
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            p.hand.refreshHandLayout();
        }

        this.isDone = true;
    }
}
