package thetormented.cards.special.statue;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.cards.BaseCard;
import thetormented.util.CardStats;

public class Misery extends BaseCard {
    public static final String ID = makeID(Misery.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.STATUS, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.SPECIAL, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.NONE, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );
    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final int SELF_DAMAGE = 2;

    public Misery() {
        super(ID, info); //Pass the required information to the BaseCard constructor.
        setExhaust(true);
        canUpgrade();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //受到2点伤害
        addToBot(new DamageAction(p, new DamageInfo(p, SELF_DAMAGE, DamageInfo.DamageType.THORNS)));
        //抽1张牌
        addToBot(new DrawCardAction(1));
    }

    @Override
    public AbstractCard makecopy() {
        return new Misery();
    }
}
