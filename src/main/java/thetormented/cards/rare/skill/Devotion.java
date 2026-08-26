package thetormented.cards.rare.skill;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import thetormented.actions.UpdateDebtAction;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.powers.debuff.DebtPower;
import thetormented.util.CardStats;

public class Devotion extends BaseCard {
    public static final String ID = makeID(Devotion.class.getSimpleName());

    private static final int COST = 0;
    private static final int BASE_DRAW = 1;
    private static final int UPG_DRAW = 1; // 1 -> 2

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            COST
    );

    public Devotion() {
        super(ID, info);
        setMagic(BASE_DRAW, UPG_DRAW);
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int debt = p.hasPower(DebtPower.POWER_ID) ? p.getPower(DebtPower.POWER_ID).amount : 0;
        int vuln = p.hasPower(VulnerablePower.POWER_ID) ? p.getPower(VulnerablePower.POWER_ID).amount : 0;
        int weak = p.hasPower(WeakPower.POWER_ID) ? p.getPower(WeakPower.POWER_ID).amount : 0;
        int frail = p.hasPower(FrailPower.POWER_ID) ? p.getPower(FrailPower.POWER_ID).amount : 0;

        int total = debt + vuln + weak + frail;

        if (debt > 0) {
            addToBot(new UpdateDebtAction(p, p, -debt));
        }
        if (vuln > 0) {
            addToBot(new ReducePowerAction(p, p, VulnerablePower.POWER_ID, vuln));
        }
        if (weak > 0) {
            addToBot(new ReducePowerAction(p, p, WeakPower.POWER_ID, weak));
        }
        if (frail > 0) {
            addToBot(new ReducePowerAction(p, p, FrailPower.POWER_ID, frail));
        }

        if (total > 0) {
            addToBot(new GainEnergyAction(total));
            addToBot(new DrawCardAction(p, total * this.magicNumber));
        }
    }

    @Override
    public AbstractCard makecopy() {
        return new Devotion();
    }
}
