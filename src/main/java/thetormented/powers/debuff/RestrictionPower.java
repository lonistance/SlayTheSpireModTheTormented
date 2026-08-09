package thetormented.powers.debuff;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thetormented.actions.UpdateDebtAction;
import thetormented.powers.BasePower;

import static thetormented.BasicMod.makeID;

public class RestrictionPower extends BasePower implements UpdateDebtAction.OnDebtChangeSubscriber {
    public static final String POWER_ID = makeID(RestrictionPower.class.getSimpleName());

    private static final PowerType POWER_TYPE = PowerType.DEBUFF;
    private static final boolean IS_TURN_BASED = true;

    public RestrictionPower(AbstractCreature owner) {
        // 传入 -1 作为 amount 隐藏层数显示，因为该能力不可叠加 [cite: 67]
        super(POWER_ID, POWER_TYPE, IS_TURN_BASED, owner, null, -1);
    }

    /**
     * 回合结束时：自动移除此限制 [cite: 66, 68]
     */
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }

    /**
     * 血债增加时：不关注增加的变化
     */
    @Override
    public void onDebtIncrease(int increasedAmount) {
        // 无需任何操作，继续保持束缚状态
    }

    /**
     * 血债减少时：检查血债是否已被完全移除
     */
    @Override
    public void onDebtReduce(int reducedAmount) {
        // 获取玩家当前最新的血债状态
        AbstractPower debtPower = this.owner.getPower(DebtPower.POWER_ID);

        // 如果血债已被移除 (null) 或层数归零 (<= 0)，则解除束缚
        if (debtPower == null || debtPower.amount <= 0) {
            this.flash();
            // 使用 addToTop 确保它立刻插队生效，第一时间解除能量获取限制 [cite: 67]
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    // ==========================================================
    // 补丁：利用 ModTheSpire 拦截原版能量增加方法 [cite: 66]
    // ==========================================================
    @SpirePatch(clz = com.megacrit.cardcrawl.ui.panels.EnergyPanel.class, method = "addEnergy")
    public static class PreventEnergyGainPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(int e) {
            if (AbstractDungeon.player != null && AbstractDungeon.player.hasPower(RestrictionPower.POWER_ID)) {
                // 闪烁提示玩家为什么能量没加上
                AbstractDungeon.player.getPower(RestrictionPower.POWER_ID).flash();

                // 返回 Return() 拦截原逻辑，阻止能量增加 [cite: 66]
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }
}