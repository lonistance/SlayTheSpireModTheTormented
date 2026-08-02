package thetormented.cards.common.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.util.CardStats;

public class Agony extends BaseCard {
    // 1. 卡牌 ID 与基础配置常量
    public static final String ID = makeID(Agony.class.getSimpleName());

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.COMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );

    // 2. 数值配置变量（方便后期调整平衡性）
    private static final int DAMAGE_BASE = 7;
    private static final int DAMAGE_UPGRADE = 3; // 升级后增加 3（总共 10）

    private static final int WEAK_STACKS = 1;      // 给予 1 层虚弱
    private static final int VULNERABLE_STACKS = 1; // 给予 1 层易伤

    public Agony() {
        super(ID, info);
        // 设置基础伤害与升级增加量
        setDamage(DAMAGE_BASE, DAMAGE_UPGRADE);
        // 将虚弱/易伤层数存入 magicNumber
        setMagic(WEAK_STACKS);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 1. 造成伤害
        DamageInfo damageInfo = new DamageInfo(p, this.damage, this.damageTypeForTurn);
        AbstractGameAction.AttackEffect attackEffect = AbstractGameAction.AttackEffect.SLASH_HEAVY;
        this.addToBot(new DamageAction(m, damageInfo, attackEffect));

        // 2. 给予 1 层虚弱
        WeakPower weakToApply = new WeakPower(m, WEAK_STACKS, false);
        this.addToBot(new ApplyPowerAction(m, p, weakToApply, WEAK_STACKS));

        // 3. 给予 1 层易伤
        VulnerablePower vulnerableToApply = new VulnerablePower(m, VULNERABLE_STACKS, false);
        this.addToBot(new ApplyPowerAction(m, p, vulnerableToApply, VULNERABLE_STACKS));
    }

    @Override
    public AbstractCard makecopy() { //Optional
        return new Agony();
    }
}