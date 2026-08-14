package thetormented.cards.uncommon.skill;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
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
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        CardGroup hand = p.hand;
        ArrayList<AbstractCard> originals = new ArrayList<>();
        ArrayList<AbstractCard> replacements = new ArrayList<>();
        for (int i = 0; i < hand.group.size(); i++) {
            AbstractCard c = hand.group.get(i);
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

        // 先为每张原牌播放"被消耗"动画（仅视觉，不真正消耗）
        for (AbstractCard c : originals) {
            c.untip();
            c.unhover();
            c.unfadeOut();
            playExhaustVisual(c);
        }

        // 替换手牌内容，并为每张生成牌播放"加入手牌"动画
        for (int i = 0; i < originals.size(); i++) {
            AbstractCard old = originals.get(i);
            AbstractCard replacement = replacements.get(i);
            int idx = hand.group.indexOf(old);
            if (idx < 0) {
                continue;
            }
            hand.group.set(idx, replacement);
            playAddedToHandVisual(replacement);
        }
        hand.refreshHandLayout();

        this.plays++;
        if (this.plays >= this.magicNumber) {
            AbstractCard powerCard = getRandomPowerCard();
            int index = hand.group.indexOf(this);
            if (powerCard != null && index >= 0) {
                playExhaustVisual(this);
                hand.group.set(index, powerCard);
                playAddedToHandVisual(powerCard);
                hand.refreshHandLayout();
                this.exhaust = true;
            }
        }
    }

    private void playExhaustVisual(AbstractCard card) {
        card.shrink();
        AbstractDungeon.effectsQueue.add(new ExhaustCardEffect(card));
    }

    private void playAddedToHandVisual(AbstractCard card) {
        AbstractDungeon.effectsQueue.add(new ShowCardAndAddToHandEffect(card));
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