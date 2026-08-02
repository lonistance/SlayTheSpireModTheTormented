package thetormented.cards.uncommon.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.util.CardStats;

public class DeterringStrike extends BaseCard {
    public static final String ID = makeID(DeterringStrike.class.getSimpleName());

    // 声明常量变量，避免数字硬编码参与计算或函数调用
    private static final int CARD_COST = 1;
    private static final int BASE_DAMAGE = 8;
    private static final int UPGRADE_PLUS_DAMAGE = 3;
    private static final int BASE_WEAK_AMOUNT = 1;
    private static final int UPGRADE_PLUS_WEAK = 1;

    private static final CardStats STATS = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            CARD_COST
    );

    public DeterringStrike() {
        super(ID, STATS);
        setDamage(BASE_DAMAGE,UPGRADE_PLUS_DAMAGE);
        setMagic(BASE_WEAK_AMOUNT, UPGRADE_PLUS_WEAK);

        // 标记打击标签（可选，若属于 Strike 体系卡牌）
        this.tags.add(CardTags.STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 1. 造成伤害
        DamageInfo.DamageType damageType = DamageInfo.DamageType.NORMAL;
        AbstractGameAction.AttackEffect effect = AbstractGameAction.AttackEffect.SLASH_HEAVY;

        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, damageType), effect));

        // 2. 判断敌人意图是否为攻击
        if (m != null && isAttackIntent(m.intent)) {
            int weakStacks = this.magicNumber;
            boolean isSourcePlayer = false;

            addToBot(new ApplyPowerAction(m, p, new WeakPower(m, weakStacks, isSourcePlayer), weakStacks));
        }
    }

    @Override
    public AbstractCard makecopy() {
        return new DeterringStrike();
    }

    /**
     * 辅助方法：检查怪物的意图是否包含攻击
     */
    private boolean isAttackIntent(AbstractMonster.Intent intent) {
        return intent == AbstractMonster.Intent.ATTACK
                || intent == AbstractMonster.Intent.ATTACK_BUFF
                || intent == AbstractMonster.Intent.ATTACK_DEBUFF
                || intent == AbstractMonster.Intent.ATTACK_DEFEND;
    }


}