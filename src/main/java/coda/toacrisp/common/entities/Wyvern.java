package coda.toacrisp.common.entities;

import coda.toacrisp.client.TACKeyBindings;
import coda.toacrisp.common.entities.goal.FlyingDragonWanderGoal;
import coda.toacrisp.common.entities.goal.FollowDriverGoal;
import coda.toacrisp.registry.TACEntities;
import coda.toacrisp.registry.TACItems;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;


// todo - flying, isSaddled() doesn't seem to be saving
public class Wyvern extends TamableAnimal implements Saddleable, FlyingAnimal {
    private static final EntityDataAccessor<Boolean> SADDLED = SynchedEntityData.defineId(Wyvern.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(Wyvern.class, EntityDataSerializers.BOOLEAN);
    public Entity previousDriver = null;

    public Wyvern(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new WyvernMoveController();
    }

    public static AttributeSupplier.Builder createWyvernAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 60.0D).add(Attributes.MOVEMENT_SPEED, 0.225D).add(Attributes.FLYING_SPEED, 0.2D).add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(0, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(2, new FollowParentGoal(this, 1.15D));
        this.goalSelector.addGoal(2, new FollowDriverGoal(this));
        this.goalSelector.addGoal(3, new FlyingDragonWanderGoal(this, 20, 5));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, true, p -> getOwner() != null && !p.is(getOwner())));
    }

    @Override
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        if (pPlayer.getItemInHand(pHand).isEmpty() && isTame() && getOwner() != null && getOwner().is(pPlayer) && isSaddled() && getPassengers().isEmpty()) {
            pPlayer.startRiding(this);
        }

        if (pPlayer.getItemInHand(pHand).is(Items.STICK)) {
            setOwnerUUID(pPlayer.getUUID());
            setTame(true);
            navigation.stop();
            setTarget(null);
        }

        return super.mobInteract(pPlayer, pHand);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SADDLED, false);
        this.entityData.define(FLYING, false);
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        return new FlyingPathNavigation(this, worldIn);
    }

    public boolean isFlying() {
        return entityData.get(FLYING);
    }

    public void setFlying(boolean flying) {
        this.entityData.define(FLYING, flying);
    }

    @Override
    public boolean isSaddleable() {
        return isTame() && !isSaddled();
    }

    @Override
    public void equipSaddle(@Nullable SoundSource soundSource) {
        this.setSaddled(true);
        if (soundSource != null) {
            this.level.playSound(null, this, SoundEvents.HORSE_SADDLE, soundSource, 0.5F, 1.0F);
        }
    }

    public boolean isSaddled() {
        return this.entityData.get(SADDLED);
    }

    public void setSaddled(boolean p_30505_) {
        this.entityData.set(SADDLED, p_30505_);
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        return false;
    }


    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    public boolean onClimbable() {
        return false;
    }

    @Nullable
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    public boolean canBeControlledByRider() {
        return this.getControllingPassenger() instanceof LivingEntity && this.isOwnedBy((LivingEntity) this.getControllingPassenger());
    }

    @Override
    protected void removePassenger(Entity passenger) {
        if (getControllingPassenger() == passenger) {
            this.previousDriver = passenger;
        }
        super.removePassenger(passenger);
    }

    public static Vec3 getYawVec(float yaw, double xOffset, double zOffset) {
        return new Vec3(xOffset, 0, zOffset).yRot(-yaw * ((float) Math.PI / 180f));
    }

    @Override
    public void positionRider(Entity passenger) {
        Vec3 pos = getYawVec(yBodyRot, 0.0F, 0.0F).add(getX(), getY() + 0.9F, getZ());
        passenger.setPos(pos.x, pos.y, pos.z);
    }

    @Override
    public void travel(Vec3 vec3d) {
        boolean flying = this.isFlying();
        float speed = (float) this.getAttributeValue(flying ? Attributes.FLYING_SPEED : Attributes.MOVEMENT_SPEED);

        if (canBeControlledByRider()) {
            LivingEntity entity = (LivingEntity) getControllingPassenger();
            double moveX = entity.xxa * 0.5;
            double moveY = vec3d.y;
            double moveZ = entity.zza;

            yHeadRot = entity.yHeadRot;
            xRot = entity.xRot * 0.65f;
            yRot = Mth.rotateIfNecessary(yHeadRot, yRot, isFlying() ? 5 : 7);

            if (isControlledByLocalInstance()) {
                if (isFlying()) {
                    moveX = vec3d.x;
                    moveY = Minecraft.getInstance().options.keyJump.isDown() ? 0.5F : TACKeyBindings.DRAGON_DESCEND.isDown() ? -0.5 : 0F;
                    moveZ = moveZ > 0 ? moveZ : 0;
                    setSpeed(speed * 0.005F);
                }
                else {
                    speed *= 0.225f;
                    if (entity.jumping) {
                        flying = true;
                        jumpFromGround();
                    }
                }

                vec3d = new Vec3(moveX, moveY, moveZ);
                setSpeed(speed);
            }
            else if (entity instanceof Player) {
                calculateEntityAnimation(this, true);
                setDeltaMovement(Vec3.ZERO);
                if (!level.isClientSide && isFlying())
                    ((ServerPlayer) entity).connection.aboveGroundVehicleTickCount = 0;
                return;
            }
        }
        if (flying) {
            this.moveRelative(speed, vec3d);
            this.move(MoverType.SELF, getDeltaMovement());
            this.setDeltaMovement(getDeltaMovement().scale(0.91f));
            this.calculateEntityAnimation(this, true);
        }
        else {
            super.travel(vec3d);
        }
    }

    public boolean isHighEnough(int altitude) {
        return this.getAltitude(altitude) >= altitude;
    }

    public double getAltitude() {
        return this.getAltitude(level.getHeight());
    }

    public double getAltitude(int limit) {
        BlockPos.MutableBlockPos pointer = blockPosition().mutable();
        int i = 0;

        while(i <= limit && pointer.getY() > level.dimensionType().minY() && !level.getBlockState(pointer).getMaterial().isSolid())
        {
            ++i;
            pointer.setY(blockPosition().getY() - i);
        }

        return i;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob ageable) {
        return TACEntities.WYVERN.get().create(level);
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity pLivingEntity) {
        Vec3 vec3 = getCollisionHorizontalEscapeVector(this.getBbWidth(), pLivingEntity.getBbWidth(), this.getYRot() + (pLivingEntity.getMainArm() == HumanoidArm.RIGHT ? 90.0F : -90.0F));
        Vec3 vec31 = this.getDismountLocationInDirection(vec3, pLivingEntity);
        if (vec31 != null) {
            return vec31;
        } else {
            Vec3 vec32 = getCollisionHorizontalEscapeVector(this.getBbWidth(), pLivingEntity.getBbWidth(), this.getYRot() + (pLivingEntity.getMainArm() == HumanoidArm.LEFT ? 90.0F : -90.0F));
            Vec3 vec33 = this.getDismountLocationInDirection(vec32, pLivingEntity);
            return vec33 != null ? vec33 : this.position();
        }
    }

    @Nullable
    private Vec3 getDismountLocationInDirection(Vec3 pDirection, LivingEntity pPassenger) {
        double d0 = this.getX() + pDirection.x;
        double d1 = this.getBoundingBox().minY;
        double d2 = this.getZ() + pDirection.z;
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for(Pose pose : pPassenger.getDismountPoses()) {
            mutable.set(d0, d1, d2);
            double d3 = this.getBoundingBox().maxY + 0.75D;

            while(true) {
                double d4 = this.level.getBlockFloorHeight(mutable);
                if ((double)mutable.getY() + d4 > d3) {
                    break;
                }

                if (DismountHelper.isBlockFloorValid(d4)) {
                    AABB aabb = pPassenger.getLocalBoundsForPose(pose);
                    Vec3 vec3 = new Vec3(d0, (double)mutable.getY() + d4, d2);
                    if (DismountHelper.canDismountTo(this.level, pPassenger, aabb.move(vec3))) {
                        pPassenger.setPose(pose);
                        return vec3;
                    }
                }

                mutable.move(Direction.UP);
                if (!((double)mutable.getY() < d3)) {
                    break;
                }
            }
        }

        return null;
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return new ItemStack(TACItems.WYVERN_SPAWN_EGG.get());
    }

    private class WyvernMoveController extends MoveControl {

        public WyvernMoveController() {
            super(Wyvern.this);
        }

        @Override
        public void tick() {
            // original movement behavior if the entity isn't flying
            if (!isFlying()) {
                super.tick();
                return;
            }

            if (this.operation == MoveControl.Operation.MOVE_TO) {
                this.operation = MoveControl.Operation.WAIT;
                double xDif = wantedX - mob.getX();
                double yDif = wantedY - mob.getY();
                double zDif = wantedZ - mob.getZ();
                double sqd = xDif * xDif + yDif * yDif + zDif * zDif;
                if (sqd < (double) 2.5000003E-7F) {
                    mob.setYya(0.0F);
                    mob.setZza(0.0F);
                    return;
                }

                double root = Mth.sqrt((float) (xDif * xDif + zDif * zDif));
                float xDir = (float) (-(Mth.atan2(yDif, root) * (double) (180F / (float) Math.PI)));
                float yDir = (float) (Mth.atan2(zDif, xDif) * (double) (180F / (float) Math.PI)) - 90.0F;
                mob.yRot = rotlerp(mob.yRot, yDir, 90.0F);
                mob.xRot = rotlerp(mob.xRot, xDir, 5f);

                float speed = (float) (speedModifier * mob.getAttributeValue(Attributes.FLYING_SPEED));
                mob.setSpeed(speed);
                mob.setYya(yDif > 0.0D ? speed : -speed);
            } else {
                mob.setYya(0.0F);
                mob.setZza(0.0F);
            }
        }
    }
}
