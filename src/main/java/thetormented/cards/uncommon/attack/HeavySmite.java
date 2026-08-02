package thetormented.cards.uncommon.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.powers.debuff.DebtPower; // 替换为您项目中实际的血债 Power 类名或 ID
import thetormented.util.CardStats;

public class HeavySmite extends BaseCard {
    public static final String ID = makeID(HeavySmite.class.getSimpleName());

    // 声明常量变量，避免数字/字符串硬编码参与计算或函数调用
    private static final int CARD_COST = 1;
    private static final int BASE_DAMAGE = 12;
    private static final int UPGRADE_PLUS_DAMAGE = 4;
    private static final int REQUIRED_BLOOD_DEBT_AMOUNT = 2;

    private static final String BLOOD_DEBT_POWER_ID = DebtPower.POWER_ID;
    private static final String CARD_CANT_PLAY_MESSAGE = "需要至少 2 层血债才能打出。";

    private static final CardStats STATS = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            CARD_COST
    );

    public HeavySmite() {
        super(ID, STATS);
        this.baseDamage = BASE_DAMAGE;
        this.baseMagicNumber = REQUIRED_BLOOD_DEBT_AMOUNT;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canUseCard = super.canUse(p, m);
        if (!canUseCard) {
            return false;
        }

        int currentBloodDebt = getPowerAmount(p, BLOOD_DEBT_POWER_ID);
        int requiredAmount = this.magicNumber;

        if (currentBloodDebt < requiredAmount) {
            this.cantUseMessage = CARD_CANT_PLAY_MESSAGE;
            return false;
        }

        return true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        DamageInfo.DamageType damageType = DamageInfo.DamageType.NORMAL;
        AbstractGameAction.AttackEffect attackEffect = AbstractGameAction.AttackEffect.BLUNT_HEAVY;

        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, damageType), attackEffect));
    }

    @Override
    public AbstractCard makecopy() {
        return new HeavySmite();
    }

    /**
     * 辅助获取玩家身上的特定 Buff/Debuff 层数
     */
    private int getPowerAmount(AbstractPlayer player, String powerID) {
        if (player == null || player.powers == null) {
            return 0;
        }
        for (AbstractPower power : player.powers) {
            if (power != null && powerID.equals(power.ID)) {
                return power.amount;
            }
        }
        return 0;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            initializeDescription();
        }
    }
}