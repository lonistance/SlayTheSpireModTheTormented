package thetormented.cards.rare.skill;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.util.CardStats;

public class RockSolid extends BaseCard {
    public static final String ID = makeID(RockSolid.class.getSimpleName());

    private static final int COST = 2;
    private static final int BASE_BLOCK = 28;
    private static final int UPG_BLOCK = 8; // 28 -> 36
    private static final int MAX_HP_LOSS = 3;

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            COST
    );

    public RockSolid() {
        super(ID, info);
        setBlock(BASE_BLOCK, UPG_BLOCK);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));
        p.decreaseMaxHealth(MAX_HP_LOSS);
    }

    @Override
    public AbstractCard makecopy() {
        return new RockSolid();
    }
}
