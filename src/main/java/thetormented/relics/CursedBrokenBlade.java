package thetormented.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thetormented.actions.CursedBrokenBladeAction;
import thetormented.actions.UpdateSinAction;
import thetormented.character.Tormented;

import static thetormented.BasicMod.makeID;

public class CursedBrokenBlade extends BaseRelic {
    public static final String ID = makeID(CursedBrokenBlade.class.getSimpleName());
    private static final int SIN_PER_TURN = 3;

    // 第 1 回合不施加原罪，从第 2 回合起每回合开始施加（给玩家启动时间与容错）。
    private boolean firstTurnSkipped = false;

    public CursedBrokenBlade() {
        super(ID, "cursedBrokenBlade", Tormented.Meta.CARD_COLOR, RelicTier.STARTER, LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStartPreDraw() {
        this.firstTurnSkipped = false;
        this.flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new CursedBrokenBladeAction());
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
        return new CursedBrokenBlade();
    }
}
