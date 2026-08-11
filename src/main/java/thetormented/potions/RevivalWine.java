package thetormented.potions;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static thetormented.BasicMod.makeID;

public class RevivalWine extends BasePotion {
    public static final String POTION_ID = makeID(RevivalWine.class.getSimpleName());

    public RevivalWine() {
        super(POTION_ID, 1, PotionRarity.UNCOMMON, PotionSize.BOTTLE, PotionColor.WEAK);
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
        this.addToBot(new GainEnergyAction(1));
        this.addToBot(new DrawCardAction(p, 2));
    }
}
