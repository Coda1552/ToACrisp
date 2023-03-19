package coda.toacrisp.common.entities;

import coda.toacrisp.common.entities.goal.FlyingDragonWanderGoal;
import coda.toacrisp.common.entities.goal.FollowDriverGoal;
import coda.toacrisp.registry.TACEntities;
import coda.toacrisp.registry.TACItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.Nullable;


// todo - flying, isSaddled() doesn't seem to be saving
public class Wyvern extends TamableAnimal implements Saddleable, FlyingAnimal {
    private static final EntityDataAccessor<Boolean> SADDLED = SynchedEntityData.defineId(Wyvern.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(Wyvern.class, EntityDataSerializers.BOOLEAN);
    public Entity previousDriver = null;

    public Wyvern(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new FlyingMoveControl(this, 10, true);
    }

    public Wyvern(PlayMessages.SpawnEntity packet, Level world) {
        this(TACEntities.WYVERN.get(), world);
    }

    public static AttributeSupplier.Builder createWyvernAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 60.0D).add(Attributes.MOVEMENT_SPEED, 0.225D).add(Attributes.FLYING_SPEED, 0.5D).add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    // adds the random flying goal, flying = 0 means is on ground, 1 means is flying, -1 means is getting down, while in -1 is more likely to go down than up
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(0, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(2, new FollowParentGoal(this, 1.15D));
        this.goalSelector.addGoal(2, new FollowDriverGoal(this));
        this.goalSelector.addGoal(3, new FlyingDragonWanderGoal(this, 20, 5));
        this.goalSelector.addGoal(1, new RandomStrollGoal(this, 0.8, 20) {
            @Override
            protected Vec3 getPosition() {
                if(Wyvern.this.getPersistentData().getInt("flying") != 0) {
                    RandomSource random = Wyvern.this.getRandom();
                    double dirX = Wyvern.this.getX() + ((random.nextFloat() * 2 - 1) * 10);
                    double dirY;
                    if(Wyvern.this.getPersistentData().getInt("flying")==-1) {
                        dirY = Wyvern.this.getY() + ((random.nextFloat() * 2 - 1) * -10 - 3.5);
                    } else {
                        dirY = Wyvern.this.getY() + ((random.nextFloat() * 2 - 1) * 10);
                    }
                    double dirZ = Wyvern.this.getZ() + ((random.nextFloat() * 2 - 1) * 10);
                    return new Vec3(dirX, dirY, dirZ);
                }
                else return Wyvern.this.getDeltaMovement();
            }
        });
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, true, p -> getOwner() != null && !p.is(getOwner())));

    }

    // we set timer to 0 when mounting so that works fine in travel method
    @Override
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        if (pPlayer.getItemInHand(pHand).isEmpty() && isTame() && getOwner() != null && getOwner().is(pPlayer) && isSaddled() && getPassengers().isEmpty()) {
            pPlayer.startRiding(this);
            this.getPersistentData().putInt("timer", 0);
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
    protected PathNavigation createNavigation(Level world) {
        return new FlyingPathNavigation(this, world);
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

    // we set flying to -1 when spawning so that it doesn't fall immediately if spawned in air
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, @javax.annotation.Nullable CompoundTag tag) {
        SpawnGroupData retval = super.finalizeSpawn(world, difficulty, reason, livingdata, tag);
        this.getPersistentData().putInt("flying", -1);
        return retval;
    }

    // we make the entity look where the player is looking and travel vertically if in flying mode and timer is 0, so that when pressing the jump key to switch to flying mode it doesn't stop the vertical momentum immediately
    // the faster it goes the more it moves vertically
    // if walking we slow it down or else it's too fast
    @Override
    public void travel(Vec3 dir) {
        if (this.isVehicle()) {
            Entity passener = this.getPassengers().get(0);
            this.setYRot(passener.getYRot());
            this.yRotO = this.getYRot();
            this.setXRot(passener.getXRot() * 0.5F);
            this.setRot(this.getYRot(), this.getXRot());
            this.yBodyRot = passener.getYRot();
            this.yHeadRot = passener.getYRot();
            if (this.getPersistentData().getInt("flying") != 0 && this.getPersistentData().getInt("timer") <= 0) {
                double currentSpeed = Math.pow(Math.pow(this.getDeltaMovement().x, 2) + Math.pow(this.getDeltaMovement().z, 2), 0.5);
                super.travel(new Vec3(0, 0, ((LivingEntity) passener).zza * 3));
                this.setDeltaMovement(new Vec3(this.getDeltaMovement().x , this.getXRot() * -0.04 * currentSpeed, this.getDeltaMovement().z));
            }
            else {
                this.setSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED) / (float) 2.5);
                super.travel(new Vec3(0, 0, ((LivingEntity) passener).zza));
            }
            return;
        }
        super.travel(dir);
    }

    // here we handle the flying states,
    // if not ridden and if going towards the ground it will check for non-air blocks below and if it finds them, switch to walking
    // if it is on ground or flying, we wait for the timer to be 0, and we switch mode
    // if it is ridden and on ground and with timer 0 it will switch to walking
    // if the timer is greater than 0 we just scale it down by 1
    @Override
    public void baseTick() {
        super.baseTick();
        if(!this.isVehicle()) {
            if (this.getPersistentData().getInt("flying") == -1) {
                if (!((this.level.getBlockState(new BlockPos(this.getX(), this.getY() - 2, this.getZ()))).getBlock() == Blocks.AIR || (this.level.getBlockState(new BlockPos(this.getX(), this.getY() - 1, this.getZ()))).getBlock() == Blocks.AIR)) {
                    this.getPersistentData().putInt("flying", 0);
                    this.getPersistentData().putInt("timer", 150);
                }
            } else if (this.getPersistentData().getInt("timer") == 0) {
                if (this.getPersistentData().getInt("flying") == 0) {
                    this.getPersistentData().putInt("flying", 1);
                    this.getPersistentData().putInt("timer", 100);
                } else {
                    this.getPersistentData().putInt("flying", -1);
                }
            }
        } else if (this.onGround && this.getPersistentData().getInt("timer") == 0) {
            this.getPersistentData().putInt("flying", 0);
            this.getPersistentData().putInt("timer", -1);
        }
        if (this.getPersistentData().getInt("timer") > 0) {
            this.getPersistentData().putInt("timer", this.getPersistentData().getInt("timer") - 1);
        }
    }

    // set no gravity if flying
    @Override
    public void setNoGravity(boolean ignored) {
        super.setNoGravity(this.getPersistentData().getInt("flying") != 0);
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

}
