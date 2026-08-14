package thetormented.cards.uncommon.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.util.CardStats;

public class TemperedSword extends BaseCard {
    public static final String ID = makeID(TemperedSword.class.getSimpleName());

    private static final int COST = 1;
    private static final int BASE_DAMAGE = 9;
    private static final int DAMAGE_PER_STATUS = 3; // 基础增伤加 1
    private static final int UPG_DAMAGE_PER_STATUS = 1; // 3 -> 4

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            COST
    );

    public TemperedSword() {
        super(ID, info);
        // 基础伤害 8 点（未升级与升级后均保持 8 点）
        setDamage(BASE_DAMAGE);
        // 设置加成系数：未升级 2，升级后 3
        setMagic(DAMAGE_PER_STATUS, UPG_DAMAGE_PER_STATUS);

        // 实时预览：总伤害 = 基础伤害 + 消耗状态牌加成（力量/易伤由 applyPowers 计算）
        setCustomVar("TOTAL_DAMAGE", VariableType.DAMAGE, BASE_DAMAGE);
    }

    @Override
    protected String getInjectedDescription() {
        return extDescription(0);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 在打出时重新计算一次包含状态牌加成的伤害
        calculateCardDamage(m);
        addToBot(new DamageAction(
                m,
                new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_VERTICAL
        ));
    }

    /**
     * 计算消耗堆中状态牌的数量
     */
    private int countExhaustedStatusCards() {
        int count = 0;
        AbstractPlayer p = AbstractDungeon.player;
        if (p != null && p.exhaustPile != null) {
            for (AbstractCard c : p.exhaustPile.group) {
                if (c.type == CardType.STATUS) {
                    count++;
                }
            }
        }
        return count;
    }

    // --- 实时面板 preview 逻辑 ---

    @Override
    public void applyPowers() {
        int originalBaseDamage = this.baseDamage;
        this.baseDamage += countExhaustedStatusCards() * this.magicNumber;

        super.applyPowers();

        this.baseDamage = originalBaseDamage;
        this.isDamageModified = (this.damage != this.baseDamage);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int originalBaseDamage = this.baseDamage;
        this.baseDamage += countExhaustedStatusCards() * this.magicNumber;

        super.calculateCardDamage(mo);

        this.baseDamage = originalBaseDamage;
        this.isDamageModified = (this.damage != this.baseDamage);
    }

    @Override
    public AbstractCard makecopy() {
        return new TemperedSword();
    }
}