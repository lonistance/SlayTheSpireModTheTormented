package thetormented.cards.uncommon.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.util.CardStats;

public class DeterringStrike extends BaseCard {
    public static final String ID = makeID(DeterringStrike.class.getSimpleName());

    private static final int CARD_COST = 1;
    private static final int BASE_DAMAGE = 8;
    private static final int UPGRADE_PLUS_DAMAGE = 3; // 8 -> 11

    // magicNumber 用于表示本回合降低的力量数值
    private static final int BASE_STRENGTH_LOSS = 2;
    private static final int UPGRADE_PLUS_STRENGTH_LOSS = 1; // 2 -> 3

    private static final CardStats STATS = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            CARD_COST
    );

    public DeterringStrike() {
        super(ID, STATS);
        setDamage(BASE_DAMAGE, UPGRADE_PLUS_DAMAGE);
        setMagic(BASE_STRENGTH_LOSS, UPGRADE_PLUS_STRENGTH_LOSS);

        this.tags.add(CardTags.STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 1. 造成伤害
        addToBot(new DamageAction(
                m,
                new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL),
                AbstractGameAction.AttackEffect.SLASH_HEAVY
        ));

        // 2. 若敌人的意图为攻击，使其本回合失去力量
        if (m != null && isAttackIntent(m.intent)) {
            // 施加 -X 力量
            addToBot(new ApplyPowerAction(
                    m, p,
                    new StrengthPower(m, -this.magicNumber),
                    -this.magicNumber
            ));

            // 如果敌人没有 Artifact (人工制品) 抵消 Debuff，则在回合结束时通过 GainStrengthPower 恢复力量
            if (!m.hasPower("Artifact")) {
                addToBot(new ApplyPowerAction(
                        m, p,
                        new GainStrengthPower(m, this.magicNumber),
                        this.magicNumber
                ));
            }
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