package thetormented.relics;

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.relics.DamageModApplyingRelic;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thetormented.character.Tormented;

import java.util.Collections;
import java.util.List;

import static thetormented.BasicMod.makeID;

public class UndyingEmber extends BaseRelic implements DamageModApplyingRelic {
    public static final String ID = makeID(UndyingEmber.class.getSimpleName());
    private static final float REDUCTION_MULT = 0.75f;

    public UndyingEmber() {
        super(ID, "undyingEmber", Tormented.Meta.CARD_COLOR, RelicTier.BOSS, LandingSound.MAGICAL);
    }

    @Override
    public boolean shouldPushMods(DamageInfo info, Object instigator, List<AbstractDamageModifier> currentMods) {
        return true;
    }

    @Override
    public List<AbstractDamageModifier> modsToPush(DamageInfo info, Object instigator, List<AbstractDamageModifier> currentMods) {
        return Collections.singletonList(new UndyingEmberDamageMod());
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (info.owner == AbstractDungeon.player) {
            return damageAmount;
        }
        if (UndyingEmberDamageMod.hasDebuff(AbstractDungeon.player)) {
            return (int) (damageAmount * REDUCTION_MULT);
        }
        return damageAmount;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new UndyingEmber();
    }
}
