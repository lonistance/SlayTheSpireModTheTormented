package thetormented.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thetormented.cards.uncommon.attack.ChaosDirty;
import thetormented.powers.debuff.DebtPower;

public class PollutedChaosDamageAction extends AbstractGameAction {
    private final ChaosDirty card;
    private final AbstractPlayer p;
    private final AbstractMonster m;

    public PollutedChaosDamageAction(ChaosDirty card, AbstractPlayer p, AbstractMonster m) {
        this.card = card;
        this.p = p;
        this.m = m;
        this.actionType = ActionType.DAMAGE;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            // 此时所有在此之前的 ApplyPowerAction 已经全部结算完成！
            // 再次检查玩家身上的 DebtPower 实际层数（若被抵消/未挂上，此处为 null/0）
            int debtAmount = 0;
            AbstractPower debtPower = p.getPower(DebtPower.POWER_ID);
            if (debtPower != null) {
                debtAmount = debtPower.amount;
            }

            // 计算包含血债加成后的基础伤害
            int totalBaseDamage = card.baseDamage + (debtAmount * card.magicNumber);

            // 借助临时卡牌实例或动态 DamageInfo 计算力量/易伤等最终伤害
            DamageInfo info = new DamageInfo(p, totalBaseDamage, card.damageTypeForTurn);
            info.applyPowers(p, m);

            // 造成最终伤害并播放音效/特效
            if (m != null && !m.isDeadOrEscaped()) {
                m.damage(info);
                if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                    AbstractDungeon.actionManager.clearPostCombatActions();
                }
            }
        }
        tickDuration();
    }
}