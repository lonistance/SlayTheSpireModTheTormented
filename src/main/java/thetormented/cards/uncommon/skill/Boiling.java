package thetormented.cards.uncommon.skill;

import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.actions.ApplyBleedAction;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.util.CardStats;

import java.util.ArrayList;

public class Boiling extends BaseCard {
    public static final String ID = makeID(Boiling.class.getSimpleName());

    private static final int COST = 1;
    private static final int BASE_BLEED = 5;
    private static final int UPG_BLEED = 1; // 5 -> 6

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.NONE,
            COST
    );

    public Boiling() {
        super(ID, info);
        setMagic(BASE_BLEED, UPG_BLEED);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> cardsToExhaust = new ArrayList<>();
        for (AbstractCard c : p.hand.group) {
            if (c != this && c.type != CardType.ATTACK) {
                cardsToExhaust.add(c);
            }
        }

        // 消耗所有非攻击牌
        for (AbstractCard c : cardsToExhaust) {
            addToBot(new ExhaustSpecificCardAction(c, p.hand));
        }

        // 每消耗 1 张牌，给予随机 1 名敌人 !M! 层流血
        for (int i = 0; i < cardsToExhaust.size(); i++) {
            AbstractMonster mo = AbstractDungeon.getRandomMonster();
            if (mo != null && !mo.isDeadOrEscaped()) {
                addToBot(new ApplyBleedAction(mo, p, this.magicNumber));
            }
        }
    }

    @Override
    public AbstractCard makecopy() {
        return new Boiling();
    }
}
