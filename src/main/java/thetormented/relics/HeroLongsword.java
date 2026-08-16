package thetormented.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thetormented.actions.UpdateSinAction;
import thetormented.character.Tormented;

import static thetormented.BasicMod.makeID;

public class HeroLongsword extends BaseRelic {
    public static final String ID = makeID(HeroLongsword.class.getSimpleName());
    private static final int SIN_PER_TURN = 3;

    // 第 1 回合不施加原罪，从第 2 回合起每回合开始施加（给玩家启动时间与容错）。
    private boolean firstTurnSkipped = false;

    public HeroLongsword() {
        super(ID, "heroLongsword", Tormented.Meta.CARD_COLOR, RelicTier.BOSS, LandingSound.CLINK);
    }

    @Override
    public void atBattleStartPreDraw() {
        this.firstTurnSkipped = false;
    }

    @Override
    public void onEquip() {
        AbstractDungeon.player.loseRelic(CursedBrokenBlade.ID);
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if ((c.hasTag(AbstractCard.CardTags.STARTER_STRIKE) || c.hasTag(AbstractCard.CardTags.STARTER_DEFEND)) && c.canUpgrade()) {
                c.upgrade();
                CardCrawlGame.sound.play("CARD_UPGRADE");
            }
        }
    }

    @Override
    public void atTurnStart() {
        if (!this.firstTurnSkipped) {
            this.firstTurnSkipped = true;
            return;
        }
        this.flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new UpdateSinAction(AbstractDungeon.player, AbstractDungeon.player, SIN_PER_TURN));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + SIN_PER_TURN + DESCRIPTIONS[1];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new HeroLongsword();
    }
}
