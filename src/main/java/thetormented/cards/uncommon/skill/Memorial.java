package thetormented.cards.uncommon.skill;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.actions.UpdateSinAction;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.powers.debuff.DebtPower;
import thetormented.util.CardStats;

public class Memorial extends BaseCard {
    public static final String ID = makeID(Memorial.class.getSimpleName());

    private static final int COST = 0;
    private static final int BASE_BLOCK = 5;
    private static final int UPG_BLOCK = 3; // 5 -> 8
    private static final int DRAW_AMOUNT = 2;
    private static final int SIN_GAIN = 4;

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            COST
    );

    public Memorial() {
        super(ID, info);
        setBlock(BASE_BLOCK, UPG_BLOCK);
        setMagic(DRAW_AMOUNT);
        setCustomVar("SIN", SIN_GAIN);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean hasDebt = p.hasPower(DebtPower.POWER_ID) && p.getPower(DebtPower.POWER_ID).amount > 0;
        if (!hasDebt) {
            addToBot(new GainBlockAction(p, p, this.block));
            addToBot(new DrawCardAction(p, this.magicNumber));
        }
        addToBot(new UpdateSinAction(p, p, SIN_GAIN));
    }

    @Override
    public AbstractCard makecopy() {
        return new Memorial();
    }
}
