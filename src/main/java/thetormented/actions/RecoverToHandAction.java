package thetormented.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

// RelentlessEntanglement 的“血债增加时移回手牌”恢复动作。
// 原版洗牌动画（ShuffleVfx）启动时会把弃牌堆立即清空、把卡牌交给动画，
// 动画播完才将卡牌落位到抽牌堆——窗口期内卡牌不属于任何牌组，
// 排队执行的 DiscardToHandAction 会因 contains 判定失败而静默空转。
// 本动作改为轮询等待：卡落在弃牌堆则照常拉回；若洗牌动画已将其送入抽牌堆，
// 则从抽牌堆拉回手牌；动画窗口期内绝不触碰卡牌（任何移动都会破坏
// ShuffleVfx 收尾、造成卡牌重复），只等待落位；超时或卡已被消耗则放弃。
public class RecoverToHandAction extends AbstractGameAction {
    private static final int MAX_FRAMES = 180; // 最多等待 3 秒（60 fps），防幽灵任务

    private final AbstractCard card;
    private int framesLeft;

    public RecoverToHandAction(AbstractCard card) {
        this.card = card;
        this.framesLeft = MAX_FRAMES;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.startDuration = 0.5f;
        this.duration = this.startDuration;
    }

    @Override
    public void update() {
        AbstractPlayer p = AbstractDungeon.player;

        if (p.hand.contains(this.card)) {
            this.isDone = true;
            return;
        }
        if (p.exhaustPile.contains(this.card) || p.limbo.contains(this.card)) {
            this.isDone = true;
            return;
        }
        if (p.discardPile.contains(this.card)) {
            moveToHand(p);
            this.isDone = true;
            return;
        }
        if (p.drawPile.contains(this.card)) {
            // 洗牌动画已把卡牌落位到抽牌堆：从抽牌堆拉回手牌
            p.drawPile.removeCard(this.card);
            moveToHand(p);
            this.isDone = true;
            return;
        }

        // 仍处于洗牌动画窗口（卡牌暂不属于任何牌组）：等待落位
        this.framesLeft--;
        if (this.framesLeft <= 0) {
            this.isDone = true;
        }
    }

    private void moveToHand(AbstractPlayer p) {
        this.card.unfadeOut();
        this.card.unhover();
        p.hand.addToHand(this.card);
        this.card.applyPowers();
        p.hand.refreshHandLayout();
        p.hand.glowCheck();
    }
}