package thetormented.powers.buff; // 请修改为你的实际包名

import com.megacrit.cardcrawl.core.AbstractCreature;
import thetormented.powers.BasePower;

import static thetormented.BasicMod.makeID;


public class SinPower extends BasePower {
    public static final String POWER_ID = makeID(SinPower.class.getSimpleName());

    // 每 SIN_PER_DEBT 点原罪折算 1 点血债（也用于被人工制品抵消血债时的同步扣除）
    public static final int SIN_PER_DEBT = 5;

    private static final PowerType POWER_TYPE = PowerType.BUFF;
    private static final boolean IS_TURN_BASED = false;

    public SinPower(AbstractCreature owner, int amount) {
        super(POWER_ID, POWER_TYPE, IS_TURN_BASED, owner, amount);
    }

    public SinPower(AbstractCreature owner, AbstractCreature source,int amount) {
        super(POWER_ID, POWER_TYPE, IS_TURN_BASED, owner, source, amount);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + 1 + DESCRIPTIONS[1];
    }
}