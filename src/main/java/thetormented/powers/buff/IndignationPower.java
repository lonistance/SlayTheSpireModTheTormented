package thetormented.powers.buff;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.powers.BasePower;

import static thetormented.BasicMod.makeID;

public class IndignationPower extends BasePower {
    public static final String POWER_ID = makeID(IndignationPower.class.getSimpleName());

    private static final PowerType POWER_TYPE = PowerType.BUFF;
    private static final boolean IS_TURN_BASED = true;

    private boolean usedThisTurn = true;
    private boolean firstTurnSkipped = false;

    public IndignationPower(AbstractCreature owner, int amount) {
        super(POWER_ID, POWER_TYPE, IS_TURN_BASED, owner, amount);
    }

    @Override
    public void atStartOfTurn() {
        this.usedThisTurn = false;
        // 打出第 1 张牌前：本回合所有手牌临时显示 0 费（提示“第 1 张牌免费”）
        // 引擎在 power.atStartOfTurn 之前已对手牌执行 resetAttributes，故此处设置不会被清掉
        if (this.owner instanceof AbstractPlayer) {
            for (AbstractCard c : ((AbstractPlayer) this.owner).hand.group) {
                c.costForTurn = 0;
                c.isCostModifiedForTurn = true;
            }
        }
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (!this.usedThisTurn) {
            this.usedThisTurn = true;
            card.costForTurn = 0;
            card.isCostModifiedForTurn = true;
            // 打出第 1 张牌后：恢复其余手牌的实际费用显示
            if (this.owner instanceof AbstractPlayer) {
                for (AbstractCard c : ((AbstractPlayer) this.owner).hand.group) {
                    if (c != card) {
                        c.isCostModifiedForTurn = false;
                        c.costForTurn = c.cost;
                    }
                }
            }
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayerTurn) {
        if (isPlayerTurn) {
            // 本回合未打出任何牌：同样恢复所有手牌的实际费用显示
            if (!this.usedThisTurn && this.owner instanceof AbstractPlayer) {
                for (AbstractCard c : ((AbstractPlayer) this.owner).hand.group) {
                    c.isCostModifiedForTurn = false;
                    c.costForTurn = c.cost;
                }
            }
            if (!this.firstTurnSkipped) {
                this.firstTurnSkipped = true;
                return;
            }
            this.amount--;
            if (this.amount <= 0) {
                addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
            } else {
                updateDescription();
            }
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
