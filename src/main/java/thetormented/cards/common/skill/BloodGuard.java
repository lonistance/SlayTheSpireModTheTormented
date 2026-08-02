package thetormented.cards.common.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.powers.debuff.BleedPower;
import thetormented.util.CardStats;

public class BloodGuard extends BaseCard {
    public static final String ID = makeID(BloodGuard.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR, // 卡牌颜色
            CardType.SKILL,                 // 技能牌
            CardRarity.COMMON,              // 普通
            CardTarget.SELF_AND_ENEMY,               // 目标：自己和单个敌人
            1                               // 1 Cost
    );

    private static final int BASE_BLOCK = 6;
    private static final int UPGRADE_BLOCK = 2;
    private static final int BASE_MAGIC = 6;
    private static final int UPGRADE_MAGIC = 2;

    public BloodGuard() {
        super(ID, info);
        setBlock(BASE_BLOCK,UPGRADE_BLOCK);
        setMagic(BASE_MAGIC,UPGRADE_MAGIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 获得格挡
        int blockAmount = this.block;
        addToBot(new GainBlockAction(p, p, blockAmount));

        // 给予目标敌人 Bleed 效果
        int bleedAmount = this.magicNumber;
        addToBot(new ApplyPowerAction(m, p, new BleedPower(m, bleedAmount), bleedAmount));
    }

    @Override
    public AbstractCard makecopy() {
        return new BloodGuard();
    }


}
