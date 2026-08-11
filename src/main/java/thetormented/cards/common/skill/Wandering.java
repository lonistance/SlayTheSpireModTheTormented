package thetormented.cards.common.skill;

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.actions.UpdateSinAction;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.util.CardStats;

public class Wandering extends BaseCard {
    public static final String ID = makeID(Wandering.class.getSimpleName());

    private static final int COST = 0;
    private static final int DRAW_AMOUNT = 1;
    private static final int UPG_DRAW_AMOUNT = 1; // 升级增加 1 张抽牌（共 2 张）
    private static final int SIN_REDUCTION = 3;

    // 卡牌属性配置
    private static final CardType CARD_TYPE = CardType.SKILL;
    private static final CardRarity CARD_RARITY = CardRarity.COMMON;
    private static final CardTarget CARD_TARGET = CardTarget.SELF;

    public Wandering() {
        super(ID, new CardStats(
                Tormented.Meta.CARD_COLOR, // 替换为你的角色卡牌颜色
                CARD_TYPE,
                CARD_RARITY,
                CARD_TARGET,
                COST
        ));
        setMagic(DRAW_AMOUNT, UPG_DRAW_AMOUNT);
        setCustomVar("SIN", SIN_REDUCTION);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DrawCardAction(magicNumber));
        addToBot(new SelectCardsInHandAction(
                1,
                "Exhaust",
                false, // 不可取消
                false, // 不可选择0张
                card -> true, // 过滤条件：手牌中的任何牌都可以选
                abstractCards -> {
                    // 选牌完成后的回调闭包
                    for (AbstractCard c : abstractCards) {
                        // 执行消耗
                        p.hand.moveToExhaustPile(c);
                        c.exhaust = true;

                        // 判定是否为状态牌
                        if (c.type == AbstractCard.CardType.STATUS) {
                            addToTop(new UpdateSinAction(p, p, -SIN_REDUCTION));
                        }
                    }
                    abstractCards.clear();
                }
        ));
    }

    @Override
    public AbstractCard makecopy() {
        return new Wandering();
    }
}
