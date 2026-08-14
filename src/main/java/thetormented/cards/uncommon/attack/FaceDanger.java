package thetormented.cards.uncommon.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.cards.BaseCard;
import thetormented.cards.special.status.Misery; // 假设“苦痛/苦难”卡牌的类路径
import thetormented.character.Tormented;
import thetormented.util.CardStats;

public class FaceDanger extends BaseCard {
    public static final String ID = makeID(FaceDanger.class.getSimpleName());

    private static final int COST = 3;
    private static final int DAMAGE = 16;
    private static final int UPG_DAMAGE = 4; // 16 -> 20

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            COST
    );

    public FaceDanger() {
        super(ID, info);
        setDamage(DAMAGE, UPG_DAMAGE);
        this.cardsToPreview = new Misery();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(
                m,
                new DamageInfo(p, damage, damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_HEAVY
        ));
    }

    /**
     * 计算本场战斗中打出 Misery 的总次数
     */
    private int countPlayedMisery() {
        int count = 0;
        if (AbstractDungeon.actionManager != null) {
            for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisCombat) {
                // 校验卡牌是否为苦痛/苦难（可通过卡牌类或 ID 进行比对）
                if (c instanceof Misery || (c.cardID != null && c.cardID.endsWith("Misery"))) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 当场上/手牌触发更新时重新计算卡牌费用
     */
    @Override
    public void applyPowers() {
        super.applyPowers();
        updateCostByMisery();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        updateCostByMisery();
    }

    /**
     * 当其他卡牌被打出时触发，用于实时更新手牌中的费用显示
     */
    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        updateCostByMisery();
    }

    /**
     * 核心逻辑：根据打出的苦痛数量设定当回合/当前卡牌的实际消耗
     */
    private void updateCostByMisery() {
        int playedCount = countPlayedMisery();
        int newCost = Math.max(0, COST - playedCount);

        // setCostForTurn 会动态更新卡牌当前的打出费用（不会永久破坏原始 baseCost）
        if (this.costForTurn != newCost) {
            setCostForTurn(newCost);
        }
    }

    @Override
    public AbstractCard makecopy() {
        return new FaceDanger();
    }
}