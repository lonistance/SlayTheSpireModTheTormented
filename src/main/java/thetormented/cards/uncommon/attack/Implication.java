package thetormented.cards.uncommon.attack;

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.powers.debuff.BleedPower;
import thetormented.util.CardStats;

public class Implication extends BaseCard {
    public static final String ID = makeID(Implication.class.getSimpleName());

    // 定义基本数值常量，避免硬编码表达式
    private static final int COST = 1;
    private static final int DAMAGE = 8;
    private static final int UPGRADE_PLUS_DMG = 3; // 8 -> 11

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    public Implication() {
        super(ID, new CardStats(
                Tormented.Meta.CARD_COLOR,
                TYPE,
                RARITY,
                TARGET,
                COST
        ));

        setDamage(DAMAGE, UPGRADE_PLUS_DMG);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 1. 消耗 1 张手牌
        addToBot(new SelectCardsInHandAction(
                1,
                "Exhaust",
                false, // 不可取消
                false, // 不可选择0张
                card -> true,
                abstractCards -> {
                    for (AbstractCard c : abstractCards) {
                        p.hand.moveToExhaustPile(c);
                        c.exhaust = true;
                    }
                    abstractCards.clear();
                }
        ));

        // 2. 对所有敌人造成伤害
        addToBot(new DamageAllEnemiesAction(
                p,
                this.damage,
                DamageInfo.DamageType.NORMAL,
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL
        ));

        // 3. 每对 1 名拥有流血的敌人造成伤害，抽 1 张牌
        int drawAmount = 0;
        for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
            if (mo != null && !mo.isDeadOrEscaped()) {
                AbstractPower bleed = mo.getPower(BleedPower.POWER_ID);
                if (bleed != null && bleed.amount > 0) {
                    drawAmount++;
                }
            }
        }
        if (drawAmount > 0) {
            addToBot(new DrawCardAction(drawAmount));
        }
    }

    @Override
    public AbstractCard makecopy() {
        return new Implication();
    }
}