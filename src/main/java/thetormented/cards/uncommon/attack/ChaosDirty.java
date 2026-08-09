package thetormented.cards.uncommon.attack;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thetormented.actions.PollutedChaosDamageAction;
import thetormented.actions.UpdateSinAction;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.powers.debuff.DebtPower;
import thetormented.util.CardStats;

public class ChaosDirty extends BaseCard {
    public static final String ID = makeID(ChaosDirty.class.getSimpleName());

    private static final int COST = 1;
    private static final int BASE_DAMAGE = 5;
    private static final int BASE_MAGIC = 3;
    private static final int UPGRADE_MAGIC = 1; // 3 -> 4
    private static final int SIN_GAIN = 10;

    public ChaosDirty() {
        super(ID, new CardStats(
                Tormented.Meta.CARD_COLOR,
                CardType.ATTACK,
                CardRarity.UNCOMMON,
                CardTarget.ENEMY,
                COST
        ));

        setDamage(BASE_DAMAGE);
        setMagic(BASE_MAGIC, UPGRADE_MAGIC);
        setCustomVar("SIN", SIN_GAIN);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 1. 获得原罪（如获得原罪会引发血债变化，也是先压入队列）
        addToBot(new UpdateSinAction(p, p, SIN_GAIN));

        // 2. 将自定义伤害 Action 压入队列
        // 此 Action 会等待 UpdateSinAction 及前面所有 Buff 变更完全结算后再计算伤害！
        addToBot(new PollutedChaosDamageAction(this, p, m));
    }

    // --- 保持手牌中实时显示伤害数字的面板预览 ---
    @Override
    public void applyPowers() {
        int originalBaseDamage = this.baseDamage;
        this.baseDamage += getDebtDamageBonus();
        super.applyPowers();
        this.baseDamage = originalBaseDamage;
        this.isDamageModified = (this.damage != this.baseDamage);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int originalBaseDamage = this.baseDamage;
        this.baseDamage += getDebtDamageBonus();
        super.calculateCardDamage(mo);
        this.baseDamage = originalBaseDamage;
        this.isDamageModified = (this.damage != this.baseDamage);
    }

    private int getDebtDamageBonus() {
        AbstractPlayer p = AbstractDungeon.player;
        if (p != null) {
            AbstractPower debtPower = p.getPower(DebtPower.POWER_ID);
            if (debtPower != null && debtPower.amount > 0) {
                return debtPower.amount * this.magicNumber;
            }
        }
        return 0;
    }

    @Override
    public AbstractCard makecopy() {
        return new ChaosDirty();
    }
}