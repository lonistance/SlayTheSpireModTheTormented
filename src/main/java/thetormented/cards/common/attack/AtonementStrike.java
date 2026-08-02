package thetormented.cards.common.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.powers.debuff.DebtPower;
import thetormented.util.CardStats;

public class AtonementStrike extends BaseCard {
    // 1. 卡牌 ID 与基础配置变量/常量
    public static final String ID = makeID(AtonementStrike.class.getSimpleName());

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.COMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );

    // 2. 卡牌数值配置变量
    private static final int DAMAGE_BASE = 6;
    private static final int DAMAGE_UPGRADE = 2; // 升级后增加 2（总共 8）

    private static final int DRAW_BASE = 2;
    private static final int DRAW_UPGRADE = 1; // 升级后增加 1（总共 3）

    public AtonementStrike() {
        super(ID, info);

        // 初始化伤害与魔法数字（抽牌数）
        setDamage(DAMAGE_BASE, DAMAGE_UPGRADE);
        setMagic(DRAW_BASE, DRAW_UPGRADE);

        // 添加“打击”标签（Strike Tag）
        this.tags.add(CardTags.STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 造成基础伤害
        DamageInfo damageInfo = new DamageInfo(p, this.damage, this.damageTypeForTurn);
        AbstractGameAction.AttackEffect attackEffect = AbstractGameAction.AttackEffect.SLASH_DIAGONAL;
        this.addToBot(new DamageAction(m, damageInfo, attackEffect));

        // 检测玩家是否有 DebtPower（血债）且层数 > 0
        boolean hasDebt = p.hasPower(DebtPower.POWER_ID) && p.getPower(DebtPower.POWER_ID).amount > 0;

        if (hasDebt) {
            this.addToBot(new DrawCardAction(p, this.magicNumber));
        }
    }

    @Override
    public AbstractCard makecopy() { //Optional
        return new AtonementStrike();
    }
}