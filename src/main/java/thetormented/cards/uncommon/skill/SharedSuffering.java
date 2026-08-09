package thetormented.cards.uncommon.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.powers.debuff.DebtPower;
import thetormented.util.CardStats;

public class SharedSuffering extends BaseCard {
    public static final String ID = makeID(SharedSuffering.class.getSimpleName());

    private static final int COST = 1;
    private static final int BASE_BUFF_PER_DEBT = 1;
    private static final int UPG_BUFF_PER_DEBT = 1; // 1 -> 2

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.ALL_ENEMY,
            COST
    );

    public SharedSuffering() {
        super(ID, info);
        setMagic(BASE_BUFF_PER_DEBT, UPG_BUFF_PER_DEBT);

        // 标记消耗属性 (Exhaust)
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 1. 获取玩家身上最新的“血债”层数
        int debtAmount = 0;
        AbstractPower debtPower = p.getPower(DebtPower.POWER_ID);
        if (debtPower != null) {
            debtAmount = debtPower.amount;
        }

        // 2. 只有在血债 > 0 时才施加 Debuff
        if (debtAmount > 0) {
            int stacksToApply = debtAmount * this.magicNumber;

            // 遍历场上所有活着的怪物施加 虚弱 和 易伤
            for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
                if (mo != null && !mo.isDeadOrEscaped()) {
                    // 施加虚弱
                    addToBot(new ApplyPowerAction(
                            mo, p,
                            new WeakPower(mo, stacksToApply, false),
                            stacksToApply
                    ));
                    // 施加易伤
                    addToBot(new ApplyPowerAction(
                            mo, p,
                            new VulnerablePower(mo, stacksToApply, false),
                            stacksToApply
                    ));
                }
            }
        }
    }

    @Override
    public AbstractCard makecopy() {
        return new SharedSuffering();
    }
}