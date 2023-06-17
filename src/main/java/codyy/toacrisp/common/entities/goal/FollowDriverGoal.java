package codyy.toacrisp.common.entities.goal;

import codyy.toacrisp.common.entities.Wyvern;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class FollowDriverGoal extends Goal {
    private final Wyvern mob;
    private int time;
    private int timeToRecalcPath;

    public FollowDriverGoal(Wyvern mob) {
        this.mob = mob;

        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        return this.mob.previousDriver != null;
    }

    public boolean canContinueToUse() {
        return this.canUse() && !this.mob.isLeashed() && !this.mob.isPassenger() && this.mob.distanceToSqr(this.mob.previousDriver) < 100 && ++time < 120;
    }

    public void start() {
        this.time = 0;
        this.timeToRecalcPath = 0;
    }

    public void stop() {
        this.mob.previousDriver = null;
        this.mob.getNavigation().stop();
    }

    public void tick() {
        Entity following = this.mob.previousDriver;
        this.mob.getLookControl().setLookAt(following, 10.0F, (float) this.mob.getMaxHeadXRot());
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = 10;
            this.mob.getNavigation().moveTo(following, 1.0F);
        }
    }
}