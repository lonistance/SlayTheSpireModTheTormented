package thetormented.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.OnApplyPowerRelic;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static thetormented.BasicMod.makeID;

public class BlackstoneLantern extends BaseRelic implements OnApplyPowerRelic {
    public static final String ID = makeID(BlackstoneLantern.class.getSimpleName());

    private boolean triggeredThisCombat = false;

    public BlackstoneLantern() {
        super(ID, "blackstoneLantern", RelicTier.COMMON, LandingSound.SOLID);
    }

    @Override
    public void atBattleStartPreDraw() {
        triggeredThisCombat = false;
    }

    @Override
    public boolean onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (!triggeredThisCombat && power.type == AbstractPower.PowerType.DEBUFF && target instanceof AbstractMonster) {
            triggeredThisCombat = true;
            this.flash();
            AbstractDungeon.player.gainEnergy(1);
        }
        return true;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BlackstoneLantern();
    }
}
