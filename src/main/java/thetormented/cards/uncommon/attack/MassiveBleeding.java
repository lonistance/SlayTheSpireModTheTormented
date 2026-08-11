package thetormented.cards.uncommon.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.powers.debuff.BleedPower;
import thetormented.util.CardStats;

public class MassiveBleeding extends BaseCard {
    public static final String ID = makeID(MassiveBleeding.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );
    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final int DAMAGE = 6;
    private static final int EXTRA_DAMAGE = 1;
    private static final int UPG_EXTRA_DAMAGE = 1;

    public MassiveBleeding() {
        super(ID, info); //Pass the required information to the BaseCard constructor.
        setDamage(DAMAGE); //Sets the card's damage and how much it changes when upgraded.
        setMagic(EXTRA_DAMAGE, UPG_EXTRA_DAMAGE);

        // 实时预览：总伤害 = 当前伤害（含力量/易伤）+ 流血层数 * 倍率
        setCustomVar("TOTAL_DAMAGE", VariableType.DAMAGE, DAMAGE, 0,
                (c, m, base) -> base,
                (c, m, val) -> val + getBleedAmount(m) * c.magicNumber);
    }

    @Override
    protected String getInjectedDescription() {
        return extDescription(0);
    }

    private int getBleedAmount(AbstractMonster m) {
        if (m == null) {
            return 0;
        }
        AbstractPower bleedPower = m.getPower(BleedPower.POWER_ID);
        return bleedPower != null ? bleedPower.amount : 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_VERTICAL));

        int extraDamage = 0;
        if (m != null) {
            AbstractPower bleedPower = m.getPower(BleedPower.POWER_ID);
            if (bleedPower != null) {
                extraDamage = bleedPower.amount * magicNumber;
            }
        }
        if (extraDamage > 0) {
            addToBot(new DamageAction(m, new DamageInfo(p, extraDamage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        }
    }

    @Override
    public AbstractCard makecopy() {
        return new MassiveBleeding();
    }
}
