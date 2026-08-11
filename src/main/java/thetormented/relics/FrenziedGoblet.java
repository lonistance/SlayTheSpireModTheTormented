package thetormented.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thetormented.character.Tormented;

import static thetormented.BasicMod.makeID;

public class FrenziedGoblet extends BaseRelic {
    public static final String ID = makeID(FrenziedGoblet.class.getSimpleName());
    private static final int HEAL_AMOUNT = 8;

    public FrenziedGoblet() {
        super(ID, "frenziedGoblet", Tormented.Meta.CARD_COLOR, RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        if (m != null && !m.hasPower(MinionPower.POWER_ID) && !AbstractDungeon.player.isDeadOrEscaped()) {
            flash();
            AbstractDungeon.player.heal(HEAL_AMOUNT);
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new FrenziedGoblet();
    }
}
