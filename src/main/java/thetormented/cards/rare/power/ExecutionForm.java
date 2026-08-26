package thetormented.cards.rare.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.powers.buff.ExecutionFormPower;
import thetormented.util.CardStats;

public class ExecutionForm extends BaseCard {
    public static final String ID = makeID(ExecutionForm.class.getSimpleName());

    private static final int COST = 3;
    private static final int BASE_ATTACKS = 1;
    private static final int UPG_ATTACKS = 1; // 1 -> 2

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.RARE,
            CardTarget.SELF,
            COST
    );

    public ExecutionForm() {
        super(ID, info);
        setMagic(BASE_ATTACKS, UPG_ATTACKS);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new ExecutionFormPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractCard makecopy() {
        return new ExecutionForm();
    }
}
