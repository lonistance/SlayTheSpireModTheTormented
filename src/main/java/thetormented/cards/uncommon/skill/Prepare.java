package thetormented.cards.uncommon.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EquilibriumPower;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.util.CardStats;

public class Prepare extends BaseCard {
    public static final String ID = makeID(Prepare.class.getSimpleName());

    private static final int COST = 1;
    private static final int BASE_DRAW = 2;
    private static final int UPG_DRAW = 1; // 2 -> 3 张牌

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.NONE, // 保留/抽牌类技能牌无目标选择，使用 NONE 或 SELF 均可
            COST
    );

    public Prepare() {
        super(ID, info);
        setMagic(BASE_DRAW, UPG_DRAW);

        // 固有与消耗设置
        this.isInnate = true;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 1. 抽牌 (未升级 2 张，升级后 3 张)
        addToBot(new DrawCardAction(p, this.magicNumber));

        // 2. 使用原版的 EquilibriumPower（均衡/保留效果）
        // EquilibriumPower 的构造函数参数为 (AbstractCreature owner, int amount)
        addToBot(new ApplyPowerAction(p, p, new EquilibriumPower(p, 1), 1));
    }

    @Override
    public AbstractCard makecopy() {
        return new Prepare();
    }
}