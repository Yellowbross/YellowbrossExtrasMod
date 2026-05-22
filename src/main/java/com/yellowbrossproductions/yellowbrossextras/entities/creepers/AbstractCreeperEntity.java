package com.yellowbrossproductions.yellowbrossextras.entities.creepers;

import com.yellowbrossproductions.yellowbrossextras.entities.CameraShakeEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.YextrasEntity;
import com.yellowbrossproductions.yellowbrossextras.packet.PacketHandler;
import com.yellowbrossproductions.yellowbrossextras.packet.ParticlePacket;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

public class AbstractCreeperEntity extends Monster implements CreeperEnemy, YextrasEntity {
    private static final EntityDataAccessor<Integer> DATA_SWELL_DIR = SynchedEntityData.defineId(AbstractCreeperEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_IS_POWERED = SynchedEntityData.defineId(AbstractCreeperEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_IGNITED = SynchedEntityData.defineId(AbstractCreeperEntity.class, EntityDataSerializers.BOOLEAN);
    public int oldSwell;
    public int swell;
    public int maxSwell = 30;
    public int explosionRadius = 3;
    private int droppedSkulls;
    public boolean shouldCalculateSwell = true;

    public float absorbedCreepers = 0;
    float f;

    public AbstractCreeperEntity(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    public int getMaxFallDistance() {
        return this.getTarget() == null ? 3 : 3 + (int)(this.getHealth() - 1.0F);
    }

    public boolean causeFallDamage(float p_149687_, float p_149688_, DamageSource p_149689_) {
        boolean flag = super.causeFallDamage(p_149687_, p_149688_, p_149689_);
        this.swell += (int)(p_149687_ * 1.5F);
        if (this.swell > this.maxSwell - 5) {
            this.swell = this.maxSwell - 5;
        }

        return flag;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SWELL_DIR, -1);
        this.entityData.define(DATA_IS_POWERED, false);
        this.entityData.define(DATA_IS_IGNITED, false);
    }

    @Override
    public boolean hurt(DamageSource p_21016_, float p_21017_) {
        if (p_21016_.getEntity() instanceof CreeperEnemy) {
            this.heal(p_21017_);
            this.makeHealParticles();
            return false;
        }
        if (p_21016_ == DamageSource.FALL) {
            p_21017_ = 0.0F;
        }
        return super.hurt(p_21016_, p_21017_);
    }

    public void makeHealParticles() {
        if (!this.level.isClientSide) {
            for (ServerPlayer serverPlayer : ((ServerLevel)this.level).players()) {
                if (serverPlayer.distanceToSqr(this) < 4096.0D) {
                    ParticlePacket packet = new ParticlePacket();

                    for(int i = 0; i < 6; ++i) {
                        double d0 = (-0.5 + this.random.nextGaussian());
                        double d1 = (-0.5 + this.random.nextGaussian());
                        double d2 = (-0.5 + this.random.nextGaussian());
                        packet.queueParticle(ParticleTypes.HEART, false, new Vec3(this.getRandomX(0.5D), this.getY() + 1.0D, this.getRandomZ(0.5D)), new Vec3(d0, 0.0D, d2));
                    }

                    PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), packet);
                }
            }
        }
    }

    protected SoundEvent getHurtSound(DamageSource p_32309_) {
        return SoundEvents.CREEPER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.CREEPER_DEATH;
    }

    protected int getMaxAbsorbs() {
        return 1;
    }

    public void addAdditionalSaveData(CompoundTag p_32304_) {
        super.addAdditionalSaveData(p_32304_);
        if (this.entityData.get(DATA_IS_POWERED)) {
            p_32304_.putBoolean("powered", true);
        }

        p_32304_.putShort("Fuse", (short)this.maxSwell);
        p_32304_.putByte("ExplosionRadius", (byte)this.explosionRadius);
        p_32304_.putBoolean("ignited", this.isIgnited());
    }

    public void readAdditionalSaveData(CompoundTag p_32296_) {
        super.readAdditionalSaveData(p_32296_);
        this.entityData.set(DATA_IS_POWERED, p_32296_.getBoolean("powered"));
        if (p_32296_.contains("Fuse", 99)) {
            this.maxSwell = p_32296_.getShort("Fuse");
        }

        if (p_32296_.contains("ExplosionRadius", 99)) {
            this.explosionRadius = p_32296_.getByte("ExplosionRadius");
        }

        if (p_32296_.getBoolean("ignited")) {
            this.ignite();
        }

    }

    @Override
    public boolean hasLineOfSight(Entity p_147185_) {
        if (p_147185_.level != this.level) {
            return false;
        } else {
            Vec3 vec3 = new Vec3(this.getX(), this.getEyeY(), this.getZ());
            Vec3 vec31 = new Vec3(p_147185_.getX(), p_147185_.getEyeY(), p_147185_.getZ());
            if (vec31.distanceTo(vec3) > 128.0D) {
                return false;
            } else {
                return true;
            }
        }
    }

    public boolean isInAttackSight(Entity p_147185_) {
        if (p_147185_.level != this.level) {
            return false;
        } else {
            Vec3 vec3 = new Vec3(this.getX(), this.getEyeY(), this.getZ());
            Vec3 vec31 = new Vec3(p_147185_.getX(), p_147185_.getEyeY(), p_147185_.getZ());
            if (vec31.distanceTo(vec3) > 128.0D) {
                return false;
            } else {
                return this.level.clip(new ClipContext(vec3, vec31, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() == HitResult.Type.MISS;
            }
        }
    }

    public void tick() {
        this.calculateSwell();

        if (this.absorbedCreepers >= this.getMaxAbsorbs() && this.random.nextInt(16) == 0) {
            this.ignite();
        }

        super.tick();
    }

    public void calculateSwell() {
        if (this.shouldCalculateSwell) {
            this.oldSwell = this.swell;
            if (this.isIgnited()) {
                this.setSwellDir(1);
            }

            int i = this.getSwellDir();
            if (i > 0 && this.swell == 0) {
                this.playSound(SoundEvents.CREEPER_PRIMED, 1.0F, 0.5F);
                this.gameEvent(GameEvent.PRIME_FUSE);
            }

            this.swell += i;
            if (this.swell < 0) {
                this.swell = 0;
            }

            if (this.swell >= this.maxSwell) {
                this.swell = this.maxSwell;
                f = ((this.absorbedCreepers / 3) + 1) * (this.isPowered() ? 2.0F : 1.0F);
                this.explodeCreeper();
            }
        }
    }

    @Override
    public void die(DamageSource p_21014_) {
        super.die(p_21014_);
        this.shouldCalculateSwell = false;
    }

    public boolean doHurtTarget(Entity p_32281_) {
        return true;
    }

    public boolean isPowered() {
        return this.entityData.get(DATA_IS_POWERED);
    }

    public float getSwelling(float p_32321_) {
        return Mth.lerp(p_32321_, (float)this.oldSwell, (float)this.swell) / (float)(this.maxSwell - 2);
    }

    public int getSwellDir() {
        return this.entityData.get(DATA_SWELL_DIR);
    }

    public void setSwellDir(int p_32284_) {
        this.entityData.set(DATA_SWELL_DIR, p_32284_);
    }

    public void thunderHit(ServerLevel p_32286_, LightningBolt p_32287_) {
        super.thunderHit(p_32286_, p_32287_);
        this.entityData.set(DATA_IS_POWERED, true);
    }

    protected InteractionResult mobInteract(Player p_32301_, InteractionHand p_32302_) {
        ItemStack itemstack = p_32301_.getItemInHand(p_32302_);
        if (itemstack.is(Items.FLINT_AND_STEEL)) {
            this.level.playSound(p_32301_, this.getX(), this.getY(), this.getZ(), SoundEvents.FLINTANDSTEEL_USE, this.getSoundSource(), 1.0F, this.random.nextFloat() * 0.4F + 0.8F);
            if (!this.level.isClientSide) {
                this.ignite();
                itemstack.hurtAndBreak(1, p_32301_, (p_32290_) -> {
                    p_32290_.broadcastBreakEvent(p_32302_);
                });
            }

            return InteractionResult.sidedSuccess(this.level.isClientSide);
        } else {
            return super.mobInteract(p_32301_, p_32302_);
        }
    }

    public void explodeCreeper() {
        if (!this.level.isClientSide) {
            Explosion.BlockInteraction explosion$blockinteraction = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this) ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE;
            this.dead = true;
            this.level.explode(this, this.getX(), this.getY(), this.getZ(), (float)this.explosionRadius * f, explosion$blockinteraction);
            CameraShakeEntity.cameraShake(this.level, position(), 30, 0.1f, 0, 15);
            this.discard();
            this.spawnLingeringCloud();
        }

    }

    public void spawnLingeringCloud() {
        Collection<MobEffectInstance> collection = this.getActiveEffects();
        if (!collection.isEmpty()) {
            AreaEffectCloud areaeffectcloud = new AreaEffectCloud(this.level, this.getX(), this.getY(), this.getZ());
            areaeffectcloud.setRadius(2.5F);
            areaeffectcloud.setRadiusOnUse(-0.5F);
            areaeffectcloud.setWaitTime(10);
            areaeffectcloud.setDuration(areaeffectcloud.getDuration() / 2);
            areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float)areaeffectcloud.getDuration());

            for(MobEffectInstance mobeffectinstance : collection) {
                areaeffectcloud.addEffect(new MobEffectInstance(mobeffectinstance));
            }

            this.level.addFreshEntity(areaeffectcloud);
        }

    }

    public boolean isIgnited() {
        return this.entityData.get(DATA_IS_IGNITED);
    }

    public void ignite() {
        this.entityData.set(DATA_IS_IGNITED, true);
    }

    public boolean canDropMobsSkull() {
        return this.isPowered() && this.droppedSkulls < 1;
    }

    public void increaseDroppedSkulls() {
        ++this.droppedSkulls;
    }

    class CreeperExplodeGoal extends Goal {
        private final AbstractCreeperEntity creeper;
        @Nullable
        private LivingEntity target;

        public CreeperExplodeGoal(AbstractCreeperEntity p_25919_) {
            this.creeper = p_25919_;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse() {
            LivingEntity livingentity = this.creeper.getTarget();
            return this.creeper.getSwellDir() > 0 || livingentity != null && this.creeper.distanceToSqr(livingentity) < 9.0D && this.creeper.isInAttackSight(livingentity);
        }

        public void start() {
            this.creeper.getNavigation().stop();
            this.target = this.creeper.getTarget();
        }

        public void stop() {
            this.target = null;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            if (this.target == null) {
                this.creeper.setSwellDir(-1);
            } else if (this.creeper.distanceToSqr(this.target) > 49.0D) {
                this.creeper.setSwellDir(-1);
            } else if (!this.creeper.isInAttackSight(this.target)) {
                this.creeper.setSwellDir(-1);
            } else if (!this.target.isAlive() && !(this.creeper.swell >= 27)) {
                this.creeper.setSwellDir(-1);
            } else {
                this.creeper.setSwellDir(1);
            }
        }
    }

    class MergeWithMyLoveGoal extends Goal {
        private final AbstractCreeperEntity creeper;
        private final Class<? extends AbstractCreeperEntity> lookingForType;
        private AbstractCreeperEntity myLove = null;

        MergeWithMyLoveGoal(AbstractCreeperEntity creeper, Class<? extends AbstractCreeperEntity> lookingForType) {
            this.creeper = creeper;
            this.lookingForType = lookingForType;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (this.creeper.getTarget() != null || (!(this.creeper instanceof ParacreeperEntity) && !this.creeper.isOnGround())) return false;

            List<? extends AbstractCreeperEntity> nearbyMobs = this.creeper.level.getEntitiesOfClass(this.lookingForType, this.creeper.getBoundingBox().inflate(10.0d), predicate -> predicate.absorbedCreepers < predicate.getMaxAbsorbs());

            double closestDistanceSq = Double.MAX_VALUE;
            for (AbstractCreeperEntity potentialLove : nearbyMobs) {
                if (potentialLove != this.creeper && potentialLove.isAlive() && !potentialLove.isRemoved()) {
                    double distanceSq = potentialLove.distanceToSqr(this.creeper);
                    if (distanceSq < closestDistanceSq) {
                        closestDistanceSq = distanceSq;
                        this.myLove = potentialLove;
                    }
                }
            }

            return this.myLove != null;
        }

        @Override
        public boolean canContinueToUse() {
            double maxDist = 10.0d * 10.0d;

            return this.myLove != null && this.myLove.isAlive() && !this.myLove.isRemoved() && this.myLove.absorbedCreepers < this.myLove.getMaxAbsorbs() && this.creeper.distanceToSqr(this.myLove) <= maxDist && this.creeper.getTarget() == null;
        }

        @Override
        public void tick() {
            if (this.myLove != null) {
                this.creeper.getNavigation().moveTo(this.myLove, 1.0D);

                if (this.creeper.distanceToSqr(this.myLove) < 9.0D && AbstractCreeperEntity.this.random.nextInt(8) == 0 && !this.creeper.level.isClientSide) {
                    this.myLove.absorbedCreepers += this.creeper.absorbedCreepers + 1;
                    this.creeper.playSound(SoundEvents.SNOWBALL_THROW, 2.0F, 1.0F);
                    this.creeper.dead = true;
                    this.creeper.discard();
                }
            }
        }

        @Override
        public void stop() {
            this.myLove = null;
            this.creeper.getNavigation().stop();
        }
    }
}
