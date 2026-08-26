package thetormented.cards.common.skill;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.util.CardStats;
import thetormented.powers.debuff.DebtPower;

public class SacredLand extends BaseCard {
    // 1. 卡牌 ID 与基础配置常量
    public static final String ID = makeID(SacredLand.class.getSimpleName());

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.COMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.SELF, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );
    // 2. 数值配置变量（便于后续调整平衡性）
    private static final int BLOCK_BASE = 6;
    private static final int BLOCK_UPGRADE = 2; // 升级后格挡 8
    private static final int DRAW_AMOUNT = 2;   // 基础抽 2 张牌
    private static final int DRAW_UPGRADE = 1;  // 升级后抽 3 张牌

    public SacredLand() {
        super(ID, info);
        // 设置基础格挡与升级增加量
        setBlock(BLOCK_BASE, BLOCK_UPGRADE);
        // 将抽牌数存入 magicNumber
        setMagic(DRAW_AMOUNT, DRAW_UPGRADE);
    }

    // 每有 1 点血债就少抽 1 张，最低抽 0 张
    private int getEffectiveDraw() {
        return Math.max(0, this.magicNumber - getPlayerPowerAmount(DebtPower.POWER_ID));
    }

    @Override
    protected String getInjectedDescription() {
        String ext = extDescription(0);
        if (ext == null) {
            return null;
        }
        return ext.replace("!M!", String.valueOf(getEffectiveDraw()));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 1. 获得基础格挡
        addToBot(new GainBlockAction(p, p, this.block));

        // 2. 抽牌：每有 1 点血债少抽 1 张
        addToBot(new DrawCardAction(p, getEffectiveDraw()));
    }

    @Override
    public AbstractCard makecopy() {
        return new SacredLand();
    }
}