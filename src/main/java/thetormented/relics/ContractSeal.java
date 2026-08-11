package thetormented.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thetormented.character.Tormented;
import thetormented.powers.debuff.DebtPower;

import static thetormented.BasicMod.makeID;

public class ContractSeal extends BaseRelic {
    public static final String ID = makeID(ContractSeal.class.getSimpleName());
    private static final int GOLD_AMOUNT = 15;

    public ContractSeal() {
        super(ID, "contractSeal", Tormented.Meta.CARD_COLOR, RelicTier.RARE, LandingSound.HEAVY);
    }

    @Override
    public void onVictory() {
        if (!AbstractDungeon.player.hasPower(DebtPower.POWER_ID)) {
            flash();
            AbstractDungeon.player.gainGold(GOLD_AMOUNT);
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ContractSeal();
    }
}
