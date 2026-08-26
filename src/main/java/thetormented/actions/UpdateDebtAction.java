package thetormented.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thetormented.powers.buff.SinPower;
import thetormented.powers.debuff.DebtPower;


public class UpdateDebtAction extends AbstractGameAction {
    private static final int SIN_PER_DEBT = SinPower.SIN_PER_DEBT;

    public UpdateDebtAction(AbstractCreature target, AbstractCreature source, int debtChange) {
        this.target = target;
        this.source = source;
        this.amount = debtChange; // Debt 的增减量
        this.actionType = ActionType.DEBUFF;
    }

    @Override
    public void update() {
        if (this.target != null && this.amount != 0) {
            AbstractCreature p = this.target;

            // 1. 增加 Debt 的逻辑
            if (this.amount > 0) {
                // 检测目标是否有人工制品 (Artifact)
                if (p.hasPower(ArtifactPower.POWER_ID)) {
                    // Debt 施加会被 Artifact 抵消，同步扣除对应的 Sin (每1点Debt对应5点Sin)
                    int sinRefund = this.amount * SIN_PER_DEBT;
                    this.addToTop(new ReducePowerAction(p, p, SinPower.POWER_ID, sinRefund));
                } else {
                    // 没有被抵消时，触发 Debt 增加监听钩子
                    notifyDebtIncrease(p, this.amount);
                }

                // 提交 ApplyPowerAction（原版 ApplyPowerAction 会自行处理人工制品的动画与消耗逻辑）
                this.addToTop(new ApplyPowerAction(p, this.source, new DebtPower(p, this.amount), this.amount));
            }
            // 2. 减少 Debt 的逻辑
            else {
                int reduceDebtAmount = -this.amount;
                this.addToTop(new ReducePowerAction(p, this.source, DebtPower.POWER_ID, reduceDebtAmount));

                // 触发 Debt 减少监听钩子
                notifyDebtReduce(p, reduceDebtAmount);
            }
        }
        this.isDone = true;
    }

    /**
     * 通知所有能力、遗物及卡牌：Debt 增加
     */
    private void notifyDebtIncrease(AbstractCreature creature, int amount) {
        // 1. 通知 Abilities / Powers
        for (AbstractPower power : creature.powers) {
            if (power instanceof OnDebtChangeSubscriber) {
                ((OnDebtChangeSubscriber) power).onDebtIncrease(amount);
            }
        }

        // 如果目标是玩家，还需通知遗物和卡牌
        if (creature instanceof AbstractPlayer) {
            AbstractPlayer player = (AbstractPlayer) creature;
            for (AbstractRelic relic : player.relics) {
                if (relic instanceof OnDebtChangeSubscriber) {
                    ((OnDebtChangeSubscriber) relic).onDebtIncrease(amount);
                }
            }
            notifyCardsInGroup(player.hand, amount, true);
            notifyCardsInGroup(player.drawPile, amount, true);
            notifyCardsInGroup(player.discardPile, amount, true);
        }
    }

    /**
     * 通知所有能力、遗物及卡牌：Debt 减少
     */
    private void notifyDebtReduce(AbstractCreature creature, int amount) {
        for (AbstractPower power : creature.powers) {
            if (power instanceof OnDebtChangeSubscriber) {
                ((OnDebtChangeSubscriber) power).onDebtReduce(amount);
            }
        }

        if (creature instanceof AbstractPlayer) {
            AbstractPlayer player = (AbstractPlayer) creature;
            for (AbstractRelic relic : player.relics) {
                if (relic instanceof OnDebtChangeSubscriber) {
                    ((OnDebtChangeSubscriber) relic).onDebtReduce(amount);
                }
            }
            notifyCardsInGroup(player.hand, amount, false);
            notifyCardsInGroup(player.drawPile, amount, false);
            notifyCardsInGroup(player.discardPile, amount, false);
        }
    }

    private void notifyCardsInGroup(com.megacrit.cardcrawl.cards.CardGroup group, int amount, boolean isIncrease) {
        for (AbstractCard card : group.group) {
            if (card instanceof OnDebtChangeSubscriber) {
                if (isIncrease) {
                    ((OnDebtChangeSubscriber) card).onDebtIncrease(amount);
                } else {
                    ((OnDebtChangeSubscriber) card).onDebtReduce(amount);
                }
            }
        }
    }

    /**
     * 接口：供检测“Debt 增加/减少”的能力牌/遗物实现
     */
    public interface OnDebtChangeSubscriber {
        void onDebtIncrease(int increasedAmount);
        void onDebtReduce(int reducedAmount);
    }
}