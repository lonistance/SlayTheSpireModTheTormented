package thetormented.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import thetormented.powers.debuff.DebtPower;

public class RiotAction extends AbstractGameAction {
    private final int energyOnUse;
    private final int baseDamage;
    private final int damageAddition; // 即 magicNumber
    private final AbstractPlayer p;
    private final AbstractMonster m;

    public RiotAction(AbstractPlayer p, AbstractMonster m, int baseDamage, int damageAddition, int energyOnUse) {
        this.p = p;
        this.m = m;
        this.baseDamage = baseDamage;
        this.damageAddition = damageAddition;
        this.energyOnUse = energyOnUse;
        this.actionType = ActionType.DAMAGE;
        this.duration = Settings.ACTION_DUR_XFAST;
    }

    @Override
    public void update() {
        // 1. 计算本次使用的基础能量次数
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }

        // 2. 兼容原版“化学 X (Chemical X)”遗物 (+2 次效果)
        if (this.p.hasRelic("Chemical X")) {
            effect += 2;
            this.p.getRelic("Chemical X").flash();
        }

        // 3. 执行攻击结算
        if (effect > 0) {
            // 实时获取结算瞬间最新的 DebtPower 层数（包含排队中的 Buff）
            int debtAmount = 0;
            AbstractPower debtPower = p.getPower(DebtPower.POWER_ID);
            if (debtPower != null) {
                debtAmount = debtPower.amount;
            }

            // 计算加成后的单发基础伤害
            int totalBaseDamage = this.baseDamage + (debtAmount * this.damageAddition);

            for (int i = 0; i < effect; i++) {
                DamageInfo info = new DamageInfo(p, totalBaseDamage, DamageInfo.DamageType.NORMAL);
                info.applyPowers(p, m);

                // 使用 addToTop 确保多段伤害按顺序排在队列最上方
                this.addToTop(new DamageAction(
                        m,
                        info,
                        AttackEffect.SLASH_VERTICAL
                ));
            }

            // 4. 关键：强行扣除全部能量（忽略 freeToPlayOnce）
            // 注意：重复打出（Double Tap 等）第二次触发时，EnergyPanel 已经是 0，不会重复多扣能量
            if (EnergyPanel.totalCount > 0) {
                this.p.energy.use(EnergyPanel.totalCount);
            }
        }

        this.isDone = true;
    }
}