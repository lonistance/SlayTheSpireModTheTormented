package thetormented.cards.uncommon.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.util.CardStats;

public class BrokenArmor extends BaseCard {
    public static final String ID = makeID(BrokenArmor.class.getSimpleName());

    private static final int COST = 2;
    private static final int BLOCK_AMT = 15;
    private static final int UPG_BLOCK_AMT = 5; // 15 -> 20
    private static final int FRAIL_AMT = 1;

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            COST
    );

    public BrokenArmor() {
        super(ID, info);
        setBlock(BLOCK_AMT, UPG_BLOCK_AMT);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 1. 获得 15 点格挡 (升级后 20)
        addToBot(new GainBlockAction(p, p, this.block));

        // 2. 给自己施加 1 层脆弱
        addToBot(new ApplyPowerAction(
                p,
                p,
                new FrailPower(p, FRAIL_AMT, false),
                FRAIL_AMT
        ));
    }

    @Override
    public AbstractCard makecopy() {
        return new BrokenArmor();
    }
}