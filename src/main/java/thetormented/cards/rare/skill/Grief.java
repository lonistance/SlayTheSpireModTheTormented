package thetormented.cards.rare.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
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
import thetormented.powers.buff.GriefPower;
import thetormented.util.CardStats;

import java.util.ArrayList;

public class Grief extends BaseCard {
    public static final String ID = makeID(Grief.class.getSimpleName());

    private static final int COST = 1;
    private static final int STATUS_ADD = 1;
    private static final int BLEED_BASE = 4;   // 3 -> 4
    private static final int BLEED_UPG = 1; // 4 -> 5

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,    //UNCOMMON -> RARE
            CardTarget.SELF,
            COST
    );

    public Grief() {
        super(ID, info);
        setMagic(BLEED_BASE, BLEED_UPG);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new MakeTempCardInHandAction(getRandomStatus()));
        addToBot(new ApplyPowerAction(p, p, new GriefPower(p, p, this.magicNumber), this.magicNumber));
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
