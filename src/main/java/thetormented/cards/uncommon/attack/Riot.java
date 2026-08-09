package thetormented.cards.uncommon.attack;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import thetormented.actions.RiotAction;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.powers.debuff.DebtPower;
import thetormented.util.CardStats;

public class Riot extends BaseCard {
    public static final String ID = makeID(Riot.class.getSimpleName());

    private static final int DAMAGE = 4;
    private static final int UPG_DAMAGE = 1;
    private static final int DAMAGE_ADDITION = 1;
    private static final int UPG_DAMAGE_ADDITION = 1;

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            -1 // X 费
    );

    public Riot() {
        super(ID, info);
        setDamage(DAMAGE, UPG_DAMAGE);
        setMagic(DAMAGE_ADDITION, UPG_DAMAGE_ADDITION);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 1. 获取当前实际可用的能量快照
        int count = this.energyOnUse;
        if (p.hasRelic("Chemical X")) {
            // 注意：不要在这里直接修改 count 影响 Action，留给 Action 处理即可
        }

        // 如果是从手牌正常打出，且未被手动设置 energyOnUse
        if (count == -1) {
            count = EnergyPanel.totalCount;
        }

        // 2. 将能量快照直接传入 RiotAction，不依赖 freeToPlayOnce
        addToBot(new RiotAction(p, m, this.baseDamage, this.magicNumber, count));

        // 3. 必须在 use 结束时将 this.rawDescription 标记或重置，防止重复打出时 energyOnUse 被清零
        // 尖塔的 Power 在重复打出卡牌时会再次调用 card.use(p, m)，此时经过上面的 count 传递，
        // 第二次 use 会直接使用第一次保存在 Action 中的 count 快照！
    }

    // --- 保持面板根据血债实时预览数值 ---
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
        return new Riot();
    }
}