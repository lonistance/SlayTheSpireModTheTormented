package thetormented.cards.uncommon.attack;

import com.evacipated.cardcrawl.mod.stslib.patches.powerInterfaces.BetterOnExhaustPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.cards.BaseCard;
import thetormented.cards.special.statue.Misery;
import thetormented.character.Tormented;
import thetormented.util.CardStats;

public class BravingDangers extends BaseCard {
    // 基础常量配置
    public static final String ID = makeID(BravingDangers.class.getSimpleName());

    private static final int COST = 3;
    private static final int DAMAGE = 18;
    private static final int UPGRADE_DAMAGE = 6;
    private static final int COST_REDUCTION_PER_EXHAUST = 1;

    private static final String PAIN_CARD_ID = Misery.ID;

    public BravingDangers() {
        super(ID, new CardStats(
                Tormented.Meta.CARD_COLOR,
                CardType.ATTACK,
                CardRarity.UNCOMMON,
                CardTarget.ENEMY,
                3
        ));

        // 使用变量保存数值，方便后续调整
        setDamage(DAMAGE, UPGRADE_DAMAGE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 造成攻击伤害
        DamageInfo damageInfo = new DamageInfo(p, this.damage, this.damageTypeForTurn);
        AbstractGameAction.AttackEffect effect = AbstractGameAction.AttackEffect.SLASH_HEAVY;
        addToBot(new DamageAction(m, damageInfo, effect));
    }

    @Override
    public AbstractCard makecopy() {
        return new BravingDangers();
    }

    @Override
    public void triggerOnExhaust() {
        if (this.cardID.equals(Misery.ID)) {
            modifyCostForCombat(-COST_REDUCTION_PER_EXHAUST);
        }
    }
}