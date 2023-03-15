package coda.toacrisp.common.entities.goal;

import coda.toacrisp.common.entities.Wyvern;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class FlyingDragonWanderGoal extends RandomStrollGoal {
    private final Wyvern dragon;
    private final int distance;
    private final int verticalRange;

    public FlyingDragonWanderGoal(Wyvern dragon, int distance, int verticalRange) {
        super(dragon, 1.0F, 1);
        this.dragon = dragon;
        this.distance = distance;
        this.verticalRange = verticalRange;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    public boolean canUse() {
        if (this.dragon.isVehicle()) {
            return false;
        } else {
            if (!this.forceTrigger) {
                if (this.dragon.getNoActionTime() >= 100) {
                    return false;
                }

                if (this.dragon.getRandom().nextInt(this.interval) != 0) {
                    return false;
                }
            }

            Vec3 vec3 = this.findPos();
            if (vec3 == null) {
                return false;
            } else {
                this.wantedX = vec3.x;
                this.wantedY = vec3.y;
                this.wantedZ = vec3.z;
                this.forceTrigger = false;
                return true;
            }
        }
    }

    public boolean canContinueToUse() {
        boolean flag = !this.dragon.getNavigation().isDone() && !this.dragon.isVehicle();
        BlockPos pos = getBlockUnder(dragon);

        if (pos != null) {
            if (dragon.level.getBlockState(pos).is(Blocks.WATER)) {
                return true;
            } else {
                return flag;
            }
        } else {
            return flag;
        }
    }

    public void start() {
        this.dragon.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
    }

    public void stop() {
        super.stop();
    }

    @Nullable
    private Vec3 findPos() {
        Vec3 vec3 = dragon.getViewVector(0.0F);

        Vec3 vec32 = HoverRandomPos.getPos(dragon, distance, verticalRange, vec3.x, vec3.z, Mth.PI / 2F, 3, 1);
        return vec32 != null ? vec32 : AirAndWaterRandomPos.getPos(dragon, distance, verticalRange, -2, vec3.x, vec3.z, Mth.PI / 2F);
    }

    public BlockPos getBlockUnder(LivingEntity mob) {
        final BlockPos.MutableBlockPos position = mob.blockPosition().mutable();
        BlockState state = mob.level.getBlockState(position);
        while (state.isAir() && state.getFluidState().isEmpty()) {
            position.move(Direction.DOWN);
            if (position.getY() <= 0) return null;
        }
        return position;
    }
}