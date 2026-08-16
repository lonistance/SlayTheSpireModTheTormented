package thetormented.cards.uncommon.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.cards.BaseCard;
import thetormented.cards.special.status.Misery; // 假设你的 Misery 类的包路径，按实际情况调整
import thetormented.character.Tormented;
import thetormented.util.CardStats;

public class HeavyPast extends BaseCard {
    public static final String ID = makeID(HeavyPast.class.getSimpleName());

    private static final int COST = 1;
    private static final int DAMAGE = 6;
    private static final int UPG_DAMAGE = 2; // 6 -> 8
    private static final int HITS = 2;       // 造成 2 次伤害
    private static final int DRAW_BASE = 1;
    private static final int DRAW_UPG = 0;   // 升级不增加抽牌数（保持 1）

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            COST
    );

    public HeavyPast() {
        super(ID, info);
        setDamage(DAMAGE, UPG_DAMAGE);
        setMagic(DRAW_BASE, DRAW_UPG);

        // 如果 Misery 是状态牌，可以在预览中展示它（方便玩家鼠标悬停时查看生成的牌）
        this.cardsToPreview = new Misery();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 1. 造成 2 次伤害
        for (int i = 0; i < HITS; i++) {
            addToBot(new DamageAction(
                    m,
                    new DamageInfo(p, damage, damageTypeForTurn),
                    AbstractGameAction.AttackEffect.SLASH_DIAGONAL
            ));
        }

        // 2. 向抽牌堆随机位置加入 1 张 Misery
        // 参数：卡牌对象, 数量, 是否随机位置(true=随机, false=顶部), 是否自动排队(true)
        addToBot(new MakeTempCardInDrawPileAction(new Misery(), 1, true, true));

        // 3. 向弃牌堆加入 1 张 Misery
        addToBot(new MakeTempCardInDiscardAction(new Misery(), 1));

        // 4. 抽 !M! 张牌
        addToBot(new DrawCardAction(p, this.magicNumber));
    }

    @Override
    public AbstractCard makecopy() {
        return new HeavyPast();
    }
}