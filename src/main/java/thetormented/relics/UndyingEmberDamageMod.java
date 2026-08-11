package thetormented.relics;

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class UndyingEmberDamageMod extends AbstractDamageModifier {
    private static final float BONUS_MULT = 1.25f;

    @Override
    public float atDamageFinalGive(float damage, DamageInfo.DamageType type, AbstractCreature target, AbstractCard card) {
        if (target != null && hasDebuff(target)) {
            return damage * BONUS_MULT;
        }
        return damage;
    }

    public static boolean hasDebuff(AbstractCreature c) {
        if (c == null) {
            return false;
        }
        for (AbstractPower p : c.powers) {
            if (p.type == AbstractPower.PowerType.DEBUFF) {
                return true;
            }
        }
        return false;
    }

    @Override
    public AbstractDamageModifier makeCopy() {
        return new UndyingEmberDamageMod();
    }
}
