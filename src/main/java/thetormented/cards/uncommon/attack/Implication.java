package thetormented.cards.uncommon.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.actions.ImplicationAction;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented; // 替换为你的角色定义类
import thetormented.util.CardStats;

public class Implication extends BaseCard {
    public static final String ID = makeID(Implication.class.getSimpleName());

    // 定义基本数值常量，避免硬编码表达式
    private static final int COST = 1;
    private static final int DAMAGE = 6;
    private static final int UPGRADE_PLUS_DMG = 2;

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public Implication() {
        super(ID, new CardStats(
                Tormented.Meta.CARD_COLOR, // 替换为你的卡牌颜色定义
                TYPE,
                RARITY,
                TARGET,
                COST
        ));

        // 绑定基础伤害与二次伤害（两者数值一致，均跟随升级提升）
        setDamage(DAMAGE, UPGRADE_PLUS_DMG);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int primaryDamageVal = this.damage;
        int secondaryDamageVal = this.damage;

        // 1. 对所选单体敌人造成一次伤害
        this.addToBot(new DamageAction(
                m,
                new DamageInfo(p, primaryDamageVal, DamageInfo.DamageType.NORMAL),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL
        ));

        // 2. 触发针对所有带“流血”敌人的二次伤害
        this.addToBot(new ImplicationAction(p, secondaryDamageVal));
    }

    @Override
    public AbstractCard makecopy() {
        return new Implication();
    }
}