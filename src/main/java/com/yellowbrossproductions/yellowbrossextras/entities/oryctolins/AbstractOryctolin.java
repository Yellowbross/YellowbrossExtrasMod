package com.yellowbrossproductions.yellowbrossextras.entities.oryctolins;

import com.yellowbrossproductions.yellowbrossextras.entities.CameraShake;
import com.yellowbrossproductions.yellowbrossextras.entities.YExtrasMob;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.IsDefenderAligned;
import com.yellowbrossproductions.yellowbrossextras.init.YEParticleTypes;
import com.yellowbrossproductions.yellowbrossextras.init.YESoundEvents;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;

public abstract class AbstractOryctolin extends YExtrasMob implements IsOryctolinAligned, Enemy {
    protected static final EntityDataAccessor<Boolean> IS_CELEBRATING = SynchedEntityData.defineId(AbstractOryctolin.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> SHAKE_MULTIPLIER = SynchedEntityData.defineId(AbstractOryctolin.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> FACE_STATE = SynchedEntityData.defineId(AbstractOryctolin.class, EntityDataSerializers.INT);
    private int wave;
    private boolean canJoinRaid;
    private int ticksOutsideRaid;
    protected int attackType;
    protected int attackTicks;

    public AnimationState anim_celebrate = new AnimationState();
    public AnimationState anim_death = new AnimationState();
    public AnimationState anim_running = new AnimationState();
    public AnimationState anim_idle = new AnimationState();

    public AbstractOryctolin(EntityType<? extends YExtrasMob> entityType, Level level) {
        super(entityType, level);
        ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(true);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new OpenDoorGoal(this, true));
        super.registerGoals();
    }

    protected void customServerAiStep() {
        if (!this.isNoAi() && GoalUtils.hasGroundPathNavigation(this)) {
            ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(true);
        }

        super.customServerAiStep();
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(true);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_CELEBRATING, false);
        this.entityData.define(SHAKE_MULTIPLIER, 0);
        this.entityData.define(FACE_STATE, 0);
    }

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        return false;
    }

    public abstract void applyRaidBuffs(int wave, boolean var2);

    public boolean canJoinRaid() {
        return this.canJoinRaid;
    }

    public void setCanJoinRaid(boolean canJoinRaid) {
        this.canJoinRaid = canJoinRaid;
    }

    public int getShakeMultiplier() {
        return this.entityData.get(SHAKE_MULTIPLIER);
    }

    public void setShakeMultiplier(int shake) {
        if (!this.level().isClientSide) {
            this.entityData.set(SHAKE_MULTIPLIER, shake);
        }
    }

    @Nullable
    public abstract SoundEvent getPopDeathSound();

    public int getFace() {
        return this.entityData.get(FACE_STATE);
    }

    public void setFace(int face) {
        if (!this.level().isClientSide) {
            this.entityData.set(FACE_STATE, face);
        }
    }

    @Override
    public void tick() {
        if (this.attackType > 0) {
            this.attackTicks += 1;
        } else {
            this.attackTicks = 0;
        }

        if (this.isDeadOrDying()) {
            this.setXRot(0.0F);
            this.xRotO = this.getXRot();
            this.setYBodyRot(this.getYHeadRot());
            this.setYRot(this.getYHeadRot());
            this.yRotO = this.getYHeadRot();
        }
        super.tick();
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.getEntity() instanceof AbstractOryctolin oryctolin) {
            if (oryctolin.getTeam() != null || this.getTeam() != null) {
                return EntityUtil.canHurtThisMob(oryctolin, this) && super.hurt(pSource, pAmount);
            } else {
                return super.hurt(pSource, pAmount);
            }
        }
        return super.hurt(pSource, pAmount);
    }

    @Override
    public void die(DamageSource pDamageSource) {
        if (!this.level().isClientSide) this.goalSelector.getRunningGoals().forEach(WrappedGoal::stop);

        if (this.lastHurtByPlayerTime > 0) this.lastHurtByPlayerTime = 10000;

        SoundEvent hurt = this.getHurtSound(pDamageSource);
        if (hurt != null) this.playSound(hurt, this.getSoundVolume(), this.getVoicePitch());
        this.playSound(YESoundEvents.ENTITY_ORYCTOLIN_INFLATE.get(), 2.0F, 1.0F);

        this.setAnimationState("death");
    }

    @Override
    protected void tickDeath() {
        this.setShakeMultiplier(this.deathTime * 4);
        if (++this.deathTime >= 15) {
            this.playSound(YESoundEvents.ENTITY_ORYCTOLIN_POP.get(), 3.0F, 1.0F);
            if (this.getPopDeathSound() != null) this.playSound(this.getPopDeathSound(), this.getSoundVolume(), this.getVoicePitch());
            this.explode(1.5d, 5.0d);
            this.makePopParticles();
            CameraShake.cameraShake(this.level(), position(), 20, 0.3f, 0, 15);

            super.die(this.damageSources().generic());

            if (!this.level().isClientSide) this.remove(RemovalReason.KILLED);
        }
    }

    private void explode(double hurtSize, double flingSize) {
        List<Entity> list1 = EntityUtil.getEntitiesFromAABB(this.level(), hurtSize, this, Entity::isAlive);
        List<Entity> list2 = EntityUtil.getEntitiesFromAABB(this.level(), flingSize, this, Entity::isAlive);

        for (Entity entity : list1) {
            if (entity instanceof LivingEntity living) {
                boolean team = EntityUtil.canHurtThisMob(living, this);
                if (team && entity.isAlive() && EntityUtil.isMobNotInCreativeMode(entity)) {
                    living.hurt(this.damageSources().explosion(this, this), 15.0F);
                }
            }
        }
        for (Entity entity : list2) {
            if (EntityUtil.isMobNotInCreativeMode(entity)) {
                double x = this.getX() - entity.getX();
                double y = this.getY() - entity.getY();
                double z = this.getZ() - entity.getZ();
                double d = Math.sqrt(x * x + y * y + z * z);

                entity.hurtMarked = true;
                entity.addDeltaMovement(new Vec3(-x / d * 0.7D, (-y / d * 0.7D) + (entity.onGround() ? 1.2 : 0), -z / d * 0.7D));
            }
        }
    }

    public void makePopParticles() {
        EntityUtil.makeCircleParticles(this.level(), this.getPosition(0).add(0, 0.4, 0), ParticleTypes.POOF, false, 60, 4.0F, Vec3.ZERO, 0.0F);
        EntityUtil.makeCircleParticles(this.level(), this.getPosition(0).add(0, 0.4, 0), ParticleTypes.WITCH, false, 60, 3.0F, Vec3.ZERO, 0.0F);
        EntityUtil.makeAParticle(this.level(), ParticleTypes.EXPLOSION, false, new Vec3(this.getX(), this.getY() + this.getEyeHeight(), this.getZ()), new Vec3(0, 0, 0));
        for (int i = 0; i < 50; ++i) {
            EntityUtil.makeAParticle(this.level(), YEParticleTypes.ORYCTOLIN_SHARD.get(), false, this.getBoundingBox().getCenter(), new Vec3(
                    -0.5f + this.random.nextFloat(),
                    (-0.5f + this.random.nextFloat()) + (this.onGround() ? 0.5f : 0.0f),
                    -0.5f + this.random.nextFloat()
            ));
        }
        for (int i = 0; i < 75; ++i) {
            EntityUtil.makeAParticle(this.level(), YEParticleTypes.MAGIC_TWINKLE.get(), false, this.getBoundingBox().getCenter(), new Vec3(
                    -0.5f + this.random.nextFloat(),
                    (-0.5f + this.random.nextFloat()) + (this.onGround() ? 0.5f : 0.0f),
                    -0.5f + this.random.nextFloat()
            ));
        }
    }

    public void updateAnimations() {
        this.anim_celebrate.animateWhen(this.getAnimationState().equals("celebrate"), this.tickCount);
        this.anim_death.animateWhen(this.getAnimationState().equals("death"), this.tickCount);

        float moveX = (float) (this.getX() - this.xo);
        float moveZ = (float) (this.getZ() - this.zo);
        float speed = Mth.sqrt(moveX * moveX + moveZ * moveZ);
        boolean isRunning = speed > 0.2;
        this.anim_running.animateWhen(isRunning && this.onGround() && this.isAlive() && this.attackType == 0, this.tickCount);
        this.anim_idle.animateWhen(!isRunning && this.onGround() && this.isAlive() && this.attackType == 0, this.tickCount);
    }

    protected PathNavigation createNavigation(Level pLevel) {
        return new OryctolinNavigation(this, pLevel);
    }

    static class OryctolinNavigation extends GroundPathNavigation {
        public OryctolinNavigation(Mob mob, Level level) {
            super(mob, level);
        }

        protected PathFinder createPathFinder(int pMaxVisitedNodes) {
            this.nodeEvaluator = new OryctolinNodeEvaluator();
            return new PathFinder(this.nodeEvaluator, pMaxVisitedNodes);
        }
    }

    static class OryctolinNodeEvaluator extends WalkNodeEvaluator {
        OryctolinNodeEvaluator() {
        }

        @Override
        public boolean canOpenDoors() {
            return true;
        }

        @Override
        public boolean canPassDoors() {
            return true;
        }

        @Override
        protected BlockPathTypes evaluateBlockPathType(BlockGetter pLevel, BlockPos pPos, BlockPathTypes pNodeType) {
            return super.evaluateBlockPathType(pLevel, pPos, pNodeType);
        }
    }

    class CustomAttackGoal extends Goal {
        public CustomAttackGoal() {
            this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return false;
        }

        @Override
        public void tick() {
            getNavigation().stop();

            LivingEntity entity = AbstractOryctolin.this.getTarget();
            if (entity != null) {
                getLookControl().setLookAt(entity, 30.0F, 30.0F);
            }

            navigation.stop();
        }

        @Override
        public void stop() {
            attackTicks = 0;
            attackType = 0;
            setAnimationState("none");
            setShakeMultiplier(0);
            setFace(0);
        }
    }
}
