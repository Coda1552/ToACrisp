package codyy.toacrisp.common.entities;

import codyy.toacrisp.registry.TACEntities;
import codyy.toacrisp.registry.TACItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
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
public class Wyvern extends TamableAnimal implements Saddleable, OwnableEntity {
    private static final EntityDataAccessor<Boolean> SADDLED = SynchedEntityData.defineId(Wyvern.class, EntityDataSerializers.BOOLEAN);

    public Wyvern(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new FlyingMoveControl(this, 10, false    );
    }

    public static AttributeSupplier.Builder createWyvernAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 60.0D).add(Attributes.MOVEMENT_SPEED, 0.225D).add(Attributes.FLYING_SPEED, 0.5D).add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(0, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(2, new FollowParentGoal(this, 1.15D));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, true, p -> getOwnerUUID() != null && !p.getUUID().equals(getOwnerUUID())));
    }

    @Override
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        if (pPlayer.getItemInHand(pHand).isEmpty() && isTame() && getOwnerUUID() != null && getOwnerUUID().equals(pPlayer.getUUID()) && isSaddled() && getPassengers().isEmpty()) {
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
    }

    @Override
    protected PathNavigation createNavigation(Level world) {
        return new FlyingPathNavigation(this, world);
    }

    @Override
    public boolean isSaddleable() {
        return isTame() && !isSaddled();
    }

    @Override
    public void equipSaddle(@Nullable SoundSource soundSource) {
        this.setSaddled(true);
        if (soundSource != null) {
            this.level().playSound(null, this, SoundEvents.HORSE_SADDLE, soundSource, 0.5F, 1.0F);
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

    @Override
    public LivingEntity getControllingPassenger() {
        return this.getPassengers().isEmpty() || !(this.getPassengers().get(0) instanceof LivingEntity) ? null : (LivingEntity)this.getPassengers().get(0);
    }

    public static Vec3 getYawVec(float yaw, double xOffset, double zOffset) {
        return new Vec3(xOffset, 0, zOffset).yRot(-yaw * ((float) Math.PI / 180f));
    }

    @Override
    protected void positionRider(Entity passenger, MoveFunction p_19958_) {
        Vec3 pos = getYawVec(yBodyRot, 0.0F, 0.0F).add(getX(), getY() + 0.9F, getZ());
        passenger.setPos(pos.x, pos.y, pos.z);
    }

    @Override
    public void travel(Vec3 pTravelVector) {
        super.travel(pTravelVector);
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
                double d4 = this.level().getBlockFloorHeight(mutable);
                if ((double)mutable.getY() + d4 > d3) {
                    break;
                }

                if (DismountHelper.isBlockFloorValid(d4)) {
                    AABB aabb = pPassenger.getLocalBoundsForPose(pose);
                    Vec3 vec3 = new Vec3(d0, (double)mutable.getY() + d4, d2);
                    if (DismountHelper.canDismountTo(this.level(), pPassenger, aabb.move(vec3))) {
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

}
