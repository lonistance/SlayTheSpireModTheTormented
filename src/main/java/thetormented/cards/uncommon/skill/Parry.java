package thetormented.cards.uncommon.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.powers.buff.ParryPower;
import thetormented.util.CardStats;

public class Parry extends BaseCard {
    public static final String ID = makeID(Parry.class.getSimpleName());

    private static final int COST = 1;
    private static final int BASE_BLOCK = 6;
    private static final int UPG_BLOCK = 3; // 5 -> 8

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            COST
    );

    public Parry() {
        super(ID, info);
        setBlock(BASE_BLOCK, UPG_BLOCK);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));
        addToBot(new ApplyPowerAction(p, p, new ParryPower(p, p, 1)));
    }

    @Override
    public AbstractCard makecopy() {
        return new Parry();
    }
}
