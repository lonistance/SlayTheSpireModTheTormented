package thetormented.cards.rare.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import thetormented.actions.UnceasingWarKillAction;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.util.CardStats;

public class UnceasingWar extends BaseCard {
    public static final String ID = makeID(UnceasingWar.class.getSimpleName());

    private static final int COST = 1;
    private static final int BASE_DAMAGE = 4;
    private static final int UPG_DAMAGE = 1; // 4 -> 5
    private static final int BASE_HITS = 2;

    private static int bonusHits = 0;

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.RARE,
            CardTarget.ENEMY,
            COST
    );

    public UnceasingWar() {
        super(ID, info);
        setDamage(BASE_DAMAGE, UPG_DAMAGE);
        setCustomVar("HITS", VariableType.MAGIC, BASE_HITS, 0, (c, m, base) -> base + bonusHits);
        setExhaust(true);
    }

    public static void addBonusHit() {
        bonusHits++;
    }

    public static void resetBonusHits() {
        bonusHits = 0;
    }

    private static void refreshDisplayedCopies() {
        if (AbstractDungeon.player == null) {
            return;
        }
        for (AbstractCard c : AbstractDungeon.player.hand.group) refreshIfCopy(c);
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) refreshIfCopy(c);
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) refreshIfCopy(c);
        for (AbstractCard c : AbstractDungeon.player.exhaustPile.group) refreshIfCopy(c);
    }

    private static void refreshIfCopy(AbstractCard c) {
        if (c instanceof UnceasingWar) {
            c.applyPowers();
        }
    }

    public static void onKill() {
        addBonusHit();
        refreshDisplayedCopies();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean countKill = !m.isDeadOrEscaped() && !m.hasPower(MinionPower.POWER_ID);
        int hits = BASE_HITS + bonusHits;
        for (int i = 0; i < hits; i++) {
            addToBot(new DamageAction(m, new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }
        addToBot(new UnceasingWarKillAction(m, countKill));
    }

    @Override
    public AbstractCard makecopy() {
        return new UnceasingWar();
    }
}
