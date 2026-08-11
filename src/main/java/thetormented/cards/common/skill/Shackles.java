package thetormented.cards.common.skill;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.cards.BaseCard;
import thetormented.cards.special.status.Misery; // Import your Misery status card class here
import thetormented.character.Tormented;
import thetormented.util.CardStats;

public class Shackles extends BaseCard {
    public static final String ID = makeID(Shackles.class.getSimpleName());

    private static final int COST = 1;
    private static final int CARD_ADD = 2;
    private static final int BLOCK_PER_STATUS = 5;
    private static final int UPG_BLOCK_PER_STATUS = 2; // 5 -> 7

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
        setCustomVar("CARD_ADD", CARD_ADD);
        this.cardsToPreview = new Misery();

        // 实时预览：手牌中状态牌数（含本卡将加入的 2 张苦痛）* 每张格挡
        setCustomVar("TOTAL_BLOCK", VariableType.MAGIC, 0, 0, (c, m, base) -> {
            int statusCount = 0;
            AbstractPlayer p = AbstractDungeon.player;
            if (p != null && p.hand != null) {
                for (AbstractCard card : p.hand.group) {
                    if (card.type == CardType.STATUS) {
                        statusCount++;
                    }
                }
            }
            return (statusCount + CARD_ADD) * c.magicNumber;
        });
    }

    @Override
    protected String getInjectedDescription() {
        return extDescription(0);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 1. Add 2 Misery status card to hand
        addToBot(new MakeTempCardInHandAction(new Misery(), CARD_ADD));

        // 2. Count Status cards in hand (including the Misery that will be added, if calculated during/after execution)
        // Note: MakeTempCardInHandAction hasn't finished yet in the action queue,
        // so we calculate dynamically or include a +2 count for the generated Misery.
        int statusCount = 0;
        for (AbstractCard c : p.hand.group) {
            if (c.type == CardType.STATUS) {
                statusCount++;
            }
        }

        // Add 2 for the Misery cards currently queued to enter the hand
        statusCount += CARD_ADD;

        // 3. Gain Block = magicNumber * statusCount
        int totalBlock = this.magicNumber * statusCount;
        if (totalBlock > 0) {
            addToBot(new GainBlockAction(p, p, totalBlock));
        }
    }

    @Override
    public AbstractCard makecopy() {
        return new Shackles();
    }
}