package thetormented.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;

public class BetaArtUnlockPatch {
    @SpirePatch(clz = SingleCardViewPopup.class, method = "canToggleBetaArt")
    public static class CanToggleBetaArt {
        @SpirePrefixPatch
        public static SpireReturn<Boolean> Prefix() {
            return SpireReturn.Return(true);
        }
    }
}
