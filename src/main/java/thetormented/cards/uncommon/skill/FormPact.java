package thetormented.cards.uncommon.skill;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thetormented.cards.BaseCard;
import thetormented.cards.special.status.Misery; // 请确认 Misery 卡牌的实际类路径
import thetormented.character.Tormented;
import thetormented.util.CardStats;

public class FormPact extends BaseCard {
    public static final String ID = makeID(FormPact.class.getSimpleName());

    private static final int COST = 0;
    private static final int BASE_ENERGY = 2;
    private static final int UPG_ENERGY = 1; // 2 -> 3 能量

    private static final CardStats info = new CardStats(
            Tormented.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            COST
    );

    public FormPact() {
        super(ID, info);
        setMagic(BASE_ENERGY, UPG_ENERGY);

        // 标记衍生牌预览，方便玩家鼠标悬停在《缔结契约》上时预览《苦痛》
        this.cardsToPreview = new Misery();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 1. 获得能量 (未升级 2 点，升级后 3 点)
        addToBot(new GainEnergyAction(this.magicNumber));

        // 2. 将 1 张《苦痛》加入手牌
        addToBot(new MakeTempCardInHandAction(new Misery(), 1));
    }

    @Override
    public AbstractCard makecopy() {
        return new FormPact();
    }
}