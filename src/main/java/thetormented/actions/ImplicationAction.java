package thetormented.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.powers.debuff.BleedPower; // 假设你的流血 Power 类路径在此

public class ImplicationAction extends AbstractGameAction {
    private final int secondaryDamage;

    public ImplicationAction(AbstractCreature source, int secondaryDamage) {
        this.source = source;
        this.secondaryDamage = secondaryDamage;
        this.actionType = ActionType.DAMAGE;
    }

    @Override
    public void update() {
        // 遍历当前房间内的所有活着的怪物
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (m != null && !m.isDeadOrEscaped()) {
                // 检查敌人是否拥有流血 (BleedPower)
                if (m.hasPower(BleedPower.POWER_ID)) {
                    this.addToTop(new DamageAction(
                            m,
                            new DamageInfo(this.source, this.secondaryDamage, DamageInfo.DamageType.NORMAL),
                            AttackEffect.SLASH_HEAVY
                    ));
                }
            }
        }
        this.isDone = true;
    }
}