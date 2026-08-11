package thetormented.cards.uncommon.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.util.CardStats;

public class BrokenArmor extends BaseCard {
    public static final String ID = makeID(BrokenArmor.class.getSimpleName());

    private static final int COST = 2;
    private static final int BLOCK_AMT = 16;
    private static final int BASE_WEAK_AMT = 2;
    private static final int UPG_WEAK_AMT = -1; // 2 -> 1 层虚弱

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            COST
    );

    public BrokenArmor() {
        super(ID, info);
        setBlock(BLOCK_AMT);
        setMagic(BASE_WEAK_AMT, UPG_WEAK_AMT);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 1. 获得 16 点格挡
        addToBot(new GainBlockAction(p, p, this.block));

        // 2. 给自己施加虚弱 (未升级 2 层，升级后 1 层)
        addToBot(new ApplyPowerAction(
                p,
                p,
                new WeakPower(p, this.magicNumber, false),
                this.magicNumber
        ));
    }

    @Override
    public AbstractCard makecopy() {
        return new BrokenArmor();
    }
}