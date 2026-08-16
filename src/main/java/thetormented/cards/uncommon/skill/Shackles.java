package thetormented.cards.uncommon.skill;

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
    private static final int BLOCK_PER_STATUS = 6;
    private static final int UPG_BLOCK_PER_STATUS = 2; // 6 -> 8

    private static final CardStats STATS = new CardStats(
            Tormented.Meta.CARD_COLOR, // White / Neutral / Custom Color
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            COST
    );

    public Shackles() {
        super(ID, STATS);
        setMagic(BLOCK_PER_STATUS, UPG_BLOCK_PER_STATUS);
        // 始终加入 CARD_ADD（2）张苦痛；满手（10 张）时格挡计算只按有效加入的 1 张算。
        // !CARD_ADD! 恒显示 2（=CARD_ADD 默认值，无需变换）。
        this.cardsToPreview = new Misery();

        // 实时预览：手牌中状态牌数（含按有效数量计入的苦痛）* 每张格挡。
        setCustomVar("TOTAL_BLOCK", VariableType.MAGIC, 0, 0, (c, m, base) -> {
            int statusCount = 0;
            AbstractPlayer p = AbstractDungeon.player;
            if (p != null && p.hand != null) {
                for (AbstractCard card : p.hand.group) {
                    if (card.type == CardType.STATUS) {
                        statusCount++;
                    }
                }
                statusCount += (p.hand.size() >= 10) ? 1 : CARD_ADD;
            }
            return statusCount * c.magicNumber;
        });
    }

    @Override
    protected String getInjectedDescription() {
        return extDescription(0);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 始终加入 2 张苦痛（MakeTempCardInHandAction 会把放不下的溢出进弃牌堆）。
        // 满手（打出前手牌 10 张）时，格挡只按有效加入的 1 张苦痛计算。
        addToBot(new MakeTempCardInHandAction(new Misery(), CARD_ADD));
        int effectiveAdd = (p.hand.size() >= 10) ? 1 : CARD_ADD;

        // 统计手牌中状态牌数（含按有效数量计入的苦痛），获得 magicNumber * statusCount 点格挡
        int statusCount = 0;
        for (AbstractCard c : p.hand.group) {
            if (c.type == CardType.STATUS) {
                statusCount++;
            }
        }
        statusCount += effectiveAdd;

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