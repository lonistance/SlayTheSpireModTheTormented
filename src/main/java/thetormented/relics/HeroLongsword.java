package thetormented.relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
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
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if ((c.hasTag(AbstractCard.CardTags.STARTER_STRIKE) || c.hasTag(AbstractCard.CardTags.STARTER_DEFEND)) && c.canUpgrade()) {
                c.upgrade();
                CardCrawlGame.sound.play("CARD_UPGRADE");
            }
        }
        // 递延移除 CursedBrokenBlade：不可在 onEquip 中直接 loseRelic ——
        // onEquip 由“遗物落位动画结束”时的 AbstractRelic.update() 触发（isAnimating 到达
        // targetX/targetY 后置 isDone 并调用 onEquip），而该 update() 正位于 OverlayMenu
        // 对 player.relics 的 for-each 迭代内部；此时 remove 会在下一次迭代 next() 抛
        // ConcurrentModificationException（v1.0.1 实测崩溃）。改为入队一次性特效，在下一帧
        // 特效更新时移除（彼时对 player.relics 已无存活迭代器）。
        AbstractDungeon.effectList.add(new AbstractGameEffect() {
            {
                this.duration = 0.05f;
            }

            @Override
            public void update() {
                this.duration -= Gdx.graphics.getDeltaTime();
                if (this.duration < 0.0f) {
                    AbstractDungeon.player.loseRelic(CursedBrokenBlade.ID);
                    this.isDone = true;
                }
            }

            @Override
            public void render(SpriteBatch sb) {
            }

            @Override
            public void dispose() {
            }
        });
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
