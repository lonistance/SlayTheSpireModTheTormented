package thetormented.cards.common.skill;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.actions.PredictiveMeasuresAction;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.util.CardStats;

public class PredictiveMeasures extends BaseCard {
    public static final String ID = makeID(PredictiveMeasures.class.getSimpleName());

    // 数值常量（避免硬编码，方便后期调参）
    private static final int COST = 1;
    private static final int UPGRADED_COST = 0;
    private static final int DRAW_AMOUNT = 1;
    private static final int REDUCED_COST_THIS_TURN = 0;

    private static final CardStats STATS = new CardStats(
            Tormented.Meta.CARD_COLOR, // 替换为您的角色卡牌颜色Enum
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            COST
    );

    public PredictiveMeasures() {
        super(ID, STATS);
        setCostUpgrade(UPGRADED_COST);
        this.baseMagicNumber = DRAW_AMOUNT;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 将抽牌与判断逻辑交由自定义 Action 处理
        addToBot(new PredictiveMeasuresAction(this.magicNumber, REDUCED_COST_THIS_TURN));
    }

    @Override
    public AbstractCard makecopy() {
        return new PredictiveMeasures();
    }
}