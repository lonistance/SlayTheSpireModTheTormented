package thetormented.cards.uncommon.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.powers.debuff.BleedPower;// 请确认你的血债 Power 实际路径
import thetormented.powers.debuff.DebtPower;
import thetormented.util.CardStats;

public class Bloodstain extends BaseCard {
    public static final String ID = makeID(Bloodstain.class.getSimpleName());

    private static final int COST = 1;
    private static final int BASE_BLEED = 4;
    private static final int UPG_BLEED = 0; // 升级不增加施加流血

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            COST
    );

    public Bloodstain() {
        super(ID, info);
        setMagic(BASE_BLEED, UPG_BLEED);
        setExhaust(true, false); // 基础消耗，升级后去除消耗
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            // super.upgrade() 会处理升级描述切换与消耗去除（baseExhaust ^ upgExhaust）
            super.upgrade();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m == null) return;

        // 1. 获取玩家当前的血债层数
        int debtAmount = 0;
        AbstractPower debtPower = p.getPower(DebtPower.POWER_ID);
        if (debtPower != null) {
            debtAmount = debtPower.amount;
        }

        // 2. 计算总共给予的“次数”：基础 1 次 + 每有 1 层血债额外 1 次
        int totalTimes = 1 + debtAmount;

        // 3. 循环触发指定次数的 ApplyPowerAction（确保是独立的“多次”施加）
        for (int i = 0; i < totalTimes; i++) {
            addToBot(new ApplyPowerAction(
                    m,
                    p,
                    new BleedPower(m, p, this.magicNumber),
                    this.magicNumber
            ));
        }
    }

    @Override
    public AbstractCard makecopy() {
        return new Bloodstain();
    }
}