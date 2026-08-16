package thetormented.cards.common.skill;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.util.CardStats;

public class BrokenArmor extends BaseCard {
    public static final String ID = makeID(BrokenArmor.class.getSimpleName());

    private static final int COST = 1;
    private static final int BLOCK_AMT = 10;
    private static final int UPG_BLOCK_AMT = 4; // 10 -> 14
    private static final int BLOCK_DECAY = 2;

    // 初始格挡（含升级）；每场战斗开始由 CombatStartPatch 复位
    public int initialBaseBlock;

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            COST
    );

    public BrokenArmor() {
        super(ID, info);
        setBlock(BLOCK_AMT, UPG_BLOCK_AMT);
        this.initialBaseBlock = this.baseBlock;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 本次按当前格挡获得，随后直接衰减基础格挡 2 点（不降为负）
        addToBot(new GainBlockAction(p, p, this.block));
        this.baseBlock = Math.max(0, this.baseBlock - BLOCK_DECAY);
        this.isBlockModified = true;
    }

    @Override
    public void upgrade() {
        super.upgrade();
        this.initialBaseBlock = this.baseBlock;
    }

    @Override
    public AbstractCard makecopy() {
        return new BrokenArmor();
    }
}