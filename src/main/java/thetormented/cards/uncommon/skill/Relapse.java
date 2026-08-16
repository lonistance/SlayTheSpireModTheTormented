package thetormented.cards.uncommon.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.powers.debuff.BleedPower;
import thetormented.powers.debuff.DeepWoundPower;
import thetormented.util.CardStats;

public class Relapse extends BaseCard {
    public static final String ID = makeID(Relapse.class.getSimpleName());

    // 卡牌属性基础配置：Cost=1, Type=SKILL, Rarity=UNCOMMON, Target=ENEMY
    private static final CardStats STATS = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            1
    );

    private static final int DEEP_WOUND_AMT = 1;
    private static final int BLEED_AMT = 3;

    public Relapse() {
        super(ID, STATS);
        this.baseMagicNumber = DEEP_WOUND_AMT;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 先给目标（升级后为全体敌人）施加 3 层流血，再接原有 DeepWound 逻辑
        if (this.upgraded) {
            // 升级后：给全体敌人施加 3 层流血 + DeepWoundPower
            for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (!mo.isDeadOrEscaped()) {
                    this.addToBot(new ApplyPowerAction(mo, p, new BleedPower(mo, p, BLEED_AMT), BLEED_AMT));
                    this.addToBot(new ApplyPowerAction(
                            mo,
                            p,
                            new DeepWoundPower(mo, p, this.magicNumber),
                            this.magicNumber
                    ));
                }
            }
        } else {
            // 未升级：仅给指定单个敌人施加 3 层流血 + DeepWoundPower
            if (m != null) {
                this.addToBot(new ApplyPowerAction(m, p, new BleedPower(m, p, BLEED_AMT), BLEED_AMT));
                this.addToBot(new ApplyPowerAction(
                        m,
                        p,
                        new DeepWoundPower(m, p, this.magicNumber),
                        this.magicNumber
                ));
            }
        }
    }

    @Override
    public AbstractCard makecopy() {
        return new Relapse();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            // 升级后目标类型切换为全体敌人
            this.target = CardTarget.ALL_ENEMY;
            // 更新描述（将单体描述切换为群体描述）
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}