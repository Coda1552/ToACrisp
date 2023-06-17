package codyy.toacrisp.common.entities;

import codyy.toacrisp.registry.TACEntities;
import codyy.toacrisp.registry.TACItems;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

// todo - fix targeting (currently the cockatrice's target and beam are not synced)
public class Cockatrice extends TamableAnimal {
    private static final EntityDataAccessor<Integer> DATA_ID_ATTACK_TARGET = SynchedEntityData.defineId(Cockatrice.class, EntityDataSerializers.INT);
    @Nullable
    private LivingEntity clientSideCachedAttackTarget;
    private int clientSideAttackTime;

    public Cockatrice(EntityType<? extends TamableAnimal> p_27557_, Level p_27558_) {
        super(p_27557_, p_27558_);
        this.setTame(false);
    }

    public static AttributeSupplier.Builder createCockatriceAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 16.0D).add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.ATTACK_DAMAGE, 4.0D).add(Attributes.FOLLOW_RANGE, 64.0D);
    }

    @Override
    public void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(0, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Player.class, 25.0F, 1.2D, 1.4D) {
            @Override
            public boolean canUse() {
                return super.canUse() && !((Cockatrice) mob).isTame();
            }
        });
        this.goalSelector.addGoal(1, new CockatriceEntityAttackGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0F, true));
        this.goalSelector.addGoal(2, new FollowParentGoal(this, 1.2F));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, LivingEntity.class, 10.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(4, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(5, new BreedGoal(this, 1.2F));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Rabbit.class, false));
    }

    @Override
    public void setTame(boolean pTamed) {
        super.setTame(pTamed);
        if (pTamed) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(24.0D);
            this.setHealth(24.0F);
        } else {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(16.0D);
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob ageable) {
        return TACEntities.COCKATRICE.get().create(level);
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return pStack.is(Items.WITHER_ROSE);
    }

    @Override
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        Item item = itemstack.getItem();

        if (!level.isClientSide) {
            if (this.isTame()) {
                if (this.isFood(itemstack) && this.getHealth() < this.getMaxHealth()) {
                    if (!pPlayer.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }

                    this.heal((float)itemstack.getFoodProperties(this).getNutrition());
                    return InteractionResult.SUCCESS;
                }

                if (!isFood(itemstack)) {
                    InteractionResult interactionresult = super.mobInteract(pPlayer, pHand);
                    if ((!interactionresult.consumesAction() || this.isBaby()) && this.isOwnedBy(pPlayer)) {
                        this.setOrderedToSit(!this.isOrderedToSit());
                        this.jumping = false;
                        this.navigation.stop();
                        this.setTarget(null);
                        return InteractionResult.SUCCESS;
                    }

                    return interactionresult;
                }

                // todo - remove tame item and make only hatched babies tamed
            }
            else if (itemstack.is(Items.BONE)) {
                if (!pPlayer.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                if (this.random.nextInt(3) == 0 && !ForgeEventFactory.onAnimalTame(this, pPlayer)) {
                    this.tame(pPlayer);
                    this.navigation.stop();
                    this.setTarget(null);
                    this.setOrderedToSit(true);
                    this.level.broadcastEntityEvent(this, (byte)7);
                } else {
                    this.level.broadcastEntityEvent(this, (byte)6);
                }

                return InteractionResult.SUCCESS;
            }
        }

        return super.mobInteract(pPlayer, pHand);
    }

    void setActiveAttackTarget(int p_32818_) {
        this.entityData.set(DATA_ID_ATTACK_TARGET, p_32818_);
    }

    public boolean hasActiveAttackTarget() {
        return this.entityData.get(DATA_ID_ATTACK_TARGET) != 0;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_ATTACK_TARGET, 0);
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> p_32834_) {
        super.onSyncedDataUpdated(p_32834_);
        if (DATA_ID_ATTACK_TARGET.equals(p_32834_)) {
            this.clientSideAttackTime = 0;
            this.clientSideCachedAttackTarget = null;
        }

    }

    @Nullable
    public LivingEntity getActiveAttackTarget() {
        if (!this.hasActiveAttackTarget()) {
            return null;
        }
        else if (this.level.isClientSide) {
            if (this.clientSideCachedAttackTarget != null) {
                return this.clientSideCachedAttackTarget;
            }
            else {
                Entity entity = this.level.getEntity(this.entityData.get(DATA_ID_ATTACK_TARGET));
                if (entity instanceof LivingEntity) {
                    this.clientSideCachedAttackTarget = (LivingEntity)entity;
                    return this.clientSideCachedAttackTarget;
                }
                else {
                    return null;
                }
            }
        }
        else {
            return this.getTarget();
        }
    }

    public int getAttackDuration() {
        return 10;
    }

    public float getAttackAnimationScale(float p_32813_) {
        return ((float)this.clientSideAttackTime + p_32813_) / (float)this.getAttackDuration();
    }

    @Override
    public boolean wantsToAttack(LivingEntity pTarget, LivingEntity pOwner) {
        if (!(pTarget instanceof Creeper) && !(pTarget instanceof Ghast)) {
            if (pTarget instanceof Cockatrice cockatrice) {
                return !cockatrice.isTame() || cockatrice.getOwnerUUID() != pOwner.getUUID();
            } else if (pTarget instanceof Player && pOwner instanceof Player && !((Player)pOwner).canHarmPlayer((Player)pTarget)) {
                return false;
            } else if (pTarget instanceof AbstractHorse && ((AbstractHorse)pTarget).isTamed()) {
                return false;
            } else {
                return !(pTarget instanceof TamableAnimal) || !((TamableAnimal)pTarget).isTame();
            }
        } else {
            return false;
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (hasActiveAttackTarget() && getActiveAttackTarget() != null && getActiveAttackTarget().isAlive() && distanceToSqr(getActiveAttackTarget()) < 64.0D) {
            createParticles(getActiveAttackTarget());

            if (!getActiveAttackTarget().hasEffect(MobEffects.POISON)) {
                getActiveAttackTarget().addEffect(new MobEffectInstance(MobEffects.POISON, 200, 2));
            }

        }
    }

    private void createParticles(LivingEntity target) {
        RandomSource random = target.getRandom();

        for (int i = 0; i < 3; i++) {
            target.level.addParticle(new DustParticleOptions(Vec3.fromRGB24(0xaef668).toVector3f(), 1.0F), target.getX() - 0.2d + (random.nextDouble()/2d), target.getY() + (target.getBbHeight() / 2),  target.getZ() - 0.2d + (random.nextDouble()/2d), 0, 0, 0);
        }
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return new ItemStack(TACItems.COCKATRICE_SPAWN_EGG.get());
    }

    static class CockatriceEntityAttackGoal extends Goal {
        private final Cockatrice cockatrice;
        private int attackTime;

        public CockatriceEntityAttackGoal(Cockatrice p_32871_) {
            this.cockatrice = p_32871_;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        public boolean canUse() {
            LivingEntity livingentity = this.cockatrice.getTarget();
            return !cockatrice.isAggressive() && livingentity != null && livingentity.isAlive();
        }

        public boolean canContinueToUse() {
            return !cockatrice.isAggressive() && super.canContinueToUse() && this.cockatrice.getTarget() != null && cockatrice.getTarget().isAlive();
        }

        public void start() {
            this.attackTime = -10;
            LivingEntity target = this.cockatrice.getTarget();
            if (target != null) {
                this.cockatrice.getLookControl().setLookAt(target, 90.0F, 90.0F);
                this.cockatrice.navigation.moveTo(target, 0.8D);
            }

            this.cockatrice.hasImpulse = true;
        }

        public void stop() {
            if (cockatrice.getActiveAttackTarget() == null) {
                this.cockatrice.setActiveAttackTarget(0);
                this.cockatrice.setTarget(null);
            }
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity target = this.cockatrice.getTarget();
            if (target != null) {
                this.cockatrice.getLookControl().setLookAt(target, 90.0F, 90.0F);
                ++this.attackTime;

                if (cockatrice.distanceToSqr(target) > 256.0D) {
                    stop();
                    this.cockatrice.setActiveAttackTarget(0);
                    return;
                }

                if (this.attackTime == 0) {
                    this.cockatrice.setActiveAttackTarget(target.getId());
                }
                else if (this.attackTime >= this.cockatrice.getAttackDuration()) {
                    float f = 0.0F; // todo - add damage value
                    target.hurt(this.cockatrice.m_269291_().m_269333_(this.cockatrice), (float) this.cockatrice.getAttributeValue(Attributes.ATTACK_DAMAGE) * f);
                }

                super.tick();
            }
        }

    }

}
