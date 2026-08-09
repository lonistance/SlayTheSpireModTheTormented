package thetormented.cards.uncommon.skill;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.cards.BaseCard;
import thetormented.cards.special.status.Misery;
import thetormented.character.Tormented;
import thetormented.util.CardStats;

import java.util.ArrayList;

public class Grief extends BaseCard {
    public static final String ID = makeID(Grief.class.getSimpleName());

    private static final int COST = 1;
    private static final int STATUS_ADD = 2;
    private static final int BASE_BLOCK = 3;
    private static final int UPG_BLOCK = 1; // 3 -> 4

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            COST
    );

    public Grief() {
        super(ID, info);
        setMagic(BASE_BLOCK, UPG_BLOCK);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < STATUS_ADD; i++) {
            addToBot(new MakeTempCardInHandAction(getRandomStatus()));
        }

        int statusCount = 0;
        for (AbstractCard c : p.hand.group) {
            if (c.type == CardType.STATUS) {
                statusCount++;
            }
        }
        statusCount += STATUS_ADD;

        int totalBlock = this.magicNumber * statusCount;
        if (totalBlock > 0) {
            addToBot(new GainBlockAction(p, p, totalBlock));
        }
    }

    private AbstractCard getRandomStatus() {
        ArrayList<AbstractCard> pool = new ArrayList<>();
        pool.add(new Slimed());
        pool.add(new Wound());
        pool.add(new Dazed());
        pool.add(new Burn());
        pool.add(new VoidCard());
        pool.add(new Misery());
        return pool.get(AbstractDungeon.miscRng.random(pool.size() - 1));
    }

    @Override
    public AbstractCard makecopy() {
        return new Grief();
    }
}
