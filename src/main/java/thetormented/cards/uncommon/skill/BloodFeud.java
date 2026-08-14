package thetormented.cards.uncommon.skill;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.actions.TriggerBleedAction;
import thetormented.cards.BaseCard;
import thetormented.cards.special.status.Misery;
import thetormented.character.Tormented;
import thetormented.util.CardStats;

public class BloodFeud extends BaseCard {
    public static final String ID = makeID(BloodFeud.class.getSimpleName());

    private static final int COST = 1;
    private static final int BASE_PERCENT = 75;  // 升级前结算 75% 的流血
    private static final int UPG_PERCENT = 150;  // 升级后结算 150% 的流血

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,    // RARE -> UNCOMMON
            CardTarget.SELF,
            COST
    );

    public BloodFeud() {
        super(ID, info);
        setMagic(BASE_PERCENT, UPG_PERCENT - BASE_PERCENT);  // 升级只追加 75，合计 150
        this.cardsToPreview = new Misery();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new MakeTempCardInHandAction(new Misery()));
        addToBot(new TriggerBleedAction(this.magicNumber));
    }

    @Override
    public AbstractCard makecopy() {
        return new BloodFeud();
    }
}
