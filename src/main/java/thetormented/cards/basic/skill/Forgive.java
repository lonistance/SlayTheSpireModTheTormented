package thetormented.cards.basic.skill;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.actions.UpdateSinAction;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.util.CardStats;


public class Forgive extends BaseCard {
    public static final String ID = makeID(Forgive.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.BASIC, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.SELF, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );

    private static final int BLOCK = 6;
    private static final int UPG_BLOCK = 3;
    private static final int SIN_LOSS = 5;
    private static final int UPG_SIN_LOSS = 2;

    public Forgive() {
        super(ID, info);
        setBlock(BLOCK,UPG_BLOCK);
        setMagic(SIN_LOSS, UPG_SIN_LOSS);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //获得格挡
        p.addBlock(block);
        //减少5点Sin
        addToBot(new UpdateSinAction(p, p, -magicNumber));
    }

    @Override
    public AbstractCard makecopy() {
        return new Forgive();
    }
}