package thetormented.potions;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.EquilibriumPower;

import static thetormented.BasicMod.makeID;

public class TearDrop extends BasePotion {
    public static final String POTION_ID = makeID(TearDrop.class.getSimpleName());

    public TearDrop() {
        super(POTION_ID, 1, PotionRarity.RARE, PotionSize.SPHERE, PotionColor.ANCIENT);
        this.isThrown = false;
        this.targetRequired = false;
    }

    @Override
    public String getDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void use(AbstractCreature target) {
        AbstractPlayer p = AbstractDungeon.player;
        this.addToBot(new ApplyPowerAction(p, p, new EquilibriumPower(p, this.potency), this.potency));
    }
}
