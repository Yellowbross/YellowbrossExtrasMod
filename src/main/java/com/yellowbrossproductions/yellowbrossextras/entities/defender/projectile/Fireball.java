package com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile;

import com.yellowbrossproductions.yellowbrossextras.entities.CameraShake;
import com.yellowbrossproductions.yellowbrossextras.entities.projectile.CustomAbstractHurtingProjectile;
import com.yellowbrossproductions.yellowbrossextras.init.YEEntityTypes;
import com.yellowbrossproductions.yellowbrossextras.init.YESoundEvents;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Fireball extends CustomAbstractHurtingProjectile {
    private static final EntityDataAccessor<Integer> SIZE = SynchedEntityData.defineId(Fireball.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> SHOULD_FLASH = SynchedEntityData.defineId(Fireball.class, EntityDataSerializers.BOOLEAN);
    Vec3 startPos = Vec3.ZERO;
    Vec3 destPos = Vec3.ZERO;
    public boolean wasHit = false;
    List<LivingEntity> caught = new ArrayList<>();
    public boolean defenderYeet = false;

    public Fireball(EntityType<? extends CustomAbstractHurtingProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public Fireball(Level level, Entity owner, Vec3 position, Vec3 motion) {
        super(YEEntityTypes.Fireball.get(), position.x, position.y, position.z, motion.x, motion.y, motion.z, level);
        this.setOwner(owner);
        this.xPower *= this.getHitBackMultiplier();
        this.yPower *= this.getHitBackMultiplier();
        this.zPower *= this.getHitBackMultiplier();
        this.startPos = position;
        this.destPos = this.startPos.add(motion.scale(60));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SIZE, 0);
        this.entityData.define(SHOULD_FLASH, false);
    }

    public int getSize() {
        return this.entityData.get(SIZE);
    }

    public void setSize(int size) {
        int i = Mth.clamp(size, 1, 3);
        this.entityData.set(SIZE, i);
        this.reapplyPosition();
        this.refreshDimensions();
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public boolean ignoreExplosion() {
        return true;
    }

    public boolean shouldFlash() {
        return this.entityData.get(SHOULD_FLASH);
    }

    public void setShouldFlash(boolean shouldFlash) {
        this.entityData.set(SHOULD_FLASH, shouldFlash);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.getDirectEntity() instanceof Fireball) return false;
        if (this.getSize() == 1) return false;
        this.wasHit = true;
        if (!this.level().isClientSide) this.setShouldFlash(false);
        return super.hurt(pSource, pAmount);
    }

    @Override
    public double getHitBackMultiplier() {
        if (this.getSize() == 2) return 0.5;
        if (this.getSize() == 3) return 0.25;
        return 1.0;
    }

    @Override
    public boolean doesHittingMeChangeMyOwner() {
        return false;
    }

    public void refreshDimensions() {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        super.refreshDimensions();
        this.setPos(d0, d1, d2);
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (SIZE.equals(pKey)) {
            this.refreshDimensions();
        }

        super.onSyncedDataUpdated(pKey);
    }

    public EntityDimensions getDimensions(Pose pPose) {
        return super.getDimensions(pPose).scale(1.5f * (float)this.getSize()).scale(this.getSize() == 3 ? 2.6F : 1.0F);
    }

    @Override
    public void tick() {
        if (this.defenderYeet) {
            this.setDeltaMovement(Vec3.ZERO);

            if (this.tickCount <= 8) {
                if (this.getOwner() != null) this.setPos(this.getOwner().position().add(0, 3, 0).add(0, 0.15 * this.tickCount, 0));
            }
            if (this.tickCount == 9) {
                this.playSound(YESoundEvents.YEET.get(), 3.0F, 0.9F);
                this.playSound(YESoundEvents.YEET.get(), 3.0F, 0.8F);
                this.playSound(YESoundEvents.YEET.get(), 3.0F, 0.7F);
                this.defenderYeet = false;
            }
        }
        super.tick();

        if (this.isInWater() || this.tickCount > 150 || !this.level().getWorldBorder().isWithinBounds(this.blockPosition())) this.explode(3.0F);

        if (this.startPos != Vec3.ZERO && this.destPos != Vec3.ZERO && !this.wasHit) {
            int amount = Math.max(25, 60 - (int)(this.tickCount / 5));
            if (this.getSize() == 1) {
                for (int i = 0; i < amount; i++) {
                    double trailFactor = i / (amount - 1.0D);
                    double tx = this.startPos.x + (this.destPos.x - this.startPos.x) * trailFactor;
                    double ty = this.startPos.y + (this.destPos.y - this.startPos.y) * trailFactor;
                    double tz = this.startPos.z + (this.destPos.z - this.startPos.z) * trailFactor;
                    BlockPos.MutableBlockPos yPosition = BlockPos.containing(tx, ty, tz).mutable();
                    while (yPosition.getY() > this.level().getMinBuildHeight()
                            && !this.level().getBlockState(yPosition).blocksMotion()
                            && !this.level().getBlockState(yPosition).liquid()) {
                        yPosition.move(Direction.DOWN);
                    }
                    Vec3 pos = new Vec3(tx, yPosition.getY() + 1.65, tz);

                    EntityUtil.makeAParticle(this.level(), ParticleTypes.SOUL_FIRE_FLAME, true, pos, Vec3.ZERO);
                }
            } else {
                EntityUtil.makeSimpleTrail(this, ParticleTypes.SOUL_FIRE_FLAME, amount,
                        this.getX(), this.getY(), this.getZ(),
                        this.destPos.x, this.destPos.y, this.destPos.z, 0);
            }

            if (this.tickCount > 47 && this.getSize() < 3) {
                this.wasHit = true;
                this.yPower = -0.1;
            }
        }

        if (!this.level().isClientSide &&
                !this.level().getBlockState(this.getBlockPosBelowThatAffectsMyMovement()).blocksMotion()
                && !this.level().getBlockState(this.getBlockPosBelowThatAffectsMyMovement()).liquid()
                && this.getSize() == 1 && !this.wasHit) {
            this.setPos(this.position().add(0, -1, 0));
        }

        Iterator<LivingEntity> iterator = this.caught.iterator();

        while (iterator.hasNext()) {
            LivingEntity caught = iterator.next();

            if (this.distanceTo(caught) > this.getBoundingBox().getSize() + 0.75d) iterator.remove();

            this.setDeltaMovement(this.getDeltaMovement().scale(0.995d));
            caught.hurtMarked = true;
            caught.setDeltaMovement(this.getDeltaMovement().scale(1.1d));
        }
    }

    @Override
    protected ParticleOptions getTrailParticle() {
        if (this.getSize() > 1) return ParticleTypes.LARGE_SMOKE;
        return ParticleTypes.SMOKE;
    }

    @Override
    public boolean shouldDoParticles() {
        return true;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setSize(pCompound.getInt("Size") + 1);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("Size", this.getSize() - 1);
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        if (this.getSize() < 3 || this.distanceToSqr(pResult.getEntity()) < 10.0D) {
            if (pResult.getEntity() instanceof LivingEntity living && pResult.getEntity() != this.getOwner()) {
                if (!living.fireImmune()) {
                    living.invulnerableTime = 0;
                    living.hurt(this.damageSources().thrown(this, this.getOwner()), 1.0F * this.getSize());
                    living.invulnerableTime = 0;
                    living.setSecondsOnFire(8);
                    living.playSound(SoundEvents.PLAYER_HURT_ON_FIRE, 2.0F, living.getVoicePitch());
                }
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        if (this.level().getBlockState(pResult.getBlockPos().above()).blocksMotion()
                || this.getSize() > 1
                || pResult.getDirection() == Direction.UP
                || pResult.getDirection() == Direction.DOWN) {
            BlockPos.MutableBlockPos pos = pResult.getBlockPos().mutable();
            pos = pos.move(pResult.getDirection(), 2);
            this.setPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
            this.explode(3.0F);
        }
        else this.setPos(this.position().add(0, 1, 0));
    }

    @Override
    public boolean isPickable() {
        return this.getSize() != 1 && super.isPickable();
    }

    public void makeExplodeParticles() {
        for(int i = 0; i < 20 * this.getSize(); ++i) {
            double d0 = (-0.5 + this.random.nextGaussian());
            double d1 = (-0.5 + this.random.nextGaussian());
            double d2 = (-0.5 + this.random.nextGaussian());
            EntityUtil.makeAParticle(this.level(), ParticleTypes.FLAME, false, this.position(), new Vec3(d0, d1, d2));
        }
        for(int i = 0; i < 10 * this.getSize(); ++i) {
            double d0 = (-0.5 + this.random.nextGaussian());
            double d1 = (-0.5 + this.random.nextGaussian());
            double d2 = (-0.5 + this.random.nextGaussian());
            EntityUtil.makeAParticle(this.level(), ParticleTypes.LARGE_SMOKE, true, this.position(), new Vec3(d0, d1, d2));
        }
        EntityUtil.makeAParticle(this.level(), ParticleTypes.EXPLOSION, true, this.position(), Vec3.ZERO);
        if (this.getSize() > 1) EntityUtil.makeAParticle(this.level(), ParticleTypes.EXPLOSION_EMITTER, true, this.position(), Vec3.ZERO);
    }

    private void explode(float s) {
        if (this.level().isClientSide) return;

        this.makeExplodeParticles();
        SoundEvent sound = switch (this.getSize()) {
            case 3 -> YESoundEvents.HUGE_EXPLOSION.get();
            case 2 -> SoundEvents.GENERIC_EXPLODE;
            default -> SoundEvents.PLAYER_HURT_ON_FIRE;
        };
        this.playSound(SoundEvents.PLAYER_HURT_ON_FIRE, 2.0F, 1.0F);
        this.playSound(sound, 1.5F * this.getSize(), 1.0F);
        if (this.getSize() == 3) CameraShake.cameraShake(this.level(), this.position(), 50, 0.1f, 0, 20);

        float size = s * this.getSize();

        List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, new AABB(this.position().subtract(size, size, size), this.position().add(size, size, size)), p -> p.isAlive() && p != this.getOwner());

        for (LivingEntity entity : list) {
            boolean canHurt = !(this.getOwner() instanceof Mob owner) || EntityUtil.canHurtThisMob(entity, owner);
            if (canHurt) {
                DamageSource damageSource = this.damageSources().thrown(this, this.getOwner());
                entity.invulnerableTime = 0;
                entity.hurt(damageSource, 8.0F * this.getSize() * EntityUtil.multiplyToScrewArmor(entity, 0.2f));
            }
        }

        if (this.getSize() == 2) this.fireProjectiles(this.getOwner(), 8, this.getBbHeight() / 2, 1.0f, 1, true);
        if (this.getSize() == 3) this.fireProjectiles(this.getOwner(), 50, this.getBbHeight() / 2, 1.0f, 2, false);

        this.discard();
    }

    public void fireProjectiles(@Nullable Entity spawner, int amount, double yOffset, float speed, int size, boolean circleOrSphere) {
        if (this.level().isClientSide()) return;

        if (circleOrSphere) {
            for (int i = 0; i < amount; i++) {
                float TAU = (float) (2 * StrictMath.PI);

                float yaw = i * (TAU / amount);
                float vx = speed * Mth.cos(yaw);
                float vz = speed * Mth.sin(yaw);
                Vec3 direction = new Vec3(vx, 0, vz);

                if (!this.level().getBlockState(BlockPos.containing(this.position().add(direction))).blocksMotion()) {
                    Fireball bullet = new Fireball(this.level(), spawner, this.position().add(0, yOffset, 0), direction);

                    bullet.setSize(size);
                    this.level().addFreshEntity(bullet);
                }
            }
        } else {
            double goldenRatio = (1.0 + Math.sqrt(5.0)) / 2.0;
            for (int i = 0; i < amount; i++) {
                double theta = 2 * Math.PI * i / goldenRatio;
                double phi = Math.acos(1.0 - 2.0 * (i + 0.5) / amount);

                double x = Math.cos(theta) * Math.sin(phi);
                double y = Math.cos(phi);
                double z = Math.sin(theta) * Math.sin(phi);

                Vec3 direction = new Vec3(x, y, z).normalize().scale(speed);

                if (!this.level().getBlockState(BlockPos.containing(this.position().add(direction))).blocksMotion()) {
                    Fireball bullet = new Fireball(this.level(), spawner, this.position().add(0, yOffset, 0), direction);

                    bullet.setSize(size);
                    this.level().addFreshEntity(bullet);
                }
            }
        }
    }
}
