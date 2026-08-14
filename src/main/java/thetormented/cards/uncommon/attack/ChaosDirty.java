package thetormented.cards.uncommon.attack;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import thetormented.actions.PollutedChaosDamageAction;
import thetormented.actions.UpdateSinAction;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.powers.buff.SinPower;
import thetormented.powers.debuff.DebtPower;
import thetormented.util.CardStats;

public class ChaosDirty extends BaseCard {
    public static final String ID = makeID(ChaosDirty.class.getSimpleName());

    private static final int COST = 1;
    private static final int BASE_DAMAGE = 6;   // 5 -> 6
    private static final int UPGRADE_DAMAGE = 1;
    private static final int BASE_MAGIC = 3;
    private static final int UPGRADE_MAGIC = 1;
    private static final int SIN_GAIN = 10;

    public ChaosDirty() {
        super(ID, new CardStats(
                Tormented.Meta.CARD_COLOR,
                CardType.ATTACK,
                CardRarity.UNCOMMON,
                CardTarget.ENEMY,
                COST
        ));

        setDamage(BASE_DAMAGE, UPGRADE_DAMAGE);
        setMagic(BASE_MAGIC, UPGRADE_MAGIC);
        setCustomVar("SIN", SIN_GAIN);

        // 实时预览：总伤害 = 基础伤害 + 血债加成（力量/易伤由 applyPowers 计算）
        setCustomVar("TOTAL_DAMAGE", VariableType.DAMAGE, BASE_DAMAGE);
    }

    @Override
    protected String getInjectedDescription() {
        return extDescription(0);
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

    // 预览必须与 PollutedChaosDamageAction 的结算口径一致：
    // - 无人工制品：打出后原罪会转化为血债，本次打出的加成 = (当前 debt + 本次转化的 debt) * magic
    // - 有人工制品：转化被抵消，本次加成 = 当前 debt * magic
    private int getDebtDamageBonus() {
        AbstractPlayer p = AbstractDungeon.player;
        if (p == null) {
            return 0;
        }

        AbstractPower debtPower = p.getPower(DebtPower.POWER_ID);
        int debt = (debtPower != null) ? debtPower.amount : 0;

        if (p.hasPower(ArtifactPower.POWER_ID)) {
            return debt * this.magicNumber;
        }

        AbstractPower sinPower = p.getPower(SinPower.POWER_ID);
        int currentSin = (sinPower != null) ? sinPower.amount : 0;
        int debtAfterPlay = (currentSin + SIN_GAIN) / SinPower.SIN_PER_DEBT;
        int debtGainFromPlay = debtAfterPlay - (currentSin / SinPower.SIN_PER_DEBT);

        return (debt + debtGainFromPlay) * this.magicNumber;
    }

    @Override
    public AbstractCard makecopy() {
        return new ChaosDirty();
    }
}