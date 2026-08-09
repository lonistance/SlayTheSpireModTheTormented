package thetormented.cards.rare.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import thetormented.cards.BaseCard;
import thetormented.character.Tormented;
import thetormented.powers.buff.IndignationPower;
import thetormented.util.CardStats;

public class Indignation extends BaseCard {
    public static final String ID = makeID(Indignation.class.getSimpleName());

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            -1 // X 费
    );

    public Indignation() {
        super(ID, info);
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int effect = this.energyOnUse;
        if (effect == -1) {
            effect = EnergyPanel.totalCount;
        }
        if (this.upgraded) {
            effect++;
        }
        if (p.hasRelic("Chemical X")) {
            effect += 2;
            p.getRelic("Chemical X").flash();
        }

        if (effect > 0) {
            addToBot(new ApplyPowerAction(p, p, new IndignationPower(p, effect)));
        }
    }

    @Override
    public AbstractCard makecopy() {
        return new Indignation();
    }
}
