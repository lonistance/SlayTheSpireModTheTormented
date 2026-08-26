package thetormented.cards.rare.skill;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import thetormented.cards.BaseCard;
import thetormented.cards.rare.attack.UnceasingWar;
import thetormented.cards.rare.power.ExecutionForm;
import thetormented.character.Tormented;
import thetormented.util.CardStats;

import java.util.ArrayList;

public class Pardon extends BaseCard {
    public static final String ID = makeID(Pardon.class.getSimpleName());

    private static final int COST = 1;
    private static final int PLAYS_TO_TRANSFORM = 3;

    private int plays = 0;

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            COST
    );

    public Pardon() {
        super(ID, info);
        setMagic(PLAYS_TO_TRANSFORM); // 3, both base and upgraded
        // 动态显示“此牌已打出 N 次”（EXTENDED_DESCRIPTION[0]）
        setCustomVar("PLAYS", VariableType.MAGIC, 0, 0, (c, m, base) -> ((Pardon) c).plays);
    }

    @Override
    protected String getInjectedDescription() {
        return extDescription(0);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> originals = new ArrayList<>();
        ArrayList<AbstractCard> replacements = new ArrayList<>();
        for (int i = 0; i < p.hand.group.size(); i++) {
            AbstractCard c = p.hand.group.get(i);
            if (c == this) {
                continue;
            }
            AbstractCard replacement = getRandomTransformedCard();
            if (replacement == null) {
                continue;
            }
            originals.add(c);
            replacements.add(replacement);
        }

        // 真正把原牌从手牌中移除（removeCard 为纯列表移除，不触发任何消耗类钩子），
        // 再播放官方消耗动画（ExhaustCardEffect 仅视觉：音效/火星/淡出）
        for (AbstractCard c : originals) {
            c.untip();
            c.unhover();
            c.unfadeOut();
            p.hand.removeCard(c);
            AbstractDungeon.effectList.add(new ExhaustCardEffect(c));
        }
        p.hand.refreshHandLayout();

        // 生成方式与官方 DeadBranch 一致：MakeTempCardInHandAction 内部会 markCardAsSeen、
        // 复制一张并播放"飞入手中"动画，同时处理手牌已满等边界
        for (AbstractCard replacement : replacements) {
            this.addToBot(new MakeTempCardInHandAction(replacement, false));
        }

        this.plays++;
        if (this.plays >= this.magicNumber) {
            AbstractCard powerCard = getRandomPowerCard();
            if (powerCard != null) {
                // use() 返回后官方流程会真正消耗本牌（UseCardAction -> moveToExhaustPile，
                // 已自带消耗动画与消耗钩子），这里只需声明消耗并生成能力牌
                this.exhaust = true;
                this.addToBot(new MakeTempCardInHandAction(powerCard, false));
            }
        }
    }

    private boolean isExcluded(AbstractCard c) {
        return c.cardID.equals(this.cardID)
                || c.cardID.equals(UnceasingWar.ID)
                || c.cardID.equals(ExecutionForm.ID);
    }

    private AbstractCard getRandomTransformedCard() {
        ArrayList<AbstractCard> pool = new ArrayList<>();
        for (AbstractCard c : CardLibrary.getAllCards()) {
            if (isExcluded(c)) {
                continue;
            }
            if (c.type == CardType.STATUS || c.type == CardType.CURSE) {
                continue;
            }
            if (c.color != Tormented.Meta.CARD_COLOR) {
                continue;
            }
            if (c.rarity == CardRarity.COMMON || c.rarity == CardRarity.UNCOMMON || c.rarity == CardRarity.RARE) {
                pool.add(c);
            }
        }
        if (pool.isEmpty()) {
            return null;
        }
        AbstractCard copy = pool.get(AbstractDungeon.cardRandomRng.random(pool.size() - 1)).makeCopy();
        if (this.upgraded) {
            copy.upgrade();
        }
        return copy;
    }

    private AbstractCard getRandomPowerCard() {
        ArrayList<AbstractCard> pool = new ArrayList<>();
        for (AbstractCard c : CardLibrary.getAllCards()) {
            if (isExcluded(c)) {
                continue;
            }
            if (c.type != CardType.POWER) {
                continue;
            }
            if (c.color != Tormented.Meta.CARD_COLOR) {
                continue;
            }
            pool.add(c);
        }
        if (pool.isEmpty()) {
            return null;
        }
        AbstractCard copy = pool.get(AbstractDungeon.cardRandomRng.random(pool.size() - 1)).makeCopy();
        if (this.upgraded) {
            copy.upgrade();
        }
        return copy;
    }

    @Override
    public void upgrade() {
        upgradeName(); // plays requirement stays 3
    }

    @Override
    public AbstractCard makecopy() {
        return new Pardon();
    }
}