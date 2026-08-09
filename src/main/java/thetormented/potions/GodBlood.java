package thetormented.potions;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thetormented.actions.ApplyBleedAction;

import static thetormented.BasicMod.makeID;

public class GodBlood extends BasePotion {
    public static final String POTION_ID = makeID(GodBlood.class.getSimpleName());

    public GodBlood() {
        super(POTION_ID, 18, PotionRarity.COMMON, PotionSize.H, PotionColor.WHITE);
        this.isThrown = true;
        this.targetRequired = true;
    }

    @Override
    public String getDescription() {
        return DESCRIPTIONS[0] + this.potency + DESCRIPTIONS[1];
    }

    @Override
    public void use(AbstractCreature target) {
        this.addToBot(new ApplyBleedAction(target, AbstractDungeon.player, this.potency));
    }
}
