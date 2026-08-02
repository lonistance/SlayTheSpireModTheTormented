package thetormented.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class PredictiveMeasuresAction extends AbstractGameAction {
    private final int targetCost;

    public PredictiveMeasuresAction(int amountToDraw, int costForTurn) {
        this.amount = amountToDraw;
        this.targetCost = costForTurn;
        this.actionType = ActionType.DRAW;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (AbstractDungeon.player.drawPile.isEmpty()) {
                // 如果抽牌堆为空，触发弃牌堆洗牌重抽
                if (!AbstractDungeon.player.discardPile.isEmpty()) {
                    addToTop(new PredictiveMeasuresAction(this.amount, this.targetCost));
                    addToTop(new com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction());
                    this.isDone = true;
                    return;
                }
            }

            if (!AbstractDungeon.player.drawPile.isEmpty()) {
                // 预先获取抽牌堆顶部的卡牌引用
                AbstractCard drawnCard = AbstractDungeon.player.drawPile.getTopCard();

                // 执行基础抽牌动作
                AbstractDungeon.player.draw(this.amount);

                // 判断若抽到的是攻击牌，修改其本回合打出前的费用
                if (drawnCard.type == AbstractCard.CardType.ATTACK) {
                    drawnCard.setCostForTurn(this.targetCost);
                }
            }
        }
        tickDuration();
    }
}