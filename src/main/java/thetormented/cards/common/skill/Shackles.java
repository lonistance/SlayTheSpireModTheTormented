package thetormented.cards.common.skill;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.cards.BaseCard;
import thetormented.cards.special.status.Misery; // Import your Misery status card class here
import thetormented.character.Tormented;
import thetormented.util.CardStats;

public class Shackles extends BaseCard {
    public static final String ID = makeID(Shackles.class.getSimpleName());

    private static final int COST = 1;
    private static final int BLOCK_PER_STATUS = 3;
    private static final int UPG_BLOCK_PER_STATUS = 1; // 3 -> 4

    private static final CardStats STATS = new CardStats(
            Tormented.Meta.CARD_COLOR, // White / Neutral / Custom Color
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            COST
    );

    public Shackles() {
        super(ID, STATS);
        setMagic(BLOCK_PER_STATUS, UPG_BLOCK_PER_STATUS);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 1. Add 1 Misery status card to hand
        addToBot(new MakeTempCardInHandAction(new Misery(), 1));

        // 2. Count Status cards in hand (including the Misery that will be added, if calculated during/after execution)
        // Note: MakeTempCardInHandAction hasn't finished yet in the action queue,
        // so we calculate dynamically or include a +1 count for the generated Misery.
        int statusCount = 0;
        for (AbstractCard c : p.hand.group) {
            if (c.type == CardType.STATUS) {
                statusCount++;
            }
        }

        // Add 1 for the Misery card currently queued to enter the hand
        statusCount += 1;

        // 3. Gain Block = magicNumber * statusCount
        int totalBlock = this.magicNumber * statusCount;
        setCustomVar("TOTAL_BLOCK", totalBlock);
        if (totalBlock > 0) {
            addToBot(new GainBlockAction(p, p, totalBlock));
        }
    }

    @Override
    public AbstractCard makecopy() {
        return new Shackles();
    }
}