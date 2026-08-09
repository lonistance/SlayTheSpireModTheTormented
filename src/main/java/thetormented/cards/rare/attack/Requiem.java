package thetormented.cards.rare.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.cards.BaseCard;
import thetormented.cards.special.status.Misery;
import thetormented.character.Tormented;
import thetormented.util.CardStats;

public class Requiem extends BaseCard {
    public static final String ID = makeID(Requiem.class.getSimpleName());

    private static final int COST = 3;
    private static final int BASE_DAMAGE = 5;
    private static final int UPG_DAMAGE = 1;

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.RARE,
            CardTarget.ALL_ENEMY,
            COST
    );

    public Requiem() {
        super(ID, info);
        setDamage(BASE_DAMAGE, UPG_DAMAGE);
        setCustomVar("HITS", VariableType.MAGIC, 0, 0, (c, m, base) -> exhaustedMiseryCount());
    }

    private int exhaustedMiseryCount() {
        if (AbstractDungeon.player == null || AbstractDungeon.player.exhaustPile == null) {
            return 0;
        }
        int count = 0;
        for (AbstractCard c : AbstractDungeon.player.exhaustPile.group) {
            if (Misery.ID.equals(c.cardID)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int hits = exhaustedMiseryCount();
        for (int i = 0; i < hits; i++) {
            AbstractMonster target = AbstractDungeon.getMonsters().getRandomMonster(true);
            if (target != null) {
                addToBot(new DamageAction(target, new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            }
        }
    }

    @Override
    public AbstractCard makecopy() {
        return new Requiem();
    }
}
