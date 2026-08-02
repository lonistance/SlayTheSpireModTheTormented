package thetormented.cards.common.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.powers.debuff.LoseEnergyNextTurnPower;
import thetormented.util.CardStats;


public class Loan extends BaseCard {
    public static final String ID = makeID(Loan.class.getSimpleName());

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.COMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.SELF, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );

    // 卡牌属性定义（变量定义便于后期修改调整）
    private static final int ENERGY_GAIN = 3;
    private static final int ENERGY_LOSS_NEXT_TURN = 1;

    public Loan() {
        super(ID, info);
        setExhaust(true);
        setSelfRetain(false,true);
        setMagic(ENERGY_GAIN);
        setCustomVar("E_LOSS",ENERGY_LOSS_NEXT_TURN);   // Next turn lose 1 energy
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 1. 本回合获得能量
        this.addToBot(new GainEnergyAction(magicNumber));
        // 2. 赋予下回合扣能量的 Power
        this.addToBot(new ApplyPowerAction(p, p, new LoseEnergyNextTurnPower(p, p, ENERGY_LOSS_NEXT_TURN)));
    }

    @Override
    public AbstractCard makecopy() {
        return new Loan();
    }
}