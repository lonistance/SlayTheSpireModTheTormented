package thetormented.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thetormented.cards.common.skill.BrokenArmor;
import thetormented.powers.buff.ReconcileFatePower;

// 每场战斗开始时复位"战斗中才持续"的卡牌状态（当前：BrokenArmor 的格挡衰减、
// ReconcileFatePower 的已触发卡实例集合）。
// 牌堆实例跨战斗复用，字段本身会残留，必须在此复位。
public class CombatStartPatch {
    @SpirePatch(clz = AbstractPlayer.class, method = "applyStartOfCombatLogic")
    public static class ResetCombatCounters {
        @SpirePrefixPatch
        public static void Prefix(AbstractPlayer __instance) {
            ReconcileFatePower.clearFiredCards();
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                if (c instanceof BrokenArmor) {
                    BrokenArmor ba = (BrokenArmor) c;
                    ba.baseBlock = ba.initialBaseBlock;
                }
            }
        }
    }
}