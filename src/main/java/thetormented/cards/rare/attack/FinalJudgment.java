package thetormented.cards.rare.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thetormented.actions.UpdateSinAction;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.powers.buff.SinPower;
import thetormented.powers.debuff.DebtPower;
import thetormented.util.CardStats;

public class FinalJudgment extends BaseCard {
    public static final String ID = makeID(FinalJudgment.class.getSimpleName());

    private static final int COST = 0;
    private static final int BASE_DAMAGE = 30;
    private static final int UPG_DAMAGE = 10; // 30 -> 40
    private static final int MIN_DEBT = 4;

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.RARE,
            CardTarget.ALL_ENEMY,
            COST
    );

    public FinalJudgment() {
        super(ID, info);
        setDamage(BASE_DAMAGE, UPG_DAMAGE);
        this.isMultiDamage = true;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!super.canUse(p, m)) {
            return false;
        }
        if (!(p.hasPower(DebtPower.POWER_ID) && p.getPower(DebtPower.POWER_ID).amount >= MIN_DEBT)) {
            this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
            return false;
        }
        return true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAllEnemiesAction(p, this.multiDamage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.SLASH_HEAVY));

        AbstractPower sin = p.getPower(SinPower.POWER_ID);
        if (sin != null && sin.amount > 0) {
            addToBot(new UpdateSinAction(p, p, -sin.amount));
        }
    }

    @Override
    public AbstractCard makecopy() {
        return new FinalJudgment();
    }
}
